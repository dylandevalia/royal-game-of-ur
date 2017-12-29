package game.dylandevalia.royal_game_of_ur.client.game.entities;

import game.dylandevalia.royal_game_of_ur.client.game.GameLogic;
import game.dylandevalia.royal_game_of_ur.client.game.GameLogic.Players;
import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Graphics2D;
import java.util.LinkedList;

public class Counter extends BaseEntity {
	
	/**
	 * The width of a counter scales to the window width
	 */
	public static final int WIDTH = Window.WIDTH / 15;
	
	/**
	 * The current index through the counter's route
	 * -1 is used to describe a counter not currently on the route
	 */
	public int currentRouteIndex = -1;
	
	/**
	 * The player that the counter belongs to
	 */
	public Players player;
	/**
	 * A stack of targets which the counter will move to in order
	 */
	private LinkedList<TargetInfo> targets = new LinkedList<>();
	/**
	 * The current target
	 */
	private TargetInfo target;
	
	/**
	 * True if the mouse is hovering over this counter
	 */
	private boolean mouseHovering = false;
	
	/**
	 * How quickly the counter will move towards its current target
	 */
	private static final int speed = 8;
	
	/**
	 * Is the counter currently moving
	 */
	private boolean isMoving = false;
	
	public Counter(int x, int y, Players player) {
		super(x, y, WIDTH, WIDTH);
		target = new TargetInfo(pos, false);
		this.player = player;
	}
	
	public void setTarget(Vector2D target, boolean captured) {
		targets.add(new TargetInfo(target, captured));
	}
	
	public void update(Vector2D mousePos) {
		super.update();
		
		// See if the mouse is over this counter
		mouseHovering = isColliding(mousePos);
		
		if (atTarget() && targets.isEmpty()) {
			return;
		}
		
		double dist = Vector2D.dist(pos, target.getPos());
		
		if (dist > speed) {
			pos.add(Vector2D.sub(target.getPos(), pos).setMag(target.captured ? speed * 2 : speed));
			isMoving = true;
		} else {    // At target
			pos.set(target.getPos());
			isMoving = false;
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
		return pos == target.getPos();
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
			: (player == Players.ONE ? GameLogic.one_colour : GameLogic.two_colour));
		g.fillOval((int) drawPos.x, (int) drawPos.y, width, height);
	}
	
	private class TargetInfo {
		
		private Vector2D target;
		private boolean captured;
		
		public TargetInfo(Vector2D target, boolean captured) {
			this.target = target;
			this.captured = captured;
		}
		
		public Vector2D getPos() {
			return target;
		}
		
		public boolean isCaptured() {
			return captured;
		}
	}
}
