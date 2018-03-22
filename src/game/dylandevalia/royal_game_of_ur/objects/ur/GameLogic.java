package game.dylandevalia.royal_game_of_ur.objects.ur;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Framework;
import game.dylandevalia.royal_game_of_ur.objects.ur.Player.PlayerID;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.AI;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.AIController;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.AIController.MoveState;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Pair;
import game.dylandevalia.royal_game_of_ur.utility.UrDice;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Keeps track of all the objects logic and enums
 */
public class GameLogic {
	
	/** The game board */
	private Board board;
	
	/** The two players */
	private Player playerOne, playerTwo;
	
	/** The current player's turn */
	private Player currentPlayer;
	
	/** The previous player */
	private Player previousPlayer;
	
	/** The current roll of the dice */
	private int currentRoll = -1;
	
	/** Has the objects been won */
	private Player won = null;
	
	/** Should the objects allow counters to be moved */
	private boolean allowMove = false;
	
	/** Should the objects be allowed to roll */
	private boolean allowRoll = true;
	
	/** Is the scene animating */
	private boolean animating = false;
	
	/** Should the game skip animations */
	private boolean instantAnimate;
	
	/** The dice controller */
	private UrDice dice;
	
	/**
	 * @param boardStartLength The length of the board's starting area
	 * @param boardMidLength   The length of the board's middle area
	 * @param boardEndLen      The length of the board's end area
	 * @param noCounters       The number of counters each player has
	 * @param animateGame      Should the game use animations
	 * @param noDice           The number of dice the board will use
	 */
	public GameLogic(
		int boardStartLength, int boardMidLength, int boardEndLen,
		int noCounters, boolean animateGame,
		int noDice,
		AI playerOneAI, AI playerTwoAI
	) {
		// Create board
		board = new Board(boardStartLength, boardMidLength, boardEndLen);
		
		// Create players
		playerOne = new Player(
			PlayerID.ONE,
			"Ed",
			ColorMaterial.PURPLE,
			board.getRouteLength(),
			playerOneAI
		);
		playerTwo = new Player(
			PlayerID.TWO,
			"Dylan",
			ColorMaterial.GREEN,
			board.getRouteLength(),
			playerTwoAI
		);
		
		// Generate board and counters
		board.generate(playerOne.getRoute(), playerTwo.getRoute());
		generateCounters(noCounters);
		
		// Set player to one
		currentPlayer = playerOne;
		previousPlayer = playerOne;
		
		// Create dice
		dice = new UrDice(noDice);
		
		// Should animate game
		this.instantAnimate = !animateGame;
		if (!animateGame) {
			CounterCluster.instantAnimate = true;
			Counter.instantAnimate = true;
		}
	}
	
	public GameLogic(boolean animateGame, AI playerOneAI, AI playerTwoAI) {
		this(
			4, 8, 2,
			6, animateGame,
			4,
			playerOneAI, playerTwoAI
		);
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
	 * Rerolls the dice and resets {@link #allowMove} and {@link #allowRoll} booleans Also checks if
	 * the next move is possible and resets if required
	 */
	public void rollDice() {
		allowMove = true;
		allowRoll = false;
		
		currentRoll = dice.roll();
		Log.trace("GAME", "Rolled: " + currentRoll);
		
		if (arePossibleMoves() && currentPlayer.isAI()) {
			takeAITurn();
		}
	}
	
	/**
	 * Checks if the next move is possible or should swap and reset players
	 */
	private boolean arePossibleMoves() {
		if (currentRoll == 0) {
			nextTurn(true);
			return false;
		} else if (!AIController.arePossibleMoves(currentPlayer, currentRoll)) {
			Log.debug(
				"GAME",
				"No possible moves for player "
					+ currentPlayer.getId()
					+ " - swapping players"
			);
			nextTurn(true);
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the counter can move by checking: 1) Did the mouse click on the counter 2) If the
	 * counter is in play (not in end cluster) and 3) If the move, if played, would it be blocked!
	 *
	 * @param mousePos The position of the mouse
	 * @param counter  The counter to check if it can move
	 * @return True if move is possible
	 */
	public boolean canCounterMove(Vector2D mousePos, Counter counter) {
		return counter.isColliding(mousePos)
			&& counter.getCurrentRouteIndex() < board.getRouteLength()
			&& AIController.checkMove(currentPlayer.getRoute(), counter, currentRoll)
			!= MoveState.BLOCKED;
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
		
		if (counter.getCurrentRouteIndex() < 0) {
			// Get out of starting cluster
			CounterCluster cluster = player.getStartCluster();
			cluster.remove(counter);
		} else {
			// Set current tile to not have counter
			route[counter.getCurrentRouteIndex()].setCounter(null);
		}
		
		final int oldRouteIndex = counter.getCurrentRouteIndex();
		
		// Go through each tile one by one
		for (int i = 0; i < spaces; i++) {
			switch (AIController.checkMove(route, counter, 1)) {
				case END:
					player.getEndCluster().add(counter);
					counter.setCurrentRouteIndex(board.getRouteLength());
					return null;
				default:
					Tile nextTile = board.getNextTile(route, counter, true);
					animating = true;
					counter.setTarget(counterInTilePosition(nextTile), false);
					break;
			}
		}
		
		final int newRouteIndex = counter.getCurrentRouteIndex();
		
		Tile finalTile = route[newRouteIndex];
		if (finalTile.hasCounter()) {
			// Must be opponent counter since moveCounter is not run if
			// is blocked by player's counter - move back to start
			final Counter takenCounter = finalTile.getCounter();
			final Player otherPlayer = getOtherPlayer();
			if (instantAnimate) {
				moveCounterToStart(otherPlayer, takenCounter);
			} else {
				new Thread(() -> {
					int dist = (newRouteIndex - oldRouteIndex) * Tile.WIDTH;
					int time = (int) Math.ceil((dist * 1.0) / Counter.SPEED);
					Utility.sleep(time * (int) Framework.GAME_HERTZ);
					
					moveCounterToStart(otherPlayer, takenCounter);
				}).start();
			}
		}
		finalTile.setCounter(counter);
		return finalTile;
	}
	
	/**
	 * Moves the counter to the starting cluster along the player's route
	 *
	 * @param player  The player who owns the counter
	 * @param counter The counter to move
	 */
	private void moveCounterToStart(Player player, Counter counter) {
		Tile[] route = player.getRoute();
		while (AIController.checkMove(route, counter, -1) != MoveState.START) {
			Tile nextTile = board.getNextTile(route, counter, false);
			animating = true;
			counter.setTarget(counterInTilePosition(nextTile), true);
		}
		player.getStartCluster().add(counter);
		counter.setCurrentRouteIndex(-1);
	}
	
	/**
	 * Runs at the end of a turn - checks if the game is won and initiates the next turn
	 *
	 * @param finalTile The final tile that the counter just move to that turn
	 */
	public void endOfTurn(Tile finalTile) {
		if (checkIfWon()) {
			return;
		}
		
		nextTurn(finalTile == null || !finalTile.isRosette());
	}
	
	/**
	 * Checks if the objects is won by seeing if either player's end cluster is equal to the number
	 * of counters
	 *
	 * @return True if the objects is won
	 */
	private boolean checkIfWon() {
		if (
			playerOne.getEndCluster().getSize() == playerOne.getCounters().length
				|| playerTwo.getEndCluster().getSize() == playerTwo.getCounters().length
			) {
			won = currentPlayer;
			Log.debug("GAME", "GAME WON! - " + won.getId());
			return true;
		}
		return false;
	}
	
	/**
	 * Resets turn and switches players if needed
	 *
	 * @param swapPlayers Whether the players should be swapped
	 */
	private void nextTurn(boolean swapPlayers) {
		previousPlayer = currentPlayer;
		if (swapPlayers) {
			swapPlayers();
		}
		
		allowMove = false;
		allowRoll = true;
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
	 * Calculates and performs move for the AI
	 */
	private void takeAITurn() {
		ArrayList<Pair<Counter, MoveState>> moves = AIController
			.getPlayableCounters(currentPlayer, currentRoll);
		
		Counter counterToMove = currentPlayer.getAI()
			.makeMove(this, moves);
		Tile finalTile = moveCounter(currentPlayer, counterToMove, currentRoll);
		
		endOfTurn(finalTile);
	}
	
	public void update(Vector2D mousePos) {
		if (isWon()) {
			return;
		}
		
		animating = false;
		
		// Update counters
		Counter hoveringCounter = updateCounters(
			mousePos,
			null,
			playerOne.getCounters(),
			currentPlayer == playerOne
		);
		hoveringCounter = updateCounters(
			mousePos,
			hoveringCounter,
			playerTwo.getCounters(),
			currentPlayer == playerTwo
		);
		
		MoveState moveState = null;
		Tile hoveringTile = null;
		if (hoveringCounter != null) {
			moveState = AIController
				.checkMove(currentPlayer.getRoute(), hoveringCounter, currentRoll);
			int index = hoveringCounter.getCurrentRouteIndex() + currentRoll;
			hoveringTile = currentPlayer.getRoute()[
				(index >= board.getRouteLength()) ? index - currentRoll : index
				];
		}
		
		// Update board (tiles)
		board.update(moveState, hoveringTile);
		
		if (!animating && currentPlayer.isAI()) {
			rollDice();
		}
	}
	
	/**
	 * Goes through a counter set and updates all the pieces. If mouse is hovering over a counter,
	 * returns that counter
	 *
	 * @param mousePos        The position of the mouse
	 * @param hoveringCounter Counter the mouse is hovering over (else {@code null})
	 * @param counters        The array of counters
	 * @param isCurrentPlayer Does the counter set belong to the current player
	 * @return The counter the mouse is hovering over or {@code null}
	 */
	private Counter updateCounters(
		Vector2D mousePos,
		Counter hoveringCounter,
		Counter[] counters,
		boolean isCurrentPlayer
	) {
		for (Counter counter : counters) {
			counter.update(
				mousePos,
				allowMove && isCurrentPlayer
					&& counter.getCurrentRouteIndex() < board.getRouteLength()
			);
			
			if (!animating && counter.isMoving()) {
				animating = true;
			}
			
			if (hoveringCounter == null && counter.isMouseHovering()) {
				hoveringCounter = counter;
			}
		}
		return hoveringCounter;
	}
	
	public void draw(Graphics2D g, double interpolate) {
		board.draw(g, interpolate);
		
		// drawCounters(g, interpolate);
		for (Counter counter : playerOne.getCounters()) {
			counter.draw(g, interpolate);
		}
		for (Counter counter : playerTwo.getCounters()) {
			counter.draw(g, interpolate);
		}
	}
	
	/**
	 * Draws the counters in the correct order
	 */
	private void drawCounters(Graphics2D g, double interpolate) {
		ArrayList<Counter> still = new ArrayList<>(),
			cluster = new ArrayList<>(),
			moving = new ArrayList<>();
		
		Counter[] one = playerOne.getCounters(), two = playerTwo.getCounters();
		for (int i = 0; i < playerOne.getCounters().length; i++) {
			if (!one[i].isMoving()) {
				still.add(one[i]);
			} else if (one[i].getCurrentRouteIndex() < 0
				|| one[i].getCurrentRouteIndex() >= board.getRouteLength()
				) {
				
			}
			
		}
		
		still.forEach(c -> c.draw(g, interpolate));
		
	}
	
	
	/* ------- */
	/* Getters */
	/* ------- */
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public Player getPreviousPlayer() {
		return previousPlayer;
	}
	
	/**
	 * Gets the other player that's not the current player
	 *
	 * @return The current player's opponent
	 */
	public Player getOtherPlayer() {
		if (currentPlayer == playerOne) {
			return playerTwo;
		} else if (currentPlayer == playerTwo) {
			return playerOne;
		} else {
			Log.error("GAME", "Can't find other player");
			return null;
		}
	}
	
	public int getCurrentRoll() {
		return currentRoll;
	}
	
	public boolean isWon() {
		return won != null;
	}
	
	public Player getWon() {
		return won;
	}
	
	public boolean isAllowMove() {
		return allowMove;
	}
	
	public boolean isAllowRoll() {
		return allowRoll;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public UrDice getDice() {
		return dice;
	}
	
	public boolean isAnimating() {
		return animating;
	}
	
	public int playerFitness(PlayerID id) {
		Player player = (id == PlayerID.ONE) ? playerOne : playerTwo;
		Player other = (id == PlayerID.ONE) ? playerTwo : playerOne;
		return player.getEndCluster().getSize() - other.getEndCluster().getSize();
	}
}
