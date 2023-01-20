package hciu.pub.mcmod.melodycraft.network;

import java.util.Set;

import hciu.pub.mcmod.melodycraft.MelodyCraftMod;
import hciu.pub.mcmod.melodycraft.utils.ReflectionHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkManager {

	private static SimpleNetworkWrapper wrapper = null;

	public static void init() {
		int id = 0;
		wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("mec");
		Set<Class<?>> classes = ReflectionHelper.getClasses("hciu.pub.mcmod.melodycraft.network");
		for (Class<?> cls : classes) {
			if (cls.getSimpleName().startsWith("SMessage") || cls.getSimpleName().startsWith("CMessage")) {
				Class<?> internal = null;
				for (Class<?> i : cls.getDeclaredClasses()) {
					if (i.getSimpleName().equals("Handler")) {
						internal = i;
						break;
					}
				}
				try {
					ReflectionHelper.method(SimpleNetworkWrapper.class, "registerMessage", "registerMessage", wrapper,
							internal, cls, id++, cls.getSimpleName().startsWith("SMessage") ? Side.SERVER : Side.CLIENT);
				} catch (ReflectiveOperationException e) {
					MelodyCraftMod.getLogger().warn("Failed to load network message " + cls.getName());
					MelodyCraftMod.getLogger().catching(e);
					e.printStackTrace();
				}
			}
		}
	}

	public static SimpleNetworkWrapper getWrapper() {
		return wrapper;
	}

}
