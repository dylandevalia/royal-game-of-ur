package game.dylandevalia.royal_game_of_ur.objects.ur;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.AI;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;

public class Player {
	
	/**
	 * The id of the player
	 *
	 * @see PlayerID
	 */
	private PlayerID id;
	
	/** Name of the player */
	private String name;
	
	/** Counters owned by the player */
	private Counter[] counters;
	
	/** The start and end clusters of counters */
	private CounterCluster startCluster, endCluster;
	
	/** The route that the player's counters will follow */
	private Tile[] route;
	
	/**
	 * The colours associated with the player
	 *
	 * @see ColorMaterial
	 */
	private Color[] colors;
	
	/** Is the player AI controlled */
	private AI ai = null;
	
	Player(PlayerID id, String name, Color[] colors, int routeLength, boolean isAI) {
		this.id = id;
		this.name = name;
		this.colors = colors;
		route = new Tile[routeLength];
		if (isAI) {
			ai = new AI();
		}
	}
	
	/**
	 * Generates the start/end clusters and initialises all the counters
	 *
	 * @param noCounters The number of counters to create
	 * @param startPos The position of the start cluster
	 * @param endPos The position of the end cluster
	 */
	void generateCounters(int noCounters, Vector2D startPos, Vector2D endPos) {
		counters = new Counter[noCounters];
		
		startCluster = new CounterCluster(startPos, true);
		endCluster = new CounterCluster(endPos, false);
		
		for (int i = 0; i < noCounters; i++) {
			counters[i] = startCluster.addNew(id, colors);
		}
	}
	
	PlayerID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Counter[] getCounters() {
		return counters;
	}
	
	public Tile[] getRoute() {
		return route;
	}
	
	CounterCluster getStartCluster() {
		return startCluster;
	}
	
	CounterCluster getEndCluster() {
		return endCluster;
	}
	
	public Color[] getColors() {
		return colors;
	}
	
	public Color getMainColor() {
		return colors[5];
	}
	
	boolean isAI() {
		return ai != null;
	}
	
	AI getAI() {
		return ai;
	}
	
	/**
	 * Player identifications
	 */
	public enum PlayerID {
		ONE, TWO
	}
}
