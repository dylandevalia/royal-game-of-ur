package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Pause implements State {
	
	private StateManager stateManager;
	
	@Override
	public void initialise(StateManager stateManager) {
		this.stateManager = stateManager;
	}
	
	@Override
	public void update() {
	
	}
	
	@Override
	public void draw(Graphics2D g2d, double interpolate) {
		g2d.setColor(Color.RED);
		g2d.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			stateManager.unloadState(StateManager.GameState.PLAY);
			stateManager.setState(StateManager.GameState.MAIN_MENU);
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
	
	}
}
