package hciu.pub.mcmod.hciusutils.gui.render;

import hciu.pub.mcmod.hciusutils.gui.ISmartGuiComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class SimpleTextureDrawer<T extends ISmartGuiComponent> extends AbstractTextureDrawer<T> {

	protected ResourceLocation texture;
	protected int u, v, sx, sy;


	public SimpleTextureDrawer(T obj, ResourceLocation texture, int u, int v, int sx, int sy) {
		super(obj);
		this.texture = texture;
		this.u = u;
		this.v = v;
		this.sx = sx;
		this.sy = sy;
	}


	@Override
	public void draw() {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(texture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.currentScreen.drawTexturedModalRect(obj.getActualX(), obj.getActualY(), u, v, sx, sy);
	}

}
