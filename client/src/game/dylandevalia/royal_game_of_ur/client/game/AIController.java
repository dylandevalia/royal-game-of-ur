package game.dylandevalia.royal_game_of_ur.client.game;

import game.dylandevalia.royal_game_of_ur.client.game.GameLogic.MoveState;
import game.dylandevalia.royal_game_of_ur.client.game.GameLogic.Players;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Counter;
import game.dylandevalia.royal_game_of_ur.utility.Log;

public class AIController {
	
	private Board board;
	private Counter[] one_counters, two_counters;
	
	public AIController(
		Board board,
		Counter[] one_counters,
		Counter[] two_counters
	) {
		this.board = board;
		this.one_counters = one_counters;
		this.two_counters = two_counters;
	}
	
	/**
	 * Checks if, for the given player and dice roll, there are any possible moves
	 *
	 * @param player The player to check move
	 * @param spaces The amount of spaces the counters will move
	 * @return True if there are possible moves
	 */
	public boolean arePossibleMoves(Players player, int spaces) {
		for (Counter counter : getPlayerCounters(player)) {
			if (board.checkMove(counter, spaces) != MoveState.BLOCKED) {
				return true;
			}
		}
		return false;
	}
	
	private Counter[] getPlayerCounters(Players player) {
		if (player == Players.ONE) {
			return one_counters;
		} else if (player == Players.TWO) {
			return two_counters;
		} else {
			Log.error("AI", "Unknown player to get counter");
			return null;
		}
	}
}
