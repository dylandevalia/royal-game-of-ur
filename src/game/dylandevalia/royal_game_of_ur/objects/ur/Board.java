package game.dylandevalia.royal_game_of_ur.objects.ur;

import game.dylandevalia.royal_game_of_ur.gui.Window;
import java.awt.Graphics2D;

public class Board {
	
	/** The lengths of each segment of the board */
	private int startingTilesLen, middleTilesLen, endTilesLen;
	
	/** The array of all tiles in the board */
	private Tile[] tiles;
	
	/** The indexes in {@link #tiles} which should be rosette squares */
	private int[] rosetteSquares = {3, 7, 11, 17, 19};
	
	/**
	 * Creates the board using {@link Tile}s in the correct positions
	 *
	 * @param startingTilesLen The length of the starting segment
	 * @param middleTilesLen   The length of the middle segment
	 * @param endTilesLen      The length of the end segment
	 */
	public Board(int startingTilesLen, int middleTilesLen, int endTilesLen) {
		this.startingTilesLen = startingTilesLen;
		this.middleTilesLen = middleTilesLen;
		this.endTilesLen = endTilesLen;
		
		tiles = new Tile[(2 * startingTilesLen) + middleTilesLen + (2 * endTilesLen)];
	}
	
	/**
	 * Generates the board of tiles using {@link #startingTilesLen}, {@link #middleTilesLen} and
	 * {@link #endTilesLen} as well as {@link #rosetteSquares}
	 */
	public void generate(Tile[] one_route, Tile[] two_route) {
		// Set tile width programmatically based on size available
		Tile.WIDTH = Window.WIDTH / (middleTilesLen + 2);
		
		// Calculate each row's y position
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
	 * Gets the next tile in the route
	 *
	 * @param counter The counter to move
	 * @return The tile to move to
	 */
	public Tile getNextTile(Tile[] route, Counter counter, boolean forward) {
		counter.setCurrentRouteIndex(counter.getCurrentRouteIndex() + (forward ? 1 : -1));
		if (counter.getCurrentRouteIndex() < 0) {
			return route[0];
		} else if (counter.getCurrentRouteIndex() >= route.length) {
			return route[route.length - 1];
		}
		return route[counter.getCurrentRouteIndex()];
	}
	
	public int getStartLen() {
		return startingTilesLen;
	}
	
	public int getMidLen() {
		return startingTilesLen + middleTilesLen;
	}
	
	public int getRouteLength() {
		return startingTilesLen + middleTilesLen + endTilesLen;
	}
	
	public Tile getTile(int index) {
		if (index < 0 || index >= tiles.length) {
			return null;
		}
		return tiles[index];
	}
	
	public int getNoTiles() {
		return tiles.length;
	}
	
	public void update() {
		for (Tile tile : tiles) {
			tile.update();
		}
	}
	
	public void draw(Graphics2D g, double interpolate) {
		for (Tile tile : tiles) {
			tile.draw(g, interpolate);
		}
	}
}
