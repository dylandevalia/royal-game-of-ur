package game.dylandevalia.royal_game_of_ur.states;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Framework;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.TextButton.Alignment;
import game.dylandevalia.royal_game_of_ur.objects.menu.Node;
import game.dylandevalia.royal_game_of_ur.utility.ICallback;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
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
		
		nodes = new Node[150];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(
				Utility.randBetween(-200, Window.WIDTH + 200),
				Utility.randBetween(-200, Window.HEIGHT + 200)
			);
		}
		
		Color base = ColorMaterial.INDIGO[8];
		Color hover = ColorMaterial.INDIGO[8];
		base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 100);
		hover = new Color(hover.getRed(), hover.getGreen(), hover.getBlue(), 200);
		
		btn_play = new TextButton(
			(Window.WIDTH / 2), (Window.HEIGHT / 2),
			25, 25,
			new Font("TimesRoman", Font.BOLD, 64),
			Alignment.CENTER,
			"Play Game",
			base, hover,
			ColorMaterial.INDIGO[0]
		);
		btn_play.setOnClickListener(this::loadGame);
		
		btn_quit = new TextButton(
			(Window.WIDTH / 2), 3 * (Window.HEIGHT / 4),
			25, 25,
			new Font("TimesRoman", Font.PLAIN, 24),
			Alignment.CENTER,
			"Quit",
			base, hover,
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
		Vector2D mousePos = Framework.getMousePos();
		btn_play.update(mousePos);
		btn_quit.update(mousePos);
	}
	
	public void draw(Graphics2D g, double interpolate) {
		Paint oldPaint = g.getPaint();
		GradientPaint gradientPaint = new GradientPaint(
			-100, -100,
			ColorMaterial.INDIGO[4],
			Window.WIDTH + 200, Window.HEIGHT + 200,
			ColorMaterial.INDIGO[9]
		);
		g.setPaint(gradientPaint);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		g.setPaint(oldPaint);
		
		Vector2D mousePos = Framework.getMousePos();
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].draw(g, interpolate, nodes, i);
			
			Vector2D nPos = nodes[i].getPos();
			double dist = Vector2D.dist(mousePos, nPos);
			if (dist < 300) {
				Color c = ColorMaterial.INDIGO[3];
				g.setColor(
					new Color(
						c.getRed(), c.getGreen(), c.getBlue(),
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
		
		switch (fade) {
			case NONE:
				break;
			case DOWN:
				g.setColor(new Color(0, 0, 0, fadeNum--));
				g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
				if (fadeNum <= 0) {
					fade = Fade.NONE;
				}
				break;
			case UP:
				g.setColor(new Color(0, 0, 0, fadeNum += 2));
				g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
				if (fadeNum >= 254) {
					fade.runCallback();
				}
				break;
		}
	}
	
	private void loadGame() {
		if (!stateManager.isLoaded(StateManager.GameState.GAME_UR)) {
			Log.info("MENU", "Loading GAME_UR");
			stateManager.loadState(StateManager.GameState.GAME_UR);
		}
		fade = Fade.UP;
		fade.setCallback(this::startGame);
	}
	
	private void startGame() {
		Log.info("MENU", "Starting ur");
		stateManager.setState(StateManager.GameState.GAME_UR);
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
		
		ICallback callback;
		
		void setCallback(ICallback callback) {
			this.callback = callback;
		}
		
		void runCallback() {
			callback.callback();
		}
	}
}
