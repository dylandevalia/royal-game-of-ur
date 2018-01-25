package game.dylandevalia.royal_game_of_ur.client.game;

import game.dylandevalia.royal_game_of_ur.client.game.entities.Counter;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Tile;

public class AIController {
	
	/** The game board */
	private Board board;
	
	
	private Player playerOne, playerTwo;
	
	public AIController(Board board, Player playerOne,
		Player playerTwo) {
		this.board = board;
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
	}
	
	/**
	 * Checks move a certain amount ahead and returns the corresponding {@link MoveState}
	 *
	 * @param counter The counter to calculate new position
	 * @param spaces  The amount of spaces ahead to check
	 * @return The appropriate {@link MoveState} according to the move
	 */
	public MoveState checkMove(Tile[] route, Counter counter, int spaces) {
		int newIndex = counter.currentRouteIndex + spaces;
		
		/* Going off the board */
		if (newIndex < 0) {
			return MoveState.START;
		} else if (newIndex == route.length) {
			// Needs exact number to exit board
			return MoveState.END;
		} else if (newIndex > route.length) {
			return MoveState.BLOCKED;
		}
		
		Tile newTile = route[newIndex];
		if (newTile.hasCounter()) {
			if (newTile.getCounter().player == counter.player) {
				return MoveState.BLOCKED;
			} else if (newTile.isRosette()) {
				return MoveState.BLOCKED;
			} else {
				return MoveState.CAPTURE;
			}
		}
		
		return MoveState.EMPTY;
	}
	
	/**
	 * Checks if, for the given player and dice roll, there are any possible moves
	 *
	 * @param spaces The amount of spaces the counters will move
	 * @return True if there are possible moves
	 */
	public boolean arePossibleMoves(Player player, int spaces) {
		for (Counter counter : player.getCounters()) {
			if (checkMove(player.getRoute(), counter, spaces) != MoveState.BLOCKED) {
				return true;
			}
		}
		return false;
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
