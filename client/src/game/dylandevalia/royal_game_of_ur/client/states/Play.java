package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.game.AIController;
import game.dylandevalia.royal_game_of_ur.client.game.Board;
import game.dylandevalia.royal_game_of_ur.client.game.CounterCluster;
import game.dylandevalia.royal_game_of_ur.client.game.GameLogic;
import game.dylandevalia.royal_game_of_ur.client.game.GameLogic.MoveState;
import game.dylandevalia.royal_game_of_ur.client.game.Player;
import game.dylandevalia.royal_game_of_ur.client.game.Player.PlayerNames;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Counter;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Tile;
import game.dylandevalia.royal_game_of_ur.client.game.entities.buttons.TextButton;
import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Framework;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

// TODO: Delay captured counter returning until taken
public class Play implements State {
	
	/** Reference to the state manager */
	private StateManager stateManager;
	
	/** GameLogic board */
	private Board board;
	
	/** Holds the game logic */
	private GameLogic game;
	
	/* Counters */
	
	/** The number of counters each player should have */
	private int noCounters = 6;
	
	private Player playerOne, playerTwo;
	
	/* Buttons */
	
	/** Reroll button */
	private TextButton btn_roll;
	
	@Override
	public void initialise(StateManager stateManager) {
		this.stateManager = stateManager;
		
		board = new Board(4, 8, 2);
		playerOne = new Player(PlayerNames.ONE, board.getRouteLength());
		playerTwo = new Player(PlayerNames.TWO, board.getRouteLength());
		
		game = new GameLogic(
			new AIController(board, playerOne, playerTwo)
		);
		Log.info("PLAY", "GameLogic created");
		
		board.generate(playerOne.getRoute(), playerTwo.getRoute());
		generateCounters();
		
		int btn_roll_width = 100;
		int btn_roll_height = 60;
		btn_roll = new TextButton(
			Window.WIDTH - (int) (btn_roll_width * 1.15),
			(Window.HEIGHT / 2) - (btn_roll_height / 2),
			btn_roll_width, btn_roll_height,
			"Roll",
			ColorMaterial.AMBER[5], ColorMaterial.AMBER[3], ColorMaterial.GREY[9]
		);
		btn_roll.setOnClickListener(game::rollDice);
		Log.info("PLAY", "Generation completed");
	}
	
	/**
	 * Generates the counters, clusters and their starting positions
	 */
	private void generateCounters() {
		playerOne.generateCounters(
			noCounters,
			counterInTilePosition(playerOne.getRoute()[0])
				.add(0, Tile.WIDTH),
			counterInTilePosition(playerOne.getRoute()[board.getRouteLength() - 1])
				.add(0, Tile.WIDTH)
		);
		playerTwo.generateCounters(
			noCounters,
			counterInTilePosition(playerTwo.getRoute()[0])
				.sub(0, Tile.WIDTH),
			counterInTilePosition(playerTwo.getRoute()[board.getRouteLength() - 1])
				.sub(0, Tile.WIDTH)
		);
	}
	
	@Override
	public void update() {
		// Get mouse position
		Vector2D mousePos = Framework.getMousePos();
		
		// Update board (tiles)
		board.update();
		
		// Update counters
		for (Counter counter : playerOne.getCounters()) {
			counter.update(mousePos,
				game.allowMove
					&& game.currentPlayer == PlayerNames.ONE
			);
		}
		for (Counter counter : playerTwo.getCounters()) {
			counter.update(
				mousePos,
				game.allowMove
					&& game.currentPlayer == PlayerNames.TWO
			);
		}
		
		// Update buttons
		btn_roll.setActive(game.allowRoll);
		btn_roll.update(mousePos);
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		g.setColor(ColorMaterial.GREY[2]);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		
		/* Objects */
		
		board.draw(g, interpolate);
		for (Counter counter : playerOne.getCounters()) {
			counter.draw(g, interpolate);
		}
		for (Counter counter : playerTwo.getCounters()) {
			counter.draw(g, interpolate);
		}
		
		btn_roll.draw(g, interpolate);
		
		
		/* Text */
		
		g.setFont(new Font("TimesRoman", Font.BOLD, 32));
		FontMetrics fm = g.getFontMetrics();
		
		// Current player
		g.setColor(game.getPlayerColour()[5]);
		String turn = "Player: " + game.getPlayerName();
		g.drawString(turn, (Window.WIDTH - 20) - fm.stringWidth(turn), fm.getHeight());
		
		// Current / previous roll
		// On first turn show nothing
		if (game.currentRoll >= 0) {
			String roll = Integer.toString(game.currentRoll);
			if (game.allowRoll) {
				// Previous player rolled last
				g.setColor(game.getPlayerColour(game.previousPlayer)[5]);
				roll = "Previous Roll: " + roll;
			} else {
				// Else use current player colour
				roll = "Current Roll: " + roll;
			}
			g.drawString(
				roll,
				(Window.WIDTH - 20) - fm.stringWidth(roll),
				Window.HEIGHT - fm.getHeight()
			);
		}
	}
	
	/**
	 * Moves a counter a certain amount of spaces through the given route. Adds all the moves along
	 * the way so that the counter will still move through the route rather than move directly to
	 * the last target
	 *
	 * @param counter The counter to move
	 * @param spaces  The amount of spaces to move the counter along the route
	 */
	private Tile moveCounter(Counter counter, int spaces) {
		Player player = playerNameToPlayer(counter.player);
		Tile[] route = player.getRoute();
		
		if (counter.currentRouteIndex < 0) {
			// Get out of starting cluster
			CounterCluster cluster = player.getStartCluster();
			cluster.remove(counter);
		} else {
			route[counter.currentRouteIndex].setCounter(null);
		}
		
		// Go through each tile one by one
		for (int i = 0; i < Math.abs(spaces); i++) {
			switch (board.checkMove(route, counter, (spaces > 0) ? 1 : -1)) {
				case START:
					player.getStartCluster().add(counter);
					counter.currentRouteIndex = -1;
					return null;
				case END:
					player.getEndCluster().add(counter);
					counter.currentRouteIndex = board.getRouteLength();
					return null;
				default:
					Tile nextTile = board.getNextTile(route, counter, spaces > 0);
					counter.setTarget(counterInTilePosition(nextTile), spaces < 0);
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
	 * @param counter  The counter to check and move
	 * @return True if successfully clicked on a counter
	 */
	private boolean processClick(Vector2D mousePos, Counter counter) {
		Player player = playerNameToPlayer(counter.player);
		if (
			!counter.isColliding(mousePos)                                      // Not clicked on
				|| counter.currentRouteIndex >= board.getRouteLength()              // Out of play
				|| board.checkMove(player.getRoute(), counter, game.currentRoll)
				== MoveState.BLOCKED  // Can't move
			) {
			return false;
		}
		// Counter was clicked on, in play and can move
		
		Tile finalCounter = moveCounter(counter, game.currentRoll);
		
		// Check if game is won
		if (
			playerOne.getEndCluster().getSize() == noCounters
				|| playerTwo.getEndCluster().getSize() == noCounters
			) {
			game.won = true;
			Log.debug("PLAY", "GAME WON!");
			return true;
		}
		
		game.nextTurn(finalCounter == null || !finalCounter.isRosette());
		
		// Return since we found the counter, there's not point
		// looking through the rest of them
		return true;
	}
	
	private Player playerNameToPlayer(PlayerNames name) {
		if (name == PlayerNames.ONE) {
			return playerOne;
		} else if (name == PlayerNames.TWO) {
			return playerTwo;
		} else {
			Log.error("PLAY", "Unknown player name (to player)");
			return null;
		}
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
		if (game.won) {
			// If game is won, ignore
			return;
		}
		
		// Get mouse position
		Vector2D mousePos = new Vector2D(e.getX(), e.getY());
		
		/* Button */
		if (
			game.allowRoll
				&& btn_roll.isColliding(mousePos)
			) {
			btn_roll.press();
			return;
		}
		
		/* Counters */
		if (!game.allowMove) {
			// If not allowing moving counters then escape
			return;
		}
		
		// Go through current player's counters and see if it was clicked
		for (Counter counter : playerNameToPlayer(game.currentPlayer).getCounters()) {
			if (processClick(mousePos, counter)) {
				return;
			}
		}
	}
}
