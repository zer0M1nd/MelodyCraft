package hciu.pub.mcmod.melodycraft.client.gui;

import hciu.pub.mcmod.hciusutils.gui.render.CustomCharsetManager;
import hciu.pub.mcmod.melodycraft.MelodyCraftMod;
import net.minecraft.util.ResourceLocation;

public class GuiMelodyCraftConstants {

	public static final ResourceLocation MUSIC_GAME = new ResourceLocation(MelodyCraftMod.MODID,
			"textures/gui/container/mug.png");
	public static final ResourceLocation MISCS = new ResourceLocation(MelodyCraftMod.MODID,
			"textures/gui/container/miscs.png");

	public static final int COLOR_MAIN_FRAME = getColorByRGB(0, 0, 0);
	public static final int COLOR_MAIN_IN = getColorByRGB(0, 3, 34);
	public static final int COLOR_BUTTON_FRAME_DISABLED = getColorByRGB(75, 78, 90);
	public static final int COLOR_BUTTON_FRAME = getColorByRGB(17, 153, 204);
	public static final int COLOR_BUTTON_INNER = getColorByRGB(0, 2, 17);
	public static final int COLOR_BUTTON_INNER_SELECTED = getColorByRGB(0, 62, 98);
	public static final int COLOR_TEXT_INNER = getColorByRGB(11, 12, 15);
	public static final int COLOR_TEXT_FRAME = getColorByRGB(62, 91, 159);
	public static final int COLOR_TEXT_FRAME_SELECTED = getColorByRGB(255, 242, 0);

	public static final CustomCharsetManager CHARSET_SMALL_NUMBER = new CustomCharsetManager(6, 7);
	public static final CustomCharsetManager CHARSET_MEDIUM_NUMBER = new CustomCharsetManager(8, 9);
	public static final CustomCharsetManager CHARSET_LARGE_NUMBER = new CustomCharsetManager(18, 20);

	static {
		CHARSET_SMALL_NUMBER.setChars("1234567890%.", MUSIC_GAME, 97, 0);
		CHARSET_MEDIUM_NUMBER.setChars("1234567890%.", MUSIC_GAME, 0, 0);
		CHARSET_LARGE_NUMBER.setChars("12345", MUSIC_GAME, 0, 9);
		CHARSET_LARGE_NUMBER.setChars("67890", MUSIC_GAME, 0, 29);
	}

	public static int getColorByRGB(int r, int g, int b) {
		return (0xFF << 24) | (r << 16) | (g << 8) | b;
	}

	public static int getColorByRGBA(int r, int g, int b, int a) {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	/**
	 * 
	 * @param h 0 ~ 360
	 * @param s 0 ~ 100
	 * @param v 0 ~ 100
	 * @return
	 */
	public static int getColorByHSV(int h, int s, int v) {
		int[] rgb = HSVtoRGB(new float[] { (float) (h / 360.0), (float) (s / 100.0), (float) (v / 100.0) });
		return getColorByRGB(rgb[0], rgb[1], rgb[2]);
	}

	public static int[] HSVtoRGB(float[] hsv) {
		int h = (int) (hsv[0] * 6);
		float f = hsv[0] * 6 - h;
		float p = hsv[2] * (1 - hsv[1]);
		float q = hsv[2] * (1 - f * hsv[1]);
		float t = hsv[2] * (1 - (1 - f) * hsv[1]);
		switch (h) {
		case 0:
			return new int[] { (int) (hsv[2] * 255), (int) (t * 255), (int) (p * 255) };
		case 1:
			return new int[] { (int) (q * 255), (int) (hsv[2] * 255), (int) (p * 255) };
		case 2:
			return new int[] { (int) (p * 255), (int) (hsv[2] * 255), (int) (t * 255) };
		case 3:
			return new int[] { (int) (p * 255), (int) (q * 255), (int) (hsv[2] * 255) };
		case 4:
			return new int[] { (int) (t * 255), (int) (p * 255), (int) (hsv[2] * 255) };
		case 5:
			return new int[] { (int) (hsv[2] * 255), (int) (p * 255), (int) (q * 255) };
		default:
			return new int[] { 0, 0, 0 };
		}
	}

}
