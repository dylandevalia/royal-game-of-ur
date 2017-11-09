package game.dylandevalia.royal_game_of_ur.client.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import game.dylandevalia.royal_game_of_ur.client.game.Game;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.networking.PacketManager;

/**
 * Creates a network client and connects to the server
 */
public class ClientController {
	// See if message has been received from listener
	public boolean gameRunning = true;
	
	/**
	 * Runs the initialisation of the network client and creates the listeners
	 * @param ip    The IP address of the server to connect to
	 * @param port  The port of server
	 * @throws Exception    IOException if the client fails to connect
	 */
	public void run(String ip, int port, final Game game) throws Exception {
		Log.info("Client", "Initialising client");
		// Start client
		Client client = new Client();
		// Register packet object
		client.getKryo().register(PacketManager.class);
		
		Log.info("Client", "Starting client");
		// Start client
		client.start();
		
		// Connect to the networking - wait 5000ms before failing
		client.connect(5000, ip, port, port);
		
		client.addListener(new Listener() {
			// Called when client is connected
			@Override
			public void connected(Connection c) {
				Log.info("Client", "Received connection from " + c.getRemoteAddressTCP().getHostString());
				game.clientConnected(c);
			}
			
			// Called when packet is received
			@Override
			public void received(Connection c, Object p) {
				if (p instanceof PacketManager) {
//					Log.info("Client", "Packet received");
//					PacketManager packet = (PacketManager) p;
//					gameRunning = true;
//
//					Log.info("Client", "Received message: " + packet.message);
//
//					if (packet.message.contains("player one")) {
//						Log.debug("Client Game", "I am player one");
//					} else if (packet.message.contains("player two")) {
//						Log.debug("Client Game", "I am player two");
//					}
					
					PacketManager packet = (PacketManager) p;
					game.packetReceived(c, packet);
				}
			}
			
			// Called when client disconnects
			@Override
			public void disconnected(Connection c) {
				Log.info("Client", "Connection lost");
				game.clientDisconnected(c);
			}
		});
		
		// Waits for message and polls for packets every second
		while (gameRunning) {
			Thread.sleep(1000);
		}
		
		Log.info("Client", "Client closing");
	}
}
