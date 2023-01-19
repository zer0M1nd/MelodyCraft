package hciu.pub.mcmod.melodycraft.client;

import hciu.pub.mcmod.melodycraft.item.ItemList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class RenderLoader {

	public static void registerItemRenders() {
		registerItemRender(ItemList.ARCADE);
	}

	private static void registerItemRender(Item i, int meta) {
		ModelLoader.setCustomModelResourceLocation(i, meta,
				new ModelResourceLocation(i.getRegistryName(), "inventory"));
	}

	private static void registerItemRender(Item i) {
		registerItemRender(i, 0);
	}

}
