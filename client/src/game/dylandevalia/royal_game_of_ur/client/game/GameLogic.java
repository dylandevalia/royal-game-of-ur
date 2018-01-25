package game.dylandevalia.royal_game_of_ur.client.game;

import game.dylandevalia.royal_game_of_ur.client.game.AIController.MoveState;
import game.dylandevalia.royal_game_of_ur.client.game.Player.PlayerNames;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Counter;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Tile;
import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.UrDice;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Graphics2D;

/**
 * Keeps track of all the game logic and enums
 */
public class GameLogic {
	
	// TODO
	/** The name for {@link PlayerNames}.ONE */
	private static final String one_name = "1";
	
	/** The name for {@link PlayerNames}.TWO */
	private static final String two_name = "2";
	
	private Board board;
	
	private Player playerOne, playerTwo;
	
	/** The current player's turn */
	private Player currentPlayer;
	
	/** The previous player */
	private Player previousPlayer;
	
	/** The current roll of the dice */
	private int currentRoll = -1;
	
	/** Has the game been won */
	private boolean won = false;
	
	/** Should the game allow counters to be moved */
	private boolean allowMove = false;
	
	/** Should the game be allowed to roll */
	private boolean allowRoll = true;
	
	/** AI controller */
	private AIController ai;
	
	/** The dice controller */
	private UrDice dice = new UrDice();
	
	public GameLogic(
		int boardStartLength, int boardMidLength, int boardEndLen,
		int noCounters
	) {
		board = new Board(boardStartLength, boardMidLength, boardEndLen);
		playerOne = new Player(
			PlayerNames.ONE,
			ColorMaterial.PURPLE,
			board.getRouteLength()
		);
		playerTwo = new Player(
			PlayerNames.TWO,
			ColorMaterial.GREEN,
			board.getRouteLength()
		);
		
		board.generate(playerOne.getRoute(), playerTwo.getRoute());
		generateCounters(noCounters);
		
		// Set player to one
		currentPlayer = playerOne;
		previousPlayer = playerOne;
		
		ai = new AIController(board, playerOne, playerTwo);
	}
	
	/**
	 * Generates the counters, clusters and their starting positions
	 */
	private void generateCounters(int noCounters) {
		playerOne.generateCounters(
			noCounters,
			counterInTilePosition(playerOne.getRoute()[0])
				.add(0, Tile.WIDTH),
			counterInTilePosition(playerOne.getRoute()[board.getRouteLength() - 1])
				.add(0, Tile.WIDTH)
		);
		playerTwo.generateCounters(
			noCounters,
			counterInTilePosition(playerTwo.getRoute()[0])
				.sub(0, Tile.WIDTH),
			counterInTilePosition(playerTwo.getRoute()[board.getRouteLength() - 1])
				.sub(0, Tile.WIDTH)
		);
	}
	
	/**
	 * Gets the position vector of the middle of the given tile where a counter should sit
	 *
	 * @param tile The tile which the counter should sit in
	 * @return The position vector of the counter
	 */
	private Vector2D counterInTilePosition(Tile tile) {
		return new Vector2D(
			tile.getPos().x + (Tile.WIDTH / 2) - (Counter.WIDTH / 2),
			tile.getPos().y + (Tile.WIDTH / 2) - (Counter.WIDTH / 2)
		);
	}
	
	/**
	 * Swaps the current player
	 */
	private void swapPlayers() {
		if (currentPlayer == playerOne) {
			currentPlayer = playerTwo;
		} else if (currentPlayer == playerTwo) {
			currentPlayer = playerOne;
		} else {
			Log.error("GAME", "Trying to swap unknown player");
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
		checkMoveIsPossible();
	}
	
	/**
	 * Checks if the next move is possible or should swap and reset players
	 */
	private void checkMoveIsPossible() {
		if (currentRoll == 0) {
			nextTurn(true);
		} else if (!ai.arePossibleMoves(currentPlayer, currentRoll)) {
			Log.debug(
				"GAME",
				"No possible moves for player "
					+ currentPlayer.getName()
					+ " - swapping players"
			);
			nextTurn(true);
		}
	}
	
	/**
	 * Checks if the counter can move by checking:
	 * 1) Did the mouse click on the counter
	 * 2) If the counter is in play (not in end cluster)
	 * and 3) If the move, if played, would it be blocked!
	 *
	 * @param mousePos The position of the mouse
	 * @param counter  The counter to check if it can move
	 * @return True if move is possible
	 */
	public boolean isMovePossible(Vector2D mousePos, Counter counter) {
		return counter.isColliding(mousePos)
			&& counter.currentRouteIndex < board.getRouteLength()
			&& ai.checkMove(currentPlayer.getRoute(), counter, currentRoll) != MoveState.BLOCKED;
	}
	
	/**
	 * Checks if the game is won by seeing if either player's end
	 * cluster is equal to the number of counters
	 *
	 * @param noCounters The number of counters that the game is using
	 * @return True if the game is won
	 */
	public boolean checkIfWon(int noCounters) {
		if (
			playerOne.getEndCluster().getSize() == noCounters
				|| playerTwo.getEndCluster().getSize() == noCounters
			) {
			won = true;
			Log.debug("GAME", "GAME WON!");
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the other player that's not the current player
	 *
	 * @return The current player's opponent
	 */
	private Player getOtherPlayer() {
		if (currentPlayer == playerOne) {
			return playerTwo;
		} else if (currentPlayer == playerTwo) {
			return playerOne;
		} else {
			Log.error("GAME", "Can't find other player");
			return null;
		}
	}
	
	/**
	 * Moves a counter a certain amount of spaces through the given route. Adds all the moves along
	 * the way so that the counter will still move through the route rather than move directly to
	 * the last target
	 *
	 * @param counter The counter to move
	 * @param spaces  The amount of spaces to move the counter along the route
	 */
	public Tile moveCounter(Player player, Counter counter, int spaces) {
		Tile[] route = player.getRoute();
		
		if (counter.currentRouteIndex < 0) {
			// Get out of starting cluster
			CounterCluster cluster = player.getStartCluster();
			cluster.remove(counter);
		} else {
			route[counter.currentRouteIndex].setCounter(null);
		}
		
		// Go through each tile one by one
		for (int i = 0; i < Math.abs(spaces); i++) {
			switch (ai.checkMove(route, counter, (spaces > 0) ? 1 : -1)) {
				case START:
					player.getStartCluster().add(counter);
					counter.currentRouteIndex = -1;
					return null;
				case END:
					player.getEndCluster().add(counter);
					counter.currentRouteIndex = board.getRouteLength();
					return null;
				default:
					Tile nextTile = board.getNextTile(route, counter, spaces > 0);
					counter.setTarget(counterInTilePosition(nextTile), spaces < 0);
					break;
			}
		}
		
		Tile finalTile = route[counter.currentRouteIndex];
		if (finalTile.hasCounter()) {
			// Must be opponent counter since moveCounter is not run if
			// is blocked by player's counter - move back to start
			moveCounter(getOtherPlayer(), finalTile.getCounter(), -board.getRouteLength());
		}
		finalTile.setCounter(counter);
		return finalTile;
	}
	
	public void update(Vector2D mousePos) {
		// Update board (tiles)
		board.update();
		
		// Update counters
		for (Counter counter : playerOne.getCounters()) {
			counter.update(mousePos,
				allowMove
					&& currentPlayer == playerOne
			);
		}
		for (Counter counter : playerTwo.getCounters()) {
			counter.update(
				mousePos,
				allowMove
					&& currentPlayer == playerTwo
			);
		}
	}
	
	public void draw(Graphics2D g, double interpolate) {
		board.draw(g, interpolate);
		for (Counter counter : playerOne.getCounters()) {
			counter.draw(g, interpolate, playerOne.getColors());
		}
		for (Counter counter : playerTwo.getCounters()) {
			counter.draw(g, interpolate, playerTwo.getColors());
		}
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public Player getPreviousPlayer() {
		return previousPlayer;
	}
	
	public int getCurrentRoll() {
		return currentRoll;
	}
	
	public boolean isWon() {
		return won;
	}
	
	public boolean isAllowMove() {
		return allowMove;
	}
	
	public boolean isAllowRoll() {
		return allowRoll;
	}
}
