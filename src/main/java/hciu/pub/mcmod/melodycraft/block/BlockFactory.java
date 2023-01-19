package hciu.pub.mcmod.melodycraft.block;

import java.lang.reflect.Constructor;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

/**
 * 方块工厂，调用create()可返回一个简单的方块对象
 *
 * @author AoXiang_Soar
 */
public class BlockFactory {
	/**
	 * 调用此函数可以返回一个简单的方块并自动完成注册过程，不可用于没有无参构造方法的类
	 * 
	 * @param cls 该方块的类，如果你的方块是没有任何额外功能的普通方块（如钻石块），可以填null
	 * @param name 该方块的注册名
	 * @param resistance 该方块的抗爆等级
	 * @param hardness 该方块的硬度
	 * @param tool 开采该方块所需的工具类型
	 * @param harvestLevel 开采该方块所需的工具等级
	 * @param tab 该方块所在的创造模式物品栏，不加入可以填null
	 * @return 一个方块对象
	 */
	public static Block create(@Nullable Class<? extends Block> cls, String name, @Nullable Float resistance,
			@Nullable Float hardness, @Nullable String tool, @Nullable Integer harvestLevel,
			@Nullable CreativeTabs tab) {
		try {
			Block b;
			if (cls != null) {
				Constructor<? extends Block> con = cls.getConstructor();
				b = con.newInstance();
			} else
				b = new Block(Material.GROUND);
			b.setRegistryName(name).setUnlocalizedName(name);
			if (hardness != null)
				b.setHardness(hardness);
			if (resistance != null)
				b.setResistance(resistance);
			if ((tool != null) || (harvestLevel != null))
				b.setHarvestLevel(tool, harvestLevel);
			b.setCreativeTab(tab);
			BlockList.addBlock(b);
			return b;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
