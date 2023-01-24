package hciu.pub.mcmod.melodycraft.mug.saves;

import java.util.UUID;

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

	private UUID playerUUID;

	public PlayResult(Identifier songid, hciu.pub.mcmod.melodycraft.mug.data.Chart.Identifier chartid,
			EnumJudgeLevel judgeLevel, int score, double acc, int[] judge, long time, UUID playerUUID) {
		super();
		this.songid = songid;
		this.chartid = chartid;
		this.judgeLevel = judgeLevel;
		this.score = score;
		this.acc = acc;
		this.judge = judge;
		this.time = time;
		this.playerUUID = playerUUID;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
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

	public String accStr() {
		return ((int) (acc * 100) + "." + String.format("%02d", ((int) (acc * 10000) % 100)) + "%");
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

	public boolean checkFC() {
		return judge[3] == 0;
	}

	public boolean checkAP() {
		return judge[1] + judge[2] + judge[3] == 0;
	}

}
