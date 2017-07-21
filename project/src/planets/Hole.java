package planets;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class Hole {

	private float x, y;
	private float radius;
	
	public Hole(float x, float y, float radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public void render() {
		
		GL11.glColorMask(false, false, false, false);
		GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 1);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
		
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		    GL11.glVertex2d(x,y);
		    for(float i = 0; i <= 360; i+=5) {
		    	GL11.glVertex2d(
		    			x + radius * Math.sin(Math.toRadians(i)),
		    			y + radius * Math.cos(Math.toRadians(i)));
		    }
	    GL11.glEnd();
	    
	   
	    
	}
	
	public boolean collidesWithPoint(float x, float y) {
		Vector2f vecToHole = new Vector2f(this.x - x, this.y - y);
		if(vecToHole.length() < radius)
			return true;
		return false;
		
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

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	
	
}
