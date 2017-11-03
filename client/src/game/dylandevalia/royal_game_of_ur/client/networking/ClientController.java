package game.dylandevalia.royal_game_of_ur.client.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.PacketManager;
import game.dylandevalia.royal_game_of_ur.utility.ServerInformation;

public class ClientController {
	// Client object
	private Client client;

	private boolean messageReceived = false;
	
	public void run(String ip, int port) throws Exception {
		Log.info("Client", "Initialising client");
		// Start client
		client = new Client();
		// Register packet object
		client.getKryo().register(PacketManager.class);
		
		Log.info("Client", "Starting client");
		// Start client
		client.start();
		
		// Connect to the networking - wait 5000ms before failing
		client.connect(5000, ip, ServerInformation.TCP_PORT, ServerInformation.UDP_PORT);
		
		client.addListener(new Listener() {
			// Called when client is connected
			@Override
			public void connected(Connection c) {
				Log.info("Client", "Received connection from " + c.getRemoteAddressTCP().getHostString());
			}
			
			// Called when packet is received
			@Override
			public void received(Connection c, Object p) {
				if (p instanceof PacketManager) {
					PacketManager packet = (PacketManager) p;
					messageReceived = true;
					
					Log.info("Client", "Received message: " + packet.message);
					
					if (packet.message.contains("player one")) {
						Log.debug("Client Game", "I am player one");
					} else if (packet.message.contains("player two")) {
						Log.debug("Client Game", "I am player two");
					}
				}
			}
			
			// Called when client disconnects
			@Override
			public void disconnected(Connection c) {
				Log.info("Client", "Connection lost");
			}
		});
		
		// Waits for message
		while (!messageReceived) {
			Thread.sleep(1000);
		}
		
		Log.info("Client", "Client closing");
	}
}
