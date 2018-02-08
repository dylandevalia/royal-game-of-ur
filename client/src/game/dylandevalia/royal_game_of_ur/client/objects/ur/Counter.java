package game.dylandevalia.royal_game_of_ur.client.objects.ur;

import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.client.objects.base.buttons.AbstractButton;
import game.dylandevalia.royal_game_of_ur.client.objects.ur.Player.PlayerID;
import game.dylandevalia.royal_game_of_ur.utility.Pair;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

public class Counter extends AbstractButton {
	
	/** The width of a counter scales to the window width */
	public static final int WIDTH = Window.WIDTH / 15;
	
	/** How quickly the counter will move towards its current target */
	public static final int SPEED = 8;
	
	public static boolean instantAnimate = false;
	
	/**
	 * The current index through the counter's route
	 * -1 is used to describe a counter not currently on the route
	 */
	public int currentRouteIndex = -1;
	
	/** The player that the counter belongs to */
	public PlayerID player;
	
	/** A stack of targets which the counter will move to in order */
	private LinkedList<Pair<Vector2D, Boolean>> targets = new LinkedList<>();
	
	/** The current target */
	private Pair<Vector2D, Boolean> target;
	
	/** Is the counter currently moving */
	private boolean isMoving = false;
	
	/** Allow hovering over counter */
	private boolean allowHover = true;
	
	
	public Counter(int x, int y, PlayerID player) {
		super(x, y, WIDTH, WIDTH, Shape.CIRCLE);
		target = new Pair<>(pos, false);
		this.player = player;
	}
	
	public void update(Vector2D mousePos, boolean allowHover) {
		super.update(mousePos);
		this.allowHover = allowHover;
		
		if (atTarget() && targets.isEmpty()) {
			return;
		}
		
		double dist = Vector2D.dist(pos, target.getKey());
		
		if (instantAnimate) {
			pos.set(targets.getLast().getKey());
			targets.clear();
		} else if (dist > SPEED) { // If more that speed away from the target
			// Move speed towards target
			pos.add(
				Vector2D.sub(target.getKey(), pos)
					.setMag(target.getValue() ? SPEED * 2 : SPEED)
			);
			isMoving = true;
		} else {    // Less that speed away from target
			// So just move to target
			pos.set(target.getKey());
			isMoving = false;
			
			// Target is next target if exists
			if (!targets.isEmpty()) {
				target = targets.getFirst();
				targets.removeFirst();
			}
		}
	}
	
	public void draw(Graphics2D g, double interpolate, Color[] colors) {
		super.draw(g, interpolate);
		
		int shade = (allowHover && isMouseHovering) ? 3 : 5;
		g.setColor(colors[shade]);
		g.fillOval((int) drawPos.x, (int) drawPos.y, width, height);
	}
	
	/**
	 * If the counter is at the current target
	 *
	 * @return Boolean if at the current target position
	 */
	private boolean atTarget() {
		return pos == target.getKey();
	}
	
	public void setTarget(Vector2D target, boolean captured) {
		targets.add(new Pair<>(target, captured));
	}
	
	public boolean isMoving() {
		return isMoving;
	}
}
