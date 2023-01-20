package hciu.pub.mcmod.melodycraft.network;

import hciu.pub.mcmod.melodycraft.MelodyCraftMod;
import hciu.pub.mcmod.melodycraft.tileentity.TileEntityArcade;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CMessageStartGameRequest implements IMessage {

	private BlockPos pos;
	private int worldId;

	public CMessageStartGameRequest() {
	}

	public CMessageStartGameRequest(World world,BlockPos pos) {
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

	public static class Handler implements IMessageHandler<CMessageStartGameRequest, IMessage> {
		@Override
		public IMessage onMessage(CMessageStartGameRequest msg, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().player;
			World world = MelodyCraftMod.getMcServer().getWorld(msg.worldId);
			BlockPos pos = msg.pos;
			if (world.getTileEntity(pos) instanceof TileEntityArcade) {
				
			}
			return null;
		}
	}
}
