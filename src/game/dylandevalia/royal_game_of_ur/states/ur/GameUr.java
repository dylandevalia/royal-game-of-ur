package game.dylandevalia.royal_game_of_ur.states.ur;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Framework;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.base.Background;
import game.dylandevalia.royal_game_of_ur.objects.base.Background.Node;
import game.dylandevalia.royal_game_of_ur.objects.base.Fade;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton.Alignment;
import game.dylandevalia.royal_game_of_ur.objects.ur.Counter;
import game.dylandevalia.royal_game_of_ur.objects.ur.GameLogic;
import game.dylandevalia.royal_game_of_ur.objects.ur.Player;
import game.dylandevalia.royal_game_of_ur.objects.ur.Tile;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.AI;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.Library;
import game.dylandevalia.royal_game_of_ur.states.AbstractState;
import game.dylandevalia.royal_game_of_ur.states.StateManager.GameState;
import game.dylandevalia.royal_game_of_ur.utility.Bundle;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * State which plays the Royal Game of Ur
 */
public class GameUr extends AbstractState {
	
	/** The number of counters each player should have */
	public static int noCounters = 6;
	
	/** The number of dice to be used */
	public static int noDice = 4;
	
	/** Holds the objects logic */
	private GameLogic game;
	
	/** Reroll button */
	private TextButton btn_roll;
	
	/** Reference to the current player in the game */
	private Player currentPlayer;
	
	@Override
	public void initialise(Bundle bundle) {
		game = new GameLogic(true, null, new AI(Library.thousand_126));
		Log.info("GAME_UR", "GameLogic created");
		
		btn_roll = new TextButton(
			Window.WIDTH - 20, (Window.HEIGHT / 2),
			20, 10,
			new Font("TimesRoman", Font.BOLD,
				(int) Utility.mapWidth(28, 56)
			),
			Alignment.RIGHT,
			"Roll",
			ColorMaterial.AMBER[5], ColorMaterial.AMBER[3], ColorMaterial.blueGrey,
			ColorMaterial.GREY[9]
		);
		btn_roll.setOnClickListener(game::rollDice);
		Log.info("GAME_UR", "Generation completed");
		
		if (bundle != null) {
			bg = new Background(
				game.getCurrentPlayer().getColors(),
				(Node[]) bundle.get("nodes")
			);
		} else {
			bg = new Background(game.getCurrentPlayer().getColors());
		}
		
		fade = new Fade(ColorMaterial.GREY[0], ColorMaterial.GREY[0], 5, true);
	}
	
	@Override
	public void onSet(Bundle bundle) {
		if (bundle != null) {
			bg.setNodes((Node[]) bundle.get("nodes"));
		}
	}
	
	@Override
	public void update() {
		// Update background
		bg.update();
		
		// Get mouse position
		Vector2D mousePos = Framework.getMousePos();
		
		// Update game logic
		game.update(mousePos);
		
		// Update buttons
		btn_roll.setActive(game.isAllowRoll() && !game.isAnimating());
		btn_roll.update(mousePos);
		
		// If player has changed, update reference and update background colours
		if (currentPlayer != game.getCurrentPlayer()) {
			currentPlayer = game.getCurrentPlayer();
			bg.setColors(currentPlayer.getColors(), true);
		}
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		bg.draw(g, interpolate);
		
		game.draw(g, interpolate);
		
		btn_roll.draw(g, interpolate);
		
		
		/* Text */
		
		g.setFont(new Font("TimesRoman", Font.BOLD,
			(int) Utility.mapWidth(32, 64)
		));
		FontMetrics fm = g.getFontMetrics();
		
		// Current player
		g.setColor(game.getCurrentPlayer().getColors()[3]);
		String turn = "Player: " + game.getCurrentPlayer().getName();
		g.drawString(turn, (Window.WIDTH - 20) - fm.stringWidth(turn), fm.getHeight());
		
		// Current / previous roll
		// On first turn show nothing
		if (game.getCurrentRoll() >= 0) {
			String roll = Integer.toString(game.getCurrentRoll());
			if (game.isAllowRoll()) {
				// Previous player rolled last
				g.setColor(game.getPreviousPlayer().getColors()[3]);
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
		
		fade.draw(g);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
			Bundle bundle = new Bundle().put("nodes", bg.getNodes());
			
			if (!stateManager.isLoaded(GameState.PAUSE)) {
				stateManager.loadState(GameState.PAUSE, bundle);
			}
			stateManager.setState(GameState.PAUSE, bundle);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (game.isWon()) {
			// If objects is won, ignore
			return;
		}
		
		// Get mouse position
		Vector2D mousePos = new Vector2D(e.getX(), e.getY());
		
		if (game.isAllowRoll() && btn_roll.isColliding(mousePos)) {
			btn_roll.press();
			return;
		}
		
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
	 * @param counter  The counter to check and move
	 * @return True if successfully clicked on a counter
	 */
	private boolean processClick(Vector2D mousePos, Counter counter) {
		if (!game.canCounterMove(mousePos, counter)) {
			return false;
		}
		
		// Counter was clicked on, in play and can move
		Tile finalTile = game
			.moveCounter(game.getCurrentPlayer(), counter, game.getCurrentRoll());
		
		game.endOfTurn(finalTile);
		
		// Return since we found the counter, there's not point
		// looking through the rest of them
		return true;
	}
}
