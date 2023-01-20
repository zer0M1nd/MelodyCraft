package hciu.pub.mcmod.melodycraft.block;

import hciu.pub.mcmod.melodycraft.client.gui.GuiMain;
import hciu.pub.mcmod.melodycraft.tileentity.TileEntityArcade;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockArcade extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool UPPER = PropertyBool.create("upper");

	protected static final AxisAlignedBB[] AABB = new AxisAlignedBB[] { new AxisAlignedBB(0, 0, 0.875, 1, 1, 1),
			new AxisAlignedBB(0, 0, 0, 1, 1, 0.125), new AxisAlignedBB(0.875, 0, 0, 1, 1, 1),
			new AxisAlignedBB(0, 0, 0, 0.125, 1, 1) };

	public BlockArcade() {
		super(Material.IRON);
		this.setCreativeTab(CreativeTabMelodyCraft.MELODYCRAFT_TAB).setRegistryName("arcade").setUnlocalizedName("arcade")
				.setHardness(6).setResistance(5).setHarvestLevel("pickaxe", 2);
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(UPPER, false));
	}

	public int getLightValue(IBlockState state) {
		return (Boolean) state.getProperties().get(UPPER) ? 8 : 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		if (blockState.getValue(UPPER)) {
			return AABB[blockState.getValue(FACING).ordinal() - 2];
		} else {
			return super.getCollisionBoundingBox(blockState, worldIn, pos);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getValue(UPPER)) {
			return AABB[state.getValue(FACING).ordinal() - 2];
		} else {
			return super.getBoundingBox(state, source, pos);
		}
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, UPPER);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(FACING).ordinal() - 2) | (state.getValue(UPPER) ? 4 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.blockState.getBaseState().withProperty(FACING, EnumFacing.values()[(meta & 3) + 2])
				.withProperty(UPPER, meta >= 4);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
		worldIn.setBlockState(pos.up(),
				state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(UPPER, true), 2);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			return true;
		}
		Minecraft.getMinecraft().displayGuiScreen(new GuiMain(null, (TileEntityArcade) worldIn.getTileEntity(pos)));
		// playerIn.sendMessage(
		// new TextComponentString("Value:" + ((TileEntityMatterDestroyer)
		// worldIn.getTileEntity(pos)).value));
		return true;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		BlockPos anotherPos = state.getValue(UPPER) ? pos.down() : pos.up();
		if (worldIn.getBlockState(anotherPos).getBlock() == state.getBlock()) {
			if (worldIn.getBlockState(anotherPos).getValue(FACING) == state.getValue(FACING)) {
				if (worldIn.getBlockState(anotherPos).getValue(UPPER) != state.getValue(UPPER)) {
					return;
				}
			}
		}
		worldIn.setBlockToAir(pos);
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		if (pos.getY() >= worldIn.getHeight() - 1) {
			return false;
		} else {
			return super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up());
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityArcade();
	}
}
