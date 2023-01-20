package hciu.pub.mcmod.melodycraft.mug.data;

import hciu.pub.mcmod.melodycraft.mug.data.Note.NoteKeyMode;
import hciu.pub.mcmod.melodycraft.mug.data.Note.NoteKeyModeLong;
import hciu.pub.mcmod.melodycraft.mug.data.Note.NoteTiming;
import hciu.pub.mcmod.melodycraft.mug.data.Note.NoteTimingBase;

abstract public class NoteInPlay<T extends Note> {

	protected T note;
	protected int id;
	protected boolean isVisited = false;

	public NoteInPlay(T note, int id) {
		this.note = note;
		this.id = id;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	public T getNote() {
		return note;
	}

	public int getId() {
		return id;
	}

	public static class NoteKeyModeInPlay<T extends NoteKeyMode> extends NoteInPlay<T> {

		public NoteKeyModeInPlay(T note, int id) {
			super(note, id);
		}

	}

	public static class NoteKeyModeLongInPlay<T extends NoteKeyModeLong> extends NoteKeyModeInPlay<T> {
		protected boolean isHeadVisited = false;
		protected int judgedCount = 0;
		protected boolean isMissed = false;

		public NoteKeyModeLongInPlay(T note, int id) {
			super(note, id);
		}

		public boolean isHeadVisited() {
			return isHeadVisited;
		}

		public void setHeadVisited(boolean isHeadVisited) {
			this.isHeadVisited = isHeadVisited;
		}

		public int getJudgedCount() {
			return judgedCount;
		}

		public void addJudgedCount() {
			this.judgedCount++;
		}

		public void addJudgedCount(int count) {
			this.judgedCount += count;
		}

		public boolean isFullyJudged() {
			return judgedCount >= note.getComboCount() - 2;
		}
		
		public boolean isMissed() {
			return isMissed;
		}
		
		public void setMissed(boolean isMissed) {
			this.isMissed = isMissed;
		}
	}

	public static class NoteTimingInPlay<T extends NoteTiming> extends NoteInPlay<T> {

		public NoteTimingInPlay(T note, int id) {
			super(note, id);
		}

	}

	public static class NoteTimingBaseInPlay<T extends NoteTimingBase> extends NoteInPlay<T> {

		public NoteTimingBaseInPlay(T note, int id) {
			super(note, id);
		}

	}

	public static NoteInPlay<?> createNoteInPlay(Note note, int id) {
		if (note.getClass() == NoteKeyMode.class) {
			return new NoteKeyModeInPlay<NoteKeyMode>((NoteKeyMode) note, id);
		} else if (note.getClass() == NoteKeyModeLong.class) {
			return new NoteKeyModeLongInPlay<NoteKeyModeLong>((NoteKeyModeLong) note, id);
		} else if (note.getClass() == NoteTiming.class) {
			return new NoteTimingInPlay<NoteTiming>((NoteTiming) note, id);
		} else if (note.getClass() == NoteTimingBase.class) {
			return new NoteTimingBaseInPlay<NoteTimingBase>((NoteTimingBase) note, id);
		}
		return null; // TODO 抛出异常
	}
}
