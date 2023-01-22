package hciu.pub.mcmod.melodycraft.mug.data;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.common.collect.ImmutableList;

import hciu.pub.mcmod.melodycraft.MelodyCraftMod;
import hciu.pub.mcmod.melodycraft.mug.data.Note.NoteKeyMode;
import hciu.pub.mcmod.melodycraft.utils.MiscsHelper;
import net.minecraft.client.resources.I18n;

abstract public class Chart {

	protected String info = "";
	protected String author = "";
	protected String date = "";
	protected List<Note> notes = new ArrayList<Note>();
	protected int difficulty = 0;
	protected int id = -1;
	protected int delay = 0;
	protected File file;

	public int getDifficulty() {
		return difficulty;
	}

	public File getFile() {
		return file;
	}

	public int getDelay() {
		return delay;
	}

	public int getId() {
		return id;
	}

	protected void toImmutable() {
		notes = ImmutableList.copyOf(notes);
	}

	public String getInfo() {
		return info;
	}

	public String getInfoDifficulty() {
		return info + " Lv." + getDifficulty();
	}

	public List<Note> getNotes() {
		return notes;
	}

	public String getAuthor() {
		return author;
	}

	public String getDate() {
		return date;
	}

	abstract public String getModeName();

	public static class ChartKeyMode extends Chart {

		protected int keys;

		public int getKeys() {
			return keys;
		}

		@Override
		public String getModeName() {
			return I18n.format("charttype." + keys + "k");
		}

	}

	public static Chart loadChartFromFile(File file) {
		Map<String, String> props = new HashMap<>();
		try {
			Scanner sc = new Scanner(new FileInputStream(file));
			while (sc.hasNextLine()) {
				String s = sc.nextLine();
				if (s.equals("notes:")) {
					break;
				}
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
			String type = props.get("type");
			Chart chart = null;
			if ("key".equals(type)) {
				chart = new ChartKeyMode();
				((ChartKeyMode) chart).keys = Integer.parseInt(props.get("keys"));
				if (((ChartKeyMode) chart).keys < 4 || ((ChartKeyMode) chart).keys > 7) {
					sc.close();
					throw new IllegalArgumentException("Currently only support 4-7K in key mode!");
				}
			} else {
				sc.close();
				throw new IllegalArgumentException("Unknown or undefined chart type!");
			}
			chart.file = file;
			chart.author = props.get("author");
			chart.date = MiscsHelper.timeFormat(file.lastModified(), "YYYY-MM-DD hh:mm:ss");
			chart.info = props.get("info");
			while (sc.hasNextLine()) {
				Note note = Note.readNote(sc.nextLine());
				if (note instanceof NoteKeyMode && chart instanceof ChartKeyMode) {
					int l = ((NoteKeyMode) note).getLane();
					if (l < 0 || l >= ((ChartKeyMode) chart).keys) {
						continue;
					}
				}
				chart.getNotes().add(note);
			}
			sc.close();
			chart.id = Integer.parseInt(props.get("id"));
			chart.difficulty = Integer.parseInt(props.get("difficulty"));
			chart.delay = Integer.parseInt(props.get("delay"));
			chart.toImmutable();
			return chart;
		} catch (Exception e) {
			MelodyCraftMod.getLogger().catching(e);
		}
		MelodyCraftMod.getLogger().warn("Failed to load chart from file " + file.getPath());
		return null;
	}

}
