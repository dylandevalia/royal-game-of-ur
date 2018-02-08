package game.dylandevalia.royal_game_of_ur.gui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The main window of the program
 */
public class Window extends JFrame {
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	// Should the window run is fullscreen or windowed
	private final boolean fullscreen = false;
	
	public Window() {
		this.setTitle("The Royal Game of Ur");
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setContentPane(new Framework());
		
		// Sets size of window
		if (fullscreen) {
			setUndecorated(true);
			setExtendedState(MAXIMIZED_BOTH);
		} else {
			pack();
			// Insets insets = getInsets();
			setSize(new Dimension(
				WIDTH, // + insets.left + insets.right,// + 6,
				HEIGHT // + insets.top + insets.bottom// + 35
			));
			setLocationRelativeTo(null);
			setResizable(false);
		}
		
		setVisible(true);
	}
}
