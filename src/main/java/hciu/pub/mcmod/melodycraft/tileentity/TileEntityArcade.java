package hciu.pub.mcmod.melodycraft.tileentity;

import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGame;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityArcade extends TileEntity {

	private MelodyCraftGame game;

	public TileEntityArcade() {
		// TODO 玩家系统写好后正确获取当前玩家
	}

	public MelodyCraftGame getGame() {
		return game;
	}

	public void setGame(MelodyCraftGame game) {
		this.game = game;
	}
}
