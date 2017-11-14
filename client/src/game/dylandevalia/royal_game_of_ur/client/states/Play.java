package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.game.Game;
import game.dylandevalia.royal_game_of_ur.client.game.objects.BaseEntity;
import game.dylandevalia.royal_game_of_ur.client.game.objects.Counter;
import game.dylandevalia.royal_game_of_ur.client.game.objects.Tile;
import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import game.dylandevalia.royal_game_of_ur.utility.networking.PacketManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Play implements State {
	private Game game;
	private int startingTilesLen = 4, middleTilesLen = 8, endTileLen = 2;
	private Tile[] tiles = new Tile[(2 * startingTilesLen) + middleTilesLen + (2 * endTileLen)];
	private Tile[] playerOneRoute = new Tile[startingTilesLen + middleTilesLen + endTileLen];
	private Tile[] playerTwoRoute = new Tile[startingTilesLen + middleTilesLen + endTileLen];
	private Counter counterOne, counterTwo;
	private MouseCircle mouseCircle;
	
	@Override
	public void initialise(Game game) {
		this.game = game;

//		Tile temp = new Tile(0, 0);
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
		for (int i = 0; i < endTileLen; i++) {
			tiles[i + aggregate] = new Tile(Tile.WIDTH * (middleTilesLen - i), rowBot);
			playerOneRoute[i + (startingTilesLen + middleTilesLen)] = tiles[i + aggregate];
		}
		aggregate += endTileLen;
		// Player two end
		for (int i = 0; i < endTileLen; i++) {
			tiles[i + aggregate] = new Tile(Tile.WIDTH * (middleTilesLen - i), rowTop);
			playerTwoRoute[i + (startingTilesLen + middleTilesLen)] = tiles[i + aggregate];
		}
		
		int[] rosetteSquares = {3, 7, 11, 17, 19};
		for (int r : rosetteSquares) {
			tiles[r].setRosette(true);
		}
		
		counterOne = new Counter((int) playerOneRoute[0].getPos().x, Window.HEIGHT - 100 - Counter.WIDTH, true);
		counterTwo = new Counter((int) playerTwoRoute[0].getPos().x, 100, false);
		
		mouseCircle = new MouseCircle();
	}
	
	@Override
	public void update() {
		for (Tile tile : tiles) tile.update();
		counterOne.update();
		counterTwo.update();
		mouseCircle.update();
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		g.setColor(ColorMaterial.GREY[2]);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		for (Tile tile : tiles) tile.draw(g, interpolate);
		counterOne.draw(g, interpolate);
		counterTwo.draw(g, interpolate);
		mouseCircle.draw(g, interpolate);
	}
	
	public void packetReceived(PacketManager packet) {
	
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == ' ') {
			moveCounter(playerOneRoute, counterOne, 2);
			moveCounter(playerTwoRoute, counterTwo, 1);
		}
	}
	
	private void moveCounter(Tile[] route, Counter counter, int spaces) {
//		if (spaces > 0) {
//			counter.setTarget(counterInTilePosition(getNextTile(route, counter)));
//			moveCounter(route, counter, --spaces);
//		}
		for (int i = 0; i < spaces; i++) {
			counter.setTarget(counterInTilePosition(getNextTile(route, counter)));
		}
	}
	
	/**
	 * Gets the next tile in the route
	 *
	 * @param route     The route the tile travels
	 * @param counter   The counter to move
	 * @return  The tile to move to
	 */
	private Tile getNextTile(Tile[] route, Counter counter) {
		counter.incrementCurrentRouteIndex();
		return route[(counter.getCurrentRouteIndex() % route.length)];
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
	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
//		game.stateManager.setState(StateManager.GameState.PAUSE);
	}
	
	private class MouseCircle extends BaseEntity {
		MouseCircle() {
			super(0, 0, 10, 10);
		}
		
		@Override
		protected void update() {
			super.update();
			this.pos.set(game.framework.mousePos.copy().sub(width / 2, height / 2));
		}
		
		@Override
		protected void draw(Graphics2D g, double interpolate) {
			super.draw(g, interpolate);
			g.setColor(ColorMaterial.deepPurple);
			g.drawOval((int) drawPos.x, (int) drawPos.y, width, height);
		}
	}
}
