package hciu.pub.mcmod.melodycraft.item;

import java.util.ArrayList;

import hciu.pub.mcmod.melodycraft.client.RenderLoader;
import hciu.pub.mcmod.melodycraft.creativetab.CreativeTabMelodyCraft;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemList {
	private static ArrayList<Item> allItems = new ArrayList<Item>(); // 必须写在所有物品对象前，否则会报错

	public static final Item ARCADE = ItemFactory.create(ItemBlockArcade.class, "arcade", CreativeTabMelodyCraft.MELODYCRAFT_TAB, 64);

	public static void registerAllItems() {
		registerItemBlocks();
	}

	private static void registerItemBlocks() {
		registerItem(ARCADE);
	}

	private static void registerItem(Item item) {
		ForgeRegistries.ITEMS.register(item);
	}

	// 批量注册物品材质
	@SideOnly(Side.CLIENT)
	public static void registerRenders() {
		allItems.forEach(i -> RenderLoader.registerItemRender(i));
	}

	public static void addItem(Item i) {
		allItems.add(i);
	}
}
