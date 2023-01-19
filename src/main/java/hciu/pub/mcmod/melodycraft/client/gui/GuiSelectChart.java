package hciu.pub.mcmod.melodycraft.client.gui;

import hciu.pub.mcmod.hciusutils.gui.SmartGuiTextLabel;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftButton;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftPictureBox;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftSimpleList;
import hciu.pub.mcmod.melodycraft.mug.EnumGameSide;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGameKeys;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGameSettings;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGameSettingsClient;
import hciu.pub.mcmod.melodycraft.mug.data.Chart;
import hciu.pub.mcmod.melodycraft.mug.data.Chart.ChartKeyMode;
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
	private GuiMelodyCraftButton buttonSelectJudge;

	private SubGuiScreenSelectJudge screenSelectJudge;

	private Song song;
	private TileEntityArcade tileEntity;

	public GuiSelectChart(GuiSelectSong parent, Song song, TileEntityArcade tileEntity) {
		super(parent);
		this.song = song;
		this.tileEntity = tileEntity;
		addComponent(buttonNext = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				startGame(song, (ChartKeyMode) listChart.getSelectedItem(), new MelodyCraftGameSettings());
			}
		});
		addComponent(buttonBack = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				Minecraft.getMinecraft().displayGuiScreen(getSupreme().getParent());
			}
		});
		buttonNext.setText(I18n.format("gui.startgame"));
		buttonBack.setText(I18n.format("gui.back"));
		addComponent(pictureBg = new GuiMelodyCraftPictureBox(this));
		addComponent(labelInfo = new SmartGuiTextLabel(this));
		;
		addComponent(listChart = new GuiMelodyCraftSimpleList<Chart>(this) {
			@Override
			public void onSelectionChanged() {
				Chart x = getSelectedItem();
				if (x == null) {
					labelInfo.setText("");
				} else {
					String t = x.getInfo();
					t = t + "\n" + I18n.format("gui.author", x.getAuthor());
					t = t + "\n" + I18n.format("gui.mode", x.getModeName());
					t = t + "\n" + I18n.format("gui.difficulty", Integer.toString(x.getDifficulty()));
					t = t + "\n" + I18n.format("gui.date", x.getDate());
					labelInfo.setText(t);
				}

			}
		});
		listChart.setButtonTexts(new String[] { I18n.format("gui.list.top"), I18n.format("gui.list.up"),
				I18n.format("gui.list.down"), I18n.format("gui.list.bottom") });
		listChart.setItems(song.getCharts());
		listChart.setDisplayFunction(e -> e.getInfo() + " Lv." + e.getDifficulty());
		if (song.getBgfile() == null) {
			pictureBg.setTexture(GuiMelodyCraftConstants.MISCS, 0, 128, 128, 128);
		} else {
			pictureBg.setTexture(song.getBgfile(), 0, 0, 256, 256);
		}
		addComponent(buttonSelectJudge = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				screenSelectJudge.setVisible(true);
			}
		});
		buttonSelectJudge.setText(I18n.format("gui.judge"));
		addComponent(screenSelectJudge = new SubGuiScreenSelectJudge(null, this));
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
		Minecraft.getMinecraft().displayGuiScreen(new GuiGame(this, tileEntity, new MelodyCraftGameSettingsClient()));
	}

	@Override
	public void onResizeSelf() {
		super.onResizeSelf();
		listChart.setBounds(ratioX(0.5), 7, 100, getSizeY() - 14);
		buttonNext.setCorners(listChart.getRelativeX() + listChart.getSizeX() + 5, getSizeY() - 27, getSizeX() - 7,
				getSizeY() - 7);
		buttonBack.setBounds(7, 7, 50, 20);
		buttonSelectJudge.setBounds(getSizeX() - 53, 7, 50, 20);
		int bgsz = Math.min(getSizeX() / 2 - 40, getSizeY() - 45);
		pictureBg.setBounds(30, 35, bgsz, bgsz);
		labelInfo.setBounds(ratioX(0.5) + 110, 52, ratioX(0.5) - 125, 100);
		screenSelectJudge.setCenterSize(ratioX(0.5), ratioY(0.5), 100,50);
	}

}
