package hciu.pub.mcmod.melodycraft.client.sound;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

import hciu.pub.mcmod.melodycraft.mug.data.Song;
import hciu.pub.mcmod.melodycraft.utils.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import paulscode.sound.SoundSystem;

public class ExternalSoundHandler {

	private static ExternalSoundHandler instance = null;

	public static ExternalSoundHandler getInstance() {
		return instance;
	}

	public static void initialize() {
		instance = new ExternalSoundHandler();
	}

	private SoundSystem mcSoundSystem;
	private Random rand = new Random();

	private ExternalSoundHandler() {
		SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
		SoundManager soundManager;
		try {
			soundManager = ReflectionHelper.getField(SoundHandler.class, "sndManager", "field_147694_f", soundHandler);
			mcSoundSystem = ReflectionHelper.getField(SoundManager.class, "sndSystem", "field_148620_e", soundManager);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	public UUID playSound(URL sound, String filename, float x, float y, float z) {
		UUID uuid = MathHelper.getRandomUUID(rand);
		String name = uuid.toString();
		mcSoundSystem.newStreamingSource(false, name, sound, filename, false, x, y, z, 0, 16.0F);
		mcSoundSystem.setVolume(name, 1.0F);
		mcSoundSystem.play(name);
		return uuid;
	}

	public UUID playSound(Song song) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		try {
			return playSound(song.getSongfile().toURI().toURL(), song.getName() + song.getSongfile().getName(),
					(float) player.posX, (float) player.posY, (float) player.posZ);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void pauseSound(UUID uuid) {
		mcSoundSystem.pause(uuid.toString());
	}

	public void resumeSound(UUID uuid) {
		mcSoundSystem.play(uuid.toString());
	}

	public void stopSound(UUID uuid) {
		mcSoundSystem.stop(uuid.toString());
	}
}
