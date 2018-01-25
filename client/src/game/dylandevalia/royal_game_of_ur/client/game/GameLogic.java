package game.dylandevalia.royal_game_of_ur.client.game;

import game.dylandevalia.royal_game_of_ur.client.game.Player.PlayerNames;
import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.UrDice;
import java.awt.Color;

/**
 * Keeps track of all the game logic and enums
 */
public class GameLogic {
	
	/** The colour for {@link PlayerNames}.ONE */
	public static final Color[] one_colour = ColorMaterial.PURPLE;
	
	/** The colour for {@link PlayerNames}.TWO */
	public static final Color[] two_colour = ColorMaterial.GREEN;
	
	/** The name for {@link PlayerNames}.ONE */
	private static final String one_name = "1";
	
	/** The name for {@link PlayerNames}.TWO */
	private static final String two_name = "2";
	
	/** The current player's turn */
	public PlayerNames currentPlayer;
	
	/** The previous player */
	public PlayerNames previousPlayer;
	
	/** The current roll of the dice */
	public int currentRoll = -1;
	
	/** Has the game been won */
	public boolean won = false;
	
	/** Should the game allow counters to be moved */
	public boolean allowMove = false;
	
	/** Should the game be allowed to roll */
	public boolean allowRoll = true;
	
	/** AI controller */
	public AIController ai;
	
	/** The dice controller */
	private UrDice dice = new UrDice();
	
	public GameLogic(AIController ai) {
		// Set player to one
		currentPlayer = PlayerNames.ONE;
		previousPlayer = PlayerNames.ONE;
		
		this.ai = ai;
	}
	
	/**
	 * Swaps the current player
	 */
	private void swapPlayers() {
		if (currentPlayer == PlayerNames.ONE) {
			currentPlayer = PlayerNames.TWO;
		} else if (currentPlayer == PlayerNames.TWO) {
			currentPlayer = PlayerNames.ONE;
		} else {
			Log.error("GAME LOGIC", "Trying to swap NONE player");
		}
	}
	
	/**
	 * Resets turn and switches players if needed
	 *
	 * @param swapPlayers Whether the players should be swapped
	 */
	public void nextTurn(boolean swapPlayers) {
		previousPlayer = currentPlayer;
		if (swapPlayers) {
			swapPlayers();
		}
		
		allowMove = false;
		allowRoll = true;
	}
	
	/**
	 * Rerolls the dice and resets {@link #allowMove} and {@link #allowRoll} booleans
	 * Also checks if the next move is possible and resets if required
	 */
	public void rollDice() {
		allowMove = true;
		allowRoll = false;
		
		currentRoll = dice.roll();
		checkMove();
	}
	
	/**
	 * Checks if the next move is possible or should swap and reset players
	 */
	private void checkMove() {
		if (currentRoll == 0) {
			nextTurn(true);
		} else if (!ai.arePossibleMoves(currentPlayer, currentRoll)) {
			Log.debug(
				"PLAY/CLICK",
				"No possible moves for player "
					+ getPlayerName()
					+ " - swapping players"
			);
			nextTurn(true);
		}
	}
	
	public Color[] getPlayerColour() {
		if (currentPlayer == PlayerNames.ONE) {
			return one_colour;
		} else if (currentPlayer == PlayerNames.TWO) {
			return two_colour;
		} else {
			Log.error("GAME LOGIC", "Player is NONE, trying to get colour");
			return null;
		}
	}
	
	public Color[] getPlayerColour(PlayerNames player) {
		if (player == PlayerNames.ONE) {
			return one_colour;
		} else if (player == PlayerNames.TWO) {
			return two_colour;
		} else {
			Log.error("GAME LOGIC", "Unknown player to get colour");
			return null;
		}
	}
	
	public String getPlayerName() {
		if (currentPlayer == PlayerNames.ONE) {
			return one_name;
		} else if (currentPlayer == PlayerNames.TWO) {
			return two_name;
		} else {
			Log.error("GAME LOGIC", "Player is NONE, trying to get name");
			return null;
		}
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
