package hciu.pub.mcmod.hciusutils.gui.render;

import hciu.pub.mcmod.hciusutils.gui.SmartGuiButton;
import net.minecraft.util.ResourceLocation;

public class ButtonTextureDrawer extends MultipleTextureDrawer<SmartGuiButton> {

	public ButtonTextureDrawer(SmartGuiButton obj, AbstractTextureDrawer<SmartGuiButton>[] subs) {
		super(obj, subs);
	}

	@Override
	protected int getModifier() {
		// System.out.println(obj.getActualX() + "," + obj.getActualY() + "," +
		// Mouse.getX() + "," + Mouse.getY() + ","
		// + Display.getX() + "," + Display.getY() + "," + obj.getMouseX() + "," +
		// obj.getMouseY());
		if (!obj.isEnabled()) {
			return 0;
		} else if (obj.checkMouse()) {
			return 2;
		}
		return 1;
	}

}
