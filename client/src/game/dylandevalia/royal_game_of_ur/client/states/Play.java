package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.game.Board;
import game.dylandevalia.royal_game_of_ur.client.game.CounterCluster;
import game.dylandevalia.royal_game_of_ur.client.game.Game.MoveState;
import game.dylandevalia.royal_game_of_ur.client.game.Game.Players;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Counter;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Tile;
import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Framework;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.UrDice;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import game.dylandevalia.royal_game_of_ur.utility.networking.PacketManager;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

// TODO: Delay captured counter returning until taken
// TODO: When counter returns to starting cluster, move other counters
//       back and add returning counter to the front #stylepoints
public class Play implements State {
	
	/**
	 * Reference to the state manager
	 */
	private StateManager stateManager;
	/**
	 * Game board
	 */
	private Board board = new Board(4, 8, 2);
	
	/* Counters */
	/**
	 * The number of counters each player should have
	 */
	private int noCounters = 6;
	/**
	 * Player one's counters
	 */
	private Counter[] one_counters = new Counter[noCounters];
	/**
	 * Player two's counters
	 */
	private Counter[] two_counters = new Counter[noCounters];
	/**
	 * The clusters for each of the start and end areas
	 */
	private CounterCluster one_countersStart, one_countersEnd, two_countersStart, two_countersEnd;
	
	/* Game logic */
	/**
	 * Keeps track of who's turn it is
	 */
	private boolean playerOnesTurn;
	/**
	 * The current roll of the dice
	 */
	private int currentRoll;
	/* Misc */
	/**
	 * The dice controller
	 */
	private UrDice dice = new UrDice();
	
	@Override
	public void initialise(StateManager stateManager) {
		this.stateManager = stateManager;
		
		board.generate();
		generateCounters();
		Log.info("Play", "Generation completed");
		
		playerOnesTurn = true;
		do {
			currentRoll = dice.roll();
		} while (currentRoll == 0);
	}
	
	/**
	 * Generates the counters, clusters and their starting positions
	 */
	private void generateCounters() {
		one_countersStart = new CounterCluster(
			counterInTilePosition(board.getRoute(Players.ONE)[0]).add(0, Tile.WIDTH),
			true
		);
		one_countersEnd = new CounterCluster(
			counterInTilePosition(board.getRoute(Players.ONE)[board.getRouteLength() - 1])
				.add(0, Tile.WIDTH),
			false
		);
		two_countersStart = new CounterCluster(
			counterInTilePosition(board.getRoute(Players.TWO)[0]).sub(0, Tile.WIDTH),
			true
		);
		two_countersEnd = new CounterCluster(
			counterInTilePosition(board.getRoute(Players.TWO)[board.getRouteLength() - 1])
				.sub(0, Tile.WIDTH),
			false
		);
		
		for (int i = 0; i < noCounters; i++) {
			one_counters[i] = one_countersStart.addNew(Players.ONE);
			two_counters[i] = two_countersStart.addNew(Players.TWO);
		}
	}
	
	@Override
	public void update() {
		board.update();
		for (Counter counter : one_counters) {
			counter.update(Framework.getMousePos());
		}
		for (Counter counter : two_counters) {
			counter.update(Framework.getMousePos());
		}
		
		if (currentRoll == 0) {
			Log.debug("Dice", "Rolled a 0 - swapping players");
			currentRoll = dice.roll();
			playerOnesTurn = !playerOnesTurn;
		}
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		g.setColor(ColorMaterial.GREY[2]);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		/* Objects */
		board.draw(g, interpolate);
		for (Counter counter : one_counters) {
			counter.draw(g, interpolate);
		}
		for (Counter counter : two_counters) {
			counter.draw(g, interpolate);
		}
		
		/* Text */
		g.setColor(ColorMaterial.GREY[9]);
		g.setFont(new Font("TimesRoman", Font.BOLD, 18));
		String turn = playerOnesTurn ? "1" : "2";
		g.drawString("Player: " + turn, Window.WIDTH - 150, 50);
		g.drawString("  Roll: " + currentRoll, Window.WIDTH - 150, Window.HEIGHT - 50);
	}
	
	/**
	 * Moves a counter a certain amount of spaces through the given route. Adds
	 * all the moves along the way so that the counter will still move through
	 * the route rather than move directly to the last target
	 *
	 * @param counter The counter to move
	 * @param spaces The amount of spaces to move the counter along the route
	 */
	private Tile moveCounter(Counter counter, int spaces) {
		Tile[] route = board.getRoute(counter.player);
		if (counter.currentRouteIndex < 0) {
			// Get out of starting cluster
			CounterCluster cluster =
				(counter.player == Players.ONE) ? one_countersStart : two_countersStart;
			cluster.remove(counter);
		} else {
			route[counter.currentRouteIndex].setCounter(null);
		}
		
		// Go through each tile one by one
		for (int i = 0; i < Math.abs(spaces); i++) {
			switch (board.checkMove(counter, (spaces > 0) ? 1 : -1)) {
				case START:
					if (counter.player == Players.ONE) {
						one_countersStart.add(counter);
					} else {
						two_countersStart.add(counter);
					}
					return null;
				case END:
					if (counter.player == Players.ONE) {
						one_countersEnd.add(counter);
					} else {
						two_countersEnd.add(counter);
					}
					return null;
				default:
					Tile nextTile = board.getNextTile(counter, spaces > 0);
					counter.setTarget(counterInTilePosition(nextTile));
					break;
			}
		}
		
		Tile finalTile = route[counter.currentRouteIndex];
		if (finalTile.hasCounter()) {
			// Must be opponent counter since moveCounter is not run if
			// is blocked by player's counter - move back to start
			moveCounter(finalTile.getCounter(), -board.getRouteLength());
		}
		finalTile.setCounter(counter);
		return finalTile;
	}
	
	/**
	 * Gets the position vector of the middle of the given tile where a counter should sit
	 *
	 * @param tile The tile which the counter should sit in
	 * @return The position vector of the counter
	 */
	private Vector2D counterInTilePosition(Tile tile) {
		return new Vector2D(
			tile.getPos().x + (Tile.WIDTH / 2) - (Counter.WIDTH / 2),
			tile.getPos().y + (Tile.WIDTH / 2) - (Counter.WIDTH / 2)
		);
	}
	
	/**
	 * Calculates if a counter has been click on and checks if it can move
	 *
	 * @param mousePos The position vector of the mouse pointer
	 * @param counter The counter to check and move
	 * @return True if successfully clicked on a counter
	 */
	private boolean processClick(Vector2D mousePos, Counter counter) {
		if (counter.isColliding(mousePos)) {                                    // Clicked on
			if (counter.currentRouteIndex < board.getRouteLength()              // In play
				&& board.checkMove(counter, currentRoll) != MoveState.BLOCKED   // Can move
				) {
				Tile finalCounter = moveCounter(
					counter,
					currentRoll
				);
				
				// Swap turn
				if (finalCounter != null && !finalCounter.isRosette()) {
					playerOnesTurn = false;
				}
				currentRoll = dice.roll();
				// Return since we found the counter, there's not point
				// looking through the rest of them
				return true;
			}
		}
		return false;
	}
	
	public void packetReceived(PacketManager packet) {
	
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
			stateManager.setState(StateManager.GameState.PAUSE);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		Vector2D mousePos = new Vector2D(e.getX(), e.getY());
		if (playerOnesTurn) {
			for (Counter counter : one_counters) {
				if (processClick(mousePos, counter)) {
					return;
				}
			}
		} else {
			for (Counter counter : two_counters) {
				if (processClick(mousePos, counter)) {
					return;
				}
			}
		}
	}
}
