package hciu.pub.mcmod.hciusutils.gui;

import hciu.pub.mcmod.hciusutils.gui.render.ButtonTextureDrawer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class SmartGuiButton extends SmartGuiComponentBase {

	private String text = "";
	private boolean enabled = true;

	public SmartGuiButton(ISmartGuiComponent holder) {
		super(holder);
		setTextureDrawer(new ButtonTextureDrawer(this, ButtonTextureDrawer.makeFlexibleSubs(this, 3,
				SmartGuiConstants.VANILLA_TEXTURE_WIDGETS, 0, 46, 200, 20, 0, 20)));
	}

	@Override
	public void drawSelf() {
		super.drawSelf();
		int j = 14737632;

		if (!this.enabled) {
			j = 10526880;
		} else if (this.checkMouse()) {
			j = 16777120;
		}
		Minecraft mc = Minecraft.getMinecraft();
		this.drawCenteredString(mc.fontRenderer, getText(), this.actualX + this.sizeX / 2,
				this.actualY + (this.sizeY - 8) / 2, j);

	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
