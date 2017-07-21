package envirorment;

import java.util.ArrayList;

import game.Core;
import objects.BasicObject;
import objects.Rocket;
import planets.Planet;
import player.Player;

public class Level {

	public ArrayList<BasicObject> objects = new ArrayList<BasicObject>();
	public ArrayList<Planet> planets = new ArrayList<Planet>();
	public ArrayList<Player> players = new ArrayList<Player>();
			
	
	Background background;
	
	public void setUp() {
		planets.add(new Planet(0, 0, 2400));
		planets.add(new Planet(6000, 5000, 1700));
		
		
		background = new Background();
		
		players.add(new Player());
		
		for(int i = 0; i < 20; i++)
		players.add(new Player());
		
	}
	
	public void draw() {
		
		
		background.render();
		
		
		for(Planet planet: planets) {
			planet.render();
		}
		for(BasicObject object: objects) {
			object.render();
		}
		for(Player player: players) {
			player.render();
		}
		
	}

	public void update() {
		for(BasicObject object: objects) {
			object.update();
		}
		for(Player player: players) {
			player.update();
		}
		
	}
	
}
