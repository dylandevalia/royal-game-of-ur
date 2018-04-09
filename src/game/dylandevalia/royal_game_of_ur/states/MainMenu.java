package game.dylandevalia.royal_game_of_ur.states;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Framework;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton.Alignment;
import game.dylandevalia.royal_game_of_ur.objects.nodes.NodeSystem;
import game.dylandevalia.royal_game_of_ur.states.StateManager.GameState;
import game.dylandevalia.royal_game_of_ur.utility.Bundle;
import game.dylandevalia.royal_game_of_ur.utility.ICallback;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * The main menu of the Royal Game of Ur
 */
public class MainMenu implements IState {
	
	/** Reference to the state manager */
	private StateManager stateManager;
	
	/** The array of nodes used for the background */
	private NodeSystem system;
	
	/** The state of the fadeState */
	private FadeState fadeState = FadeState.DOWN;
	/** The number used to control the alpha of the fadeState */
	private int fadeNum = 255;
	
	/** The buttons on the screen */
	private TextButton btn_play, btn_quit;
	
	public void initialise(StateManager stateManager, Bundle bundle) {
		this.stateManager = stateManager;
		
		Log.SET_TRACE();
		
		system = new NodeSystem();
		
		Color base = ColorMaterial.withAlpha(ColorMaterial.INDIGO[8], 50);
		Color hover = ColorMaterial.withAlpha(ColorMaterial.INDIGO[8], 150);
		
		btn_play = new TextButton(
			(Window.WIDTH / 2), (Window.HEIGHT / 2),
			25, 25,
			new Font("TimesRoman", Font.BOLD,
				(int) Utility.mapWidth(64, 128)
			),
			Alignment.CENTER,
			"Play Game",
			base, hover, base,
			ColorMaterial.INDIGO[0]
		);
		btn_play.setOnClickListener(this::loadGame);
		
		btn_quit = new TextButton(
			(Window.WIDTH / 2), 3 * (Window.HEIGHT / 4),
			25, 25,
			new Font("TimesRoman", Font.PLAIN,
				(int) Utility.mapWidth(24, 48)
			),
			Alignment.CENTER,
			"Quit",
			base, hover, base,
			ColorMaterial.INDIGO[0]
		);
		btn_quit.setOnClickListener(() -> {
			fadeState = FadeState.UP;
			fadeState.setCallback(() -> System.exit(0));
		});
	}
	
	@Override
	public void onSet(Bundle bundle) {
	
	}
	
	public void update() {
		system.update();
		
		Vector2D mousePos = Framework.getMousePos();
		btn_play.update(mousePos);
		btn_quit.update(mousePos);
	}
	
	public void draw(Graphics2D g, double interpolate) {
		GradientPaint gradientPaint = new GradientPaint(
			-100, -100,
			ColorMaterial.INDIGO[4],
			Window.WIDTH + 100, Window.HEIGHT + 100,
			ColorMaterial.INDIGO[9]
		);
		g.setPaint(gradientPaint);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		//g.setPaint(oldPaint);
		
		system.draw(g, interpolate, false);
		
		btn_play.draw(g, interpolate);
		btn_quit.draw(g, interpolate);
		
		drawFade(g);
	}
	
	/**
	 * Draws the fadeState to black over the screen depending on the state
	 *
	 * @see #fadeState
	 * @see #fadeNum
	 * @see FadeState
	 */
	private void drawFade(Graphics2D g) {
		switch (fadeState) {
			case NONE:
				break;
			case DOWN:
				if ((fadeNum -= 5) < 0) {
					fadeNum = 0;
				}
				
				g.setColor(new Color(255, 255, 255, fadeNum));
				g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
				if (fadeNum <= 0) {
					fadeState = FadeState.NONE;
					fadeNum = 0;
				}
				break;
			case UP:
				if ((fadeNum += 5) > 255) {
					fadeNum = 255;
				}
				
				g.setColor(new Color(0, 0, 0, fadeNum));
				g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
				
				g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
				if (fadeNum >= 254) {
					fadeState.runCallback();
					fadeNum = 255;
				}
				break;
		}
	}
	
	/**
	 * Loads the Royal Game of Ur state and starts the fade
	 */
	private void loadGame() {
		if (!stateManager.isLoaded(GameState.GAME_UR)) {
			stateManager.loadState(GameState.GAME_UR);
		}
		fadeState = FadeState.UP;
		fadeState.setCallback(this::startGame);
	}
	
	/**
	 * Loads sets the state to the Royal Game of Ur and unloads this state
	 */
	private void startGame() {
		Log.info("MENU", "Starting ur");
		stateManager.setState(GameState.GAME_UR);
		stateManager.unloadState(GameState.MAIN_MENU);
	}
	
	public void keyPressed(KeyEvent e) {
		// Log.debug("main menu/key press", "char: '" + e.getKeyChar() + "'");
	}
	
	public void keyReleased(KeyEvent e) {
	
	}
	
	public void mousePressed(MouseEvent e) {
	
	}
	
	public void mouseReleased(MouseEvent e) {
		// Get mouse position
		Vector2D mousePos = new Vector2D(e.getX(), e.getY());
		if (btn_play.isColliding(mousePos)) {
			btn_play.press();
		} else if (btn_quit.isColliding(mousePos)) {
			btn_quit.press();
		}
	}
	
	/**
	 * Holds the state of the fade in and out Also uses a callback function to execute when a fade
	 * is complete
	 */
	private enum FadeState {
		DOWN(), NONE(), UP();
		
		private ICallback callback;
		private Color color;
		
		void setCallback(ICallback callback) {
			this.callback = callback;
		}
		
		void runCallback() {
			callback.callback();
		}
		
		public Color getColor() {
			return color;
		}
		
		public void setColor(Color color) {
			this.color = color;
		}
	}
}
