package hciu.pub.mcmod.melodycraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemList {

	public static final CreativeTabs MELODYCRAFT_TAB = new CreativeTabs("melodycraft") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Blocks.COMMAND_BLOCK);
		}
	};

	public static final ItemBlockArcade ARCADE = new ItemBlockArcade();

	public static void registerAllItems() {

		registerItemBlocks();
	}

	private static void registerItemBlocks() {
		registerItem(ARCADE, "arcade");
	}

	private static void registerItem(Item item, String name) {
		item.setRegistryName(name).setUnlocalizedName(name).setCreativeTab(MELODYCRAFT_TAB);
		ForgeRegistries.ITEMS.register(item);
	}

}
