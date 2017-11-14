package game.dylandevalia.royal_game_of_ur.client.game.objects;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;

import java.awt.*;

public class Counter extends BaseEntity {
	public static final int WIDTH = Window.WIDTH / 20;
	
	private boolean playerOne;
	private Vector2D target;
	
	public Counter(int x, int y, boolean playerOne) {
		super(x, y, WIDTH, WIDTH);
		target = pos;
		this.playerOne = playerOne;
	}
	
	public void setTarget(Vector2D target) {
		this.target = target;
	}
	
	public void update() {
		super.update();
		
		if (pos == target) return;
		
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
		
		g.setColor(playerOne ? ColorMaterial.purple : ColorMaterial.green);
		g.fillOval((int)drawPos.x, (int)drawPos.y, width, height);
	}
}
