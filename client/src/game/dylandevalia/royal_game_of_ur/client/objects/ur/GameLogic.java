package game.dylandevalia.royal_game_of_ur.client.objects.ur;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Framework;
import game.dylandevalia.royal_game_of_ur.client.objects.ur.AIController.MoveState;
import game.dylandevalia.royal_game_of_ur.client.objects.ur.Player.PlayerID;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.UrDice;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Graphics2D;

/**
 * Keeps track of all the objects logic and enums
 */
public class GameLogic {
	
	private Board board;
	
	private Player playerOne, playerTwo;
	
	/** The current player's turn */
	private Player currentPlayer;
	
	/** The previous player */
	private Player previousPlayer;
	
	/** The current roll of the dice */
	private int currentRoll = -1;
	
	/** Has the objects been won */
	private boolean won = false;
	
	/** Should the objects allow counters to be moved */
	private boolean allowMove = false;
	
	/** Should the objects be allowed to roll */
	private boolean allowRoll = true;

//	/** AI controller */
//	private AIController ai;
	
	/** The dice controller */
	private UrDice dice = new UrDice();
	
	public GameLogic(
		int boardStartLength, int boardMidLength, int boardEndLen,
		int noCounters
	) {
		board = new Board(boardStartLength, boardMidLength, boardEndLen);
		playerOne = new Player(
			PlayerID.ONE,
			"Amy",
			ColorMaterial.PURPLE,
			board.getRouteLength(),
			false
		);
		playerTwo = new Player(
			PlayerID.TWO,
			"Bert",
			ColorMaterial.GREEN,
			board.getRouteLength(),
			true
		);
		
		board.generate(playerOne.getRoute(), playerTwo.getRoute());
		generateCounters(noCounters);
		
		// Set player to one
		currentPlayer = playerOne;
		previousPlayer = playerOne;
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
		arePossibleMoves();
	}
	
	/**
	 * Checks if the next move is possible or should swap and reset players
	 */
	private void arePossibleMoves() {
		if (currentRoll == 0) {
			nextTurn(true);
		} else if (!AIController.arePossibleMoves(currentPlayer, currentRoll)) {
			Log.debug(
				"GAME",
				"No possible moves for player "
					+ currentPlayer.getId()
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
	public boolean canCounterMove(Vector2D mousePos, Counter counter) {
		return counter.isColliding(mousePos)
			&& counter.currentRouteIndex < board.getRouteLength()
			&& AIController.checkMove(currentPlayer.getRoute(), counter, currentRoll)
			!= MoveState.BLOCKED;
	}
	
	/**
	 * Checks if the objects is won by seeing if either player's end
	 * cluster is equal to the number of counters
	 *
	 * @param noCounters The number of counters that the objects is using
	 * @return True if the objects is won
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
			// Set current tile to not have counter
			route[counter.currentRouteIndex].setCounter(null);
		}
		
		final int oldRouteIndex = counter.currentRouteIndex;
		
		// Go through each tile one by one
		for (int i = 0; i < spaces; i++) {
			switch (AIController.checkMove(route, counter, 1)) {
				case END:
					player.getEndCluster().add(counter);
					counter.currentRouteIndex = board.getRouteLength();
					return null;
				default:
					Tile nextTile = board.getNextTile(route, counter, true);
					counter.setTarget(counterInTilePosition(nextTile), false);
					break;
			}
		}
		
		final int newRouteIndex = counter.currentRouteIndex;
		
		Tile finalTile = route[newRouteIndex];
		if (finalTile.hasCounter()) {
			// Must be opponent counter since moveCounter is not run if
			// is blocked by player's counter - move back to start
			final Counter takenCounter = finalTile.getCounter();
			final Player otherPlayer = getOtherPlayer();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						int dist = (newRouteIndex - oldRouteIndex) * Tile.WIDTH;
						int time = (int) Math.ceil((dist * 1.0) / Counter.SPEED);
						Thread.sleep(time * (int) Framework.GAME_HERTZ);
						
						moveCounterToStart(otherPlayer, takenCounter);
					} catch (InterruptedException e) {
						Log.error("GAME", "Failed to sleep before moving final tile", e);
					}
				}
			}).start();
		}
		finalTile.setCounter(counter);
		return finalTile;
	}
	
	private void moveCounterToStart(Player player, Counter counter) {
		Tile[] route = player.getRoute();
		while (AIController.checkMove(route, counter, -1) != MoveState.START) {
			Tile nextTile = board.getNextTile(route, counter, false);
			counter.setTarget(counterInTilePosition(nextTile), true);
		}
		player.getStartCluster().add(counter);
		counter.currentRouteIndex = -1;
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