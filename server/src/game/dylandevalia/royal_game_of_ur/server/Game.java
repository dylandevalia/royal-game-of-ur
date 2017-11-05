package game.dylandevalia.royal_game_of_ur.server;

import com.esotericsoftware.kryonet.Connection;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.networking.PacketManager;

public class Game {
	private final Connection[] players = new Connection[2];
	
	public Game() {
		for (Connection player : players) {
			player = null;
		}
	}
	
	public boolean clientConnected(Connection c) {
		PacketManager packet = new PacketManager();
		if (players[0] == null) {
			players[0] = c;
			packet.message = "You are player one";
			players[0].sendTCP(packet);
		} else if (players[1] == null) {
			players[1] = c;
			packet.message = "You are player two";
			players[1].sendTCP(packet);
		} else {
			packet.message = "Lobby is full";
			c.sendTCP(packet);
			return false;
		}
		return true;
	}
	
	public void packetReceived(Connection c, PacketManager packet) {
		if (players[0] == c) {
			Log.debug("Server game", "Packet received from player one");
		} else if (players[1] == c) {
			Log.debug("Server game", "Packet received from player two");
		}
	}
	
	public void clientDisconnected(final Connection c) {
		if (players[0] == c) {
		} else if (players[1] == c) {
			Log.debug("Server game", "Player two disconnected");
		} else {
			Log.debug("Server game", "Someone else disconnected");
		}
	}
	
	public void run() {
		while (players[0] == null && players[1] == null) {
			sleepThread(1000);
		}
	}
	
	private void sleepThread(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Log.error("Server Game", "Thread.sleep failed", e);
		}
	}
}
