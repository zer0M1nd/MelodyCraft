package hciu.pub.mcmod.melodycraft.client;

import hciu.pub.mcmod.melodycraft.block.BlockList;
import hciu.pub.mcmod.melodycraft.item.ItemList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderLoader {

	public static void registerRenders() {
		BlockList.registerRenders();
		registerItemRender(ItemList.ARCADE);
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemRender(Item i, int meta) {
		ModelLoader.setCustomModelResourceLocation(i, meta,
				new ModelResourceLocation(i.getRegistryName(), "inventory"));
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemRender(Item i) {
		registerItemRender(i, 0);
	}

}
