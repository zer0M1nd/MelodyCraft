package hciu.pub.mcmod.melodycraft.client.sound;

import hciu.pub.mcmod.melodycraft.MelodyCraftMod;
import net.minecraftforge.client.event.sound.SoundSetupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;

public class EventSound {

	@SubscribeEvent
	public void onLoadSoundManager(SoundSetupEvent e) {
        try {
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			MelodyCraftMod.getLogger().info("WAV codec added successfully.");
		} catch (SoundSystemException e1) {
			e1.printStackTrace();
		}
	}
}
