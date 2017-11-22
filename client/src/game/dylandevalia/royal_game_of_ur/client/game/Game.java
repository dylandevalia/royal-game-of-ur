package game.dylandevalia.royal_game_of_ur.client.game;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import java.awt.Color;

/**
 * Keeps track of all the game logic and enums
 */
public class Game {
	
	public Players currentPlayer = Players.NONE;
	public static final Color one_colour = ColorMaterial.purple;
	public static final Color two_colour = ColorMaterial.green;
	
	/**
	 * Player identifications
	 */
	public enum Players {
		NONE, ONE, TWO
	}
	
	/**
	 * When a move is checked, what the result of that move would be
	 */
	public enum MoveState {
		EMPTY,      // The space is empty and the counter can be moved
		BLOCKED,    // The space is blocked by the player's own piece or cannot move off the board
		CAPTURE,    // The move will capture an opponent's piece
		START,      // The counter will be moved back to the start
		END         // The piece will move off the end of the board
	}
}
