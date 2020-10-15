package src;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.*;

public class Sprite {
	private BufferedImage img;
	public Color[][] pixels;
	public Sprite(String spritename) throws IOException {
		img = ImageIO.read(new File(spritename));
		//generate
		pixels = new Color[img.getHeight()][img.getWidth()];
		for(int col = 0;col < pixels.length;col ++) {
			for(int row = 0;row < pixels[0].length;row ++) {
				pixels[col][row] = new Color(img.getRGB(col, row));
			}
		}
	}

	public Color get(int x, int y) {
		return pixels[(int) Mathf.constrain(y, 0, pixels.length)][(int) Mathf.constrain(x, 0, pixels[0].length)];
	}
	
	public Color sample(float x, float y) {
		while(x < 0) x += 1.0f;
		while(x > 1) x -= 1.0f;
		while(y < 0) y += 1.0f;
		while(y > 1) y -= 1.0f;
		return get((int) (x * pixels[0].length), (int) (y * pixels.length));
	}
}
