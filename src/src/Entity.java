package src;

import java.io.IOException;

public class Entity {
	public float x;
	public float y;
	public Sprite sprite;
	public Entity(String name) {
		try {
			sprite = new Sprite(name + ".png");
		} catch (IOException ioe) {
			
		}
	}
}
