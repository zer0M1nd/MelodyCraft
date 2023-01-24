package hciu.pub.mcmod.melodycraft.client.gui;

import java.text.DecimalFormat;

import org.lwjgl.input.Keyboard;

import hciu.pub.mcmod.hciusutils.gui.SmartGuiButton;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiComponentBase;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiScreen;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiTextLabel;
import hciu.pub.mcmod.hciusutils.gui.render.FramedRectangleDrawer;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftButton;
import hciu.pub.mcmod.melodycraft.config.MelodyCraftGameConfig;
import hciu.pub.mcmod.melodycraft.mug.EnumJudgeLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiSettings extends GuiMelodyCraftBase {

	private MelodyCraftGameConfig config;

	private String[] gamemodes;
	private int[] keyCounts;

	private int selectedGamemode = 0;
	private SmartGuiButton selectedButtonModify = null;
	private int selectedId = -1;

	private void getGamemodes() {
		gamemodes = new String[] { "4k", "5k", "6k", "7k" };
		keyCounts = new int[] { 4, 5, 6, 7 };
	}

	public GuiSettings(SmartGuiScreen parent) {
		super(parent);
		getGamemodes();
		config = MelodyCraftGameConfig.getInstance();

		addComponent(new SmartGuiTextLabel(this, I18n.format("melodycraft.gui.settings.title")) {
			{
				setCentered(true);
			}
		}.setResizeAction(x -> x.setCenterSize(getSizeX() / 2, 20, 100, 20)));
		addComponent(new GuiMelodyCraftButton(this, I18n.format("melodycraft.gui.button.save")) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				MelodyCraftGameConfig.save();
				Minecraft.getMinecraft().displayGuiScreen(getSupreme().getParent());
			}
		}.setResizeAction(x -> x.setCenterSize(getSizeX() - 60, getSizeY() - 20, 80, 20)));
		addComponent(new GuiMelodyCraftButton(this, I18n.format("melodycraft.gui.button.cancel")) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				MelodyCraftGameConfig.load();
				Minecraft.getMinecraft().displayGuiScreen(getSupreme().getParent());
			}

		}.setResizeAction(x -> x.setCenterSize(getSizeX() - 160, getSizeY() - 20, 80, 20)));

		String[] entry = new String[] { "judge", "speed", "delay", "judgedelay", "norender", "nosound" };
		for (int i = 0; i < entry.length; i++) {
			int y = 40 + 30 * i;
			String name = entry[i];
			addComponent(new SmartGuiTextLabel(this, I18n.format("melodycraft.gui.settings." + name))
					.setResizeAction(x -> x.setBounds(10, y + 6, 100, 20)));
			int ii = i;
			if (i == 0) {
				for (int k : new int[] { -1, 1 }) {

					addComponent(
							new GuiMelodyCraftButton(this, I18n.format("melodycraft.gui.button." + (k == 1 ? "right" : "left"))) {
								@Override
								public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
									int j = Math.max(0, Math.min(config.getGlobal().getJudge().ordinal() + k,
											EnumJudgeLevel.values().length - 1));
									config.getGlobal().setJudge(EnumJudgeLevel.values()[j]);
								}
							}.setResizeAction(x -> x.setBounds(k == 1 ? 140 : 80, y, 20, 20)));
				}
				addComponent(new SmartGuiTextLabel(this, () -> config.getGlobal().getJudge().name()) {
					{
						setCentered(true);
					}
				}.setResizeAction(x -> x.setCenterSize(120, y + 10, 15, 15)));

			} else if (i <= 3) {

				int mxv = new int[] { 0, 200, 1000, 1000 }[i];
				int mnv = new int[] { 0, 1, -1000, -1000 }[i];

				for (int k = 0; k < 4; k++) {
					int kk = k, d = new int[] { -10, -1, 1, 10 }[k];
					addComponent(new GuiMelodyCraftButton(this, I18n.format(new String[] { "melodycraft.gui.button.left2",
							"melodycraft.gui.button.left", "melodycraft.gui.button.right", "melodycraft.gui.button.right2" }[k])) {
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
					}.setResizeAction(x -> x.setBounds(80 + kk * 30 + (kk >= 2 ? 30 : 0), y, 20, 20)));
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
				}.setResizeAction(x -> x.setCenterSize(150, y + 10, 15, 15)));

			} else {
				addComponent(new GuiMelodyCraftButton(this) {
					{
						setText(I18n.format(get() ? "melodycraft.gui.settings.on" : "melodycraft.gui.settings.off"));
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
						setText(I18n.format(get() ? "melodycraft.gui.settings.on" : "melodycraft.gui.settings.off"));
					}
				}.setResizeAction(x -> x.setBounds(80, y, 50, 20)));
			}
		}

		SmartGuiComponentBase[] subs = new SmartGuiComponentBase[gamemodes.length];

		for (int i = 0; i < gamemodes.length; i++) {
			int ii = i;
			addComponent((subs[i] = (new SmartGuiComponentBase(this) {
				{
					setTextureDrawer(new FramedRectangleDrawer<>(this, GuiMelodyCraftConstants.COLOR_MAIN_FRAME,
							GuiMelodyCraftConstants.COLOR_MAIN_IN, 2));
					int cnt = keyCounts[ii];
					for (int j = 0; j < cnt; j++) {
						int jj = j;
						addComponent(new GuiMelodyCraftButton(this) {
							public String getText() {
								return this == selectedButtonModify ? "_"
										: Keyboard
												.getKeyName(config.getClient().getKeyBindingForMode(gamemodes[ii])[jj]);
							};

							@Override
							public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
								selectedButtonModify = this;
								selectedId = jj;
							}
						}.setResizeAction(x -> {
							int sx = (getSizeX() - 20) / 2;
							x.setBounds(5 + (jj % 2) * (sx + 10), 5 + (jj / 2) * 30, sx, 20);
						}));
					}
				}
			})));
			subs[i].setResizeAction(x -> x.setBounds(250, 70, getSizeX() - 250 - 20, getSizeY() - 70 - 50));
			subs[i].setVisible(i == selectedGamemode);
		}

		addComponent(new GuiMelodyCraftButton(this, () -> I18n.format("melodycraft.gui.settings.keybindings")
				+ I18n.format("melodycraft.charttype." + gamemodes[selectedGamemode])) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				selectedGamemode = (selectedGamemode + 1) % gamemodes.length;
				for (int i = 0; i < gamemodes.length; i++) {
					subs[i].setVisible(i == selectedGamemode);
				}
				selectedButtonModify = null;
			}
		}.setResizeAction(x -> x.setBounds(250, 40, getSizeX() - 250 - 20, 20)));
	}

	@Override
	public void onResizeSelf() {
		super.onResizeSelf();
		// text.setCenterSize(getSizeX() / 2, 16, 100, 16);
	}

	@Override
	public void onKeyPressed(char typedChar, int keyCode) {
		super.onKeyPressed(typedChar, keyCode);
		if (selectedButtonModify != null) {
			selectedButtonModify = null;
			config.getClient().getKeyBindingForMode(gamemodes[selectedGamemode])[selectedId] = keyCode;
		}
	}

}
