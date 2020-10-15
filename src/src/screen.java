package src;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

public class screen {
	public int screenWidth = 40;
	public int screenHeight = 40;
	public float playerX = 5.00f;
	public float playerY = 5.00f;
	public float playerRot = 0.0f;
	public float playerFOV = 1.0f;
	public float playerDepth = 16;
	public float playerRotSpeed = 10f;
	public float playerMoveSpeed = 0.025f;
	
	public float[] depthBuffer;
	
	
	public Sprite wall;
	
	
	public int mapWidth = 20;
	public int mapHeight = 20;
	
	public screen() {
		StandardDraw.setCanvasSize(screenWidth*10, screenHeight*10);
	}
	public static void main(String [] args) {
		screen game = new screen();
		game.run();
	}
	
	public void run() {
		try {
			wall = new Sprite("wall.png");
		} catch (IOException ioe) {
			
		}
		Color[][] screen = new Color[screenWidth][screenHeight];
		
		String[] map = {
			"####################",
			"#..#...............#",
			"#..#...............#",
			"#..#...#...........#",
			"#..#...#...........#",
			"#......#...........#",
			"#####..............#",
			"#..................#",
			"#..................#",
			"#..................#",
			"#..................#",
			"#..................#",
			"#..................#",
			"#..................#",
			"#..................#",
			"#..................#",
			"#..................#",
			"#..................#",
			"#..................#",
			"####################",
		};
		
		StandardDraw.enableDoubleBuffering();
		float mouseX = (float) StandardDraw.mouseX();
		depthBuffer = new float[screenWidth];
		ArrayList<Entity> entities = new ArrayList<Entity>();
		entities.add(new Entity("wall") {{
			x = 15f;
			y = 15f;
		}});
		
		entities.add(new Entity("wall") {{
			x = 5f;
			y = 15f;
		}});
		
		entities.add(new Entity("wall") {{
			x = 15f;
			y = 5f;
		}});
		while(true) {
			//rotate
			if(StandardDraw.mousePressed()) {
				playerRot += ((float) StandardDraw.mouseX() - mouseX) * playerRotSpeed;
			}
			
			if(playerRot > 6.25f) {
				playerRot = 0.0f;
			}
			if(playerRot < 0) {
				playerRot = 6.25f;
			}
			playerRot = (float) Mathf.constrain(playerRot, 0, 6.25);
			mouseX = (float) StandardDraw.mouseX();
			
			//move
			if(StandardDraw.isKeyPressed(87)) {
				//w
				float moveX = (float) Math.sin(playerRot);
				float moveY = (float) Math.cos(playerRot);
				playerX += moveX * playerMoveSpeed;
				playerY += moveY * playerMoveSpeed;
				
				if(map[(int) playerY].charAt((int) playerX) == '#') {
					playerX -= moveX * playerMoveSpeed;
					playerY -= moveY * playerMoveSpeed;
				}
			}
			if(StandardDraw.isKeyPressed(65)) {
				//a
				float moveX = (float) Math.sin(playerRot - 90);
				float moveY = (float) Math.cos(playerRot - 90);
				playerX += moveX * playerMoveSpeed;
				playerY += moveY * playerMoveSpeed;
				
				if(map[(int) playerY].charAt((int) playerX) == '#') {
					playerX -= moveX * playerMoveSpeed;
					playerY -= moveY * playerMoveSpeed;
				}
			}
			if(StandardDraw.isKeyPressed(83)) {
				//s
				float moveX = (float) Math.sin(playerRot);
				float moveY = (float) Math.cos(playerRot);
				playerX -= moveX * playerMoveSpeed;
				playerY -= moveY * playerMoveSpeed;
				
				if(map[(int) playerY].charAt((int) playerX) == '#') {
					playerX += moveX * playerMoveSpeed;
					playerY += moveY * playerMoveSpeed;
				}
			}
			if(StandardDraw.isKeyPressed(68)) {
				//d
				float moveX = (float) Math.sin(playerRot + 90);
				float moveY = (float) Math.cos(playerRot + 90);
				playerX += moveX * playerMoveSpeed;
				playerY += moveY * playerMoveSpeed;
				
				if(map[(int) playerY].charAt((int) playerX) == '#') {
					playerX -= moveX * playerMoveSpeed;
					playerY -= moveY * playerMoveSpeed;
				}
			}
			
			//raycast
			for(int x = 0;x < screenWidth;x ++) {
				float rayRot = (playerRot - playerFOV/2f) + ((float) x / (float) screenWidth) * playerFOV;
				
				float distanceToWall = 0.1f;
				float scale = 0.1f;
				boolean hitWall = false;
				
				float eyeX = (float) Math.sin(rayRot);
				float eyeY = (float) Math.cos(rayRot);
				
				float sampleX = 0f;
				
				while(!hitWall && distanceToWall < playerDepth) {
					
					distanceToWall *= 1.025f;
					
					int testX = (int) (playerX + eyeX * distanceToWall);
					int testY = (int) (playerY + eyeY * distanceToWall);
					
					if(testX < 0||testX >= mapWidth || testY < 0 || testY >= mapHeight) {
						hitWall = true;
						
						distanceToWall = playerDepth;
					} else {
						if(map[testY].charAt(testX) == '#') {
							hitWall = true;
							
							float blockMidX = (float) testX + 0.5f;
							float blockMidY = (float) testY + 0.5f;
							
							float testPointX = playerX + eyeX * distanceToWall;
							float testPointY = playerY + eyeY * distanceToWall;
							
							float testAngle = (float) Math.atan2((testPointY - blockMidY), (testPointX - blockMidX));
							
							if(testAngle >= -3.14159f * 0.25f && testAngle < 3.14159f * 0.25f) {
								sampleX = testPointY - (float) testY;
							}
							if(testAngle >= 3.14159f * 0.25f && testAngle < 3.14159f * 0.75f) {
								sampleX = testPointX - (float) testX;
							}
							if(testAngle < -3.14159f * 0.25f && testAngle >= -3.14159f * 0.75f) {
								sampleX = testPointX - (float) testX;
							}
							if(testAngle >= 3.14159f * 0.75f || testAngle < -3.14159f * 0.75f) {
								sampleX = testPointY - (float) testY;
							}
						}
					}
				}
				int ceiling  = (int) ((float) (screenHeight / 2f) - screenHeight / ((float) distanceToWall));
				int floor = screenHeight - ceiling;
				
				depthBuffer[x] = distanceToWall;
				
				for(int y = 0;y < screenHeight;y ++) {
					if(y < ceiling + 1) {
						int c = 150 - (int) (y * 10);
						c = (int) Mathf.constrain(c, 0, 255);
						screen[x][y] = new Color(c, c, c);
					} else if(y > ceiling && y <= floor) {
						float sampleY = ((float)y - (float)ceiling) / ((float)ceiling - (float)floor);
						Color c = wall.sample(sampleX, sampleY);
						for(int i = 50;i < distanceToWall * 20;i += 15) {
							c = c.darker();
						}
						screen[x][y] = c;
					} else {
						int c = 150 - (int) ((screenHeight - y) * 10);
						c = (int) Mathf.constrain(c, 0, 255);
						screen[x][y] = new Color(c, c, c);
					}
				}
			}
			
			//display entities
			for(Entity entity : entities) {
				float vecX = entity.x - playerX;
				float vecY = entity.y - playerY;
				float distanceFromPlayer = (float) Math.sqrt(vecX * vecX + vecY * vecY);
				
				float eyeX = (float) Math.sin(playerRot);
				float eyeY = (float) Math.cos(playerRot);
				float objectRot = (float) (Math.atan2(eyeY, eyeX) - Math.atan2(vecY, vecX));
				
				if(objectRot < -3.14159f) {
					objectRot += 2f * 3.14159f;
				}
				if(objectRot > 3.14159f) {
					objectRot -= 2f * 3.14159f;
				}
				
				boolean inPlayerFOV = Math.abs(objectRot) < playerFOV / 2f;
				
				if(inPlayerFOV && distanceFromPlayer >= 0.5f && distanceFromPlayer < playerDepth) {
					float objectCeiling = (float) (screenHeight / 2f) - screenHeight / ((float)distanceFromPlayer);
					float objectFloor = screenHeight - objectCeiling;
					float objectHeight = objectFloor - objectCeiling;
					float objectAspectRatio = (float) entity.sprite.pixels.length / (float) entity.sprite.pixels[0].length;
					float objectWidth = objectHeight / objectAspectRatio;
					float middleOfObject = (0.5f * (objectRot / (playerFOV / 2.0f)) + 0.5f) * (float) screenWidth;
					for(float lx = 0;lx < objectWidth;lx ++) {
						for(float ly = 0;ly < objectHeight;ly ++) {
							float sampleX = lx / objectWidth;
							float sampleY = ly / objectHeight;
							Color c = entity.sprite.sample(sampleX, sampleY);
							int objectColumn = (int) (middleOfObject + lx - (objectWidth / 2f));
							if(objectColumn >= 0 && objectColumn < screenWidth) {
								if(depthBuffer[objectColumn] >= distanceFromPlayer) {
									for(int i = 50;i < distanceFromPlayer * 20;i += 15) {
										c = c.darker();
									}
									screen[objectColumn][(int) Mathf.constrain(objectCeiling + ly, 0, screen[0].length - 1)] = c;
									depthBuffer[objectColumn] = distanceFromPlayer;
								}
							}
						}
					}
				}
			}
			
			//display map
			for(int x = 0;x < map.length;x ++) {
				for(int y = 0;y < map[0].length();y ++) {
					if(map[y].charAt(x) == '#') {
						screen[x+1][screen[0].length-y-1] = new Color(150, 150, 150);
					} else {
						screen[x+1][screen[0].length-y-1] = new Color(0, 0, 0);
					}
					if((int) playerX == x && (int)playerY == y) {
						screen[x+1][screen[0].length-y-1] = new Color(255, 255, 255);
					}
				}
			}

			//draw
			for(int x = 0;x < screen.length;x ++) {
				for(int y = 0;y < screen[0].length;y ++) {
					Drawf.fill(screen[x][y]);
					Drawf.square(x*10, y*10, 10);
				}
			}
			
			
			//display stats
			Drawf.fill(0, 0, 0);
			Drawf.text(0, 7, ((int) (playerX * 10)) / 10f + "," + ((int) (playerY * 10)) / 10f + "," + ((int) (playerRot * 10)) / 10f);
			
			StandardDraw.show();
			StandardDraw.pause(10);
		}
	}

	
}
