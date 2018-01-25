package game.dylandevalia.royal_game_of_ur.client.game;

import game.dylandevalia.royal_game_of_ur.client.game.GameLogic.MoveState;
import game.dylandevalia.royal_game_of_ur.client.game.Player.PlayerNames;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Counter;
import game.dylandevalia.royal_game_of_ur.utility.Log;

public class AIController {
	
	private Board board;
	private Player playerOne, playerTwo;
	
	public AIController(Board board, Player playerOne,
		Player playerTwo) {
		this.board = board;
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
	}
	
	/**
	 * Checks if, for the given player and dice roll, there are any possible moves
	 *
	 * @param name   The player to check move
	 * @param spaces The amount of spaces the counters will move
	 * @return True if there are possible moves
	 */
	public boolean arePossibleMoves(PlayerNames name, int spaces) {
		Player player = playerNameToPlayer(name);
		for (Counter counter : player.getCounters()) {
			if (board.checkMove(player.getRoute(), counter, spaces) != MoveState.BLOCKED) {
				return true;
			}
		}
		return false;
	}
	
	private Player playerNameToPlayer(PlayerNames name) {
		if (name == PlayerNames.ONE) {
			return playerOne;
		} else if (name == PlayerNames.TWO) {
			return playerTwo;
		} else {
			Log.error("AI", "Unknown player name (to player)");
			return null;
		}
	}
}
