package hciu.pub.mcmod.melodycraft.mug;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

public class MelodyCraftGameSettingsClient {

	private int delay = 300, hitdelay = 0;
	private boolean noRender = false, noSound = true;

	private HashMap<String, int[]> keyBinding = new HashMap<>();

	public MelodyCraftGameSettingsClient() {
		keyBinding.put("4K", new int[] { Keyboard.KEY_S, Keyboard.KEY_D, Keyboard.KEY_L, Keyboard.KEY_SEMICOLON });
		keyBinding.put("5K", new int[] { Keyboard.KEY_S, Keyboard.KEY_D, Keyboard.KEY_SPACE, Keyboard.KEY_L,
				Keyboard.KEY_SEMICOLON });
		keyBinding.put("6K", new int[] { Keyboard.KEY_A, Keyboard.KEY_S, Keyboard.KEY_D, Keyboard.KEY_L,
				Keyboard.KEY_SEMICOLON, Keyboard.KEY_APOSTROPHE });
		keyBinding.put("7K", new int[] { Keyboard.KEY_A, Keyboard.KEY_S, Keyboard.KEY_D, Keyboard.KEY_SPACE,
				Keyboard.KEY_L, Keyboard.KEY_SEMICOLON, Keyboard.KEY_APOSTROPHE });
	}

	public boolean isNoRender() {
		return noRender;
	}

	public boolean isNoSound() {
		return noSound;
	}
	
	public void setNoRender(boolean noRender) {
		this.noRender = noRender;
	}
	
	public void setNoSound(boolean noSound) {
		this.noSound = noSound;
	}

	public int getDelay() {
		return delay;
	}

	public int[] getKeyBindingForMode(String mode) {
		return keyBinding.getOrDefault(mode, null);
	}

	public int getHitdelay() {
		return hitdelay;
	}
	
	public void setHitdelay(int hitdelay) {
		this.hitdelay = hitdelay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public HashMap<String, int[]> getKeyBinding() {
		return keyBinding;
	}

}
