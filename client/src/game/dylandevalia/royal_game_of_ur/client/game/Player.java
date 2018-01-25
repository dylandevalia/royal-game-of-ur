package game.dylandevalia.royal_game_of_ur.client.game;

import game.dylandevalia.royal_game_of_ur.client.game.entities.Counter;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Tile;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;

public class Player {
	
	/**
	 * The name of the player
	 *
	 * @see PlayerNames
	 */
	private PlayerNames name;
	
	/** Counters owned by the player */
	private Counter[] counters;
	
	/** The start and end clusters of counters */
	private CounterCluster startCluster, endCluster;
	
	/** The route that the player's counters will follow */
	private Tile[] route;
	
	public Player(PlayerNames name, int routeLength) {
		this.name = name;
		route = new Tile[routeLength];
	}
	
	/**
	 * Generates the start/end clusters and initialises all the counters
	 *
	 * @param noCounters The number of counters to create
	 * @param startPos   The position of the start cluster
	 * @param endPos     The position of the end cluster
	 */
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
	
	/**
	 * Player identifications
	 */
	public enum PlayerNames {
		ONE, TWO
	}
}
