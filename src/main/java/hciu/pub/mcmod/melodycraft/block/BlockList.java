package hciu.pub.mcmod.melodycraft.block;

import java.util.ArrayList;

import hciu.pub.mcmod.melodycraft.client.RenderLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockList {
	private static ArrayList<Block> allBlocks = new ArrayList<Block>(); // 必须写在所有方块对象前，否则会报错

	public static final Block ARCADE = new BlockArcade();

	public static void registerAllBlocks() {
		registerBlock(ARCADE);
		allBlocks.forEach(b -> registerBlockAndItem(b)); // 批量注册物品
	}

	private static void registerBlock(Block block) {
		ForgeRegistries.BLOCKS.register(block);
	}

	private static void registerBlockAndItem(Block block) {
		registerBlock(block);
		ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	// 批量注册物品材质
	@SideOnly(Side.CLIENT)
	public static void registerRenders() {
		allBlocks.forEach(b -> registerRender(b));
	}

	@SideOnly(Side.CLIENT)
	private static void registerRender(Block block) {
		RenderLoader.registerItemRender(Item.getItemFromBlock(block).setRegistryName(block.getRegistryName()));
	}

	// 使用这个方法手动添加方块到本mod的批量注册列表，但一般情况下你应该通过BlockFactory.create()来添加
	static void addBlock(Block b) {
		allBlocks.add(b);
	}
}
