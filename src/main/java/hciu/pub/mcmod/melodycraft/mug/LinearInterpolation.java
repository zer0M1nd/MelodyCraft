package hciu.pub.mcmod.melodycraft.mug;

import java.util.Arrays;

public class LinearInterpolation {
	public double[][] points;

	public LinearInterpolation(double[] x, double[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("Lengths of the arrays are not the same!");
		}
		points = new double[x.length][2];
		for (int i = 0; i < x.length; i++) {
			points[i][0] = x[i];
			points[i][1] = y[i];
		}
		Arrays.sort(points, (a, b) -> (int) Math.signum(a[0] - b[0]));
		int total = points.length;
		for (int i = 0; i < points.length - 1; i++) {
			if (points[i][0] == points[i + 1][0]) {
				points[i][0] = Double.NaN;
				total--;
			}
		}
		double[][] points1 = new double[total][2];
		for (int i = 0, j = 0; i < points.length; i++) {
			if(!Double.isNaN(points[i][0])) {
				points1[j++] = points[i];
			}
		}
		points = points1;
	}

	public double get(double v) {
		int l = 0, r = points.length - 2, a = 0;
		while (l <= r) {
			int mid = (l + r) >> 1;
			if (points[mid][0] <= v) {
				a = mid;
				l = mid + 1;
			} else {
				r = mid - 1;
			}
		}
		// if (a >= points.length - 1 || a < 0) {
		// return Double.NaN;
		// }
		if ((points[a + 1][0] - points[a][0]) == 0) {
			return points[a][1];
		}
		double k = (points[a + 1][1] - points[a][1]) / (points[a + 1][0] - points[a][0]);
		return points[a][1] + (v - points[a][0]) * k;
	}
}
