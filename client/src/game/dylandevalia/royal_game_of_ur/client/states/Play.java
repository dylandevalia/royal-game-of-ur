package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.game.objects.BaseEntity;
import game.dylandevalia.royal_game_of_ur.client.game.objects.Counter;
import game.dylandevalia.royal_game_of_ur.client.game.objects.Tile;
import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Framework;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.UrDice;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import game.dylandevalia.royal_game_of_ur.utility.networking.PacketManager;

import java.awt.*;
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
	private Tile[] playerOneRoute = new Tile[startingTilesLen + middleTilesLen + endTilesLen];
	// The array of tiles that player two will traverse
	private Tile[] playerTwoRoute = new Tile[startingTilesLen + middleTilesLen + endTilesLen];
	// Indexes of 'tiles' which should be rosette squares
	private int[] rosetteSquares = {3, 7, 11, 17, 19};
	/* Counters */
	// The number of counters each player should have
	private int noCounters = 6;
	// Player one's counters
	private Counter[] playerOneCounters = new Counter[noCounters];
	// Player two's counters
	private Counter[] playerTwoCounters = new Counter[noCounters];
	// Array of the starting positions of player one's counters
	private Vector2D[] playerOneCounterStartPositions = new Vector2D[noCounters];
	// Array of the starting positions of player two's counters
	private Vector2D[] playerTwoCounterStartPositions = new Vector2D[noCounters];
	/* Misc */
	private MouseCircle mouseCircle;
	private UrDice dice = new UrDice();
	
	@Override
	public void initialise(StateManager stateManager) {
		this.stateManager = stateManager;
		
		generateBoard();
		generateCounters();
		Log.info("Play", "Generation completed");
		
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
			playerOneRoute[i] = tiles[i];
		}
		aggregate += startingTilesLen;
		// Player two start
		for (int i = 0; i < startingTilesLen; i++) {
			tiles[i + aggregate] = new Tile(Tile.WIDTH * (startingTilesLen - i), rowTop);
			playerTwoRoute[i] = tiles[i + aggregate];
		}
		aggregate += startingTilesLen;
		// Middle
		for (int i = 0; i < middleTilesLen; i++) {
			tiles[i + aggregate] = new Tile(Tile.WIDTH * (i + 1), rowMid);
			playerOneRoute[i + startingTilesLen] = tiles[i + aggregate];
			playerTwoRoute[i + startingTilesLen] = tiles[i + aggregate];
		}
		aggregate += middleTilesLen;
		// Player one end
		for (int i = 0; i < endTilesLen; i++) {
			tiles[i + aggregate] = new Tile(Tile.WIDTH * (middleTilesLen - i), rowBot);
			playerOneRoute[i + (startingTilesLen + middleTilesLen)] = tiles[i + aggregate];
		}
		aggregate += endTilesLen;
		// Player two end
		for (int i = 0; i < endTilesLen; i++) {
			tiles[i + aggregate] = new Tile(Tile.WIDTH * (middleTilesLen - i), rowTop);
			playerTwoRoute[i + (startingTilesLen + middleTilesLen)] = tiles[i + aggregate];
		}
		
		for (int r : rosetteSquares) {
			tiles[r].setRosette(true);
		}
	}
	
	/**
	 * Generates the counters and their starting positions
	 */
	private void generateCounters() {
		Tile tmpTile = new Tile(
				Tile.WIDTH * startingTilesLen,
				(int) ((Window.HEIGHT / 2) * 2.5)
		);
		int x = (int) counterInTilePosition(tmpTile).x;
		int yTop = (Window.HEIGHT / 2) - (Tile.WIDTH * 2) - Counter.WIDTH;
		int yBot = (Window.HEIGHT / 2) + (Tile.WIDTH * 2);
		for (int i = 0; i < noCounters; i++) {
			playerOneCounterStartPositions[i] = new Vector2D(x - (Counter.WIDTH * i), yBot);
			playerTwoCounterStartPositions[i] = new Vector2D(x - (Counter.WIDTH * i), yTop);
			playerOneCounters[i] = new Counter((int) playerOneCounterStartPositions[i].x, (int) playerOneCounterStartPositions[i].y, true);
			playerTwoCounters[i] = new Counter((int) playerTwoCounterStartPositions[i].x, (int) playerTwoCounterStartPositions[i].y, false);
		}
	}
	
	@Override
	public void update() {
		for (Tile tile : tiles) tile.update();
		for (Counter counter : playerOneCounters) counter.update(Framework.getMousePos());
		for (Counter counter : playerTwoCounters) counter.update(Framework.getMousePos());
		mouseCircle.update();
//		Log.debug("Dice", "" + dice.roll());
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		g.setColor(ColorMaterial.GREY[2]);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		for (Tile tile : tiles) tile.draw(g, interpolate);
		for (Counter counter : playerOneCounters) counter.draw(g, interpolate);
		for (Counter counter : playerTwoCounters) counter.draw(g, interpolate);
		mouseCircle.draw(g, interpolate);

//		g.setColor(ColorMaterial.GREY[9]);
//		g.drawRect(0, 0, Window.WIDTH / 2, Window.HEIGHT / 2);
//		g.drawRect(Window.WIDTH / 2, Window.HEIGHT / 2, Window.WIDTH, Window.HEIGHT);
	}
	
	public void packetReceived(PacketManager packet) {
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	
	}
	
	/**
	 * Moves a counter a certain amount of spaces through the given route. Adds
	 * all the moves along the way so that the counter will still move through
	 * the route rather than move directly to the last target
	 *
	 * @param route   The route the counter will travel through
	 * @param counter The counter to move
	 * @param spaces  The amount of spaces to move the counter along the route
	 */
	private void moveCounter(Tile[] route, Counter counter, int spaces) {
		for (int i = 0; i < Math.abs(spaces); i++) {
			counter.setTarget(counterInTilePosition(getNextTile(route, counter, spaces > 0)));
		}
	}
	
	/**
	 * Gets the next tile in the route
	 *
	 * @param route   The route the tile travels
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
		for (Counter counter : playerOneCounters) {
			if (counter.isColliding(new Vector2D(e.getX(), e.getY()))) {
				moveCounter(playerOneRoute, counter, 1);
				return;
			}
		}
		for (Counter counter : playerTwoCounters) {
			if (counter.isColliding(new Vector2D(e.getX(), e.getY()))) {
				moveCounter(playerTwoRoute, counter, 1);
				return;
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
