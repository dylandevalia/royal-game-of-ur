package game.dylandevalia.royal_game_of_ur.client.game;

import com.esotericsoftware.kryonet.Connection;
import game.dylandevalia.royal_game_of_ur.client.gui.Framework;
import game.dylandevalia.royal_game_of_ur.client.states.StateManager;
import game.dylandevalia.royal_game_of_ur.utility.networking.PacketManager;

/**
 * Keeps track of all the game logic
 */
public class Game {
	public Framework framework;
	public StateManager stateManager = new StateManager(this);
	
	public Game(Framework framework) {
		this.framework = framework;
	}
	
	// Players
	private final Connection[] players = new Connection[2];
	
	public void clientConnected(Connection c) {
	
	}
	
	public void packetReceived(Connection c, PacketManager packet) {
		stateManager.packetReceived(packet);
	}
	
	public void clientDisconnected(Connection c) {
	
	}
}
