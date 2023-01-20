package hciu.pub.mcmod.melodycraft.client.gui.widgets;

import hciu.pub.mcmod.hciusutils.gui.ISmartGuiComponent;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiButton;
import hciu.pub.mcmod.hciusutils.gui.render.ButtonTextureDrawer;
import hciu.pub.mcmod.melodycraft.client.gui.GuiMelodyCraftConstants;

public class GuiMelodyCraftButton extends SmartGuiButton {

	public GuiMelodyCraftButton(ISmartGuiComponent holder) {
		super(holder);
		setTextureDrawer(new ButtonTextureDrawer(this,
				ButtonTextureDrawer.makeRectangleSubs(this, 3,
						new int[] { GuiMelodyCraftConstants.COLOR_BUTTON_FRAME_DISABLED, GuiMelodyCraftConstants.COLOR_BUTTON_FRAME,
								GuiMelodyCraftConstants.COLOR_BUTTON_FRAME },
						new int[] { GuiMelodyCraftConstants.COLOR_BUTTON_INNER, GuiMelodyCraftConstants.COLOR_BUTTON_INNER,
								GuiMelodyCraftConstants.COLOR_BUTTON_INNER_SELECTED },
						1)));
	}

}
