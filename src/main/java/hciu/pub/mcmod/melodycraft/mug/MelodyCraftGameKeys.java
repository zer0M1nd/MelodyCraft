package hciu.pub.mcmod.melodycraft.mug;

import hciu.pub.mcmod.melodycraft.mug.data.NoteInPlay.NoteKeyModeLongInPlay;
import hciu.pub.mcmod.melodycraft.tileentity.TileEntityArcade;
import net.minecraft.entity.player.EntityPlayer;
import hciu.pub.mcmod.melodycraft.mug.data.Song;
import hciu.pub.mcmod.melodycraft.mug.data.Chart.ChartKeyMode;

public class MelodyCraftGameKeys extends MelodyCraftGame {
	protected boolean[] laneState = new boolean[0];

	public MelodyCraftGameKeys(TileEntityArcade tileEntity, Song song, ChartKeyMode chart,
			MelodyCraftGameSettings settings, EntityPlayer player, EnumGameSide side) {
		super(tileEntity, song, chart, settings, player, side);
	}

	public boolean getLaneState(int x) {
		return laneState[x];
	}

	@Override
	public void init() {
		laneState = new boolean[((ChartKeyMode) chart).getKeys()];
		super.init();
	}

	@Override
	public ChartKeyMode getChart() {
		return (ChartKeyMode) super.getChart();
	}

	public void updateLane(int lane, boolean state) {
		laneState[lane] = state;
	}

	public void judgeNote(int id, EnumJudge judge, boolean late) {
		lastJudgeTime = getGameTimeMillis();
		lastJudge = judge;
		this.judge[judge.ordinal()]++;
		notes.get(id).setVisited(true);
		if (judge == EnumJudge.MISS) {
			modifyPerformance(-25.0);
			combo = 0;
			if (notes.get(id) instanceof NoteKeyModeLongInPlay) {
				((NoteKeyModeLongInPlay<?>) notes.get(id)).setMissed(true);
			}
		} else {
			addCombo();
			modifyPerformance(0.25);
			score += (int) ((1000 - judge.ordinal() * 200) * (1 + performance / 100));
		}
		lastJudgeLate = late;
	}

	public void judgeLongNoteHead(int id, EnumJudge judge, boolean late) {
		lastJudgeTime = getGameTimeMillis();
		lastJudge = judge;
		this.judge[judge.ordinal()]++;
		((NoteKeyModeLongInPlay<?>) notes.get(id)).setHeadVisited(true);
		if (judge == EnumJudge.MISS) {
			modifyPerformance(-25.0);
			combo = 0;
			((NoteKeyModeLongInPlay<?>) notes.get(id)).setMissed(true);
		} else {
			addCombo();
			modifyPerformance(0.25);
			score += (int) ((1000 - judge.ordinal() * 200) * (1 + performance / 100));
		}
		lastJudgeLate = late;
	}

	public void judgeLongNoteExtra(int id, int count) {
		((NoteKeyModeLongInPlay<?>) notes.get(id)).addJudgedCount(count);
		score += 100 * count;
		addCombo(count);
		modifyPerformance(0.1 * count);
	}

	public int getKeys() {
		return getChart().getKeys();
	}

	@Override
	public MelodyCraftGame newGame() {
		return new MelodyCraftGameKeys(tileEntity, song, getChart(), settings, player, side);
	}
	
	public String getGameModeName() {
		return laneState.length + "k";
	}
}
