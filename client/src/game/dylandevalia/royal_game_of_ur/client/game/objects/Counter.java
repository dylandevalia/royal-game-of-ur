package game.dylandevalia.royal_game_of_ur.client.game.objects;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;

import java.awt.*;
import java.util.LinkedList;

public class Counter extends BaseEntity {
	public static final int WIDTH = Window.WIDTH / 20;
	
	private boolean playerOne;
	private LinkedList<Vector2D> targets = new LinkedList<>();
	private Vector2D target;
	public int currentRouteIndex = -1;
	
	public Counter(int x, int y, boolean playerOne) {
		super(x, y, WIDTH, WIDTH);
		target = pos;
		this.playerOne = playerOne;
	}
	
	public void setTarget(Vector2D target) {
		targets.add(target);
	}
	
	public int getCurrentRouteIndex() {
		return currentRouteIndex;
	}
	public void incrementCurrentRouteIndex() {
		currentRouteIndex++;
	}
	
	public void update() {
		super.update();
		
		if (atTarget() && targets.isEmpty()) return;
		
		int speed = 5;
		double dist = Vector2D.dist(pos, target);
		
		if (dist > speed) {
			pos.add(Vector2D.sub(target, pos).setMag(speed));
		} else {    // At target
			pos.set(target);
			if (!targets.isEmpty()) {
				target = targets.getFirst();
				targets.removeFirst();
			}
		}
	}
	
	/**
	 * If the counter is at the current target
	 * @return  Boolean if at the current target position
	 */
	private boolean atTarget() {
		return pos == target;
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		super.draw(g, interpolate);
		
		g.setColor(playerOne ? ColorMaterial.purple : ColorMaterial.green);
		g.fillOval((int)drawPos.x, (int)drawPos.y, width, height);
	}
}
