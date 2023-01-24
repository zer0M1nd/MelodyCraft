package hciu.pub.mcmod.melodycraft.creativetab;

import hciu.pub.mcmod.melodycraft.block.BlockList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabMelodyCraft {
	public static final CreativeTabs MELODYCRAFT_TAB = new CreativeTabs("melodycraft") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(BlockList.ARCADE);
		}
	};
}
