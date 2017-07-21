package objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import envirorment.Level;
import game.ObjectAccess;
import planets.CollisionInformation;
import planets.Hole;
import planets.Planet;

public class BasicObject {

	Vector2f movingDirection;
	private float x;
	float y;
	float weight;
	int texture;
	int radius;
	int lifeTime = 0;
	int framesToLive = 300;
	private boolean activated = true;
	
	public BasicObject(float x, float y, float deltaX, float deltaY, int radius) {
		movingDirection = new Vector2f(deltaX, deltaY);
		this.setX(x);
		this.y = y;
		this.radius = radius;
		
		weight = 5;
		
	}
	
	public void update() {
		setX(getX() + movingDirection.x);
		y+=movingDirection.y;
		
		for(Planet planet : ObjectAccess.getPlanets()) {
			Vector2f vecToPlanet = new Vector2f(getX() - planet.getX(), y - planet.getY());
			float planetDisSqaured = vecToPlanet.lengthSquared() / 20f;
			float strength= -(weight * planet.getWeight())/planetDisSqaured;
			vecToPlanet.scale(1f/vecToPlanet.length());
			vecToPlanet.scale(strength);
			Vector2f.add(movingDirection, vecToPlanet, movingDirection);
			
		}
		
		lifeTime++;
		if(lifeTime > framesToLive)
			activated = false;
		
	}
	
	public void render() {

		
		// set the color of the quad (R,G,B,A)
		GL11.glColor3f(1f,0.f,0.f);
		     
		// draw quad
		GL11.glBegin(GL11.GL_QUADS);
		    GL11.glVertex2f(x-radius,y-radius);
		    GL11.glVertex2f(x+radius,y-radius);
		    GL11.glVertex2f(x+radius,y+radius);
		    GL11.glVertex2f(x-radius,y+radius);
		GL11.glEnd();
		

	}
	
	public boolean collidesWithPlanet() {
		
		for(Planet planet:ObjectAccess.getPlanets()) {
			if(planet.collidesWithPoint(x, y)) {
				return true;
			}
		}
		return false;
	}
	
	public CollisionInformation getCollisionInfos() {
		Planet colPlanet = null;
		for(Planet planet:ObjectAccess.getPlanets()) {
			
			if(planet.collidesWithPoint(x, y)) {
				colPlanet = planet;
				break;
			}
		}
		return new CollisionInformation(x, y, colPlanet);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		// TODO Auto-generated method stub
		return y;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	
}
