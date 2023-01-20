package hciu.pub.mcmod.melodycraft.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGameSettings;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGameSettingsClient;

/**
 * 
 * 单例
 * 
 * @author zer0M1nd
 *
 */
public class MelodyCraftGameConfig {

	private static final String FILENAME = "melodycraft/config.json";

	private MelodyCraftGameSettings global = new MelodyCraftGameSettings();
	private MelodyCraftGameSettingsClient client = new MelodyCraftGameSettingsClient();

	private MelodyCraftGameConfig() {

	}

	private static MelodyCraftGameConfig instance;

	public static MelodyCraftGameConfig getInstance() {
		return instance;
	}

	public static boolean load() {
		File file = new File(FILENAME);
		file.getParentFile().mkdir();
		if (file.exists()) {
			try {
				FileReader fr = new FileReader(file);
				char buffer[] = new char[1024 * 1024];
				int l = fr.read(buffer);
				instance = JSONObject.parseObject(new String(buffer, 0, l), MelodyCraftGameConfig.class);
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
				instance = new MelodyCraftGameConfig();
				return false;
			}
			return true;
		} else {
			instance = new MelodyCraftGameConfig();
			save();
			return true;
		}
	}

	public static boolean save() {
		if (instance == null) {
			return false;
		}
		File file = new File(FILENAME);
		file.getParentFile().mkdir();
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(JSONObject.toJSONString(instance, SerializerFeature.PrettyFormat));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public MelodyCraftGameSettings getGlobal() {
		return global;
	}

	public MelodyCraftGameSettingsClient getClient() {
		return client;
	}
}
