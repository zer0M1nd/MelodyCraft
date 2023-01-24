package hciu.pub.mcmod.melodycraft.mug.saves;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.ImmutableList;

import hciu.pub.mcmod.melodycraft.MelodyCraftMod;
import hciu.pub.mcmod.melodycraft.mug.data.Chart;
import hciu.pub.mcmod.melodycraft.utils.MiscsHelper;
import hciu.pub.mcmod.melodycraft.utils.ReflectionHelper;
import net.minecraft.server.MinecraftServer;

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
		// TODO may not be correct
		String s = "";
		try {
			s = ReflectionHelper.getField(MinecraftServer.class, "folderName", "field_71294_K",
					MelodyCraftMod.getMcServer());
		} catch (ReflectiveOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		if (MelodyCraftMod.isClient()) {
			s = "saves/" + s;
		}

		s = s + "/melodycraft";

		File f = new File(s);

		f.mkdirs();

		return f;
	}

	public File getRecordsFile() {
		return new File(getFolder(), "records.json");
	}

	private HashMap<Chart.Identifier, ArrayList<PlayResult>> resultLoaded = new HashMap<>();
	private HashMap<Chart.Identifier, ArrayList<PlayResult>> resultNew = new HashMap<>();

	private static class SavedStates {
		private PlayResult[] results = new PlayResult[0];

		public PlayResult[] getResults() {
			return results;
		}

		public void setResults(PlayResult[] results) {
			this.results = results;
		}
	}

	private SavedStates states = new SavedStates();

	public synchronized void load() {
		resultLoaded.clear();
		resultNew.clear();
		File file = getRecordsFile();

		if (file.exists() && !file.isDirectory()) {
			try {
				FileReader fr = new FileReader(getRecordsFile());
				// TODO 我寻思你一个记录文件不能超过10M吧
				char buffer[] = new char[1024 * 1024 * 10];
				int l = fr.read(buffer);
				states = JSONObject.parseObject(new String(buffer, 0, l), SavedStates.class);
				fr.close();
			} catch (IOException e) {
			}
		}

		for (PlayResult r : states.getResults()) {
			resultLoaded.putIfAbsent(r.getChartid(), new ArrayList<>());
			resultLoaded.get(r.getChartid()).add(r);
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
		ArrayList<PlayResult> r = new ArrayList<>();
		for (Map.Entry<Chart.Identifier, ArrayList<PlayResult>> e : resultNew.entrySet()) {
			if (resultLoaded.containsKey(e.getKey())) {
				resultLoaded.get(e.getKey()).addAll(e.getValue());
			} else {
				resultLoaded.put(e.getKey(), e.getValue());
			}
		}
		for (Map.Entry<Chart.Identifier, ArrayList<PlayResult>> e : resultLoaded.entrySet()) {
			r.addAll(e.getValue());
		}
		resultNew.clear();
		states.setResults(r.toArray(new PlayResult[r.size()]));

		try {
			File f = getRecordsFile();
			f.createNewFile();
			FileWriter fw = new FileWriter(f);
			fw.write(JSONObject.toJSONString(states, SerializerFeature.PrettyFormat,
					SerializerFeature.DisableCircularReferenceDetect));
			fw.close();
		} catch (IOException ex) {
		}
	}

	public synchronized void add(PlayResult r) {
		// System.out.println("Added");
		resultNew.putIfAbsent(r.getChartid(), new ArrayList<>());
		resultNew.get(r.getChartid()).add(r);
	}

	public List<PlayResult> getAllFor(Chart chart, UUID player) {
		ArrayList<PlayResult> arr = new ArrayList<>();
		arr.addAll(resultLoaded.getOrDefault(chart.getIdentifier(), new ArrayList<>()));
		arr.addAll(resultNew.getOrDefault(chart.getIdentifier(), new ArrayList<>()));
		return arr.stream().filter(x -> x.getPlayerUUID().equals(player)).collect(Collectors.toList());
	}

	public PlayResult getBestFor(Chart chart, UUID player) {
		return getAllFor(chart, player).stream().sorted((x, y) -> Integer.compare(y.getScore(), x.getScore()))
				.findFirst().orElse(null);
	}

}
