package hciu.pub.mcmod.melodycraft.mug.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.codec.digest.Md5Crypt;

import com.google.common.collect.ImmutableList;

import hciu.pub.mcmod.melodycraft.MelodyCraftMod;
import hciu.pub.mcmod.melodycraft.mug.data.Chart.ChartKeyMode;
import hciu.pub.mcmod.melodycraft.utils.MD5;
import hciu.pub.mcmod.melodycraft.utils.MiscsHelper;

public class Song {

	public static class Identifier {

		private String name;
		private String artist;
		private String md5;

		public Identifier(String name, String artist, String md5) {
			super();
			this.name = name;
			this.artist = artist;
			this.md5 = md5;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((artist == null) ? 0 : artist.hashCode());
			result = prime * result + ((md5 == null) ? 0 : md5.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Identifier other = (Identifier) obj;
			if (artist == null) {
				if (other.artist != null)
					return false;
			} else if (!artist.equals(other.artist))
				return false;
			if (md5 == null) {
				if (other.md5 != null)
					return false;
			} else if (!md5.equals(other.md5))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getArtist() {
			return artist;
		}

		public void setArtist(String artist) {
			this.artist = artist;
		}

		public String getMd5() {
			return md5;
		}

		public void setMd5(String md5) {
			this.md5 = md5;
		}

	}

	private Identifier identifier;

	protected String bpm;
	protected String date;
	protected File songfile;
	protected File bgfile;
	protected int id = 0;
	protected List<Chart> charts = new ArrayList<Chart>();

	public Identifier getIdentifier() {
		return identifier;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return identifier.name;
	}

	public String getArtist() {
		return identifier.artist;
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
						MelodyCraftMod.getLogger().info(
								"Song " + song.getName() + " Chart " + chart.getInfo() + " Diff " + chart.difficulty);
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

	private static Song loadSongInfo(File songinfo, File songfile, File bg) {

		Map<String, String> props = new HashMap<>();
		try {
			Song song = new Song();
			Scanner sc = new Scanner(new InputStreamReader(new FileInputStream(songinfo), "UTF-8"));
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
			String name = props.get("name");
			String artist = props.get("artist");
			String md5 = MD5.get(songfile);

			song.identifier = new Identifier(name, artist, md5);

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
