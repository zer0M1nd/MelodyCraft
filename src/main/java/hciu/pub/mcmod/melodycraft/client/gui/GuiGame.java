package hciu.pub.mcmod.melodycraft.client.gui;

import org.lwjgl.input.Keyboard;

import hciu.pub.mcmod.hciusutils.gui.SmartGuiScreen;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftButton;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftClient;
import hciu.pub.mcmod.melodycraft.client.gui.widgets.GuiMelodyCraftClientKeys;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGame;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGameKeys;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGameSettingsClient;
import hciu.pub.mcmod.melodycraft.tileentity.TileEntityArcade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiGame extends GuiMelodyCraftBase {

	private GuiMelodyCraftClient gameClient;
	private TileEntityArcade tileEntity;
	private MelodyCraftGameSettingsClient clientSettings;

	private GuiMelodyCraftButton pause;
	protected SubGuiScreenPause screenPause;

	public GuiGame(SmartGuiScreen parent, TileEntityArcade tileEntity, MelodyCraftGameSettingsClient clientSettings) {
		super(parent);
		this.tileEntity = tileEntity;
		this.clientSettings = clientSettings;
		if (tileEntity.getGame() instanceof MelodyCraftGameKeys) {
			addComponent(gameClient = new GuiMelodyCraftClientKeys(this, (MelodyCraftGameKeys) tileEntity.getGame(),
					clientSettings));
			setFocus(gameClient);
		} else {
			throw new IllegalArgumentException("Unknown Chart Type! Unexpected!");
		}

		addComponent(screenPause = new SubGuiScreenPause(null, this, gameClient));
		screenPause.setVisible(false);

		addComponent(pause = new GuiMelodyCraftButton(this) {
			@Override
			public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
				screenPause.setVisible(true);
				gameClient.pause();
			}
		});

		pause.setText(I18n.format("gui.button.pause"));
		pause.setVisible(true);

	}

	@Override
	public void onResizeSelf() {
		super.onResizeSelf();
		gameClient.setCorners(2, 2, getSizeX() - 2, getSizeY() - 2);
		screenPause.setCenterSize(ratioX(0.5), ratioY(0.5), 100, 100);
		pause.setBounds(7, 7, 50, 20);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.onMouseClicked(mouseX, mouseY, mouseButton);
		setFocus(gameClient);
	}

	@Override
	public void onKeyPressed(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_F1) {
			pause.onMouseClicked(0, 0, 0);
		} else {
			super.onKeyPressed(typedChar, keyCode);
		}
	}

	public TileEntityArcade getTileEntity() {
		return tileEntity;
	}

	public MelodyCraftGameSettingsClient getClientSettings() {
		return clientSettings;
	}
}
