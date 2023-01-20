package hciu.pub.mcmod.melodycraft.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CreativeTabMelodyCraft {
	public static final CreativeTabs MELODYCRAFT_TAB = new CreativeTabs("melodycraft") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Blocks.COMMAND_BLOCK);
		}
	};
}
