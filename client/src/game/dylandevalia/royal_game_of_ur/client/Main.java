package game.dylandevalia.royal_game_of_ur.client;

import game.dylandevalia.royal_game_of_ur.utility.ServerInformation;

public class Main {
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			new Thread() {
				@Override
				public void run() {
					try {
						new ClientProgram().run(ServerInformation.LOCALHOST, ServerInformation.TCP_PORT);
					} catch (Exception e) {
					
					}
				}
			}.start();
		}
	}
}
