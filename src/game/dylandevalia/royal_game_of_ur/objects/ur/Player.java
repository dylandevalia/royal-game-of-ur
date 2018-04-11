package game.dylandevalia.royal_game_of_ur.objects.ur;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.AI;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;

/**
 * The player class which holds the player's counters, counter route and AI
 */
public class Player {
	
	/**
	 * The id of the player
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
	private AI ai;
	
	/** The number of times one of this players counters were captured */
	private int countersCaptured = 0;
	
	/**
	 * @param id          The ID of the player
	 * @param name        The name of the player
	 * @param colors      The colour space that the player will use ({@link ColorMaterial})
	 * @param routeLength The length of the board's route for each player
	 * @param ai          The {@link AI} the player should use. {@code null} if human player
	 */
	Player(PlayerID id, String name, Color[] colors, int routeLength, AI ai) {
		this.id = id;
		this.name = name;
		this.colors = colors;
		route = new Tile[routeLength];
		this.ai = ai;
	}
	
	/**
	 * Generates the start/end clusters and initialises all the counters
	 *
	 * @param noCounters The number of counters to create
	 * @param startPos   The position of the start cluster
	 * @param endPos     The position of the end cluster
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
	
	public int getNoCounters() {
		return counters.length;
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
	
	boolean isAI() {
		return ai != null;
	}
	
	AI getAI() {
		return ai;
	}
	
	public int getCountersCaptured() {
		return countersCaptured;
	}
	
	public void countersCaptured() {
		countersCaptured++;
	}
	
	/**
	 * Player identifications
	 */
	public enum PlayerID {
		ONE, TWO
	}
}
