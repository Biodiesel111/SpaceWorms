package game;



import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import envirorment.Level;
import objects.BasicObject;
import planets.Planet;
import player.Player;

public class Game {

	Camera cam;
	Level level;
	Player currentPlayer;
	
	public void loop() {
		
		input();
		logic();
		render();
		
	}
	
	public void setUp() {
		
		cam = new Camera(1000, 0);
		level = new Level();
		level.setUp();
		
		currentPlayer = ObjectAccess.getPlayers().get(0);
		
	}
	
	public void input() {
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			cam.position.x -= 15;
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
			cam.position.x += 15;
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
			cam.position.y -= 15;
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
			cam.position.y += 15;
		
		cam.position.x = currentPlayer.getX();
		cam.position.y = currentPlayer.getY();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_1)) {
			cam.scaleFactor/=1.006;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_2)) {
			cam.scaleFactor*=1.006;
		}
		
		cam.anlge = currentPlayer.getAngleToPlanet();
		
		currentPlayer.input();
	}
	
	public void logic() {
		level.update();
		cleanUpObjects();
	}
	
	public void render() {
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);  
		
		
		System.out.println(cam.anlge);
		
		GL11.glPushMatrix();
		
		
//		GL11.glTranslatef(1920 * Core.zoomFactor * 0.5f, 1080 * Core.zoomFactor * 0.5f, 0);
		GL11.glRotated(180.0-cam.anlge, 0,0,1);
		GL11.glScaled(cam.scaleFactor, cam.scaleFactor, 1);
		GL11.glTranslatef(-cam.position.x, -cam.position.y , 0);
//		
		
		
		
		level.draw();
		currentPlayer.renderCrossHair();
		GL11.glPopMatrix();
		
	}
	
	public void cleanUpObjects() {
		for(int i = 0; i< level.objects.size(); i++) {
			if(!level.objects.get(i).isActivated()) {
				level.objects.remove(level.objects.get(i));
				i--;
			}
		}
	}
	
	public Level getLevel() {
		return level;
	}
	
}
