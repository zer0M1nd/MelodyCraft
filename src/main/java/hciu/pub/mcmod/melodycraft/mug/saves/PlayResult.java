package hciu.pub.mcmod.melodycraft.mug.saves;

import hciu.pub.mcmod.melodycraft.mug.EnumJudgeLevel;
import hciu.pub.mcmod.melodycraft.mug.data.Chart;
import hciu.pub.mcmod.melodycraft.mug.data.Song;
import hciu.pub.mcmod.melodycraft.mug.data.Song.Identifier;

public class PlayResult {

	private Song.Identifier songid;
	private Chart.Identifier chartid;

	private EnumJudgeLevel judgeLevel;
	private int score;
	private double acc;
	private int[] judge;
	private long time;

	public PlayResult(Identifier songid, hciu.pub.mcmod.melodycraft.mug.data.Chart.Identifier chartid,
			EnumJudgeLevel judgeLevel, int score, double acc, int[] judge, long time) {
		super();
		this.songid = songid;
		this.chartid = chartid;
		this.judgeLevel = judgeLevel;
		this.score = score;
		this.acc = acc;
		this.judge = judge;
		this.time = time;
	}

	public Song.Identifier getSongid() {
		return songid;
	}

	public void setSongid(Song.Identifier songid) {
		this.songid = songid;
	}

	public Chart.Identifier getChartid() {
		return chartid;
	}

	public void setChartid(Chart.Identifier chartid) {
		this.chartid = chartid;
	}

	public EnumJudgeLevel getJudgeLevel() {
		return judgeLevel;
	}

	public void setJudgeLevel(EnumJudgeLevel judgeLevel) {
		this.judgeLevel = judgeLevel;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public double getAcc() {
		return acc;
	}

	public void setAcc(double acc) {
		this.acc = acc;
	}

	public int[] getJudge() {
		return judge;
	}

	public void setJudge(int[] judge) {
		this.judge = judge;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
