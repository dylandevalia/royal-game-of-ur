package game.dylandevalia.royal_game_of_ur.states;

import game.dylandevalia.royal_game_of_ur.utility.Bundle;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class AbstractState implements IState {
	
	/** Reference to the state manager */
	protected StateManager stateManager;
	
	public void initialise(StateManager stateManager, Bundle bundle) {
		this.stateManager = stateManager;
		initialise(bundle);
	}
	
	public abstract void initialise(Bundle bundle);
	
	public void onSet(Bundle bundle) {
	
	}
	
	public abstract void update();
	
	public abstract void draw(Graphics2D g, double interpolate);
	
	public void keyPressed(KeyEvent e) {
	
	}
	
	public void keyReleased(KeyEvent e) {
	
	}
	
	public void mousePressed(MouseEvent e) {
	
	}
	
	public void mouseReleased(MouseEvent e) {
	
	}
}
