package hciu.pub.mcmod.melodycraft;

import hciu.pub.mcmod.melodycraft.block.BlockList;
import hciu.pub.mcmod.melodycraft.command.CommandMelodyCraft;
import hciu.pub.mcmod.melodycraft.item.ItemList;
import hciu.pub.mcmod.melodycraft.mug.data.SongList;
import hciu.pub.mcmod.melodycraft.tileentity.TileEntityArcade;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		BlockList.registerAllBlocks();
		ItemList.registerAllItems();
		
		GameRegistry.registerTileEntity(TileEntityArcade.class, new ResourceLocation(MelodyCraftMod.MODID, "arcade"));
	}

	public void init(FMLInitializationEvent event) {
	}

	public void postInit(FMLPostInitializationEvent event) {
		SongList.loadAllSongs();

	}

	public void onStart(FMLServerStartingEvent e) {
		if(!MelodyCraftMod.isClient()) {
			e.registerServerCommand(new CommandMelodyCraft());
		}
	}
}
