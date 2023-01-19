package hciu.pub.mcmod.melodycraft.mug;

import org.lwjgl.input.Keyboard;

public class MelodyCraftGameSettingsClient {

	private int[] keyBindings = new int[] { Keyboard.KEY_A, Keyboard.KEY_S, Keyboard.KEY_D, Keyboard.KEY_SPACE,
			Keyboard.KEY_L, Keyboard.KEY_SEMICOLON, Keyboard.KEY_APOSTROPHE };

	private int[] keyBindingsAdjust = new int[] { Keyboard.KEY_UP, Keyboard.KEY_DOWN };

	private int delay = 300, hitdelay = 0;
	private boolean noRender = false, noSound = true;

	public MelodyCraftGameSettingsClient() {
	}

	public MelodyCraftGameSettingsClient(int[] keyBindings, int delay, int hitdelay, int speed, boolean noRender,
			boolean noSound) {
		super();
		this.keyBindings = keyBindings;
		this.delay = delay;
		this.hitdelay = hitdelay;
		this.noRender = noRender;
		this.noSound = noSound;
	}

	public boolean isNoRender() {
		return noRender;
	}

	public boolean isNoSound() {
		return noSound;
	}

	public int getDelay() {
		return delay;
	}

	public int[] getKeyBindings() {
		return keyBindings;
	}

	public int getHitdelay() {
		return hitdelay;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int[] getKeyBindingsAdjust() {
		return keyBindingsAdjust;
	}

}
