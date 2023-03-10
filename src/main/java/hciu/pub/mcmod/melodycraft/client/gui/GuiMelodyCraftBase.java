package hciu.pub.mcmod.melodycraft.client.gui;

import hciu.pub.mcmod.hciusutils.gui.SmartGuiScreen;
import hciu.pub.mcmod.hciusutils.gui.render.FramedRectangleDrawer;

public class GuiMelodyCraftBase extends SmartGuiScreen {

	public GuiMelodyCraftBase(SmartGuiScreen parent) {
		super(parent, null);
		setTextureDrawer(new FramedRectangleDrawer<SmartGuiScreen>(this, GuiMelodyCraftConstants.COLOR_MAIN_FRAME,
				GuiMelodyCraftConstants.COLOR_MAIN_IN, 2));
	}

	public GuiMelodyCraftBase(SmartGuiScreen parent, SmartGuiScreen holder) {
		super(parent, holder);
		setTextureDrawer(new FramedRectangleDrawer<SmartGuiScreen>(this, GuiMelodyCraftConstants.COLOR_MAIN_FRAME,
				GuiMelodyCraftConstants.COLOR_MAIN_IN, 2));
	}

	@Override
	public void onResizeSelf() {
		this.setSize(width - 10, height - 10);
	}

}
