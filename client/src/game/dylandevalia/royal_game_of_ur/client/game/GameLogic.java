package game.dylandevalia.royal_game_of_ur.client.game;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.UrDice;
import java.awt.Color;

/**
 * Keeps track of all the game logic and enums
 */
public class GameLogic {
	
	/**
	 * The colour for {@link Players}.ONE
	 */
	public static final Color[] one_colour = ColorMaterial.PURPLE;
	/**
	 * The colour for {@link Players}.TWO
	 */
	public static final Color[] two_colour = ColorMaterial.GREEN;
	/**
	 * The name for {@link Players}.ONE
	 */
	public static final String one_name = "1";
	/**
	 * The name for {@link Players}.TWO
	 */
	public static final String two_name = "2";
	/**
	 * The current player's turn
	 */
	public Players currentPlayer = Players.ONE;
	/**
	 * The previous player
	 */
	public Players previousPlayer = Players.ONE;
	/**
	 * The current roll of the dice
	 */
	public int currentRoll = -1;
	/**
	 * Has the game been won
	 */
	public boolean won = false;
	/**
	 * Should the game allow counters to be moved
	 */
	public boolean allowMove = false;
	/**
	 * Should the game be allowed to roll
	 */
	public boolean allowRoll = true;
	/**
	 * The dice controller
	 */
	private UrDice dice = new UrDice();
	
	public GameLogic() {
		// Set player to one
		currentPlayer = Players.ONE;
		previousPlayer = Players.ONE;
	}
	
	private void swapPlayers() {
		if (currentPlayer == Players.ONE) {
			currentPlayer = Players.TWO;
		} else if (currentPlayer == Players.TWO) {
			currentPlayer = Players.ONE;
		} else {
			Log.error("GAME LOGIC", "Trying to swap NONE player");
		}
	}
	
	public void nextTurn(boolean swapPlayers) {
		previousPlayer = currentPlayer;
		if (swapPlayers) {
			swapPlayers();
		}
		
		allowMove = false;
		allowRoll = true;
	}
	
	public void rollDice() {
		allowMove = true;
		allowRoll = false;
		
		currentRoll = dice.roll();
		if (currentRoll == 0) {
			nextTurn(true);
		}
	}
	
	public Color[] getPlayerColour() {
		if (currentPlayer == Players.ONE) {
			return one_colour;
		} else if (currentPlayer == Players.TWO) {
			return two_colour;
		} else {
			Log.error("GAME LOGIC", "Player is NONE, trying to get colour");
			return null;
		}
	}
	
	public Color[] getPlayerColour(Players player) {
		if (player == Players.ONE) {
			return one_colour;
		} else if (player == Players.TWO) {
			return two_colour;
		} else {
			Log.error("GAME LOGIC", "Unknown player to get colour");
			return null;
		}
	}
	
	public String getPlayerName() {
		if (currentPlayer == Players.ONE) {
			return one_name;
		} else if (currentPlayer == Players.TWO) {
			return two_name;
		} else {
			Log.error("GAME LOGIC", "Player is NONE, trying to get name");
			return null;
		}
	}
	
	/**
	 * Player identifications
	 */
	public enum Players {
		ONE, TWO
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
