package hciu.pub.mcmod.melodycraft.mug;

public class MelodyCraftGameSettings {

	private EnumJudgeLevel judge = EnumJudgeLevel.A;

	private int speed = 80;

	public EnumJudgeLevel getJudge() {
		return judge;
	}
	
	public void setJudge(EnumJudgeLevel judge) {
		this.judge = judge;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public double speedFactor() {
		return speed / 20.0;
	}
}
