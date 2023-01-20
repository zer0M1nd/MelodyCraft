package hciu.pub.mcmod.melodycraft.client.gui.widgets;

import hciu.pub.mcmod.hciusutils.gui.ISmartGuiComponent;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiListSimple;
import hciu.pub.mcmod.hciusutils.gui.render.ButtonTextureDrawer;
import hciu.pub.mcmod.hciusutils.gui.render.FramedRectangleDrawer;
import hciu.pub.mcmod.melodycraft.client.gui.GuiMelodyCraftConstants;

public class GuiMelodyCraftSimpleList<T> extends SmartGuiListSimple<T> {

	public GuiMelodyCraftSimpleList(ISmartGuiComponent holder) {
		super(holder);
		setTextureDrawer(new FramedRectangleDrawer<>(this, GuiMelodyCraftConstants.COLOR_TEXT_FRAME,
				GuiMelodyCraftConstants.COLOR_TEXT_INNER, 1));
		setButtonTextureDrawer(e -> new ButtonTextureDrawer(e,
				ButtonTextureDrawer.makeRectangleSubs(e, 3,
						new int[] { GuiMelodyCraftConstants.COLOR_BUTTON_FRAME_DISABLED, GuiMelodyCraftConstants.COLOR_BUTTON_FRAME,
								GuiMelodyCraftConstants.COLOR_BUTTON_FRAME },
						new int[] { GuiMelodyCraftConstants.COLOR_BUTTON_INNER, GuiMelodyCraftConstants.COLOR_BUTTON_INNER,
								GuiMelodyCraftConstants.COLOR_BUTTON_INNER_SELECTED },
						1)));
		setBackgroundDrawer(e -> new FramedRectangleDrawer<>(e, GuiMelodyCraftConstants.COLOR_TEXT_FRAME_SELECTED,
				GuiMelodyCraftConstants.COLOR_TEXT_INNER, 1));
	}

}
