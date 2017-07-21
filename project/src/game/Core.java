package game;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class Core {

	public static float zoomFactor = 1f;
	public static Game game;
	
	public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(1280,720));
        
            Display.create(new PixelFormat(0, 16, 1));
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(-0.5f * 1920 * zoomFactor, 0.5f * 1920 * zoomFactor, -0.5f * 1080 * zoomFactor, 0.5f * 1080 * zoomFactor, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
        
        game = new Game();
        game.setUp();
         
        while (!Display.isCloseRequested()) {
             
            // render OpenGL here
        	
            render(); 
        	
            Display.update();
            Display.sync(60);
        }
         
        Display.destroy();
    }
	
	public void render() {
		game.loop();
	}
	
     
    public static void main(String[] argv) {
       Core core = new Core();
       core.start();
    }

}
