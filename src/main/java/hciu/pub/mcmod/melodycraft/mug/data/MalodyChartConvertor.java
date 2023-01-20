package hciu.pub.mcmod.melodycraft.mug.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import hciu.pub.mcmod.melodycraft.MelodyCraftMod;
import hciu.pub.mcmod.melodycraft.mug.LinearInterpolation;

public class MalodyChartConvertor {

	public static void convertRecursively(File dir, File to) {
		boolean maybe = false;
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				convertRecursively(f, to);
			} else if (f.getName().endsWith(".mc")) {
				maybe = true;
			}
		}
		if (maybe) {
			convertAll(dir, to);
		}
	}

	private static double getBeat(JSONArray arr) {
		return arr.getIntValue(0) + (double) arr.getIntValue(1) / (double) arr.getIntValue(2);
	}

	public static boolean convertAll(File dir, File to) {
		File folder = null;
		try {
			String name = null, artist = null, specfile = null;
			double bpm = 0;
			for (File f : dir.listFiles()) {
				if (!f.isDirectory() && f.getName().endsWith(".mc")) {
					FileInputStream in = new FileInputStream(f);
					byte[] data = new byte[in.available()];
					in.read(data);
					in.close();
					JSONObject json = JSONObject.parseObject(new String(data));
					name = json.getJSONObject("meta").getJSONObject("song").getString("title");
					artist = json.getJSONObject("meta").getJSONObject("song").getString("artist");
					for (Object i : json.getJSONArray("time")) {
						if (i instanceof JSONObject) {
							JSONObject j = (JSONObject) i;
							if (getBeat(j.getJSONArray("beat")) == 0.0) {
								bpm = j.getDoubleValue("bpm");
							}
						}
					}
					for (Object i : json.getJSONArray("note")) {
						if (i instanceof JSONObject) {
							JSONObject j = (JSONObject) i;
							if (getBeat(j.getJSONArray("beat")) == 0.0 && j.getString("sound") != null
									&& j.getString("sound").length() > 0) {
								specfile = j.getString("sound");
								break;
							}
						}
					}
					break;
				}
			}
			String fname = name;
			for (char c : "?/\\:*\"<>|".toCharArray()) {
				fname = fname.replace(c, ' ');
			}
			String foldername = fname.trim();
			folder = new File(to, foldername);
			while (folder.exists()) {
				folder = new File(to, foldername += "_");
			}
			folder.mkdir();
			File info = new File(folder, "info.mecs");
			FileWriter writer = new FileWriter(info);
			writer.write("id=" + SongList.getNextAvailableSongId() + "\n");
			writer.write("name=" + name + "\n");
			writer.write("artist=" + artist + "\n");
			writer.write("bpm=" + new DecimalFormat("#.##").format(bpm) + "\n");
			writer.close();
			File f1 = null, f2 = null;
			for (File f : dir.listFiles()) {
				if (!f.isDirectory()) {
					if (f.getName().endsWith(".wav") || f.getName().endsWith(".ogg")) {
						if (specfile == null || specfile.equals(f.getName())) {
							if (f1 != null) {
								MelodyCraftMod.getLogger().warn(
										"More than one audio file exists and MelodyCraft doesn't know which to use!");
								MelodyCraftMod.getLogger().warn("Failed to convert Malody chart in " + dir.getPath());
								return false;
							}
							f1 = new File(folder, "song" + (f.getName().endsWith(".wav") ? ".wav" : ".ogg"));
							copyFile(f, f1);
						}
					} else if (f.getName().endsWith(".jpg") && f2 == null) {
						f2 = new File(folder, "bg.png");
						convertJpgToPng(f, f2);
					} else if (f.getName().endsWith(".png") && f2 == null) {
						f2 = new File(folder, "bg.png");
						copyFile(f, f2);
					} else if (f.getName().endsWith(".mc")) {
						convertChart(f, folder);
					}
				}
			}
			if (f1 == null) {
				MelodyCraftMod.getLogger()
						.warn("More than one audio file exists and MelodyCraft doesn't know which to use!");
				MelodyCraftMod.getLogger().warn("Failed to convert Malody chart in " + dir.getPath());
				deleteAll(folder);
				return false;
			}
			return true;
		} catch (Exception e) {
			MelodyCraftMod.getLogger().warn("Caught an exception while converting Malody chart in " + dir.getPath());
			MelodyCraftMod.getLogger().catching(e);
		}
		MelodyCraftMod.getLogger().warn("Failed to convert Malody charts in " + dir.getPath());
		if (folder != null && folder.exists()) {
			deleteAll(folder);
		}
		return false;
	}

	private static void deleteAll(File f) {
		if (f.isDirectory()) {
			for (File x : f.listFiles()) {
				deleteAll(x);
			}
			f.delete();
		} else {
			f.delete();
		}
	}

	private static String toMili(double a) {
		return Long.toString((long) (a * 1000));
	}

	private static void convertJpgToPng(File in, File out) throws IOException {
		BufferedImage image = ImageIO.read(in);
		ImageIO.write(image, "png", out);
	}

	private static void copyFile(File from, File to) throws IOException {
		if (!to.exists()) {
			to.createNewFile();
		}
		FileInputStream in = new FileInputStream(from);
		FileOutputStream out = new FileOutputStream(to);
		byte[] buffer = new byte[10240];
		while (in.available() > 0) {
			int x = in.read(buffer);
			out.write(buffer, 0, x);
		}
		in.close();
		out.close();

	}

	private static class Entry {
		protected double first;
		protected String second;

		protected Entry(double first, String second) {
			this.first = first;
			this.second = second;
		}

	}

	public static int parseSafely(String s) {
		int x = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) <= '9' && s.charAt(i) >= '0') {
				x = x * 10 + s.charAt(i) - '0';
			} else if (x != 0) {
				return x;
			}
		}
		return x;
	}

	private static void convertChart(File file, File dir) throws IOException {
		FileInputStream in = new FileInputStream(file);
		byte[] data = new byte[in.available()];
		in.read(data);
		in.close();
		JSONObject json = JSONObject.parseObject(new String(data));
		String name = json.getJSONObject("meta").getString("version");
		String author = json.getJSONObject("meta").getString("creator");
		if (json.getJSONObject("meta").getIntValue("mode") != 0) {
			MelodyCraftMod.getLogger().warn("Currently only key mode allowed!");
			MelodyCraftMod.getLogger().warn("Failed to convert a single Malody chart " + file.getPath());
			return;
		}
		int key = json.getJSONObject("meta").getJSONObject("mode_ext").getIntValue("column");
		if (key < 4 || key > 7) {
			MelodyCraftMod.getLogger().warn("Currently only 4-7K mode allowed!");
			MelodyCraftMod.getLogger().warn("Failed to convert a single Malody chart " + file.getPath());
			return;
		}
		double bpm = 120;
		int delay = 0;
		for (Object o : json.getJSONArray("note")) {
			if (o instanceof JSONObject) {
				JSONObject j = (JSONObject) o;
				if (getBeat(j.getJSONArray("beat")) == 0.0 && j.getString("sound") != null
						&& j.getString("sound").length() > 0) {
					delay = j.getIntValue("offset");
				}
			}
		}
		List<Entry> notes = new ArrayList<>();
		int timingCount = json.getJSONArray("time").size();
		// System.out.println("timingCount = " + timingCount);
		if (timingCount > 1) {
			MelodyCraftMod.getLogger().warn(dir.getName() + " " + name + " is a Timing chart!");
		}
		double[] x = new double[timingCount + 1], y = new double[timingCount + 1];
		int i = 0;
		double lastbeat = 0, lastf = 60 / bpm, lasttime = 0;
		for (Object o : json.getJSONArray("time")) {
			if (o instanceof JSONObject) {
				JSONObject j = (JSONObject) o;
				x[i] = getBeat(j.getJSONArray("beat"));
				y[i] = (lasttime += (x[i] - lastbeat) * lastf);
				lastbeat = x[i];
				lastf = 60 / j.getDoubleValue("bpm");
				notes.add(new Entry(lasttime * 1000, "timing," + toMili(lasttime) + "," + String.format("%.4f", (lastf))));
				if (i == 0) {
					bpm = lastf;
				}
				i++;
			}
		}
		x[timingCount] = lastbeat + 100000;
		y[timingCount] = lasttime + 100000 * lastf;
		LinearInterpolation times = new LinearInterpolation(x, y);	
		// for (int i = 0; i < timingCount; i++) {
		// System.out.println("temp " + temp[i] + " bpm " + bpms[i] + " presum " +
		// presum[i]);
		// }
		int noteCount = 0;
		for (Object o : json.getJSONArray("note")) {
			if (o instanceof JSONObject) {
				JSONObject j = (JSONObject) o;
				if (getBeat(j.getJSONArray("beat")) == 0.0 && j.getString("sound") != null
						&& j.getString("sound").length() > 0) {
					continue;
				}
				if (j.getJSONArray("endbeat") != null) {
					double time = times.get(getBeat(j.getJSONArray("beat"))),
							endtime = times.get(getBeat(j.getJSONArray("endbeat")));
					int lane = j.getIntValue("column");
					notes.add(new Entry(time, "keylong," + toMili(time) + "," + toMili(endtime) + "," + lane));
					noteCount += 2;
				} else {
					double time = times.get(getBeat(j.getJSONArray("beat")));
					int lane = j.getIntValue("column");
					notes.add(new Entry(time, "key," + toMili(time) + "," + lane));
					noteCount++;
				}
			}
		}

		notes.sort((a, b) -> (int) Math.signum(a.first - b.first));
		String filename = name.trim();
		for (char c : "?/\\:*\"<>|".toCharArray()) {
			filename = filename.replace(c, ' ');
		}
		File f = new File(dir, filename + ".mecc");
		while (f.exists()) {
			filename += "_";
			f = new File(dir, filename + ".mecc");
		}
		String[] s = name.split("[Ll][Vv]\\.");
		int difficulty = 0;
		if (s.length > 1) {
			name = s[0];
			difficulty = parseSafely(s[s.length - 1]);
		} else {
			difficulty = (int) ((double) noteCount / ((notes.get(notes.size() - 1).first - notes.get(0).first)) * 6);
		}
		FileWriter writer = new FileWriter(f);
		writer.write("id=" + SongList.getNextAvailableChartId() + "\n");
		writer.write("info=" + name + "\n");
		writer.write("author=" + author + "\n");
		writer.write("type=key\n");
		writer.write("keys=" + key + "\n");
		writer.write("difficulty=" + difficulty + "\n");
		writer.write("delay=" + delay + "\n");
		writer.write("notes:\n");
		writer.write("timingbase,0," + String.format("%.4f", (bpm)) + "\n");
		for (Entry e : notes) {
			writer.write(e.second + "\n");
		}
		writer.close();
	}

}
