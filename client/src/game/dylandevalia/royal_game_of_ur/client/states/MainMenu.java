package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.game.Game;
import game.dylandevalia.royal_game_of_ur.client.game.objects.BaseEntity;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MainMenu implements State {
	private Game game;
	private Box box;
	
	public void initialise(Game game) {
		this.game = game;
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
	
	}
	
	public void keyReleased(KeyEvent e) {
	
	}
	
	public void mousePressed(MouseEvent e) {
	
	}
	
	public void mouseReleased(MouseEvent e) {
		game.stateManager.setState(StateManager.GameState.PLAY);
	}
	
	private class Box extends BaseEntity {
		
		Box(int x, int y) {
			this.pos.set(x, y);
		}
		
		@Override
		public void update() {
			super.update();
			
			pos.add(5, 5);
		}
		
		@Override
		public void draw(Graphics2D g2d, double interpolate) {
			super.draw(g2d, interpolate);
			
			g2d.setColor(Color.MAGENTA);
			g2d.fillRect((int) drawPos.x, (int) drawPos.y, 20, 20);
		}
	}
}
