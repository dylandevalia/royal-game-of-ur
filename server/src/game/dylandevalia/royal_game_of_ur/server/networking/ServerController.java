package game.dylandevalia.royal_game_of_ur.server.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import game.dylandevalia.royal_game_of_ur.server.Game;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.networking.PacketManager;

import java.io.IOException;

public class ServerController extends Listener {
	private Game game;
	
	public ServerController(Game game) {
		this.game = game;
	}
	
	public void run(int port) throws IOException {
		Log.info("Server", "Initialising networking");
		// Create new networking
		Server server = new Server();
		// Register packet class
		server.getKryo().register(PacketManager.class);
		// Bind ports
		server.bind(port, port);
		
		Log.info("Server", "Starting networking");
		// Start networking
		server.start();
		
		// Add listener
		server.addListener(new Listener() {
			// Called when client is connected
			public void connected(Connection c) {
				Log.info("Server", "Client connected at: " + c.getRemoteAddressTCP().getHostString());
				
				//	// Create message packet
				//	PacketManager packet = new PacketManager();
				//	packet.message = "Hello! The time is " + new Date().toString();
				//
				//	// Send the message
				//	c.sendTCP(packet);
				
				game.clientConnected(c);
			}
			
			// Called when packet is received
			public void received(Connection c, Object p) {
				Log.info("Server", "Received packet from: " + c.getRemoteAddressTCP().getHostString());
				if (p instanceof PacketManager) {
					game.packetReceived(c, (PacketManager) p);
				}
			}
			
			// Called when client disconnects
			public void disconnected(Connection c) {
				//Log.info("Server", "Client disconnected");
				game.clientDisconnected(c);
			}
		});
	}
}
