package game.dylandevalia.royal_game_of_ur.states;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.menu.Node;
import game.dylandevalia.royal_game_of_ur.states.StateManager.GameState;
import game.dylandevalia.royal_game_of_ur.utility.Bundle;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Pause state for the game
 */
public class Pause implements IState {
	
	private StateManager stateManager;
	
	private Node[] nodes;
	
	@Override
	public void initialise(StateManager stateManager, Bundle bundle) {
		this.stateManager = stateManager;
		nodes = (Node[]) bundle.get("nodes");
	}
	
	@Override
	public void onSet(Bundle bundle) {
		nodes = (Node[]) bundle.get("nodes");
	}
	
	@Override
	public void update() {
		for (Node n : nodes) {
			n.update();
		}
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		GradientPaint gradientPaint = new GradientPaint(
			-100, -100,
			ColorMaterial.RED[4],
			Window.WIDTH + 100, Window.HEIGHT + 100,
			ColorMaterial.RED[9]
		);
		g.setPaint(gradientPaint);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].draw(g, interpolate, nodes, i);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			stateManager.unloadState(GameState.GAME_UR);
			
			stateManager.loadState(GameState.MAIN_MENU);
			stateManager.setState(GameState.MAIN_MENU);
			
			// stateManager.setState(GameState.GAME_UR, new Bundle().put("nodes", nodes));
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	
	}
}
