package game.dylandevalia.royal_game_of_ur.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.PacketManager;
import game.dylandevalia.royal_game_of_ur.utility.ServerInformation;

import java.util.Date;

public class ServerProgram extends Listener {
	// Server
	private static Server server;
	
	public static void main(String[] args) throws Exception {
		Log.info("Server", "Initialising server");
		// Create new server
		server = new Server();
		// Register packet class
		server.getKryo().register(PacketManager.class);
		// Bind ports
		server.bind(ServerInformation.TCP_PORT, ServerInformation.UDP_PORT);
		
		Log.info("Server", "Starting server");
		// Start server
		server.start();
		
		// Add listener
		server.addListener(new Listener() {
			// Called when client is connected
			public void connected(Connection c) {
				Log.info("Server", "Client connected at: " + c.getRemoteAddressTCP().getHostString());
				// Create message packet
				PacketManager packet = new PacketManager();
				packet.message = "Hello! The time is " + new Date().toString();
				
				// Send the message
				c.sendTCP(packet);
			}
			
			// Called when packet is received
			public void received(Connection c, Object p) {
				Log.info("Server", "Received packet from: " + c.getRemoteAddressTCP().getHostString());
			}
			
			// Called when client disconnects
			public void disconnected(Connection c) {
				Log.info("Server", "Client disconnected");
			}
		});
	}
}
