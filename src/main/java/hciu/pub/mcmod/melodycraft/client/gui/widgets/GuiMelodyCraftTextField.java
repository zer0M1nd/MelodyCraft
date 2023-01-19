package hciu.pub.mcmod.melodycraft.client.gui.widgets;

import hciu.pub.mcmod.hciusutils.gui.ISmartGuiComponent;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiScreen;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiTextField;
import hciu.pub.mcmod.hciusutils.gui.render.FlexibleTextureDrawer;
import hciu.pub.mcmod.hciusutils.gui.render.FramedRectangleDrawer;
import hciu.pub.mcmod.melodycraft.client.gui.GuiMelodyCraftConstants;

public class GuiMelodyCraftTextField extends SmartGuiTextField {

	public GuiMelodyCraftTextField(ISmartGuiComponent holder) {
		super(holder);
		setTextureDrawer(new FramedRectangleDrawer<>(this, GuiMelodyCraftConstants.COLOR_TEXT_FRAME,
				GuiMelodyCraftConstants.COLOR_TEXT_INNER, 1));
	}

}
