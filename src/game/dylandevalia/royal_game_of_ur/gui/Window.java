package game.dylandevalia.royal_game_of_ur.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The main window of the program
 */
public class Window extends JFrame {
	
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	
	// public static int WIDTH = 1920;
	// public static int HEIGHT = 1080;
	
	/** Should the window run is fullscreen or windowed */
	private boolean fullscreen = true;
	
	public Window() {
		this.setTitle("The Royal Game of Ur");
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// Sets size of window
		if (fullscreen) {
			setUndecorated(true);
			setExtendedState(MAXIMIZED_BOTH);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			WIDTH = screenSize.width;
			HEIGHT = screenSize.height;
		} else {
			pack();
			setSize(new Dimension(
				WIDTH,
				HEIGHT
			));
			setLocationRelativeTo(null);
			setResizable(false);
		}
		
		setContentPane(new Framework());
		setVisible(true);
	}
	
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
}
