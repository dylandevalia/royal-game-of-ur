package game.dylandevalia.royal_game_of_ur.client.game.entities;

import game.dylandevalia.royal_game_of_ur.client.game.GameLogic;
import game.dylandevalia.royal_game_of_ur.client.game.Player.PlayerNames;
import game.dylandevalia.royal_game_of_ur.client.game.entities.buttons.AbstractButton;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Graphics2D;
import java.util.LinkedList;

public class Counter extends AbstractButton {
	
	/** The width of a counter scales to the window width */
	public static final int WIDTH = Window.WIDTH / 15;
	
	/** How quickly the counter will move towards its current target */
	private static final int speed = 8;
	
	/**
	 * The current index through the counter's route
	 * -1 is used to describe a counter not currently on the route
	 */
	public int currentRouteIndex = -1;
	
	/** The player that the counter belongs to */
	public PlayerNames player;
	
	/** A stack of targets which the counter will move to in order */
	private LinkedList<TargetInfo> targets = new LinkedList<>();
	
	/** The current target */
	private TargetInfo target;
	
	/** Is the counter currently moving */
	private boolean isMoving = false;
	
	/** Allow hovering over counter */
	private boolean allowHover = true;
	
	
	public Counter(int x, int y, PlayerNames player) {
		super(x, y, WIDTH, WIDTH, Shape.CIRCLE);
		target = new TargetInfo(pos, false);
		this.player = player;
	}
	
	public void update(Vector2D mousePos, boolean allowHover) {
		super.update(mousePos);
		this.allowHover = allowHover;
		
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
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		super.draw(g, interpolate);
		
		int shade = (allowHover && isMouseHovering) ? 3 : 5;
		g.setColor(
			(player == PlayerNames.ONE) ? GameLogic.one_colour[shade]
				: GameLogic.two_colour[shade]);
		g.fillOval((int) drawPos.x, (int) drawPos.y, width, height);
	}
	
	public void setTarget(Vector2D target, boolean captured) {
		targets.add(new TargetInfo(target, captured));
	}
	
	/**
	 * If the counter is at the current target
	 *
	 * @return Boolean if at the current target position
	 */
	private boolean atTarget() {
		return pos == target.getPos();
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
