package game.dylandevalia.royal_game_of_ur.states.ur;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.objects.base.Background;
import game.dylandevalia.royal_game_of_ur.objects.base.Background.Node;
import game.dylandevalia.royal_game_of_ur.objects.base.Fade;
import game.dylandevalia.royal_game_of_ur.states.AbstractState;
import game.dylandevalia.royal_game_of_ur.states.StateManager.GameState;
import game.dylandevalia.royal_game_of_ur.utility.Bundle;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class MenuPlay extends AbstractState {
	
	@Override
	public void initialise(Bundle bundle) {
		bg = new Background(ColorMaterial.INDIGO, (Node[]) bundle.get("nodes"));
		fade = new Fade(ColorMaterial.GREY[0], ColorMaterial.GREY[0], 5, false);
	}
	
	@Override
	public void onSet(Bundle bundle) {
		bg.setColors(ColorMaterial.INDIGO, false);
		bg.setColors(ColorMaterial.CYAN, true);
		bg.setNodes((Node[]) bundle.get("nodes"));
	}
	
	@Override
	public void update() {
		bg.update();
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		bg.draw(g, interpolate);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == ' ') {
			stateManager
				.loadAndSetState(GameState.MENU_MAIN, new Bundle().put("nodes", bg.getNodes()));
		}
	}
}
