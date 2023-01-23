package hciu.pub.mcmod.melodycraft.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import org.lwjgl.input.Keyboard;

import hciu.pub.mcmod.hciusutils.gui.SmartGuiScreen;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiTextLabel;
import hciu.pub.mcmod.hciusutils.gui.render.AbstractTextureDrawer;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftButton;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftPictureBox;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftSimpleList;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftTextField;
import hciu.pub.mcmod.melodycraft.client.sound.ExternalSoundHandler;
import hciu.pub.mcmod.melodycraft.mug.data.Song;
import hciu.pub.mcmod.melodycraft.mug.data.SongList;
import hciu.pub.mcmod.melodycraft.tileentity.TileEntityArcade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiSelectSong extends GuiMelodyCraftBase {

	private GuiMelodyCraftButton buttonNext;
	private GuiMelodyCraftButton buttonBack;
	private GuiMelodyCraftButton buttonPreview;
	private GuiMelodyCraftTextField textSearch;
	private GuiMelodyCraftSimpleList<Song> listSong;
	private GuiMelodyCraftPictureBox pictureBg;
	private SmartGuiTextLabel labelInfo;
	private GuiMelodyCraftButton buttonSettings;

	private UUID currentlyPlaying = null;
	private TileEntityArcade tileEntity;

	public GuiSelectSong(SmartGuiScreen parent, TileEntityArcade tileEntity) {
		super(parent);
		this.tileEntity = tileEntity;
		addComponent(buttonNext = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				if (listSong.getSelectedItem() != null) {
					Minecraft.getMinecraft().displayGuiScreen(new GuiSelectChart(GuiSelectSong.this,
							listSong.getSelectedItem(), GuiSelectSong.this.tileEntity));
				}
			}
		});
		addComponent(buttonBack = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				Minecraft.getMinecraft().displayGuiScreen(getSupreme().getParent());
			}
		});
		addComponent(buttonPreview = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				if (currentlyPlaying != null) {
					ExternalSoundHandler.getInstance().stopSound(currentlyPlaying);
					currentlyPlaying = null;
					setText(I18n.format("gui.preview"));
				} else {
					currentlyPlaying = ExternalSoundHandler.getInstance().playSound(listSong.getSelectedItem());
					setText(I18n.format("gui.stoppreview"));
				}
			}
		});
		buttonNext.setText(I18n.format("gui.next"));
		buttonBack.setText(I18n.format("gui.back"));
		buttonPreview.setText(I18n.format("gui.preview"));
		addComponent(pictureBg = new GuiMelodyCraftPictureBox(this));
		addComponent(labelInfo = new SmartGuiTextLabel(this));
		;
		addComponent(listSong = new GuiMelodyCraftSimpleList<Song>(this) {
			@Override
			public void onSelectionChanged() {
				Song song = getSelectedItem();
				if (song == null) {
					labelInfo.setText("");
					pictureBg.setTextureDrawer(AbstractTextureDrawer.createEmpty(pictureBg));
					return;
				}
				String s = song.getName();
				s = s + "\n" + I18n.format("gui.artist", song.getArtist());
				s = s + "\n" + I18n.format("gui.bpm", song.getBpm());
				s = s + "\n" + I18n.format("gui.date", song.getDate());
				labelInfo.setText(s);
				if (song.getBgfile() == null) {
					pictureBg.setTexture(GuiMelodyCraftConstants.MISCS, 0, 128, 128, 128);
				} else {
					pictureBg.setTexture(song.getBgfile(), 0, 0, 256, 256);
				}
			}
		});
		listSong.setButtonTexts(new String[] { I18n.format("gui.list.top"), I18n.format("gui.list.up"),
				I18n.format("gui.list.down"), I18n.format("gui.list.bottom") });
		listSong.setItems(SongList.getSongs());
		listSong.setDisplayFunction(e -> e.getName());
		addComponent(textSearch = new GuiMelodyCraftTextField(this) {
			@Override
			public void onKeyPressed(char typedChar, int keyCode) {
				super.onKeyPressed(typedChar, keyCode);
				if (getText().length() == 0) {
					listSong.setItems(SongList.getSongs());
				} else {
					listSong.setItems(filter(SongList.getSongs(),
							e -> e.getName().toLowerCase().contains(getText().toLowerCase())));
				}
			}

		});
		textSearch.setDefaultText(I18n.format("gui.searchsong"));
		addKeyBinding(Keyboard.KEY_RETURN, buttonNext, false);
		addComponent(buttonSettings = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiSettings(getSupreme()));
			}
		});
		buttonSettings.setText(I18n.format("gui.settings"));
	}

	@Override
	public void onResizeSelf() {
		super.onResizeSelf();
		buttonBack.setBounds(7, 7, 50, 20);
		buttonSettings.setBounds(77, 7, 50, 20);
		buttonPreview.setBounds(147, 7, 50, 20);
		textSearch.setBounds(ratioX(0.5), 7, 100, 15);
		labelInfo.setBounds(ratioX(0.5) + 110, 27, ratioX(0.5) - 125, 100);
		buttonNext.setBounds(getSizeX() - 57, getSizeY() - 27, 50, 20);
		listSong.setBounds(ratioX(0.5), 27, 100, getSizeY() - 34);
		int bgsz = Math.min(getSizeX() / 2 - 40, getSizeY() - 45);
		pictureBg.setBounds(30, 35, bgsz, bgsz);
	}

	public <T> List<T> filter(List<T> origin, Predicate<T> filter) {
		List<T> result = new ArrayList<T>();
		for (T t : origin) {
			if (filter.test(t)) {
				result.add(t);
			}
		}
		return result;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		if (currentlyPlaying != null) {
			ExternalSoundHandler.getInstance().stopSound(currentlyPlaying);
		}
	}

	public Song getSelected() {
		return listSong.getSelectedItem();
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		int[] keys = new int[] { Keyboard.KEY_LEFT, Keyboard.KEY_UP, Keyboard.KEY_DOWN, Keyboard.KEY_RIGHT };
		for (int i = 0; i < keys.length; i++) {
			if (keyCode == keys[i]) {
				listSong.pressButton(i);
			}
		}
	}

}
