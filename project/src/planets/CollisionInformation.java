package planets;

public class CollisionInformation {

	private float x, y;
	private Planet planet;
	
	public CollisionInformation(float x, float y, Planet planet) {
		this.setX(x);
		this.setY(y);
		this.setPlanet(planet);
	}

	public Planet getPlanet() {
		return planet;
	}

	public void setPlanet(Planet planet) {
		this.planet = planet;
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
	
}
