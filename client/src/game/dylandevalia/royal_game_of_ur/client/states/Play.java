package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.game.CounterCluster;
import game.dylandevalia.royal_game_of_ur.client.game.entities.BaseEntity;
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
	// The length of each segment on the board
	private int startingTilesLen = 4, middleTilesLen = 8, endTilesLen = 2;
	// Full array of all tiles in the board
	private Tile[] tiles = new Tile[(2 * startingTilesLen) + middleTilesLen + (2 * endTilesLen)];
	// The array of tiles that player one will traverse
	private Tile[] one_route = new Tile[startingTilesLen + middleTilesLen + endTilesLen];
	// The array of tiles that player two will traverse
	private Tile[] two_route = new Tile[startingTilesLen + middleTilesLen + endTilesLen];
	// Indexes of 'tiles' which should be rosette squares
	private int[] rosetteSquares = {3, 7, 11, 17, 19};
	private CounterCluster one_countersStart, one_countersEnd, two_countersStart, two_countersEnd;
	/* Counters */
	// The number of counters each player should have
	private int noCounters = 6;
	// Player one's counters
	private Counter[] one_counters = new Counter[noCounters];
	// Player two's counters
	private Counter[] two_counters = new Counter[noCounters];
	// Array of the starting positions of player one's counters
	private Vector2D[] one_counterStartPositions = new Vector2D[noCounters];
	// Array of the starting positions of player two's counters
	private Vector2D[] two_counterStartPositions = new Vector2D[noCounters];
	/* Game logic */
	// Keeps track of who's turn it is
	private boolean playerOnesTurn;
	// The current roll of the dice
	private int currentRoll;
	/* Misc */
	private MouseCircle mouseCircle;
	private UrDice dice = new UrDice();

	@Override
	public void initialise(StateManager stateManager) {
		this.stateManager = stateManager;

		generateBoard();
		generateCounters();
		Log.info("Play", "Generation completed");

		playerOnesTurn = true;
		do {
			currentRoll = dice.roll();
		} while (currentRoll == 0);
		mouseCircle = new MouseCircle();
	}

	/**
	 * Generates the board of tiles using startingTilesLen, middleTilesLen
	 * and endTilesLen as well as rosetteSquares
	 */
	private void generateBoard() {
		Tile.WIDTH = Window.WIDTH / (middleTilesLen + 2);
		int rowTop = (int) Math.floor((Window.HEIGHT / 2) - (Tile.WIDTH * 1.5));
		int rowMid = (int) Math.floor((Window.HEIGHT / 2) - (Tile.WIDTH * 0.5));
		int rowBot = (int) Math.floor((Window.HEIGHT / 2) + (Tile.WIDTH * 0.5));

		// Create tiles for board
		int aggregate = 0;
		// Player one start
		for (int i = 0; i < startingTilesLen; i++) {
			tiles[i] = new Tile(Tile.WIDTH * (startingTilesLen - i), rowBot);
			one_route[i] = tiles[i];
		}
		aggregate += startingTilesLen;
		// Player two start
		for (int i = 0; i < startingTilesLen; i++) {
			tiles[i + aggregate] = new Tile(Tile.WIDTH * (startingTilesLen - i), rowTop);
			two_route[i] = tiles[i + aggregate];
		}
		aggregate += startingTilesLen;
		// Middle
		for (int i = 0; i < middleTilesLen; i++) {
			tiles[i + aggregate] = new Tile(Tile.WIDTH * (i + 1), rowMid);
			one_route[i + startingTilesLen] = tiles[i + aggregate];
			two_route[i + startingTilesLen] = tiles[i + aggregate];
		}
		aggregate += middleTilesLen;
		// Player one end
		for (int i = 0; i < endTilesLen; i++) {
			tiles[i + aggregate] = new Tile(Tile.WIDTH * (middleTilesLen - i), rowBot);
			one_route[i + (startingTilesLen + middleTilesLen)] = tiles[i + aggregate];
		}
		aggregate += endTilesLen;
		// Player two end
		for (int i = 0; i < endTilesLen; i++) {
			tiles[i + aggregate] = new Tile(Tile.WIDTH * (middleTilesLen - i), rowTop);
			two_route[i + (startingTilesLen + middleTilesLen)] = tiles[i + aggregate];
		}

		for (int r : rosetteSquares) {
			tiles[r].setRosette(true);
		}
	}

	/**
	 * Generates the counters and their starting positions
	 */
	private void generateCounters() {
		one_countersStart = new CounterCluster(
			counterInTilePosition(one_route[0]).add(0, Tile.WIDTH),
			true
		);
		one_countersEnd = new CounterCluster(
			counterInTilePosition(one_route[one_route.length - 1]).add(0, Tile.WIDTH),
			false
		);
		two_countersStart = new CounterCluster(
			counterInTilePosition(two_route[0]).sub(0, Tile.WIDTH),
			true
		);
		two_countersEnd = new CounterCluster(
			counterInTilePosition(two_route[two_route.length - 1]).sub(0, Tile.WIDTH),
			false
		);

		for (int i = 0; i < noCounters; i++) {
			one_counters[i] = one_countersStart.addNew(true);
			two_counters[i] = two_countersStart.addNew(false);
		}
	}

	@Override
	public void update() {
		for (Tile tile : tiles) {
			tile.update();
		}
		for (Counter counter : one_counters) {
			counter.update(Framework.getMousePos());
		}
		for (Counter counter : two_counters) {
			counter.update(Framework.getMousePos());
		}
		mouseCircle.update();

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

		for (Tile tile : tiles) {
			tile.draw(g, interpolate);
		}
		for (Counter counter : one_counters) {
			counter.draw(g, interpolate);
		}
		for (Counter counter : two_counters) {
			counter.draw(g, interpolate);
		}
		mouseCircle.draw(g, interpolate);

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
//			Log.debug("next tile", "index: " + counter.currentRouteIndex + " set: " + route[counter.currentRouteIndex].hasCounter());
		}

		for (int i = 0; i < Math.abs(spaces); i++) {
			counter.setTarget(counterInTilePosition(getNextTile(route, counter, spaces > 0)));
		}

		route[counter.currentRouteIndex].setHasCounter(true);
//		Log.debug("next tile", "index: " + counter.currentRouteIndex + " set: " + route[counter.currentRouteIndex].hasCounter());
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
					&& checkMove(one_route, counter, currentRoll)
					) {
					if (counter.currentRouteIndex < 0) {
						one_countersStart.remove(counter);
					}
					Tile newTile = moveCounter(one_route, counter, currentRoll);
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
					&& checkMove(two_route, counter, currentRoll)
					) {
					if (counter.currentRouteIndex < 0) {
						two_countersStart.remove(counter);
					}
					Tile newTile = moveCounter(two_route, counter, currentRoll);
					if (!newTile.isRosette()) {
						playerOnesTurn = true;
					}
					currentRoll = dice.roll();
					return;
				}
			}
		}
	}

	// Add a small circle around the mouse
	// temporary testing to see if mouse position was available
	private class MouseCircle extends BaseEntity {

		MouseCircle() {
			super(0, 0, 10, 10);
		}

		@Override
		protected void update() {
			super.update();
			this.pos.set(Framework.getMousePos().sub(width / 2, height / 2));
		}

		@Override
		protected void draw(Graphics2D g, double interpolate) {
			super.draw(g, interpolate);
			g.setColor(ColorMaterial.deepPurple);
			g.drawOval((int) drawPos.x, (int) drawPos.y, width, height);
		}
	}
}
