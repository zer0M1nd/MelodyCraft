package hciu.pub.mcmod.melodycraft.mug;

public enum EnumJudgeLevel {
	A(100, 200, 300, 300, 0.75), B(80, 160, 240, 280, 0.85), C(65, 130, 195, 260, 1), D(50, 100, 150, 200, 1.1),
	E(40, 80, 120, 200, 1.2);
	private int perfectTime, greatTime, goodTime, preMissTime;
	private double scoreFactor;

	private EnumJudgeLevel(int perfectTime, int greatTime, int goodTime, int preMissTime, double scoreFactor) {
		this.perfectTime = perfectTime;
		this.greatTime = greatTime;
		this.goodTime = goodTime;
		this.preMissTime = preMissTime;
		this.scoreFactor = scoreFactor;
	}

	public int getPerfectTime() {
		return perfectTime;
	}

	public int getGreatTime() {
		return greatTime;
	}

	public int getGoodTime() {
		return goodTime;
	}

	public int getPreMissTime() {
		return preMissTime;
	}

	public double getScoreFactor() {
		return scoreFactor;
	}

}
