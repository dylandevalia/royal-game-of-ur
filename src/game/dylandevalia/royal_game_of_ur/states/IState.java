package game.dylandevalia.royal_game_of_ur.states;

import game.dylandevalia.royal_game_of_ur.utility.Bundle;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Interface used to create a state for the {@link StateManager}
 */
public interface IState {
	
	/**
	 * To be called when the state is first created to initialise its variables
	 *
	 * @param stateManager A reference to the state manager
	 * @param bundle       A bundle of data sent from the calling state
	 */
	void initialise(StateManager stateManager, Bundle bundle);
	
	/**
	 * To be called when the state is set as the active state
	 *
	 * @param bundle A bundle of data from the calling state
	 */
	void onSet(Bundle bundle);
	
	/**
	 * Method called to update the state
	 */
	void update();
	
	/**
	 * Method called to draw the state
	 *
	 * @param g           The graphics object to draw to the canvas
	 * @param interpolate The interpolation value or delta time
	 */
	void draw(Graphics2D g, double interpolate);
	
	/**
	 * Event handler method called when a keyboard key is pressed down
	 *
	 * @param e The key event
	 */
	void keyPressed(KeyEvent e);
	
	/**
	 * Event handler method called when a keyboard key is released
	 *
	 * @param e The key event
	 */
	void keyReleased(KeyEvent e);
	
	/**
	 * Event handler method called when a mouse button is pressed down
	 *
	 * @param e The mouse event
	 */
	void mousePressed(MouseEvent e);
	
	/**
	 * Event handler method called when a mouse button is released
	 *
	 * @param e The mouse event
	 */
	void mouseReleased(MouseEvent e);
}
