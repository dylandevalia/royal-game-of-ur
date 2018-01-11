package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.game.entities.BaseEntity;
import game.dylandevalia.royal_game_of_ur.client.gui.Canvas;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MainMenu implements State {
	
	private StateManager stateManager;
	private Box box;
	
	public void initialise(StateManager stateManager) {
		this.stateManager = stateManager;
		this.box = new Box(20, 20);
	}
	
	public void update() {
		box.update();
	}
	
	public void draw(Graphics2D g2d, double interpolate) {
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		box.draw(g2d, interpolate);
	}
	
	public void keyPressed(KeyEvent e) {
//		Log.debug("main menu/key press", "char: '" + e.getKeyChar() + "'");
	}
	
	public void keyReleased(KeyEvent e) {
	
	}
	
	public void mousePressed(MouseEvent e) {
	
	}
	
	public void mouseReleased(MouseEvent e) {
		if (!stateManager.isLoaded(StateManager.GameState.PLAY)) {
			stateManager.loadState(StateManager.GameState.PLAY);
		}
		stateManager.setState(StateManager.GameState.PLAY);
	}
	
	private class Box extends BaseEntity {
		
		Box(int x, int y) {
			super(x, y, 20, 20);
		}
		
		@Override
		public void update() {
			super.update();
			
			int speed = Canvas.getKeyState(KeyEvent.VK_SHIFT) ? 10 : 5;
			
			Vector2D vel = new Vector2D();
			if (Canvas.getKeyState(KeyEvent.VK_A)) {
				vel.add(Vector2D.LEFT());
			}
			if (Canvas.getKeyState(KeyEvent.VK_D)) {
				vel.add(Vector2D.RIGHT());
			}
			if (Canvas.getKeyState(KeyEvent.VK_W)) {
				vel.add(Vector2D.UP());
			}
			if (Canvas.getKeyState(KeyEvent.VK_S)) {
				vel.add(Vector2D.DOWN());
			}
			pos.add(vel.setMag(speed));
		}
		
		@Override
		public void draw(Graphics2D g, double interpolate) {
			super.draw(g, interpolate);
			if (!onScreen) {
				return;
			}
			g.setColor(Color.MAGENTA);
			g.fillRect((int) drawPos.x, (int) drawPos.y, width, height);
		}
	}
}
