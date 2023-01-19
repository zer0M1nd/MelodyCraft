package hciu.pub.mcmod.melodycraft.mug.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ImmutableList;

import hciu.pub.mcmod.melodycraft.MelodyCraftMod;

public class SongList {

	private static List<Song> songs = new ArrayList<Song>();
	private static int maxSongId = 1000;
	private static int maxChartId = 1000;

	private static HashMap<Integer, Song> songMap;
	private static HashMap<Integer, Chart> chartMap;

	public static void loadAllSongs() {
		MelodyCraftMod.getLogger().info("Starting loading all songs and charts");
		songMap = new HashMap<Integer, Song>();
		chartMap = new HashMap<Integer, Chart>();
		long current = System.currentTimeMillis();
		int charts = 0;
		maxSongId = maxChartId = 1000;
		songs = new ArrayList<Song>();
		File f = new File("melodycraft");
		if (!f.exists()) {
			f.mkdir();
			return;
		}
		for (File ff : f.listFiles()) {
			if (ff.isDirectory()) {
				Song song = Song.loadSongFromDirectory(ff);
				if (song != null) {
					songs.add(song);
					charts += song.charts.size();
					songMap.put(song.getId(), song);
					song.charts.forEach(c -> chartMap.put(c.getId(), c));
				}
			}
		}
		refreshId();
		songs = ImmutableList.copyOf(songs);
		MelodyCraftMod.getLogger().info("Loaded " + songs.size() + " songs and " + charts + " charts.");
		MelodyCraftMod.getLogger().info("Time cost: " + (System.currentTimeMillis() - current) + "ms.");
	}

	public static void refreshId() {
		maxSongId = maxChartId = 1000;
		songMap.keySet().forEach(a -> maxSongId = Math.max(maxSongId, a));
		chartMap.keySet().forEach(a -> maxChartId = Math.max(maxChartId, a));
	}

	public static List<Song> getSongs() {
		return songs;
	}

	public static int getNextAvailableSongId() {
		return ++maxSongId;
	}

	public static int getNextAvailableChartId() {
		return ++maxChartId;
	}

}
