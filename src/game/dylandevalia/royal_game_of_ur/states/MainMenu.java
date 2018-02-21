package game.dylandevalia.royal_game_of_ur.states;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Framework;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton.Alignment;
import game.dylandevalia.royal_game_of_ur.objects.menu.Node;
import game.dylandevalia.royal_game_of_ur.states.StateManager.GameState;
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

public class MainMenu implements IState {
	
	private StateManager stateManager;
	private Node[] nodes;
	
	private Fade fade = Fade.DOWN;
	private int fadeNum = 255;
	
	private TextButton btn_play, btn_quit;
	
	public void initialise(StateManager stateManager) {
		this.stateManager = stateManager;
		
		nodes = new Node[(int) Utility.mapWidth(150, 300)];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(
				Utility.randBetween(-200, Window.WIDTH + 200),
				Utility.randBetween(-200, Window.HEIGHT + 200)
			);
		}
		
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
			fade = Fade.UP;
			fade.setCallback(() -> System.exit(0));
		});
	}
	
	public void update() {
		for (Node n : nodes) {
			n.update();
		}

//		btn_play.setActive(fade == Fade.NONE);
//		btn_quit.setActive(fade == Fade.NONE);
		
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
		
		Vector2D mousePos = Framework.getMousePos();
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].draw(g, interpolate, nodes, i);
			
			Vector2D nPos = nodes[i].getPos();
			double dist = Vector2D.dist(mousePos, nPos);
			if (dist < 300) {
				g.setColor(
					ColorMaterial.withAlpha(
						ColorMaterial.INDIGO[3],
						(int) Utility.map(dist, 0, 300, 255, 0)
					)
				);
				g.drawLine(
					(int) mousePos.x, (int) mousePos.y,
					(int) nPos.x, (int) nPos.y
				);
			}
		}
		
		btn_play.draw(g, interpolate);
		btn_quit.draw(g, interpolate);
		
		drawFade(g);
	}
	
	private void drawFade(Graphics2D g) {
		switch (fade) {
			case NONE:
				break;
			case DOWN:
				if (--fadeNum < 0) {
					fadeNum = 0;
				}
				g.setColor(new Color(0, 0, 0, fadeNum));
				g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
				if (fadeNum <= 0) {
					fade = Fade.NONE;
					fadeNum = 0;
				}
				break;
			case UP:
				if ((++fadeNum /*+= 5*/) > 255) {
					fadeNum = 255;
				}

//				GradientPaint gradient = new GradientPaint(
//					-100, -100,
//					ColorMaterial.withAlpha(ColorMaterial.PURPLE[1], fadeNum),
//					Window.WIDTH + 100, Window.HEIGHT + 100,
//					ColorMaterial.withAlpha(ColorMaterial.PURPLE[4], fadeNum)
//				);
//
//				g.setPaint(
//					/*new Color(0, 0, 0, fadeNum)*/
//					gradient
//				);
				
				g.setColor(new Color(0, 0, 0, fadeNum));
				g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
				
				g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
				if (fadeNum >= 254) {
					fade.runCallback();
					fadeNum = 255;
				}
				break;
		}
	}
	
	private void loadGame() {
		if (!stateManager.isLoaded(GameState.GAME_UR)) {
			Log.info("MENU", "Loading GAME_UR");
			stateManager.loadState(GameState.GAME_UR);
		}
		fade = Fade.UP;
		fade.setCallback(this::startGame);
		
	}
	
	private void startGame() {
		Log.info("MENU", "Starting ur");
		stateManager.setState(GameState.GAME_UR);
		stateManager.unloadState(GameState.MAIN_MENU);
	}
	
	public void keyPressed(KeyEvent e) {
//		Log.debug("main menu/key press", "char: '" + e.getKeyChar() + "'");
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
	
	private enum Fade {
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
