package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Framework;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.client.objects.menu.Node;
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
	
	public void initialise(StateManager stateManager) {
		this.stateManager = stateManager;
		
		nodes = new Node[80];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(
				Utility.randBetween(0, Window.WIDTH),
				Utility.randBetween(0, Window.HEIGHT)
			);
		}
	}
	
	public void update() {
		for (Node n : nodes) {
			n.update();
		}
	}
	
	public void draw(Graphics2D g, double interpolate) {
		Paint oldPaint = g.getPaint();
		GradientPaint gradientPaint = new GradientPaint(
			-100, -100,
			ColorMaterial.INDIGO[4],
			Window.WIDTH + 100, Window.HEIGHT + 100,
			ColorMaterial.INDIGO[8]
		);
		g.setPaint(gradientPaint);
//		g.setColor(ColorMaterial.indigo);
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
	}
	
	public void keyPressed(KeyEvent e) {
//		Log.debug("main menu/key press", "char: '" + e.getKeyChar() + "'");
	}
	
	public void keyReleased(KeyEvent e) {
	
	}
	
	public void mousePressed(MouseEvent e) {
	
	}
	
	public void mouseReleased(MouseEvent e) {
//		if (!stateManager.isLoaded(StateManager.GameState.PLAY)) {
//			stateManager.loadState(StateManager.GameState.PLAY);
//		}
//		stateManager.setState(StateManager.GameState.PLAY);
	}
}
