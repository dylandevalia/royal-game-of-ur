package game.dylandevalia.royal_game_of_ur.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import game.dylandevalia.royal_game_of_ur.utility.PacketManager;
import game.dylandevalia.royal_game_of_ur.utility.ServerInformation;

public class ClientProgram {
	// Client object
	private Client client;

	private boolean messageReceived = false;
	
	public void run(String ip, int port) throws Exception {
		// Start client
		client = new Client();
		// Register packet object
		client.getKryo().register(PacketManager.class);
		
		// Start client
		client.start();
		
		// Connect to the server - wait 5000ms before failing
		client.connect(5000, ip, ServerInformation.TCP_PORT, ServerInformation.UDP_PORT);
		
		client.addListener(new Listener() {
			// Called when client is connected
			@Override
			public void connected(Connection c) {
				System.out.println("Received connection from " + c.getRemoteAddressTCP().getHostString());
			}
			
			// Called when packet is received
			@Override
			public void received(Connection c, Object p) {
				if (p instanceof PacketManager) {
					PacketManager packet = (PacketManager) p;
					messageReceived = true;
					
					System.out.println("Received message: " + packet.message);
				}
			}
			
			// Called when client disconnects
			@Override
			public void disconnected(Connection c) {
				System.out.println("Connection lost");
			}
		});
		
		// Waits for message
		while (!messageReceived) {
			System.out.println("Message not received");
			Thread.sleep(1000);
		}
		
		System.out.println("Client closing");
	}
}
