package game.dylandevalia.royal_game_of_ur.client.gui;

import java.awt.Insets;
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

		// Sets size of window
		if (fullscreen) {
			setUndecorated(true);
			setExtendedState(MAXIMIZED_BOTH);
		} else {
			// TODO: Fix this issue of screen being the wrong size
			// Added tmp fix by hardcoding values
			Insets insets = getInsets();
			setSize(1280 + insets.left + insets.right + 6,
				720 + insets.top + insets.bottom + 35);
			setLocationRelativeTo(null);
			setResizable(false);
		}

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setContentPane(new Framework());
		setVisible(true);
	}
}
