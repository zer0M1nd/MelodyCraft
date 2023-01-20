package hciu.pub.mcmod.melodycraft.mug.data;

abstract public class Note {

	protected long time;
	protected long endtime;

	public Note(long time) {
		super();
		this.time = this.endtime = time;
	}

	public long getTime() {
		return time;
	}

	public long getEndtime() {
		return endtime;
	}

	abstract public int getComboCount();

	abstract public int getComboCountJudged();

	abstract public int getDifficultyFactor();

	public static class NoteTiming extends Note {

		protected double factor;

		public NoteTiming(long time, double factor) {
			super(time);
			this.factor = factor;
		}

		public double getFactor() {
			return factor;
		}

		@Override
		public int getComboCount() {
			return 0;
		}

		@Override
		public int getComboCountJudged() {
			return 0;
		}

		@Override
		public int getDifficultyFactor() {
			return 0;
		}

	}

	public static class NoteTimingBase extends Note {

		protected double bpm;

		public NoteTimingBase(long time, double bpm) {
			super(time);
			this.bpm = bpm;
		}

		public double getBpm() {
			return bpm;
		}

		@Override
		public int getComboCount() {
			return 0;
		}

		@Override
		public int getComboCountJudged() {
			return 0;
		}

		@Override
		public int getDifficultyFactor() {
			return 0;
		}

	}

	public static class NoteKeyMode extends Note {
		protected int lane;

		public NoteKeyMode(long time, int lane) {
			super(time);
			this.lane = lane;
		}

		public int getLane() {
			return lane;
		}

		@Override
		public int getComboCount() {
			return 1;
		}

		@Override
		public int getComboCountJudged() {
			return 1;
		}

		@Override
		public int getDifficultyFactor() {
			return 1;
		}

	}

	public static class NoteKeyModeLong extends NoteKeyMode {

		public static final int EXTRA_JUDGE_TIME = 100;

		public NoteKeyModeLong(long time, int lane, long endtime) {
			super(time, lane);
			this.endtime = endtime;
		}

		@Override
		public int getComboCount() {
			return Math.max(2, (int) ((endtime - time) / EXTRA_JUDGE_TIME + 1));
		}

		@Override
		public int getDifficultyFactor() {
			return 2;
		}

		public int getExtraComboForHeldTime(int time) {
			return Math.min(getComboCount() - 2, time / EXTRA_JUDGE_TIME);
		}

	}

	public static Note readNote(String str) {
		String[] arr = str.split(",");
		if (arr[0].equals("key")) {
			return new NoteKeyMode(Long.parseLong(arr[1]), Integer.parseInt(arr[2]));
		} else if (arr[0].equals("keylong")) {
			return new NoteKeyModeLong(Long.parseLong(arr[1]), Integer.parseInt(arr[3]), Long.parseLong(arr[2]));
		} else if (arr[0].equals("timing")) {
			return new NoteTiming(Long.parseLong(arr[1]), Double.parseDouble(arr[2]));
		} else if (arr[0].equals("timingbase")) {
			return new NoteTimingBase(Long.parseLong(arr[1]), Double.parseDouble(arr[2]));
		} else {
			throw new IllegalArgumentException("Note with unidentified type found!");
		}
	}

}
