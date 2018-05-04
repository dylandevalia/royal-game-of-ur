package game.dylandevalia.royal_game_of_ur.states;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Framework;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.base.Background;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton.Alignment;
import game.dylandevalia.royal_game_of_ur.states.StateManager.GameState;
import game.dylandevalia.royal_game_of_ur.utility.Bundle;
import game.dylandevalia.royal_game_of_ur.utility.ICallback;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * The main menu of the Royal Game of Ur
 */
public class MainMenu implements IState {
	
	/** Reference to the state manager */
	private StateManager stateManager;
	
	/** The gradient background and nodes */
	private Background bg;
	
	/** The state of the fadeState */
	private FadeState fadeState = FadeState.DOWN;
	/** The number used to control the alpha of the fadeState */
	private int fadeNum = 255;
	
	/** The buttons on the screen */
	private TextButton title, btn_play, btn_simulate, btn_quit;
	
	public void initialise(StateManager stateManager, Bundle bundle) {
		this.stateManager = stateManager;
		
		bg = new Background(ColorMaterial.INDIGO);
		
		Color base = ColorMaterial.withAlpha(ColorMaterial.INDIGO[8], 50);
		Color hover = ColorMaterial.withAlpha(ColorMaterial.INDIGO[8], 150);
		
		title = new TextButton(
			(Window.WIDTH / 2), (Window.HEIGHT / 4),
			25, 25,
			new Font("TimesRoman", Font.BOLD,
				(int) Utility.mapWidth(96, 128)
			),
			Alignment.CENTER,
			"The Royal Game of Ur",
			new Color(0, 0, 0, 0), new Color(0, 0, 0, 0), new Color(0, 0, 0, 0),
			ColorMaterial.INDIGO[0]
		);
		title.setOnClickListener(this::loadGame);
		
		btn_play = new TextButton(
			(Window.WIDTH / 2), (Window.HEIGHT / 2),
			25, 25,
			new Font("TimesRoman", Font.BOLD,
				(int) Utility.mapWidth(64, 128)
			),
			Alignment.CENTER,
			"Play Game",
			base, hover, base,
			ColorMaterial.INDIGO[1]
		);
		btn_play.setOnClickListener(this::loadGame);
		
		btn_simulate = new TextButton(
			(Window.WIDTH / 2), 2 * (Window.HEIGHT / 3),
			25, 25,
			new Font("TimesRoman", Font.BOLD,
				(int) Utility.mapWidth(24, 48)
			),
			Alignment.CENTER,
			"Simulation",
			base, hover, base,
			ColorMaterial.INDIGO[1]
		);
		btn_simulate.setOnClickListener(this::loadSimulation);
		
		btn_quit = new TextButton(
			(Window.WIDTH / 2), 4 * (Window.HEIGHT / 5),
			25, 25,
			new Font("TimesRoman", Font.PLAIN,
				(int) Utility.mapWidth(24, 48)
			),
			Alignment.CENTER,
			"Quit",
			base, hover, base,
			ColorMaterial.INDIGO[1]
		);
		btn_quit.setOnClickListener(() -> {
			fadeState = FadeState.UP;
			fadeState.setCallback(() -> System.exit(0));
		});
	}
	
	public void update() {
		bg.update();
		
		Vector2D mousePos = Framework.getMousePos();
		title.update(mousePos);
		btn_play.update(mousePos);
		btn_simulate.update(mousePos);
		btn_quit.update(mousePos);
	}
	
	public void draw(Graphics2D g, double interpolate) {
		bg.draw(g, interpolate);
		
		title.draw(g, interpolate);
		btn_play.draw(g, interpolate);
		btn_simulate.draw(g, interpolate);
		btn_quit.draw(g, interpolate);
		
		drawFade(g);
	}
	
	/**
	 * Draws the fadeState to black over the screen depending on the state
	 *
	 * @param g The graphics object which gives access to the canvas
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
				
				g.setColor(new Color(255, 255, 255, fadeNum));
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
	 * Loads the Royal Game of Ur Simulation state and starts the fade
	 */
	private void loadSimulation() {
		if (!stateManager.isLoaded(GameState.GAME_UR_SIMULATE)) {
			stateManager.loadState(GameState.GAME_UR_SIMULATE);
		}
		fadeState = FadeState.UP;
		fadeState.setCallback(this::startSimulation);
	}
	
	/**
	 * Loads sets the state to the Royal Game of Ur and unloads this state
	 */
	private void startGame() {
		Log.info("MENU", "Starting ur");
		stateManager.setState(GameState.GAME_UR);
		stateManager.unloadState(GameState.MAIN_MENU);
	}
	
	/**
	 * Loads sets the state to the Royal Game of Ur Simulation and unloads this state
	 */
	private void startSimulation() {
		Log.info("MENU", "Starting ur");
		stateManager.setState(GameState.GAME_UR_SIMULATE);
		stateManager.unloadState(GameState.MAIN_MENU);
	}
	
	public void mouseReleased(MouseEvent e) {
		// Get mouse position
		Vector2D mousePos = new Vector2D(e.getX(), e.getY());
		if (btn_play.isColliding(mousePos)) {
			btn_play.press();
		} else if (btn_simulate.isColliding(mousePos)) {
			btn_simulate.press();
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
