package hciu.pub.mcmod.hciusutils.gui.render;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class CustomCharsetManager {

	private Map<Character, CustomChar> charMap = new HashMap<>();
	private int sx, sy;

	public CustomCharsetManager(int sx, int sy) {
		this.sx = sx;
		this.sy = sy;
	}

	public void draw(String str, int x, int y, boolean rightAlign) {
		if (rightAlign) {
			draw(str, x - str.length() * sx, y, false);
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		for (int i = 0; i < str.length(); i++) {
			CustomChar cc = charMap.get(str.charAt(i));
			if (cc != null) {
				mc.getTextureManager().bindTexture(cc.resource);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.currentScreen.drawTexturedModalRect(x + i * sx, y, cc.x, cc.y, sx, sy);
			}
		}
	}

	public void draw(String str, float x, float y, boolean rightAlign) {
		if (rightAlign) {
			draw(str, x - str.length() * sx, y, false);
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		for (int i = 0; i < str.length(); i++) {
			CustomChar cc = charMap.get(str.charAt(i));
			if (cc != null) {
				mc.getTextureManager().bindTexture(cc.resource);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.currentScreen.drawTexturedModalRect(x + i * sx, y, cc.x, cc.y, sx, sy);
			}
		}
	}

	public void setChar(char ch, ResourceLocation resource, int x, int y) {
		charMap.put(ch, new CustomChar(resource, x, y));
	}

	public void setChars(String chars, ResourceLocation resource, int x, int y) {
		for (int i = 0; i < chars.length(); i++) {
			setChar(chars.charAt(i), resource, x + sx * i, y);
		}
	}

	public static class CustomChar {
		private ResourceLocation resource;
		private int x, y;

		public CustomChar(ResourceLocation resource, int x, int y) {
			this.resource = resource;
			this.x = x;
			this.y = y;
		}
	}
}
