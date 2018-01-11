package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.game.Board;
import game.dylandevalia.royal_game_of_ur.client.game.CounterCluster;
import game.dylandevalia.royal_game_of_ur.client.game.GameLogic;
import game.dylandevalia.royal_game_of_ur.client.game.GameLogic.MoveState;
import game.dylandevalia.royal_game_of_ur.client.game.GameLogic.Players;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Counter;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Tile;
import game.dylandevalia.royal_game_of_ur.client.game.entities.buttons.TextButton;
import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Framework;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

// TODO: Delay captured counter returning until taken
public class Play implements State {
	
	/**
	 * Reference to the state manager
	 */
	private StateManager stateManager;
	
	/**
	 * GameLogic board
	 */
	private Board board = new Board(4, 8, 2);
	
	/**
	 * Holds the game logic
	 */
	private GameLogic game;
	
	/* Counters */
	/**
	 * The number of counters each player should have
	 */
	private int noCounters = 6;
	/**
	 * Player one's counters
	 */
	private Counter[] one_counters = new Counter[noCounters];
	/**
	 * Player two's counters
	 */
	private Counter[] two_counters = new Counter[noCounters];
	/**
	 * The clusters for each of the start and end areas
	 */
	
	private CounterCluster one_countersStart, one_countersEnd, two_countersStart, two_countersEnd;
	
	/* Buttons */
	
	/**
	 * Reroll button
	 */
	private TextButton btn_roll;
	
	@Override
	public void initialise(StateManager stateManager) {
		this.stateManager = stateManager;
		
		game = new GameLogic();
		Log.info("PLAY", "GameLogic created");
		
		board.generate();
		generateCounters();
		
		int btn_roll_width = 100;
		int btn_roll_height = 60;
		btn_roll = new TextButton(
			Window.WIDTH - (int)(btn_roll_width * 1.15),
			(Window.HEIGHT / 2) - (btn_roll_height / 2),
			btn_roll_width, btn_roll_height,
			"Roll",
			ColorMaterial.AMBER[5], ColorMaterial.AMBER[3], ColorMaterial.GREY[9],
			game::rollDice
		);
		Log.info("PLAY", "Generation completed");
	}
	
	/**
	 * Generates the counters, clusters and their starting positions
	 */
	private void generateCounters() {
		one_countersStart = new CounterCluster(
			counterInTilePosition(board.getRoute(Players.ONE)[0])
				.add(0, Tile.WIDTH),
			true
		);
		one_countersEnd = new CounterCluster(
			counterInTilePosition(board.getRoute(Players.ONE)[board.getRouteLength() - 1])
				.add(0, Tile.WIDTH),
			false
		);
		two_countersStart = new CounterCluster(
			counterInTilePosition(board.getRoute(Players.TWO)[0]).sub(0, Tile.WIDTH),
			true
		);
		two_countersEnd = new CounterCluster(
			counterInTilePosition(board.getRoute(Players.TWO)[board.getRouteLength() - 1])
				.sub(0, Tile.WIDTH),
			false
		);
		
		for (int i = 0; i < noCounters; i++) {
			one_counters[i] = one_countersStart.addNew(Players.ONE);
			two_counters[i] = two_countersStart.addNew(Players.TWO);
		}
	}
	
	@Override
	public void update() {
		Vector2D mousePos = Framework.getMousePos();
		
		board.update();
		
		for (Counter counter : one_counters) {
			counter.update(mousePos, game.allowMove && game.currentPlayer == Players.ONE);
		}
		for (Counter counter : two_counters) {
			counter.update(mousePos, game.allowMove && game.currentPlayer == Players.TWO);
		}
		
		btn_roll.setActive(game.allowRoll);
		btn_roll.update(mousePos);
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		g.setColor(ColorMaterial.GREY[2]);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		
		/* Objects */
		
		board.draw(g, interpolate);
		for (Counter counter : one_counters) {
			counter.draw(g, interpolate);
		}
		for (Counter counter : two_counters) {
			counter.draw(g, interpolate);
		}
		
		btn_roll.draw(g, interpolate);
		
		
		/* Text */
		
		g.setFont(new Font("TimesRoman", Font.BOLD, 32));
		FontMetrics fm = g.getFontMetrics();
		
		// Current player
		g.setColor(game.getPlayerColour()[5]);
		String turn = "Player: " + game.getPlayerName();
		g.drawString(turn, (Window.WIDTH - 20) - fm.stringWidth(turn), fm.getHeight());
		
		// Current / previous roll
		// On first turn show '-'
		String roll = (game.currentRoll < 0) ? "-" : Integer.toString(game.currentRoll);
		if (game.allowRoll) {
			// Previous player rolled last
			g.setColor(game.getPlayerColour(game.previousPlayer)[5]);
			roll = "Previous Roll: " + roll;
		} else {
			// Else use current player colour
			roll = "Current Roll: " + roll;
		}
		g.drawString(roll, (Window.WIDTH - 20) - fm.stringWidth(roll),
			Window.HEIGHT - fm.getHeight());
	}
	
	/**
	 * Moves a counter a certain amount of spaces through the given route. Adds all the moves along
	 * the way so that the counter will still move through the route rather than move directly to
	 * the last target
	 *
	 * @param counter The counter to move
	 * @param spaces The amount of spaces to move the counter along the route
	 */
	private Tile moveCounter(Counter counter, int spaces) {
		Tile[] route = board.getRoute(counter.player);
		if (counter.currentRouteIndex < 0) {
			// Get out of starting cluster
			CounterCluster cluster =
				(counter.player == Players.ONE) ? one_countersStart : two_countersStart;
			cluster.remove(counter);
		} else {
			route[counter.currentRouteIndex].setCounter(null);
		}
		
		// Go through each tile one by one
		for (int i = 0; i < Math.abs(spaces); i++) {
			switch (board.checkMove(counter, (spaces > 0) ? 1 : -1)) {
				case START:
					if (counter.player == Players.ONE) {
						one_countersStart.add(counter);
					} else {
						two_countersStart.add(counter);
					}
					counter.currentRouteIndex = -1;
					return null;
				case END:
					if (counter.player == Players.ONE) {
						one_countersEnd.add(counter);
					} else {
						two_countersEnd.add(counter);
					}
					counter.currentRouteIndex = board.getRouteLength();
					return null;
				default:
					Tile nextTile = board.getNextTile(counter, spaces > 0);
					counter.setTarget(counterInTilePosition(nextTile), spaces < 0);
					break;
			}
		}
		
		Tile finalTile = route[counter.currentRouteIndex];
		if (finalTile.hasCounter()) {
			// Must be opponent counter since moveCounter is not run if
			// is blocked by player's counter - move back to start
			moveCounter(finalTile.getCounter(), -board.getRouteLength());
		}
		finalTile.setCounter(counter);
		return finalTile;
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
	 * Checks if, for the given set of counters, there are any possible moves
	 *
	 * @param counters The counters to check ({@link #one_counters} or {@link #two_counters})
	 * @param spaces The amount of spaces the counters will move
	 * @return True if there are possible moves
	 */
	private boolean arePossibleMoves(Counter[] counters, int spaces) {
		for (Counter counter : counters) {
			if (board.checkMove(counter, spaces) != MoveState.BLOCKED) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Calculates if a counter has been click on and checks if it can move
	 *
	 * @param mousePos The position vector of the mouse pointer
	 * @param counter The counter to check and move
	 * @return True if successfully clicked on a counter
	 */
	private boolean processClick(Vector2D mousePos, Counter counter) {
		if (
			!counter.isColliding(mousePos)                                      // Not clicked on
				|| counter.currentRouteIndex >= board.getRouteLength()              // Out of play
				|| board.checkMove(counter, game.currentRoll) == MoveState.BLOCKED  // Can't move
			) {
			return false;
		}
		
		Tile finalCounter = moveCounter(counter, game.currentRoll);
		
		// Check if game is won
		if (
			one_countersEnd.getSize() == noCounters
				|| two_countersEnd.getSize() == noCounters
			) {
			game.won = true;
			Log.debug("PLAY", "GAME WON!");
			return true;
		}
		
		game.nextTurn(finalCounter == null || !finalCounter.isRosette());
		
		while (true) {
			// Check if there are possible moves
			Counter[] counters = null;
			if (game.currentPlayer == Players.ONE) {
				counters = one_counters;
			} else if (game.currentPlayer == Players.TWO) {
				counters = two_counters;
			}
			if (game.currentRoll == 0) {
				Log.debug("PLAY", "Rolled a 0 - swapping players");
			} else if (!arePossibleMoves(counters, game.currentRoll)) {
				Log.debug(
					"PLAY-CLICK",
					"No possible moves for player "
						+ game.getPlayerName()
						+ " - swapping players"
				);
			} else {
				break;
			}
		}
		
		// Return since we found the counter, there's not point
		// looking through the rest of them
		return true;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
			stateManager.setState(StateManager.GameState.PAUSE);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (game.won) {
			return;
		}
		
		Vector2D mousePos = new Vector2D(e.getX(), e.getY());
		
		if (
			game.allowRoll
				&& btn_roll.isColliding(mousePos)
			) {
			btn_roll.press();
			return;
		}
		
		if (!game.allowMove) {
			// If not allowing moving counters then escape
			return;
		}
		
		if (game.currentPlayer == Players.ONE) {
			for (Counter counter : one_counters) {
				if (processClick(mousePos, counter)) {
					return;
				}
			}
		} else if (game.currentPlayer == Players.TWO) {
			for (Counter counter : two_counters) {
				if (processClick(mousePos, counter)) {
					return;
				}
			}
		} else {
			Log.warn("PLAY", "Player NONE's turn");
		}
	}
}
