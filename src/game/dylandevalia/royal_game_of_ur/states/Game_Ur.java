package game.dylandevalia.royal_game_of_ur.states;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Framework;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton.Alignment;
import game.dylandevalia.royal_game_of_ur.objects.menu.Node;
import game.dylandevalia.royal_game_of_ur.objects.ur.Counter;
import game.dylandevalia.royal_game_of_ur.objects.ur.GameLogic;
import game.dylandevalia.royal_game_of_ur.objects.ur.Tile;
import game.dylandevalia.royal_game_of_ur.states.StateManager.GameState;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Game_Ur implements IState {
	
	/** The number of counters each player should have */
	public static int noCounters = 6;
	/** The number of dice to be used */
	public static int noDice = 4;
	/** Reference to the state manager */
	private StateManager stateManager;
	/** Board lengths */
	private int boardStartLength = 4, boardMidLength = 8, boardEndLength = 2;
	/** Holds the objects logic */
	private GameLogic game;
	
	/* Buttons */
	/** Reroll button */
	private TextButton btn_roll;
	
	private int fadeInNum = 256;
	private double fadeBgRatio = 0;
	
	private Node[] nodes;
	
	@Override
	public void initialise(StateManager stateManager) {
		this.stateManager = stateManager;
		
		game = new GameLogic(
			boardStartLength, boardMidLength, boardEndLength,
			noCounters, false,
			noDice
		);
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
		
		nodes = new Node[(int) Utility.mapWidth(150, 300)];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(
				Utility.randBetween(-200, Window.WIDTH + 200),
				Utility.randBetween(-200, Window.HEIGHT + 200)
			);
		}
	}
	
	@Override
	public void update() {
		// Get mouse position
		Vector2D mousePos = Framework.getMousePos();
		
		game.update(mousePos);
		
		// Update buttons
		btn_roll.setActive(game.isAllowRoll());
		btn_roll.update(mousePos);
		
		for (Node n : nodes) {
			n.update();
		}
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		drawBackground(g);
		
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].draw(g, interpolate, nodes, i);
		}
		
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
		
		if (--fadeInNum > 0) {
			g.setColor(new Color(0, 0, 0, fadeInNum));
			g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		}
	}
	
	private void drawBackground(Graphics2D g) {
		Paint oldPaint = g.getPaint();
		
		fadeBgRatio = Utility.clamp(fadeBgRatio += 0.05, 0.0, 1.0);
		
		int brightShade = 4;
		Color bright = new Color(
			tweenColor(fadeBgRatio, game.getCurrentPlayer().getColors()[brightShade].getRed(),
				game.getPreviousPlayer().getColors()[brightShade].getRed()),
			tweenColor(fadeBgRatio, game.getCurrentPlayer().getColors()[brightShade].getGreen(),
				game.getPreviousPlayer().getColors()[brightShade].getGreen()),
			tweenColor(fadeBgRatio, game.getCurrentPlayer().getColors()[brightShade].getBlue(),
				game.getPreviousPlayer().getColors()[brightShade].getBlue())
		);
		
		int darkShade = 9;
		Color dark = new Color(
			tweenColor(fadeBgRatio, game.getCurrentPlayer().getColors()[darkShade].getRed(),
				game.getPreviousPlayer().getColors()[darkShade].getRed()),
			tweenColor(fadeBgRatio, game.getCurrentPlayer().getColors()[darkShade].getGreen(),
				game.getPreviousPlayer().getColors()[darkShade].getGreen()),
			tweenColor(fadeBgRatio, game.getCurrentPlayer().getColors()[darkShade].getBlue(),
				game.getPreviousPlayer().getColors()[darkShade].getBlue())
		);
		
		GradientPaint gradientPaint = new GradientPaint(
			-100, -100,
			bright,
			Window.WIDTH + 100, Window.HEIGHT + 100,
			dark
		);
		g.setPaint(gradientPaint);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		g.setPaint(oldPaint);
	}
	
	private int tweenColor(double ratio, int near, int far) {
		return (int) (Math.abs((ratio * near)) + ((1 - ratio) * far));
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
			if (!stateManager.isLoaded(GameState.PAUSE)) {
				stateManager.loadState(GameState.PAUSE);
			}
			stateManager.setState(GameState.PAUSE);
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
		
		if (game.getCurrentRoll() > 0) {
			fadeBgRatio = 0;
		} else {
			fadeBgRatio = 1;
		}
		
		// Return since we found the counter, there's not point
		// looking through the rest of them
		return true;
	}
}
