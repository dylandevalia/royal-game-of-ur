package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.utility.networking.PacketManager;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Interface used to create a state for the state manager
 */
public interface State {
	
	void initialise(StateManager stateManager);
	
	void update();
	
	void draw(Graphics2D g, double interpolate);
	
	void packetReceived(PacketManager packet);
	
	void keyPressed(KeyEvent e);
	
	void keyReleased(KeyEvent e);
	
	void mousePressed(MouseEvent e);
	
	void mouseReleased(MouseEvent e);
}
