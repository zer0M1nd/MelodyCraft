package hciu.pub.mcmod.hciusutils.gui;

import hciu.pub.mcmod.hciusutils.gui.render.ScaleableTextureDrawer;
import net.minecraft.util.ResourceLocation;

public class SmartGuiPictureBox extends SmartGuiComponentBase {

	private ResourceLocation texture;
	private int textureX, textureY, textureSx, textureSy;

	public SmartGuiPictureBox(ISmartGuiComponent holder) {
		super(holder);
	}

	public void setTexture(ResourceLocation location, int x, int y, int sx, int sy) {
		texture = location;
		textureX = x;
		textureY = y;
		textureSx = sx;
		textureSy = sy;
		setTextureDrawer(new ScaleableTextureDrawer<ISmartGuiComponent>(this, location, x, y, sx, sy));
	}

	public ResourceLocation getTexture() {
		return texture;
	}

	public int getTextureX() {
		return textureX;
	}

	public int getTextureY() {
		return textureY;
	}

	public int getTextureSx() {
		return textureSx;
	}

	public int getTextureSy() {
		return textureSy;
	}

}
