package src;

public class Mathf {
	public static double random(double min, double max) {
		return Math.random()*(max-min) + min;
	}
	public static boolean chance(double chance) {
		return Math.random() <= chance;
	}
	public static double[] angletomovement(double angle) {
		double[] arr = {Math.sin(angle), Math.cos(angle)};
		return arr;
	}
	public static double dist (double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	public static double constrain (double num, double min, double max) {
		if(num < min) return min;
		if(num > max) return max;
		return num;
	}
}
