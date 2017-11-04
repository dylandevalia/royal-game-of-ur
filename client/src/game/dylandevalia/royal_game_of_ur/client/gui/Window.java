package game.dylandevalia.royal_game_of_ur.client.gui;

import javax.swing.*;

/**
 * The main window of the program
 */
public class Window extends JFrame {
	// Should the window run is fullscreen or windowed
	private final boolean fullscreen = false;
	
	public Window() {
		this.setTitle("The Royal Game of Ur");
		
		// Sets size of window
		if (fullscreen) {
			setUndecorated(true);
			setExtendedState(MAXIMIZED_BOTH);
		} else {
			setSize(1280, 720);
			setLocationRelativeTo(null);
			setResizable(false);
		}
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setContentPane(new Framework());
		setVisible(true);
	}
}
