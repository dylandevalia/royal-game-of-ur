package game.dylandevalia.royal_game_of_ur.states;

import game.dylandevalia.royal_game_of_ur.utility.Bundle;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * An abstract class which implements {@link IState} and offers some useful features such as
 * optional implementation of certain event methods and a reference to the state manager
 */
public abstract class AbstractState implements IState {
	
	/** Reference to the state manager */
	protected StateManager stateManager;
	
	/**
	 * Initialisation method called by the state manager. Stores a reference to the state manager
	 * then calls the abstract {@link #initialise(Bundle)} with just the bundle
	 *
	 * @param stateManager A reference to the state manager
	 * @param bundle       A bundle of data sent from the calling state
	 */
	public void initialise(StateManager stateManager, Bundle bundle) {
		this.stateManager = stateManager;
		initialise(bundle);
	}
	
	/**
	 * Initialisation method which the child state should override. Called from {@link
	 * #initialise(StateManager, Bundle)}
	 *
	 * @param bundle       A bundle of data sent from the calling state
	 */
	public abstract void initialise(Bundle bundle);
	
	/**
	 * Optional method called when the state is set as active
	 *
	 * @param bundle A bundle of data from the calling state
	 */
	public void onSet(Bundle bundle) {
	
	}
	
	/**
	 * Method called to update the state
	 */
	public abstract void update();
	
	/**
	 * Method called to draw the state
	 *
	 * @param g           The graphics object to draw to the canvas
	 * @param interpolate The interpolation value or delta time
	 */
	public abstract void draw(Graphics2D g, double interpolate);
	
	/**
	 * Optional event handler method called when a keyboard key is pressed down
	 *
	 * @param e The key event
	 */
	public void keyPressed(KeyEvent e) {
	
	}
	
	/**
	 * Optional event handler method called when a keyboard key is released
	 *
	 * @param e The key event
	 */
	public void keyReleased(KeyEvent e) {
	
	}
	
	/**
	 * Optional event handler method called when a mouse button is pressed down
	 *
	 * @param e The mouse event
	 */
	public void mousePressed(MouseEvent e) {
	
	}
	
	/**
	 * Optional event handler method called when a mouse button is released
	 *
	 * @param e The mouse event
	 */
	public void mouseReleased(MouseEvent e) {
	
	}
}
