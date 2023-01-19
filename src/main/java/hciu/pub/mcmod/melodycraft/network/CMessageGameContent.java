package hciu.pub.mcmod.melodycraft.network;

import java.util.UUID;

import hciu.pub.mcmod.melodycraft.MelodyCraftMod;
import hciu.pub.mcmod.melodycraft.tileentity.TileEntityArcade;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CMessageGameContent implements IMessage {

	private BlockPos pos;
	private int worldId;

	public CMessageGameContent() {
	}

	public CMessageGameContent(World world, BlockPos pos) {
		this.pos = pos;
		worldId = world.provider.getDimension();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer buffer = new PacketBuffer(buf);
		buffer.writeVarInt(worldId);
		buffer.writeBlockPos(pos);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer buffer = new PacketBuffer(buf);
		worldId = buffer.readVarInt();
		pos = buffer.readBlockPos();
	}

	public static class Handler implements IMessageHandler<CMessageGameContent, IMessage> {
		@Override
		public IMessage onMessage(CMessageGameContent msg, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().player;
			World world = MelodyCraftMod.getMcServer().getWorld(msg.worldId);
			BlockPos pos = msg.pos;
			if (world.getTileEntity(pos) instanceof TileEntityArcade) {

			}
			return null;
		}
	}
}
