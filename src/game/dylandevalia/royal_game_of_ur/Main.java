package game.dylandevalia.royal_game_of_ur;

import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import javax.swing.SwingUtilities;

public class Main {
	
	public static void main(String[] args) {
		Log.SET_DEBUG();
		
		// Start running the gui in its own thread
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Window();
			}
		});
	}
}
