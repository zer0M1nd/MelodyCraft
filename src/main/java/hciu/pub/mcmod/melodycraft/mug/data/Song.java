package hciu.pub.mcmod.melodycraft.mug.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.common.collect.ImmutableList;

import hciu.pub.mcmod.melodycraft.MelodyCraftMod;
import hciu.pub.mcmod.melodycraft.utils.MiscsHelper;

public class Song {

	protected String name;
	protected String artist;
	protected String bpm;
	protected String date;
	protected File songfile;
	protected File bgfile;
	protected int id = 0;
	protected List<Chart> charts = new ArrayList<Chart>();
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getArtist() {
		return artist;
	}

	public String getBpm() {
		return bpm;
	}

	public String getDate() {
		return date;
	}

	public File getSongfile() {
		return songfile;
	}

	public File getBgfile() {
		return bgfile;
	}

	public List<Chart> getCharts() {
		return charts;
	}

	protected void toImmutable() {
		charts = ImmutableList.copyOf(charts);
	}

	public static Song loadSongFromDirectory(File directory) {
		try {
			if (!directory.isDirectory()) {
				MelodyCraftMod.getLogger().warn("What the f**k are you doing?????");
				return null;
			}
			File f1 = null, f2 = null, f3 = null;
			for (File f : directory.listFiles()) {
				if (f.getName().endsWith(".mecs")) {
					f1 = f;
				} else if (f.getName().endsWith(".wav") || f.getName().endsWith(".ogg")) {
					f2 = f;
				} else if (f.getName().endsWith(".png")) {
					f3 = f;
				}
			}
			if (f1 == null || f2 == null) {
				MelodyCraftMod.getLogger().warn("Skipping loading song from " + directory.getPath()
						+ " as songinfo and/or audio file is missing.");
				return null;
			}
			Song song = loadSongInfo(f1, f2, f3);
			for (File f : directory.listFiles()) {
				if (f.getName().endsWith("mecc")) {
					Chart chart = Chart.loadChartFromFile(f);
					if (chart != null) {
						song.charts.add(chart);
						MelodyCraftMod.getLogger()
								.info("Song " + song.name + " Chart " + chart.info + " Diff " + chart.difficulty);
					}
				}
			}
			if (song.charts.isEmpty()) {
				MelodyCraftMod.getLogger()
						.warn("Skipping loading song from " + directory.getPath() + " as no chart exists.");
				return null;
			}
			return song;
		} catch (Exception e) {
			MelodyCraftMod.getLogger().catching(e);
		}
		MelodyCraftMod.getLogger().warn("Failed to load song from directory " + directory.getPath());
		return null;
	}

	public int getDelay() {
		return id;
	}

	private static Song loadSongInfo(File songinfo, File songfile, File bg) {

		Map<String, String> props = new HashMap<>();
		try {
			Song song = new Song();
			Scanner sc = new Scanner(new FileInputStream(songinfo));
			while (sc.hasNextLine()) {
				String s = sc.nextLine();
				String[] arr = s.split("=");
				if (arr.length == 1) {
					props.put(arr[0], "undefined");
				} else if (arr.length != 2) {
					sc.close();
					throw new IllegalArgumentException("Not enough or too many = signs!");
				} else {
					props.put(arr[0], arr[1]);
				}
			}
			sc.close();
			song.name = props.get("name");
			song.artist = props.get("artist");
			song.bpm = props.get("bpm");
			song.date = MiscsHelper.timeFormat(songinfo.lastModified(), "YYYY-MM-DD hh:mm:ss");
			song.songfile = songfile;
			song.bgfile = bg;
			song.id = Integer.parseInt(props.get("id"));
			return song;
		} catch (IOException e) {
			MelodyCraftMod.getLogger().catching(e);
		} catch (IllegalArgumentException e) {
			MelodyCraftMod.getLogger().catching(e);
		}
		MelodyCraftMod.getLogger().warn("Failed to load song info from file " + songinfo.getPath());
		return null;
	}
}
