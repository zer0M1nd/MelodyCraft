package hciu.pub.mcmod.melodycraft;

import java.io.File;
import java.util.UUID;

import org.apache.logging.log4j.Logger;

import hciu.pub.mcmod.melodycraft.client.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = MelodyCraftMod.MODID, name = MelodyCraftMod.NAME, version = MelodyCraftMod.VERSION)
public class MelodyCraftMod {
	public static final String MODID = "melodycraft";
	public static final String NAME = "MelodyCraft";
	public static final String VERSION = "@version@";

	public static final boolean DEBUG = true;

	@Instance(MODID)
	private static MelodyCraftMod instance;

	@SidedProxy(clientSide = "hciu.pub.mcmod.melodycraft.client.ClientProxy", serverSide = "hciu.pub.mcmod.melodycraft.CommonProxy")
	private static CommonProxy proxy;
	private static File configPath;
	private static MinecraftServer mcServer;
	private static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		configPath = event.getModConfigurationDirectory();
		instance = this;
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void init(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void onStart(FMLServerStartingEvent e) {
		mcServer = e.getServer();
		proxy.onStart(e);
	}

	@EventHandler
	public void onStop(FMLServerStoppingEvent e) {
		// System.out.println("stopping");
		proxy.onStop(e);
	}

	public static boolean isClient() {
		return proxy instanceof ClientProxy;
	}

	public static MelodyCraftMod getInstance() {
		return instance;
	}

	public static File getConfigPath() {
		return configPath;
	}

	public static MinecraftServer getMcServer() {
		return mcServer;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static UUID getMyUUID() {
		return EntityPlayer.getUUID(Minecraft.getMinecraft().getSession().getProfile());
	}
}
