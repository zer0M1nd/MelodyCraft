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
import hciu.pub.mcmod.melodycraft.utils.MD5;
import hciu.pub.mcmod.melodycraft.utils.MiscsHelper;
import net.minecraft.client.resources.I18n;

abstract public class Chart {

	public static class Identifier {
		private String info = "";
		private String author = "";
		private String md5 = "";

		public Identifier(String info, String author, String md5) {
			super();
			this.info = info;
			this.author = author;
			this.md5 = md5;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((author == null) ? 0 : author.hashCode());
			result = prime * result + ((info == null) ? 0 : info.hashCode());
			result = prime * result + ((md5 == null) ? 0 : md5.hashCode());
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
			if (author == null) {
				if (other.author != null)
					return false;
			} else if (!author.equals(other.author))
				return false;
			if (info == null) {
				if (other.info != null)
					return false;
			} else if (!info.equals(other.info))
				return false;
			if (md5 == null) {
				if (other.md5 != null)
					return false;
			} else if (!md5.equals(other.md5))
				return false;
			return true;
		}

		public String getInfo() {
			return info;
		}

		public void setInfo(String info) {
			this.info = info;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getMd5() {
			return md5;
		}

		public void setMd5(String md5) {
			this.md5 = md5;
		}

	}

	protected Identifier identifier;
	protected String date = "";
	protected List<Note> notes = new ArrayList<Note>();
	protected int difficulty = 0;
	protected int id = -1;
	protected int delay = 0;
	protected File file;

	public Identifier getIdentifier() {
		return identifier;
	}
	
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
		return identifier.info;
	}

	public String getInfoDifficulty() {
		return identifier.info + " Lv." + getDifficulty();
	}

	public List<Note> getNotes() {
		return notes;
	}

	public String getAuthor() {
		return identifier.author;
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
			chart.date = MiscsHelper.timeFormat(file.lastModified(), "YYYY-MM-DD hh:mm:ss");

			String info = props.get("info");
			String author = props.get("author");
			String md5 = MD5.get(file);

			chart.identifier = new Identifier(info, author, md5);

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
