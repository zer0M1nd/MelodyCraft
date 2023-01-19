package hciu.pub.mcmod.melodycraft.block;

import hciu.pub.mcmod.melodycraft.item.ItemList;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockList {

	public static final BlockArcade ARCADE = new BlockArcade();

	public static void registerAllBlocks() {
		registerBlock(ARCADE, "arcade");

	}

	private static void registerBlock(Block block, String name) {
		block.setRegistryName(name).setUnlocalizedName(name).setCreativeTab(ItemList.MELODYCRAFT_TAB);
		ForgeRegistries.BLOCKS.register(block);
	}

	private static void registerBlockAndItem(Block block, String name) {
		registerBlock(block, name);
		ForgeRegistries.ITEMS.register(new ItemBlock(block));
	}

}
