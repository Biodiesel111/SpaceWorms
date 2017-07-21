package objects;

import org.lwjgl.util.vector.Vector2f;

import game.ObjectAccess;
import player.Player;
import tools.CollisionTools;

public class Rocket extends BasicObject {

	public Rocket(float x, float y, float deltaX, float deltaY, int radius) {
		super(x, y, deltaX, deltaY, radius);
		
	}
	
	@Override
	public void update() {
		super.update();
		
		if(collidesWithPlanet() && isActivated()) {
			getCollisionInfos().getPlanet().addHole(getCollisionInfos().getX(), getCollisionInfos().getY(), 180);
			
			for(Player player: ObjectAccess.getPlayers()) {
				if(CollisionTools.circlePoint(getX(), getY(), 180, player.getX(), player.getY())) {
					Vector2f vecToHole = new Vector2f(getX() - player.getX(), getY() - player.getY());
					float len = vecToHole.length();
					vecToHole.normalise();
					player.getCurrentDirection().x = -70f * (vecToHole.x/((len/10f)+4f));
					player.getCurrentDirection().y = -70f * (vecToHole.y/((len/10f)+4f));
					System.out.println("X: " + -200f * (vecToHole.x/((len/10f)+2f)));
					System.out.println("Y: " + -200f * (vecToHole.y/((len/10f)+2f)));
				}
			}
			
			setActivated(false);
		}
	}

}
