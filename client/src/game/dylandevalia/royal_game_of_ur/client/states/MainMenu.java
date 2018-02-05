package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Framework;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.client.objects.base.buttons.TextButton;
import game.dylandevalia.royal_game_of_ur.client.objects.base.buttons.TextButton.ButtonCallback;
import game.dylandevalia.royal_game_of_ur.client.objects.menu.Node;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MainMenu implements State {
	
	private StateManager stateManager;
	private Node[] nodes;
	
	private TextButton btn_play;
	
	public void initialise(StateManager stateManager) {
		this.stateManager = stateManager;
		
		nodes = new Node[150];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(
				Utility.randBetween(-200, Window.WIDTH + 200),
				Utility.randBetween(-200, Window.HEIGHT + 200)
			);
		}
		
		int btn_play_width = 200;
		int btn_play_height = 60;
		btn_play = new TextButton(
			(Window.WIDTH / 2) - (int) (btn_play_width * 1.15),
			(Window.HEIGHT / 2) - (btn_play_height / 2),
			btn_play_width, btn_play_height,
			"Play Game",
			ColorMaterial.AMBER[5], ColorMaterial.AMBER[3], ColorMaterial.GREY[9]
		);
		btn_play.setOnClickListener(new ButtonCallback() {
			@Override
			public void callback() {
				playGame();
			}
		});
	}
	
	public void update() {
		for (Node n : nodes) {
			n.update();
		}
		btn_play.update(Framework.getMousePos());
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
	}
	
	private void playGame() {
		Log.info("MENU", "Starting game");
		if (!stateManager.isLoaded(StateManager.GameState.PLAY)) {
			stateManager.loadState(StateManager.GameState.PLAY);
		}
		stateManager.setState(StateManager.GameState.PLAY);
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
		}
	}
}
