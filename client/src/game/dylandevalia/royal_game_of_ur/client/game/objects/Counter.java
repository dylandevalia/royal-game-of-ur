package game.dylandevalia.royal_game_of_ur.client.game.objects;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;

import java.awt.*;

public class Counter extends BaseEntity {
	private boolean playerOne;
	
	public Counter(int x, int y, boolean playerOne) {
		super(x, y, Window.WIDTH / 20, Window.WIDTH / 20);
		this.playerOne = playerOne;
	}
	
	public void update(Vector2D target) {
		super.update();
		
		Vector2D vel = new Vector2D();
		int speed = 5;
		double dist = Vector2D.dist(pos, target);
		
		if (dist > speed) {
			pos.add(target.copy().sub(pos).setMag(speed));
		} else {
			pos.set(target);
		}
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		super.draw(g, interpolate);
		
		if (playerOne) {
			g.setColor(ColorMaterial.purple);
		} else {
			g.setColor(ColorMaterial.cyan);
		}
		g.fillOval((int)drawPos.x, (int)drawPos.y, width, height);
	}
}
