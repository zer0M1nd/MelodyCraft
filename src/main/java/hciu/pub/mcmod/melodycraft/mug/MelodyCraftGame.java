package hciu.pub.mcmod.melodycraft.mug;

import java.util.ArrayList;
import java.util.List;

import hciu.pub.mcmod.melodycraft.mug.data.Chart;
import hciu.pub.mcmod.melodycraft.mug.data.Note;
import hciu.pub.mcmod.melodycraft.mug.data.NoteInPlay;
import hciu.pub.mcmod.melodycraft.mug.data.Song;
import hciu.pub.mcmod.melodycraft.tileentity.TileEntityArcade;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public abstract class MelodyCraftGame {

	private static final int PREV_MILLIS = 3000;

	protected TileEntityArcade tileEntity;
	protected Song song = null;
	protected Chart chart = null;
	protected MelodyCraftGameSettings settings = null;
	protected long relativeTimeMillis = 0;
	protected List<NoteInPlay<?>> notes = null;
	protected int[] judge = new int[4];
	protected int combo = 0, score = 0;
	protected int maxCombo = 0;
	protected double performance = 100.0;
	protected EnumJudge lastJudge = EnumJudge.MISS;
	protected long lastJudgeTime = -1111111111;
	protected boolean lastJudgeLate = false;
	protected EntityPlayer player;
	protected EnumGameSide side;
	protected EnumGameState state = EnumGameState.INIT;
	protected long pausedAt = 0;

	public EnumJudge getLastJudge() {
		return lastJudge;
	}

	public long getLastJudgeTime() {
		return lastJudgeTime;
	}

	public boolean isLastJudgeLate() {
		return lastJudgeLate;
	}

	public EnumGameState getState() {
		return state;
	}

	public MelodyCraftGame(TileEntityArcade te, Song song, Chart chart, MelodyCraftGameSettings settings,
			EntityPlayer player, EnumGameSide side) {
		this.tileEntity = te;
		this.song = song;
		this.chart = chart;
		this.settings = settings;
		this.player = player;
		this.side = side;
	}

	public abstract MelodyCraftGame newGame();

	public void init() {
		judge = new int[4];
		combo = maxCombo = 0;
		score = 0;
		performance = 100.0;
		notes = new ArrayList<NoteInPlay<?>>();
		int id = 0;
		for (Note note : chart.getNotes()) {
			notes.add(NoteInPlay.createNoteInPlay(note, id++));
		}
	}

	public int getJudged() {
		return judge[0] + judge[1] + judge[2] + judge[3];
	}

	public double getAcc() {
		if (getJudged() == 0) {
			return 0;
		}
		return (judge[0] + judge[1] * 0.7 + judge[2] * 0.5) / getJudged();
	}

	public long getGameTimeMillis() {
		return this.state == EnumGameState.PAUSED ? pausedAt : Minecraft.getSystemTime() - relativeTimeMillis;
	}

	public void setGameTimeMillis(long t) {
		if(this.state == EnumGameState.PAUSED) {
			pausedAt = t;
		} else {
			this.relativeTimeMillis = Minecraft.getSystemTime() - t;
		}
	}

	public List<NoteInPlay<?>> getNotes() {
		return notes;
	}

	public void processData(int[] data) {

	}

	public void addCombo() {
		combo++;
		maxCombo = Math.max(maxCombo, combo);
	}

	public void addCombo(int count) {
		combo += count;
		maxCombo = Math.max(maxCombo, combo);
	}

	public void modifyPerformance(double delta) {
		performance = Math.min(100, Math.max(0, performance + delta));
	}

	public TileEntityArcade getTileEntity() {
		return tileEntity;
	}

	public Song getSong() {
		return song;
	}

	public Chart getChart() {
		return chart;
	}

	public MelodyCraftGameSettings getSettings() {
		return settings;
	}

	public long getRelativeTimeMillis() {
		return relativeTimeMillis;
	}

	public int[] getJudge() {
		return judge;
	}

	public int getCombo() {
		return combo;
	}

	public int getScore() {
		return score;
	}

	public int getMaxCombo() {
		return maxCombo;
	}

	public double getPerformance() {
		return performance;
	}

	public void startGame() {
		if (this.state == EnumGameState.INIT) {
			relativeTimeMillis = Minecraft.getSystemTime() - chart.getDelay() + PREV_MILLIS;
			this.state = EnumGameState.STARTED;
		}
	}

	public void pause() {
		if (this.state == EnumGameState.STARTED) {
			this.pausedAt = getGameTimeMillis();
			this.state = EnumGameState.PAUSED;
		}
	}

	public void resume() {
		if (this.state == EnumGameState.PAUSED) {
			this.state = EnumGameState.STARTED;
			this.relativeTimeMillis = Minecraft.getSystemTime() - chart.getDelay() - this.pausedAt;
		}
	}

	public void endGame() {
		resume();
		this.state = EnumGameState.ENDED;
	}

}
