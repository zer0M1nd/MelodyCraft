package hciu.pub.mcmod.melodycraft.client.gui.widgets;

import java.util.UUID;

import hciu.pub.mcmod.hciusutils.gui.ISmartGuiComponent;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiComponentBase;
import hciu.pub.mcmod.melodycraft.client.sound.ExternalSoundHandler;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGame;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGameSettingsClient;

public abstract class GuiMelodyCraftClient extends SmartGuiComponentBase {
	
	protected UUID currentlyPlaying = null;
	protected MelodyCraftGame game;
	protected MelodyCraftGameSettingsClient clientSettings;


	public GuiMelodyCraftClient(ISmartGuiComponent holder, MelodyCraftGame game,
			MelodyCraftGameSettingsClient clientSettings) {
		super(holder);
		this.game = game;
		this.clientSettings = clientSettings;
	}

	public MelodyCraftGame getGame() {
		return game;
	}

	public void startGame() {
		currentlyPlaying = null;
		getGame().startGame();
	}

	public void pause() {
		if (currentlyPlaying != null) {
			ExternalSoundHandler.getInstance().pauseSound(currentlyPlaying);
		}
		getGame().pause();
	}

	public void resume() {
		if (currentlyPlaying != null) {
			ExternalSoundHandler.getInstance().resumeSound(currentlyPlaying);
		}
		getGame().resume();
	}

	public void endGame() {
		if (currentlyPlaying != null) {
			ExternalSoundHandler.getInstance().stopSound(currentlyPlaying);
		}
		getGame().endGame();
	}
}
