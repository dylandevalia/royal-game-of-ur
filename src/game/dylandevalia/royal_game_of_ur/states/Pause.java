package game.dylandevalia.royal_game_of_ur.states;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Framework;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.base.Background;
import game.dylandevalia.royal_game_of_ur.objects.base.Background.Node;
import game.dylandevalia.royal_game_of_ur.objects.base.Fade;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton.Alignment;
import game.dylandevalia.royal_game_of_ur.states.StateManager.GameState;
import game.dylandevalia.royal_game_of_ur.utility.Bundle;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Pause state for the game
 */
public class Pause extends AbstractState {
	
	/** The background object which controls the gradient and nodes */
	private Background bg;
	
	/** Controls the fade transition effect */
	private Fade fade;
	
	/** The buttons on the screen */
	private TextButton title, btn_resume, btn_quit;
	
	@Override
	public void initialise(Bundle bundle) {
		bg = new Background(ColorMaterial.RED, (Node[]) bundle.get("nodes"));
		fade = new Fade(ColorMaterial.GREY[0], ColorMaterial.GREY[0], 5, false);
		
		Color base = ColorMaterial.withAlpha(ColorMaterial.RED[8], 50);
		Color hover = ColorMaterial.withAlpha(ColorMaterial.RED[8], 150);
		
		title = new TextButton(
			Window.WIDTH / 2, Window.HEIGHT / 4,
			25, 25,
			new Font(
				"TimesRoman", Font.BOLD,
				(int) Utility.mapWidth(96, 128)
			),
			Alignment.CENTER,
			"PAUSED",
			new Color(0, 0, 0, 0), new Color(0, 0, 0, 0), new Color(0, 0, 0, 0),
			ColorMaterial.RED[0]
		);
		
		btn_resume = new TextButton(
			Window.WIDTH / 2, Window.HEIGHT / 2,
			25, 25,
			new Font(
				"TimesRoman", Font.BOLD,
				(int) Utility.mapWidth(64, 128)
			),
			Alignment.CENTER,
			"Resume",
			base, hover, base,
			ColorMaterial.RED[1]
		);
		btn_resume.setOnClickListener(() -> loadState(GameState.GAME_UR));
		
		btn_quit = new TextButton(
			Window.WIDTH / 2, 3 * Window.HEIGHT / 4,
			25, 25,
			new Font(
				"TimesRoman", Font.BOLD,
				(int) Utility.mapWidth(24, 48)
			),
			Alignment.CENTER,
			"Quit",
			base, hover, base,
			ColorMaterial.RED[1]
		);
		btn_quit.setOnClickListener(() -> {
			fade.out();
			fade.setCallback(() -> loadState(GameState.MAIN_MENU));
		});
	}
	
	@Override
	public void onSet(Bundle bundle) {
		bg.setNodes((Node[]) bundle.get("nodes"));
	}
	
	@Override
	public void update() {
		bg.update();
		
		Vector2D mousePos = Framework.getMousePos();
		title.update(Vector2D.ZERO());
		btn_resume.update(mousePos);
		btn_quit.update(mousePos);
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		bg.draw(g, interpolate);
		
		title.draw(g, interpolate);
		btn_resume.draw(g, interpolate);
		btn_quit.draw(g, interpolate);
		
		fade.draw(g);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			loadState(GameState.GAME_UR);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// Get mouse position
		Vector2D mousePos = new Vector2D(e.getX(), e.getY());
		if (btn_resume.isColliding(mousePos)) {
			btn_resume.press();
		} else if (btn_quit.isColliding(mousePos)) {
			btn_quit.press();
		}
	}
	
	/**
	 * Loads the given state
	 *
	 * @param state The state to load
	 */
	private void loadState(GameState state) {
		stateManager.loadAndSetState(state, new Bundle().put("nodes", bg.getNodes()));
		if (state != GameState.GAME_UR) {
			stateManager.unloadState(GameState.GAME_UR);
			stateManager.unloadState(GameState.PAUSE);
		}
	}
}
