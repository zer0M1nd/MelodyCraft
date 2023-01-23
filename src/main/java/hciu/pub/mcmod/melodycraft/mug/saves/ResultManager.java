package hciu.pub.mcmod.melodycraft.mug.saves;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import hciu.pub.mcmod.melodycraft.MelodyCraftMod;
import hciu.pub.mcmod.melodycraft.config.MelodyCraftGameConfig;
import hciu.pub.mcmod.melodycraft.mug.MelodyCraftGame;
import hciu.pub.mcmod.melodycraft.mug.data.Chart;
import hciu.pub.mcmod.melodycraft.utils.MiscsHelper;

public class ResultManager {

	private static ResultManager instance = null;

	private ResultManager() {

	}

	public static ResultManager getInstance() {
		if (instance == null) {
			instance = new ResultManager();
		}
		return instance;
	}

	public File getFolder() {
		return null;
	}

	private HashMap<Chart.Identifier, ArrayList<PlayResult>> resultLoaded = new HashMap<>();
	private HashMap<Chart.Identifier, ArrayList<PlayResult>> resultNew = new HashMap<>();

	public synchronized void load() {
		resultLoaded.clear();
		resultNew.clear();
		for (File file : getFolder().listFiles()) {
			if (!file.isDirectory() && file.getName().endsWith(".json")) {
				try {
					FileReader fr = new FileReader(file);
					char buffer[] = new char[1024 * 1024];
					int l = fr.read(buffer);
					PlayResult r = JSONObject.parseObject(new String(buffer, 0, l), PlayResult.class);
					fr.close();
					add(r);
				} catch (IOException e) {
				}
			}
		}
	}

	private String atMost(String name, int len) {
		name = name.replaceAll(" ", "");

		for (char c : "?/\\:*\"<>|".toCharArray()) {
			name = name.replace(c, ' ');
		}
		return name.substring(0, Math.min(len, name.length()));
	}

	private String getSuggestedFilename(PlayResult r) {
		return MiscsHelper.timeFormat(r.getTime(), "MMDD-hhmmss-") + atMost(r.getSongid().getName(), 5) + "-"
				+ atMost(r.getChartid().getInfo(), 5) + ".json";
	}

	public synchronized void save() {
		for (Map.Entry<Chart.Identifier, ArrayList<PlayResult>> e : resultNew.entrySet()) {

			e.getValue().forEach(r -> {
				try {
					FileWriter fw = new FileWriter(new File(getFolder(), getSuggestedFilename(r)));
					fw.write(JSONObject.toJSONString(instance, SerializerFeature.PrettyFormat));
					fw.close();
				} catch (IOException ex) {
				}
			});

			if (resultLoaded.containsKey(e.getKey())) {
				resultLoaded.get(e.getKey()).addAll(e.getValue());
			} else {
				resultLoaded.put(e.getKey(), e.getValue());
			}
		}
		resultNew.clear();
	}

	public synchronized void add(PlayResult r) {
		resultLoaded.putIfAbsent(r.getChartid(), new ArrayList<>());
		resultLoaded.get(r.getChartid()).add(r);
	}
}
