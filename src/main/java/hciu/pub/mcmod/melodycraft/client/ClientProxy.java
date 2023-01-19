package hciu.pub.mcmod.melodycraft.client;

import hciu.pub.mcmod.melodycraft.CommonProxy;
import hciu.pub.mcmod.melodycraft.client.sound.EventSound;
import hciu.pub.mcmod.melodycraft.client.sound.ExternalSoundHandler;
import hciu.pub.mcmod.melodycraft.command.CommandMelodyCraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		registerClientEvents();
		RenderLoader.registerItemRenders();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		// TODO Auto-generated method stub
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		ExternalSoundHandler.initialize();
		super.postInit(event);
	}

	@Override
	public void onStart(FMLServerStartingEvent e) {
		super.onStart(e);
		ClientCommandHandler.instance.registerCommand(new CommandMelodyCraft());
	}

	public void registerClientEvents() {
		MinecraftForge.EVENT_BUS.register(new EventSound());
		MinecraftForge.EVENT_BUS.register(new EventRender());
	}
}