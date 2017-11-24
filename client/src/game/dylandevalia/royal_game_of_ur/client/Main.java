package game.dylandevalia.royal_game_of_ur.client;

import game.dylandevalia.royal_game_of_ur.client.gui.Window;
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
