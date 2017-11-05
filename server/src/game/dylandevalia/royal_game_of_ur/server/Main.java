package game.dylandevalia.royal_game_of_ur.server;

import game.dylandevalia.royal_game_of_ur.server.networking.ServerController;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.networking.ServerInformation;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		Game game = new Game();
		ServerController server = new ServerController(game);
		
		try {
			server.run(ServerInformation.TCP_PORT);
		} catch (IOException e) {
			Log.error("Server main", "Could not start networking", e);
		}
	}
}
