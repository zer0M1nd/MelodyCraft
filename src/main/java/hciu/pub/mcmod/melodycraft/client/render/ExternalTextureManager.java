package hciu.pub.mcmod.melodycraft.client.render;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import hciu.pub.mcmod.melodycraft.utils.ReflectionHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class ExternalTextureManager {

	private static ExternalTextureManager instance;

	public static ExternalTextureManager getInstance() {
		if (instance == null) {
			instance = new ExternalTextureManager();
		}
		return instance;
	}

	private Map<File, ExternalTexture> map;

	private ExternalTextureManager() {
		map = new HashMap<File, ExternalTexture>();
	}

	public void bindTexture(File resource) {
		if (!this.map.containsKey(resource)) {
			loadTexture(resource);
		}
		ExternalTexture itextureobject = this.map.get(resource);
		try {
			ReflectionHelper.method(TextureUtil.class, "bindTexture", "func_94277_a", null,
					itextureobject.getGlTextureId());
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
	
	public void loadTexture(File resource) {
		if(this.map.containsKey(resource)) {
			return;
		}
		ExternalTexture itextureobject = new ExternalTexture(resource);
		itextureobject.loadTexture(null);
		this.map.put(resource, itextureobject);
	}

	public ExternalTexture getTextureData(File f) {
		return map.get(f);
	}
}
