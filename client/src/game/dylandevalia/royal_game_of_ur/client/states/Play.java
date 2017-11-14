package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.game.Game;
import game.dylandevalia.royal_game_of_ur.client.game.objects.Counter;
import game.dylandevalia.royal_game_of_ur.client.game.objects.Tile;
import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.networking.PacketManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Play implements State {
	private Game game;
	private Tile[] tiles = new Tile[20];
	private Tile[] playerOneRoute = new Tile[14];
	private Tile[] playerTwoRoute = new Tile[14];
	private Counter counterOne, counterTwo;
	
	@Override
	public void initialise(Game game) {
		this.game = game;
		
//		Tile temp = new Tile(0, 0);
		int rowTop = (int) Math.floor((Window.HEIGHT / 2) - (Tile.WIDTH * 1.5));
		int rowMid = (int) Math.floor((Window.HEIGHT / 2) - (Tile.WIDTH * 0.5));
		int rowBot = (int) Math.floor((Window.HEIGHT / 2) + (Tile.WIDTH * 0.5));
		
		// Create tiles for board
		// Player one start
		for (int i = 0; i < 4; i++) {
			tiles[i] = new Tile(Tile.WIDTH * (4 - i), rowBot);
			playerOneRoute[i] = tiles[i];
		}
		// Player two start
		for (int i = 0; i < 4; i++) {
			tiles[i + 4] = new Tile(Tile.WIDTH * (4 - i), rowTop);
			playerTwoRoute[i] = tiles[i + 4];
		}
		// Middle
		for (int i = 0; i < 8; i++) {
			tiles[i + 8] = new Tile(Tile.WIDTH * (i + 1), rowMid);
			playerOneRoute[i + 4] = tiles[i + 8];
			playerTwoRoute[i + 4] = tiles[i + 8];
		}
		// Player one end
		for (int i = 0; i < 2; i++) {
			tiles[i + 16] = new Tile(Tile.WIDTH * (8 - i), rowBot);
			playerOneRoute[i + 12] = tiles[i + 16];
		}
		// Player two end
		for (int i = 0; i < 2; i++) {
			tiles[i + 18] = new Tile(Tile.WIDTH * (8 - i), rowTop);
			playerTwoRoute[i + 12] = tiles[i + 18];
		}
		
		int[] rosetteSquares = {3, 7, 11, 17, 19};
		for (int r : rosetteSquares) {
			tiles[r].setRosette(true);
		}
		
		counterOne = new Counter((int) playerOneRoute[0].getPos().x, Window.HEIGHT - 100, true);
		counterTwo = new Counter((int) playerTwoRoute[0].getPos().x, 100, false);
	}
	
	@Override
	public void update() {
		for (Tile tile : tiles) tile.update();
		counterOne.update();
		counterTwo.update();
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		g.setColor(ColorMaterial.GREY[2]);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		for (Tile tile : tiles) tile.draw(g, interpolate);
		counterOne.draw(g, interpolate);
		counterTwo.draw(g, interpolate);
	}
	
	public void packetReceived(PacketManager packet) {
	
	}
	
	private int curIndexOne = -1;
	private int curIndexTwo = -1;
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == ' ') {
			counterOne.setTarget(
					playerOneRoute[(++curIndexOne % playerOneRoute.length)]
					.getMidPos()
			);
			counterTwo.setTarget(
					playerTwoRoute[(++curIndexTwo % playerTwoRoute.length)]
					.getMidPos()
			);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		game.stateManager.setState(StateManager.GameState.PAUSE);
	}
}
