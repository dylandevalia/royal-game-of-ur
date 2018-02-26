package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

import game.dylandevalia.royal_game_of_ur.objects.ur.Counter;
import game.dylandevalia.royal_game_of_ur.objects.ur.Player;
import game.dylandevalia.royal_game_of_ur.objects.ur.Tile;
import game.dylandevalia.royal_game_of_ur.utility.Pair;
import java.util.ArrayList;

/**
 * Static AI class which performs various {@link Counter} calculations on the game board
 */
public class AIController {
	
	/**
	 * Checks move a certain amount ahead and returns the corresponding {@link MoveState}
	 *
	 * @param counter The counter to calculate new position
	 * @param spaces  The amount of spaces ahead to check
	 * @return The appropriate {@link MoveState} according to the move
	 */
	public static MoveState checkMove(Tile[] route, Counter counter, int spaces) {
		int newIndex = counter.getCurrentRouteIndex() + spaces;
		
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
			if (newTile.getCounter().getPlayer() == counter.getPlayer()) {
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
	public static boolean arePossibleMoves(Player player, int spaces) {
		for (Counter counter : player.getCounters()) {
			if (checkMove(player.getRoute(), counter, spaces) != MoveState.BLOCKED) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns an array list of possible moves that the player can perform
	 *
	 * @param player The player whose turn it is
	 * @param spaces The amount of spaces the counter will move (dice roll)
	 * @return The list of possible moves
	 */
	public static ArrayList<Pair<Counter, MoveState>> getPlayableCounters(Player player,
		int spaces) {
		ArrayList<Pair<Counter, MoveState>> moves = new ArrayList<>();
		for (Counter counter : player.getCounters()) {
			MoveState state = checkMove(player.getRoute(), counter, spaces);
			if (state != MoveState.BLOCKED) {
				moves.add(new Pair<>(counter, state));
			}
		}
		return moves;
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
