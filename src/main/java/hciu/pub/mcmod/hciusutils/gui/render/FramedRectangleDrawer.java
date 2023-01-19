package hciu.pub.mcmod.hciusutils.gui.render;

import hciu.pub.mcmod.hciusutils.gui.ISmartGuiComponent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class FramedRectangleDrawer<T extends ISmartGuiComponent> extends AbstractTextureDrawer<T> {

	private int colorOut, colorIn, boarderWidth;

	public FramedRectangleDrawer(T obj, int colorOut, int colorIn, int boarderWidth) {
		super(obj);
		this.colorOut = colorOut;
		this.colorIn = colorIn;
		this.boarderWidth = boarderWidth;
	}

	@Override
	public void draw() {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Gui.drawRect(obj.getActualX(), obj.getActualY(), obj.getActualX() + obj.getSizeX(),
				obj.getActualY() + obj.getSizeY(), colorOut);
		Gui.drawRect(obj.getActualX() + boarderWidth, obj.getActualY() + boarderWidth,
				obj.getActualX() + obj.getSizeX() - boarderWidth, obj.getActualY() + obj.getSizeY() - boarderWidth,
				colorIn);
	}

}
