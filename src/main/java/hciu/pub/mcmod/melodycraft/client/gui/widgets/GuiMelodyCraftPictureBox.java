package hciu.pub.mcmod.melodycraft.client.gui.widgets;

import java.io.File;

import hciu.pub.mcmod.hciusutils.gui.ISmartGuiComponent;
import hciu.pub.mcmod.hciusutils.gui.SmartGuiPictureBox;

public class GuiMelodyCraftPictureBox extends SmartGuiPictureBox {

	private File file;

	public GuiMelodyCraftPictureBox(ISmartGuiComponent holder) {
		super(holder);
	}

	public void setTexture(File file, int x, int y, int sx, int sy) {
		super.setTexture(null, x, y, sx, sy);
		this.file = file;
		setTextureDrawer(new MelodyCraftPictureBoxDrawer(this, file, x, y, sx, sy));
	}

}
