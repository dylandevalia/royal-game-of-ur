package game.dylandevalia.royal_game_of_ur.client.game.entities;

import game.dylandevalia.royal_game_of_ur.client.game.Game.Players;
import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Graphics2D;
import java.util.LinkedList;

public class Counter extends BaseEntity {
	
	public static final int WIDTH = Window.WIDTH / 15;
	
	public int currentRouteIndex = -1;
	public Players player;
	private LinkedList<Vector2D> targets = new LinkedList<>();
	private Vector2D target;
	private boolean mouseHovering = false;
	
	public Counter(int x, int y, Players player) {
		super(x, y, WIDTH, WIDTH);
		target = pos;
		this.player = player;
	}
	
	public void setTarget(Vector2D target) {
		targets.add(target);
	}
	
	public void update(Vector2D mousePos) {
		super.update();
		
		// See if the mouse is over this counter
		mouseHovering = isColliding(mousePos);
		
		if (atTarget() && targets.isEmpty()) {
			return;
		}
		
		int speed = 8;
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
	 *
	 * @return Boolean if at the current target position
	 */
	private boolean atTarget() {
		return pos == target;
	}
	
	/**
	 * Checks if the given position vector is intersecting with the counter
	 *
	 * @param other The other position
	 * @return If the vector is within the counter
	 */
	public boolean isColliding(Vector2D other) {
		return (Vector2D.dist(pos.copy().add(WIDTH / 2, WIDTH / 2), other) < WIDTH / 2);
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		super.draw(g, interpolate);
		
		g.setColor(mouseHovering ? ColorMaterial.amber
			: (player == Players.ONE ? ColorMaterial.purple : ColorMaterial.green));
		g.fillOval((int) drawPos.x, (int) drawPos.y, width, height);
	}
}
