package game.dylandevalia.royal_game_of_ur.client.gui;

import javax.swing.*;

public class Window extends JFrame {
	public boolean fullscreen = false;
	
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
