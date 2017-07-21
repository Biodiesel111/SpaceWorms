package tools;

import org.lwjgl.util.vector.Vector2f;

public class CollisionTools {

	public static boolean circlePoint(float xC, float yC, float rC, float xP, float yP) {
		
		Vector2f dis = new Vector2f(xC - xP, yC- yP);
		
		return dis.length() < rC;
		
		
	}
	
}
