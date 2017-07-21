package game;

import java.util.ArrayList;

import objects.BasicObject;
import planets.Planet;
import player.Player;

public class ObjectAccess {
	public static ArrayList<Planet> getPlanets() {
		return Core.game.getLevel().planets;
	}
	public static ArrayList<Player> getPlayers() {
		return Core.game.getLevel().players;
	}
	public static ArrayList<BasicObject> getObjects() {
		return Core.game.getLevel().objects;
	}
}
