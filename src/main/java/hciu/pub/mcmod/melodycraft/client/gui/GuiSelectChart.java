package hciu.pub.mcmod.melodycraft.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import hciu.pub.mcmod.hciusutils.gui.SmartGuiTextLabel;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftButton;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftPictureBox;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftSimpleList;
import hciu.pub.mcmod.melodycraft.config.MelodyCraftGameConfig;
import hciu.pub.mcmod.melodycraft.mug.EnumGameSide;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGameKeys;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGameSettings;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGameSettingsClient;
import hciu.pub.mcmod.melodycraft.mug.data.Chart;
import hciu.pub.mcmod.melodycraft.mug.data.Chart.ChartKeyMode;
import hciu.pub.mcmod.melodycraft.mug.saves.PlayResult;
import hciu.pub.mcmod.melodycraft.mug.saves.ResultManager;
import hciu.pub.mcmod.melodycraft.tileentity.TileEntityArcade;
import hciu.pub.mcmod.melodycraft.mug.data.Song;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiSelectChart extends GuiMelodyCraftBase {

	private GuiMelodyCraftButton buttonNext;
	private GuiMelodyCraftButton buttonBack;
	private GuiMelodyCraftSimpleList<Chart> listChart;
	private GuiMelodyCraftPictureBox pictureBg;
	private SmartGuiTextLabel labelInfo;
	private GuiMelodyCraftButton buttonSettings;

	private Song song;
	private TileEntityArcade tileEntity;

	public GuiSelectChart(GuiSelectSong parent, Song song, TileEntityArcade tileEntity) {
		super(parent);
		this.song = song;
		this.tileEntity = tileEntity;
		addComponent(buttonBack = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				Minecraft.getMinecraft().displayGuiScreen(getSupreme().getParent());
			}
		});
		buttonBack.setText(I18n.format("gui.back"));
		addComponent(pictureBg = new GuiMelodyCraftPictureBox(this));
		addComponent(labelInfo = new SmartGuiTextLabel(this));
		;
		addComponent(listChart = new GuiMelodyCraftSimpleList<Chart>(this) {
			@Override
			public void onSelectionChanged() {
				updateText();
			}
		});
		listChart.setButtonTexts(new String[] { I18n.format("gui.list.top"), I18n.format("gui.list.up"),
				I18n.format("gui.list.down"), I18n.format("gui.list.bottom") });
		listChart.setItems(song.getCharts());
		listChart.setDisplayFunction(e -> e.getInfoDifficulty());
		if (song.getBgfile() == null) {
			pictureBg.setTexture(GuiMelodyCraftConstants.MISCS, 0, 128, 128, 128);
		} else {
			pictureBg.setTexture(song.getBgfile(), 0, 0, 256, 256);
		}
		addComponent(buttonSettings = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiSettings(getSupreme()));
			}
		});
		buttonSettings.setText(I18n.format("gui.settings"));

		addComponent(buttonNext = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				startGame(song, (ChartKeyMode) listChart.getSelectedItem(),
						MelodyCraftGameConfig.getInstance().getGlobal());
			}
		});
		buttonNext.setText(I18n.format("gui.startgame"));
		addKeyBinding(Keyboard.KEY_RETURN, buttonNext, false);
	}

	private void startGame(Song song, Chart chart, MelodyCraftGameSettings settings) {
		MelodyCraftGameKeys game = null;
		if (chart instanceof ChartKeyMode) {
			game = new MelodyCraftGameKeys(tileEntity, song, (ChartKeyMode) chart, settings,
					Minecraft.getMinecraft().player, EnumGameSide.CONTROLLED);
		} else {
			throw new IllegalArgumentException("Unknown Chart Type! Unexpected!");
		}
		tileEntity.setGame(game);
		Minecraft.getMinecraft()
				.displayGuiScreen(new GuiGame(this, tileEntity, MelodyCraftGameConfig.getInstance().getClient()));
	}

	@Override
	public void onResizeSelf() {
		super.onResizeSelf();
		listChart.setBounds(ratioX(0.5), 7, 100, getSizeY() - 14);
		buttonNext.setCorners(listChart.getRelativeX() + listChart.getSizeX() + 5, getSizeY() - 27, getSizeX() - 7,
				getSizeY() - 7);
		buttonBack.setBounds(7, 7, 50, 20);
		buttonSettings.setBounds(77, 7, 50, 20);
		int bgsz = Math.min(getSizeX() / 2 - 40, getSizeY() - 45);
		pictureBg.setBounds(30, 35, bgsz, bgsz);
		labelInfo.setBounds(ratioX(0.5) + 110, 27, ratioX(0.5) - 125, ratioY(1.0) - 32);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		int[] keys = new int[] { Keyboard.KEY_LEFT, Keyboard.KEY_UP, Keyboard.KEY_DOWN, Keyboard.KEY_RIGHT };
		for (int i = 0; i < keys.length; i++) {
			if (keyCode == keys[i]) {
				listChart.pressButton(i);
			}
		}
	}

	public void updateText() {
		Chart x = listChart.getSelectedItem();
		if (x == null) {
			labelInfo.setText("");
		} else {
			String t = x.getInfo();
			t = t + "\n" + I18n.format("gui.author", x.getAuthor());
			t = t + "\n" + I18n.format("gui.mode", x.getModeName());
			t = t + "\n" + I18n.format("gui.difficulty", Integer.toString(x.getDifficulty()));
			t = t + "\n" + I18n.format("gui.date", x.getDate());

			t = t + "\n\n";

			PlayResult res = ResultManager.getInstance().getBestFor(x);

			if (res == null) {
				t = t + "\n" + I18n.format("gui.neverplayed");
			} else {
				t = t + "\n" + I18n.format("gui.bestscore");
				t = t + "\n" + res.getScore();
				t = t + "\n" + res.getJudgeLevel().name() + "  " + res.accStr() + "  "
						+ (res.checkAP() ? "AP" : (res.checkFC() ? "FC" : ""));
			}

			labelInfo.setText(t);
		}
	}
}
