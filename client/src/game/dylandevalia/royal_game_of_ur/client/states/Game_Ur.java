package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Framework;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.client.objects.base.buttons.TextButton;
import game.dylandevalia.royal_game_of_ur.client.objects.ur.Counter;
import game.dylandevalia.royal_game_of_ur.client.objects.ur.GameLogic;
import game.dylandevalia.royal_game_of_ur.client.objects.ur.Tile;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Game_Ur implements State {
	
	/** Reference to the state manager */
	private StateManager stateManager;
	
	/** Board lengths */
	private int boardStartLength = 4, boardMidLength = 8, boardEndLength = 2;
	
	/** The number of counters each player should have */
	private int noCounters = 6;
	
	/** Holds the objects logic */
	private GameLogic game;
	
	
	/* Buttons */
	
	/** Reroll button */
	private TextButton btn_roll;
	
	@Override
	public void initialise(StateManager stateManager) {
		this.stateManager = stateManager;
		
		game = new GameLogic(boardStartLength, boardMidLength, boardEndLength, noCounters);
		Log.info("PLAY", "GameLogic created");
		
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
	
	@Override
	public void update() {
		// Get mouse position
		Vector2D mousePos = Framework.getMousePos();
		
		game.update(mousePos);
		
		// Update buttons
		btn_roll.setActive(game.isAllowRoll());
		btn_roll.update(mousePos);
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		g.setColor(ColorMaterial.GREY[2]);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		game.draw(g, interpolate);
		
		btn_roll.draw(g, interpolate);
		
		
		/* Text */
		
		g.setFont(new Font("TimesRoman", Font.BOLD, 32));
		FontMetrics fm = g.getFontMetrics();
		
		// Current player
		g.setColor(game.getCurrentPlayer().getMainColor());
		String turn = "Player: " + game.getCurrentPlayer().getName();
		g.drawString(turn, (Window.WIDTH - 20) - fm.stringWidth(turn), fm.getHeight());
		
		// Current / previous roll
		// On first turn show nothing
		if (game.getCurrentRoll() >= 0) {
			String roll = Integer.toString(game.getCurrentRoll());
			if (game.isAllowRoll()) {
				// Previous player rolled last
				g.setColor(game.getPreviousPlayer().getMainColor());
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
		if (game.isWon()) {
			// If objects is won, ignore
			return;
		}
		
		// Get mouse position
		Vector2D mousePos = new Vector2D(e.getX(), e.getY());
		
		
		/* Button */
		
		if (
			game.isAllowRoll()
				&& btn_roll.isColliding(mousePos)
			) {
			btn_roll.press();
			return;
		}
		
		
		/* Counters */
		
		if (!game.isAllowMove()) {
			// If not allowing moving counters then escape
			return;
		}
		
		// Go through current player's counters and see if it was clicked
		for (Counter counter : game.getCurrentPlayer().getCounters()) {
			if (processClick(mousePos, counter)) {
				return;
			}
		}
	}
	
	/**
	 * Calculates if a counter has been click on and checks if it can move
	 *
	 * @param mousePos The position vector of the mouse pointer
	 * @param counter The counter to check and move
	 * @return True if successfully clicked on a counter
	 */
	private boolean processClick(Vector2D mousePos, Counter counter) {
		if (!game.canCounterMove(mousePos, counter)) {
			return false;
		}
		
		// Counter was clicked on, in play and can move
		Tile finalCounter = game
			.moveCounter(game.getCurrentPlayer(), counter, game.getCurrentRoll());
		
		// Check if objects is won
		if (game.checkIfWon(noCounters)) {
			return true;
		}
		
		game.nextTurn(finalCounter == null || !finalCounter.isRosette());
		
		// Return since we found the counter, there's not point
		// looking through the rest of them
		return true;
	}
}
