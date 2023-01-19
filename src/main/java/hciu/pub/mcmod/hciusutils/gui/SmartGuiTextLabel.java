package hciu.pub.mcmod.hciusutils.gui;

import net.minecraft.client.Minecraft;

public class SmartGuiTextLabel extends SmartGuiComponentBase {

	private String text = "";
	private boolean centered = false;

	public SmartGuiTextLabel(ISmartGuiComponent holder) {
		super(holder);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isCentered() {
		return centered;
	}

	public void setCentered(boolean centered) {
		this.centered = centered;
	}

	@Override
	public void drawSelf() {
		super.drawSelf();
		Minecraft mc = Minecraft.getMinecraft();
		if (centered) {
			this.drawCenteredString(mc.fontRenderer, text, this.actualX + this.sizeX / 2,
					this.actualY + (this.sizeY - 8) / 2, SmartGuiConstants.VANILLA_TEXT_COLOR_ENABLED);
		} else {
			mc.fontRenderer.drawSplitString(text, actualX, actualY, sizeX,
					SmartGuiConstants.VANILLA_TEXT_COLOR_ENABLED);
		}
	}

}
