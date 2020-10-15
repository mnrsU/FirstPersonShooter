package src;

import java.awt.Color;

public class Drawf {
	public static double cnvrt(double pixel) {
		return pixel / StandardDraw.getWidth();
	}
	/** color methods */
	public static void fill(int r, int g, int b, int alpha) {
		StandardDraw.setPenColor(new Color(r, g, b, alpha));
	}
	public static void fill(int r, int g, int b) {
		StandardDraw.setPenColor(new Color(r, g, b));
	}
	public static void fill(Color color) {
		StandardDraw.setPenColor(color);
	}
	/** draw methods */
	public static void square(double x, double y, double size) {
		StandardDraw.filledSquare(cnvrt(x), cnvrt(y), cnvrt(size));
	}
	public static void rect(double x, double y, double width, double height) {
		StandardDraw.filledRectangle(cnvrt(x), cnvrt(y), cnvrt(width), cnvrt(height));
	}
	public static void circle(double x, double y, double size) {
		StandardDraw.filledCircle(cnvrt(x), cnvrt(y), cnvrt(size));
	}
	public static void text(double x, double y, String str) {
		StandardDraw.textLeft(cnvrt(x), cnvrt(y), str);
	}
}
