package game;

import org.lwjgl.util.vector.Vector2f;

public class Camera {

	
	public Vector2f position = new Vector2f();
	public double anlge = 0f;
	public double scaleFactor = 1f;
	public Camera(int x, int y) {
		position.x = x;
		position.y = y;
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

}
