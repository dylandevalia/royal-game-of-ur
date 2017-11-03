package game.dylandevalia.royal_game_of_ur.client.states;

import java.awt.*;
import java.awt.event.KeyEvent;

public interface State {
	void initialise();
	void update();
	void draw(Graphics2D g2d, double interpolate);
	
	void keyReleased(KeyEvent event);
}
