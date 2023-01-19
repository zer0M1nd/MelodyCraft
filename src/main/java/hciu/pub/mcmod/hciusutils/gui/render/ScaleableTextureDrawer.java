package hciu.pub.mcmod.hciusutils.gui.render;

import hciu.pub.mcmod.hciusutils.gui.ISmartGuiComponent;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiComponentBase;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ScaleableTextureDrawer<T extends ISmartGuiComponent> extends SimpleTextureDrawer<T> {

	public ScaleableTextureDrawer(T obj, ResourceLocation texture, int u, int v, int sx, int sy) {
		super(obj, texture, u, v, sx, sy);
	}

	@Override
	public void draw() {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(texture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		SmartGuiScreen.drawScaledTexturedModelRect(obj.getActualX(), obj.getActualY(), obj.getSizeX(),
				obj.getSizeY(), u, v, sx, sy);
	}

}
