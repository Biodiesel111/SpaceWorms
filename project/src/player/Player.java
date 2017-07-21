package player;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import game.ObjectAccess;
import objects.Rocket;
import planets.Hole;
import planets.Planet;

public class Player {

	private Planet currentPlanet;
	float angle;
	private float x;
	private float y;
	float width = 50,height = 80;
	private Vector2f currentDirection = new Vector2f();
	
	private float shootAngle = 0;
	private float crossHairRadius = 200;
	
	int shiftPressedFrames = 0;
	
	public Player() {
		setRandomPosition();
	}
	
	public void setRandomPosition() {
		ArrayList<Planet> planets = ObjectAccess.getPlanets();
		setCurrentPlanet(planets.get((int) Math.round(Math.random() *  (planets.size()-1))));
		
		float randomAngle = (float) (Math.random() * 360f);
		
		Vector2f pos = getCurrentPlanet().getPositionForAngle(randomAngle);
		
		setX(pos.x);
		setY(pos.y);
		
	}
	
	public void input() {
		if(collidesFallingWithPlanet()) {
			Vector2f gradient = getCurrentPlanet().getGradientForAngle(360-getAngleToPlanet()+180);
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				x+= gradient.x * 0.1f;
				y+= gradient.y * 0.1f;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				x-= gradient.x * 0.1f;
				y-= gradient.y * 0.1f;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				setCurrentDirection((Vector2f) getVecToPlanetNorm().scale(-15f));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && shiftPressedFrames < 180) {
				shiftPressedFrames++;
				
			} else {
				if(shiftPressedFrames != 0) {
					ObjectAccess.getObjects().add(new Rocket(
							x - getVecToPlanetNorm().x * 50f,y - getVecToPlanetNorm().y * 50f,
							(float)getVecToCrossHair().x * ((float)shiftPressedFrames/250f),
							(float)getVecToCrossHair().y * ((float)shiftPressedFrames/250f),
							30));
					shiftPressedFrames = 0;
				}
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				
				shootAngle+=0.5f;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				
				shootAngle-=0.5f;
			}
		}
	}
	
	public void update() {
		applyGravity();
		setX(getX() + getCurrentDirection().x);
		setY(getY() + getCurrentDirection().y);
		
	}
	
	
	
	public void applyGravity() {
		
		
		
		if(!collidesFallingWithPlanet() || Math.toDegrees(Vector2f.angle(getVecToPlanet(), getCurrentDirection())) > 90) {
			Vector2f planetVecNormalized = new Vector2f();
			getVecToPlanet().normalise(planetVecNormalized);
			getCurrentDirection().x += planetVecNormalized.x * 0.2f;
			getCurrentDirection().y += planetVecNormalized.y * 0.2f;
		} else {
			getCurrentDirection().x = 0;
			getCurrentDirection().y = 0;
		}
		
	}
	
	public boolean collidesFallingWithPlanet() {
		return getCurrentPlanet().collidesWithPoint(x, y);
	}
	
	
	
	public void render() {

		
		// set the color of the quad (R,G,B,A)
		GL11.glColor3f(0f,1.f,0.f);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(getX(), getY(), 0);
		GL11.glRotated(getAngleToPlanet(), 0,0,1);
		GL11.glTranslatef(-getX(), -getY(), 0);
		     
		// draw quad
		GL11.glBegin(GL11.GL_QUADS);
		    GL11.glVertex2f(getX()-width/2f,getY()-height/2f);
		    GL11.glVertex2f(getX()+width/2f,getY()-height/2f);
		    GL11.glVertex2f(getX()+width/2f,getY());
		    GL11.glVertex2f(getX()-width/2f,getY());
		GL11.glEnd();
		
		GL11.glPopMatrix();

	}
	
	public void renderCrossHair() {
		GL11.glColor3f(0f,1.f,0.f);
		
		     
		Vector2f crossHairPos = new Vector2f();
		crossHairPos.x = (float) (getX() + Math.sin(Math.toRadians(shootAngle)) * crossHairRadius);
		crossHairPos.y = (float) (getY() + Math.cos(Math.toRadians(shootAngle)) * crossHairRadius);
		
		// draw quad
		GL11.glBegin(GL11.GL_QUADS);
		    GL11.glVertex2f(crossHairPos.x-width/4f,crossHairPos.y-width/4f);
		    GL11.glVertex2f(crossHairPos.x+width/4f,crossHairPos.y-width/4f);
		    GL11.glVertex2f(crossHairPos.x+width/4f,crossHairPos.y+width/4f);
		    GL11.glVertex2f(crossHairPos.x-width/4f,crossHairPos.y+width/4f);
		GL11.glEnd();
		

	}
	
	public Vector2f getCrossHairPos() {
		Vector2f crossHairPos = new Vector2f();
		crossHairPos.x = (float) (getX() + Math.sin(Math.toRadians(shootAngle)) * crossHairRadius);
		crossHairPos.y = (float) (getY() + Math.cos(Math.toRadians(shootAngle)) * crossHairRadius);
		return crossHairPos;
	}
	
	public Vector2f getVecToCrossHair() {
		return new Vector2f(getCrossHairPos().getX() - getX(), getCrossHairPos().getY() - getY());
	}
	
	public Vector2f getVecToPlanet() {
		return new Vector2f(getCurrentPlanet().getX() - getX(), getCurrentPlanet().getY() - getY());
	}
	
	public Vector2f getVecToPlanetNorm() {
		Vector2f normed =  getVecToPlanet();
		normed.normalise();
		return normed;
	}
	
	public float getAngleToPlanet() {
		float angle = (float) Math.toDegrees(Vector2f.angle(new Vector2f(0,1), getVecToPlanet()));
		if(getCurrentPlanet().getX() > getX())
			angle=360f-angle;
		return angle;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Vector2f getCurrentDirection() {
		return currentDirection;
	}

	public void setCurrentDirection(Vector2f currentDirection) {
		this.currentDirection = currentDirection;
	}

	public Planet getCurrentPlanet() {
		return currentPlanet;
	}

	public void setCurrentPlanet(Planet currentPlanet) {
		this.currentPlanet = currentPlanet;
	}
	
}
