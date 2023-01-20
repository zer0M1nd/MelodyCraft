package hciu.pub.mcmod.melodycraft.client.gui.widgets;

import java.io.File;

import hciu.pub.mcmod.hciusutils.gui.SmartGuiScreen;
import hciu.pub.mcmod.hciusutils.gui.render.ScaleableTextureDrawer;
import hciu.pub.mcmod.melodycraft.client.render.ExternalTextureManager;
import net.minecraft.client.renderer.GlStateManager;

public class MelodyCraftPictureBoxDrawer extends ScaleableTextureDrawer<GuiMelodyCraftPictureBox>{

	public File file;
	
	public MelodyCraftPictureBoxDrawer(GuiMelodyCraftPictureBox obj, File file, int u, int v, int sx,
			int sy) {
		super(obj, null, u, v, sx, sy);
		this.file = file;
	}

	
	@Override
	public void draw() {
		ExternalTextureManager.getInstance().bindTexture(file);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		SmartGuiScreen.drawScaledTexturedModelRect(obj.getActualX(), obj.getActualY(), obj.getSizeX(),
				obj.getSizeY(), u, v, sx, sy);
	}
}
