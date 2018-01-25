package game.dylandevalia.royal_game_of_ur.client.game;

import game.dylandevalia.royal_game_of_ur.client.game.entities.Counter;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Tile;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;

public class Player {
	
	private PlayerNames name;
	private Counter[] counters;
	private CounterCluster startCluster, endCluster;
	private Tile[] route;
	
	public Player(PlayerNames name, int routeLength) {
		this.name = name;
		route = new Tile[routeLength];
	}
	
	public void generateCounters(int noCounters, Vector2D startPos, Vector2D endPos) {
		counters = new Counter[noCounters];
		
		startCluster = new CounterCluster(startPos, true);
		endCluster = new CounterCluster(endPos, false);
		
		for (int i = 0; i < noCounters; i++) {
			counters[i] = startCluster.addNew(name);
		}
	}
	
	public Counter[] getCounters() {
		return counters;
	}
	
	public Tile[] getRoute() {
		return route;
	}
	
	public CounterCluster getStartCluster() {
		return startCluster;
	}
	
	public CounterCluster getEndCluster() {
		return endCluster;
	}
	
	/** Player identifications */
	public enum PlayerNames {
		ONE, TWO
	}
}
