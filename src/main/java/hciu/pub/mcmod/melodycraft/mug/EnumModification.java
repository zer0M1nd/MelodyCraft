package hciu.pub.mcmod.melodycraft.mug;

public enum EnumModification {
	AUTO(0), SUDDEN(1), ENDJUDGE(1), CONST(0.8), MIRROR(1), RANDOM(1), BLEED(1);
	private double scoreFactor;

	private EnumModification(double scoreFactor) {
		this.scoreFactor = scoreFactor;
	}

	public double getScoreFactor() {
		return scoreFactor;
	}
}
