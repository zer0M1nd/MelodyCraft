package hciu.pub.mcmod.hciusutils.gui.render;

import hciu.pub.mcmod.hciusutils.gui.ISmartGuiComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class FlexibleTextureDrawer<T extends ISmartGuiComponent> extends SimpleTextureDrawer<T> {

	public FlexibleTextureDrawer(T obj, ResourceLocation texture, int u, int v, int sx, int sy) {
		super(obj, texture, u, v, sx, sy);
	}

	@Override
	public void draw() {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(texture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int sx = obj.getSizeX(), sy = obj.getSizeY();
		int midx = sx / 2, midy = sy / 2;

		mc.currentScreen.drawTexturedModalRect(obj.getActualX(), obj.getActualY(), u, v, midx, midy);
		mc.currentScreen.drawTexturedModalRect(obj.getActualX() + midx, obj.getActualY(), u + this.sx - (sx - midx), v,
				sx - midx, midy);
		mc.currentScreen.drawTexturedModalRect(obj.getActualX(), obj.getActualY() + midy, u, v + this.sy - (sy - midy),
				midx, sy - midy);
		mc.currentScreen.drawTexturedModalRect(obj.getActualX() + midx, obj.getActualY() + midy,
				u + this.sx - (sx - midx), v + this.sy - (sy - midy), sx - midx, sy - midy);
	}
}
