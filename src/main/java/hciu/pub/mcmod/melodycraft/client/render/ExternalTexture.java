package hciu.pub.mcmod.melodycraft.client.render;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import hciu.pub.mcmod.melodycraft.MelodyCraftMod;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;

public class ExternalTexture extends AbstractTexture {

	protected final File file;
	protected int x,y;

	public ExternalTexture(File file) {
		super();
		this.file = file;
	}

	public void loadTexture(IResourceManager resourceManager) {
		this.deleteGlTexture();

		BufferedImage bufferedimage;
		try {
			bufferedimage = TextureUtil.readBufferedImage(new FileInputStream(file));
			x = bufferedimage.getWidth();
			y = bufferedimage.getHeight();
			TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, false, true);
		} catch (IOException e) {
			MelodyCraftMod.getLogger().warn("Failed to load external texture " + file.getAbsolutePath());
			MelodyCraftMod.getLogger().catching(e);
		}

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
}
