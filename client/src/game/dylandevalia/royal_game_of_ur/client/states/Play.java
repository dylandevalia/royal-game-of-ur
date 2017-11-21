package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.game.Board;
import game.dylandevalia.royal_game_of_ur.client.game.CounterCluster;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Counter;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Tile;
import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Framework;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.UrDice;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import game.dylandevalia.royal_game_of_ur.utility.networking.PacketManager;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Play implements State {

	// Reference to the state manager
	private StateManager stateManager;
	/* Game board */
	private Board board = new Board(4, 8, 2);

	private CounterCluster one_countersStart, one_countersEnd, two_countersStart, two_countersEnd;
	/* Counters */
	// The number of counters each player should have
	private int noCounters = 6;
	// Player one's counters
	private Counter[] one_counters = new Counter[noCounters];
	// Player two's counters
	private Counter[] two_counters = new Counter[noCounters];
	/* Game logic */
	// Keeps track of who's turn it is
	private boolean playerOnesTurn;
	// The current roll of the dice
	private int currentRoll;
	/* Misc */
	private UrDice dice = new UrDice();

	@Override
	public void initialise(StateManager stateManager) {
		this.stateManager = stateManager;

		board.generate();
		generateCounters();
		Log.info("Play", "Generation completed");

		playerOnesTurn = true;
		do {
			currentRoll = dice.roll();
		} while (currentRoll == 0);
	}

	/**
	 * Generates the counters and their starting positions
	 */
	private void generateCounters() {
		one_countersStart = new CounterCluster(
			counterInTilePosition(board.getOne_route()[0]).add(0, Tile.WIDTH),
			true
		);
		one_countersEnd = new CounterCluster(
			counterInTilePosition(board.getOne_route()[board.getRouteLength() - 1])
				.add(0, Tile.WIDTH),
			false
		);
		two_countersStart = new CounterCluster(
			counterInTilePosition(board.getTwo_route()[0]).sub(0, Tile.WIDTH),
			true
		);
		two_countersEnd = new CounterCluster(
			counterInTilePosition(board.getTwo_route()[board.getRouteLength() - 1])
				.sub(0, Tile.WIDTH),
			false
		);

		for (int i = 0; i < noCounters; i++) {
			one_counters[i] = one_countersStart.addNew(true);
			two_counters[i] = two_countersStart.addNew(false);
		}
	}

	@Override
	public void update() {
		board.update();
		for (Counter counter : one_counters) {
			counter.update(Framework.getMousePos());
		}
		for (Counter counter : two_counters) {
			counter.update(Framework.getMousePos());
		}

		if (currentRoll == 0) {
			Log.debug("Dice", "Rolled a 0 - swapping players");
			currentRoll = dice.roll();
			playerOnesTurn = !playerOnesTurn;
		}
	}

	@Override
	public void draw(Graphics2D g, double interpolate) {
		g.setColor(ColorMaterial.GREY[2]);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);

		board.draw(g, interpolate);
		for (Counter counter : one_counters) {
			counter.draw(g, interpolate);
		}
		for (Counter counter : two_counters) {
			counter.draw(g, interpolate);
		}

		g.setFont(new Font("TimesRoman", Font.BOLD, 18));
		String turn = playerOnesTurn ? "1" : "2";
		g.drawString("Player: " + turn, Window.WIDTH - 150, 50);
		g.drawString("  Roll: " + currentRoll, Window.WIDTH - 150, Window.HEIGHT - 50);

//		g.setColor(ColorMaterial.GREY[9]);
//		g.drawRect(0, 0, Window.WIDTH / 2, Window.HEIGHT / 2);
//		g.drawRect(Window.WIDTH / 2, Window.HEIGHT / 2, Window.WIDTH, Window.HEIGHT);
	}

	/**
	 * Moves a counter a certain amount of spaces through the given route. Adds
	 * all the moves along the way so that the counter will still move through
	 * the route rather than move directly to the last target
	 *
	 * @param route The route the counter will travel through
	 * @param counter The counter to move
	 * @param spaces The amount of spaces to move the counter along the route
	 */
	private Tile moveCounter(Tile[] route, Counter counter, int spaces) {
		if (counter.currentRouteIndex >= 0 && counter.currentRouteIndex < route.length) {
			route[counter.currentRouteIndex].setHasCounter(false);
		}

		for (int i = 0; i < Math.abs(spaces); i++) {
			counter.setTarget(counterInTilePosition(getNextTile(route, counter, spaces > 0)));
		}

		route[counter.currentRouteIndex].setHasCounter(true);
		return route[counter.currentRouteIndex];
	}

	/**
	 * Gets the next tile in the route
	 *
	 * @param route The route the tile travels
	 * @param counter The counter to move
	 * @return The tile to move to
	 */
	private Tile getNextTile(Tile[] route, Counter counter, boolean forward) {
		counter.currentRouteIndex += forward ? 1 : -1;
		if (counter.currentRouteIndex < 0) {
			return route[0];
		} else if (counter.currentRouteIndex >= route.length) {
			return route[route.length - 1];
		}
		return route[counter.currentRouteIndex];
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
	 * Checks move a certain amount ahead
	 *
	 * @param route The route the counter will follow
	 * @param counter The counter to calculate new position
	 * @param spaces The amount of spaces ahead to check
	 * @return Boolean if the move is viable/allowed
	 */
	private boolean checkMove(Tile[] route, Counter counter, int spaces) {
		// TODO: Replace boolean return with enum with more detail for future AI
		if (counter.currentRouteIndex + spaces < 0
			|| counter.currentRouteIndex + spaces > route.length) {
			// Outside of route
			return false;
		}
		return !route[counter.currentRouteIndex + spaces].hasCounter();
	}

	public void packetReceived(PacketManager packet) {

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
		if (playerOnesTurn) {
			for (Counter counter : one_counters) {
				if (counter.isColliding(new Vector2D(e.getX(), e.getY()))
					&& checkMove(board.getOne_route(), counter, currentRoll)
					) {
					if (counter.currentRouteIndex < 0) {
						one_countersStart.remove(counter);
					}
					Tile newTile = moveCounter(board.getOne_route(), counter, currentRoll);
					if (!newTile.isRosette()) {
						playerOnesTurn = false;
					}
					currentRoll = dice.roll();
					return;
				}
			}
		} else {
			for (Counter counter : two_counters) {
				if (counter.isColliding(new Vector2D(e.getX(), e.getY()))
					&& checkMove(board.getTwo_route(), counter, currentRoll)
					) {
					if (counter.currentRouteIndex < 0) {
						two_countersStart.remove(counter);
					}
					Tile newTile = moveCounter(board.getTwo_route(), counter, currentRoll);
					if (!newTile.isRosette()) {
						playerOnesTurn = true;
					}
					currentRoll = dice.roll();
					return;
				}
			}
		}
	}
}
