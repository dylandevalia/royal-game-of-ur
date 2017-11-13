package game.dylandevalia.royal_game_of_ur.client;

import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Log;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
//		for (int i = 0; i < 3; i++) {
//			new Thread() {
//				@Override
//				public void run() {
//					try {
//						new ClientController().run(ServerInformation.LOCALHOST, ServerInformation.TCP_PORT);
//					} catch (Exception e) {
//
//					}
//				}
//			}.start();
//		}

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
