package game.dylandevalia.royal_game_of_ur.client.game;

import game.dylandevalia.royal_game_of_ur.client.game.entities.Tile;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import java.awt.Graphics2D;

public class Board {
	
	/**
	 * The lengths of each segment of the board
	 */
	private int startingTilesLen, middleTilesLen, endTilesLen;
	/**
	 * The array of all tiles in the board
	 */
	private Tile[] tiles;
	/**
	 * The array of tiles which player one will follow in order
	 */
	private Tile[] one_route;
	/**
	 * The array of tiles which player two will follow in order
	 */
	private Tile[] two_route;
	/**
	 * The indexes in {@link #tiles} which should be rosette squares
	 */
	private int[] rosetteSquares = {3, 7, 11, 17, 19};
	
	/**
	 * Creates the board using {@link Tile}s in the correct positions
	 *
	 * @param startingTilesLen The length of the starting segment
	 * @param middleTilesLen The length of the middle segment
	 * @param endTilesLen The length of the end segment
	 */
	public Board(int startingTilesLen, int middleTilesLen, int endTilesLen) {
		this.startingTilesLen = startingTilesLen;
		this.middleTilesLen = middleTilesLen;
		this.endTilesLen = endTilesLen;
		
		tiles = new Tile[(2 * startingTilesLen) + middleTilesLen + (2 * endTilesLen)];
		one_route = new Tile[startingTilesLen + middleTilesLen + endTilesLen];
		two_route = new Tile[startingTilesLen + middleTilesLen + endTilesLen];
	}
	
	/**
	 * Generates the board of tiles using {@link #startingTilesLen}, {@link #middleTilesLen}
	 * and {@link #endTilesLen} as well as {@link #rosetteSquares}
	 */
	public void generate() {
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
	
	public Tile[] getOne_route() {
		return one_route;
	}
	
	public int getRouteLength() {
		// Using one_route but both one_route and
		// two_route should be the same length
		return one_route.length;
	}
	
	public Tile[] getTwo_route() {
		return two_route;
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
