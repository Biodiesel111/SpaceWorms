package planets;

import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import game.ObjectAccess;
import tools.SimplexNoise;

public class Planet {

	float weight;
	int texture;
	int radius;
	private int coreRadius;
	float x, y;
	float erruption;
	private float angleStep = 1f;
	
	HashMap<Float, Float> erruptions;
	private ArrayList<Hole> holes;
	
	public Planet(float x, float y, int radius) {
		
		this.x = x;
		this.y = y;
		this.radius = radius;
		setCoreRadius((int) (0.63f * radius));
		
		weight = radius * radius *(1f/35f);
		

		erruption = radius * 0.05f;
		
		setHoles(new ArrayList<Hole>());
		
		createErruptionMap();
	}
	
	private void createErruptionMap() {
		erruptions = new HashMap<Float, Float>();
		
		for(float i = 0; i < 360f; i+=getAngleStep()) {
	    	erruptions.put(i,
	    			(float) (120f*SimplexNoise.noise(
	    					x+Math.sin(Math.toRadians(i)*4f), 
	    					y+Math.cos(Math.toRadians(i)*4f)))
	    			);
	    }
		erruptions.put(360f, erruptions.get(0f));
	}
	
	public void render() {
		glEnable(GL_STENCIL_TEST); 
		for(Hole hole: getHoles()) {
			hole.render();
		}
		
		 GL11.glColorMask(true, true, true, true);
			GL11.glStencilFunc(GL11.GL_NOTEQUAL, 1, 1);
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		
		GL11.glColor3f(0.f,0.f,1.0f);
		
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		    GL11.glVertex2d(x,y);
		    for(float i = 0; i <= 360; i+=getAngleStep()) {
		    	GL11.glVertex2d(
		    			x + (radius + erruptions.get(i)) * Math.sin(Math.toRadians(i)),
		    			y + (radius + erruptions.get(i)) * Math.cos(Math.toRadians(i)));
		    }
	    GL11.glEnd();
	    GL11.glDisable(GL_STENCIL_TEST); 
	    
	    
	    GL11.glColor3f(1.f,0.f,0.0f);
	    GL11.glBegin(GL11.GL_TRIANGLE_FAN);
	    GL11.glVertex2d(x,y);
	    for(float i = 0; i <= 360; i+=getAngleStep()) {
	    	GL11.glVertex2d(
	    			x + getCoreRadius() * Math.sin(Math.toRadians(i)),
	    			y + getCoreRadius() * Math.cos(Math.toRadians(i)));
	    }
	    GL11.glEnd();
	    
	}
	
	public Vector2f getPositionForAngle(float angle) {
		angle%=360;
		Vector2f position = new Vector2f();
	    
		float angleLow = (int) angle;
		float angleHigh = (int) angle + 1;
		float interpolFactor = angle - angleLow;
		position.x = (float) ((1f - interpolFactor) * (x + (radius + erruptions.get(angleLow)) * Math.sin(Math.toRadians(angleLow))) +
				(interpolFactor) * (x + (radius + erruptions.get(angleHigh)) * Math.sin(Math.toRadians(angleHigh)))) ;
		position.y = (float) ((1f - interpolFactor) * (y + (radius + erruptions.get(angleLow)) * Math.cos(Math.toRadians(angleLow))) +
				(interpolFactor) * (y + (radius + erruptions.get(angleHigh)) * Math.cos(Math.toRadians(angleHigh)))) ;
		return position;
		
	}
	
	public float getLengthForAngle(float angle) {
		angle%=360;
		Vector2f position = new Vector2f();
	    
		float angleLow = (int) angle;
		float angleHigh = (int) angle + 1;
		float interpolFactor = angle - angleLow;
		position.x = (float) ((1f - interpolFactor) * ((radius + erruptions.get(angleLow)) * Math.sin(Math.toRadians(angleLow))) +
				(interpolFactor) * ((radius + erruptions.get(angleHigh)) * Math.sin(Math.toRadians(angleHigh)))) ;
		position.y = (float) ((1f - interpolFactor) * ((radius + erruptions.get(angleLow)) * Math.cos(Math.toRadians(angleLow))) +
				(interpolFactor) * ((radius + erruptions.get(angleHigh)) * Math.cos(Math.toRadians(angleHigh)))) ;
		return position.length();
		
	}
	
	public Vector2f getGradientForAngle(float angle) {
		angle%=360;
		Vector2f gradient = new Vector2f();
	    
		float angleLow = (int) angle;
		float angleHigh = (int) angle + 1;
		gradient.x = (float) ((x + (radius + erruptions.get(angleLow)) * Math.sin(Math.toRadians(angleLow))) -
				(x + (radius + erruptions.get(angleHigh)) * Math.sin(Math.toRadians(angleHigh)))) ;
		gradient.y = (float) ((y + (radius + erruptions.get(angleLow)) * Math.cos(Math.toRadians(angleLow))) -
				(y + (radius + erruptions.get(angleHigh)) * Math.cos(Math.toRadians(angleHigh)))) ;
		return gradient;
		
	}
	
	public Vector2f getVecFromPoint(float x, float y) {
		return new Vector2f(getX() - x, getY() - y);
	}
	
	public float getAngleOfPoint(float x, float y) {
		float angle = (float) Math.toDegrees(Vector2f.angle(new Vector2f(0,1), getVecFromPoint(x,y)));
		if(getX() > x)
			angle=360f-angle;
		return angle;
	}
	
	public boolean collidesWithPoint(float x, float y) {
		
		
			
			if(getVecFromPoint(x,y).length() < this.getCoreRadius()) {
				return true;
			}
			
			float planetLengthForAngle = getLengthForAngle(360-getAngleOfPoint(x,y)+180);
			
			
			if(getVecFromPoint(x,y).length() < planetLengthForAngle) {
				for(Hole hole: this.getHoles()) {
					Vector2f disHole = new Vector2f(hole.getX() - x, hole.getY() - y);
					if(disHole.length() < hole.getRadius()) {
						return false;
					}
				}
				return true;
			}
		
		
		return false;
		
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
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

	public void addHole(float x, float y, float radius) {
		getHoles().add(new Hole(x, y, radius));
		
	}

	public ArrayList<Hole> getHoles() {
		return holes;
	}

	public void setHoles(ArrayList<Hole> holes) {
		this.holes = holes;
	}

	public float getAngleStep() {
		return angleStep;
	}

	public void setAngleStep(float angleStep) {
		this.angleStep = angleStep;
	}

	public int getCoreRadius() {
		return coreRadius;
	}

	public void setCoreRadius(int coreRadius) {
		this.coreRadius = coreRadius;
	}
	
	
	
}
