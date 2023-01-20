package hciu.pub.mcmod.melodycraft.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;

import hciu.pub.mcmod.hciusutils.gui.SmartGuiButton;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiScreen;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiTextLabel;
import hciu.pub.mcmod.hciusutils.gui.render.AbstractTextureDrawer;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftButton;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftPictureBox;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftSimpleList;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftTextField;
import hciu.pub.mcmod.melodycraft.client.sound.ExternalSoundHandler;
import hciu.pub.mcmod.melodycraft.config.MelodyCraftGameConfig;
import hciu.pub.mcmod.melodycraft.mug.EnumJudgeLevel;
import hciu.pub.mcmod.melodycraft.mug.data.Song;
import hciu.pub.mcmod.melodycraft.mug.data.SongList;
import hciu.pub.mcmod.melodycraft.tileentity.TileEntityArcade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import scala.actors.threadpool.Arrays;

public class GuiSettings extends GuiMelodyCraftBase {

	private MelodyCraftGameConfig config;

	public GuiSettings(SmartGuiScreen parent) {
		super(parent);
		config = MelodyCraftGameConfig.getInstance();

		addComponent(new SmartGuiTextLabel(this, I18n.format("gui.settings.title")) {
			{
				setCentered(true);
			}
		}.setResizeAction(x -> x.setCenterSize(getParent().getSizeX() / 2, 20, 100, 20)));
		addComponent(new GuiMelodyCraftButton(this, I18n.format("gui.button.save")) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				MelodyCraftGameConfig.save();
				Minecraft.getMinecraft().displayGuiScreen(getSupreme().getParent());
			}
		}.setResizeAction(x -> x.setCenterSize(getParent().getSizeX() - 60, getParent().getSizeY() - 20, 80, 20)));
		addComponent(new GuiMelodyCraftButton(this, I18n.format("gui.button.cancel")) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				Minecraft.getMinecraft().displayGuiScreen(getSupreme().getParent());
			}

		}.setResizeAction(x -> x.setCenterSize(getParent().getSizeX() - 160, getParent().getSizeY() - 20, 80, 20)));

		String[] entry = new String[] { "judge", "speed", "delay", "judgedelay", "norender", "nosound" };
		for (int i = 0; i < entry.length; i++) {
			int y = 40 + 30 * i;
			String name = entry[i];
			addComponent(new SmartGuiTextLabel(this, I18n.format("gui.settings." + name))
					.setResizeAction(x -> x.setBounds(10, y + 4, 100, 16)));
			int ii = i;
			if (i == 0) {
				for (int k : new int[] { -1, 1 }) {

					addComponent(
							new GuiMelodyCraftButton(this, I18n.format("gui.button." + (k == 1 ? "right" : "left"))) {
								@Override
								public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
									int j = Math.max(0, Math.min(config.getGlobal().getJudge().ordinal() + k,
											EnumJudgeLevel.values().length - 1));
									config.getGlobal().setJudge(EnumJudgeLevel.values()[j]);
								}
							}.setResizeAction(x -> x.setBounds(k == 1 ? 160 : 100, y, 20, 20)));
				}
				addComponent(new SmartGuiTextLabel(this, () -> config.getGlobal().getJudge().name()) {
					{
						setCentered(true);
					}
				}.setResizeAction(x -> x.setCenterSize(140, y + 10, 15, 15)));

			} else if (i <= 3) {

				int mxv = new int[] { 0, 200, 1000, 1000 }[i];
				int mnv = new int[] { 0, 1, -1000, -1000 }[i];

				for (int k = 0; k < 4; k++) {
					int kk = k, d = new int[] { -10, -1, 1, 10 }[k];
					addComponent(new GuiMelodyCraftButton(this, I18n.format(new String[] { "gui.button.left2",
							"gui.button.left", "gui.button.right", "gui.button.right2" }[k])) {
						@Override
						public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
							int v = (ii == 1 ? config.getGlobal().getSpeed()
									: (ii == 2 ? config.getClient().getDelay() : config.getClient().getHitdelay()));
							v = Math.max(mnv, Math.min(mxv, v + d));
							if (ii == 1) {
								config.getGlobal().setSpeed(v);
							} else if (ii == 2) {
								config.getClient().setDelay(v);
							} else {
								config.getClient().setHitdelay(v);
							}

						}
					}.setResizeAction(x -> x.setBounds(100 + kk * 30 + (kk >= 2 ? 30 : 0), y, 20, 20)));
				}
				addComponent(new SmartGuiTextLabel(this) {
					{
						setCentered(true);
						setText(() -> {
							int v = (ii == 1 ? config.getGlobal().getSpeed()
									: (ii == 2 ? config.getClient().getDelay() : config.getClient().getHitdelay()));
							if (ii == 1) {
								return new DecimalFormat("0.0").format(v / 10.0);
							} else {
								return v + "";
							}
						});
					}
				}.setResizeAction(x -> x.setCenterSize(170, y + 10, 15, 15)));

			} else {
				addComponent(new GuiMelodyCraftButton(this) {
					{
						setText(I18n.format(get() ? "gui.settings.on" : "gui.settings.off"));
					}

					private boolean get() {
						if (ii == 4) {
							return config.getClient().isNoRender();
						} else {
							return config.getClient().isNoSound();
						}
					}

					@Override
					public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
						if (ii == 4) {
							config.getClient().setNoRender(!get());
						} else {
							config.getClient().setNoSound(!get());
						}
						setText(I18n.format(get() ? "gui.settings.on" : "gui.settings.off"));
					}
				}.setResizeAction(x -> x.setBounds(100, y, 50, 20)));
			}
		}
	}

	@Override
	public void onResizeSelf() {
		super.onResizeSelf();
		// text.setCenterSize(getSizeX() / 2, 16, 100, 16);
	}

}
