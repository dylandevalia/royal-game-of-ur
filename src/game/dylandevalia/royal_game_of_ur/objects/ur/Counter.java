package game.dylandevalia.royal_game_of_ur.objects.ur;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.base.buttons.AbstractButton;
import game.dylandevalia.royal_game_of_ur.objects.ur.Player.PlayerID;
import game.dylandevalia.royal_game_of_ur.utility.Pair;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

/**
 * The game pieces that the game uses
 */
public class Counter extends AbstractButton {
	
	/** The width of a counter scales to the window width */
	public static final int WIDTH = Window.WIDTH / 15;
	
	/** How quickly the counter will move towards its current target */
	static final double SPEED = Utility.mapWidth(10, 16);
	
	static boolean instantAnimate = false;
	
	/**
	 * The current index through the counter's route -1 is used to describe a counter not currently
	 * on the route
	 */
	private int currentRouteIndex = -1;
	
	/** The player that the counter belongs to */
	private PlayerID player;
	
	/** Player's colours */
	private Color[] colors;
	
	/** A stack of targets which the counter will move to in order */
	private LinkedList<Pair<Vector2D, Boolean>> targets = new LinkedList<>();
	
	/** The current target */
	private Pair<Vector2D, Boolean> target;
	
	/** Is the counter currently moving */
	private boolean isMoving = false;
	
	/** Allow hovering over counter */
	private boolean allowHover = true;
	
	/** Used to offset the dots on the counter design */
	private double dotOffset = Utility.randBetween(0, Math.PI / 2);
	
	// TODO: Have the counter hold a reference to the player?
	
	/**
	 * @param x      The x coordinate of the piece
	 * @param y      The y coordinate of the piece
	 * @param player The ID of the player that owns the counter
	 * @param colors The colour set that the counter should draw with
	 */
	public Counter(int x, int y, PlayerID player, Color[] colors) {
		super(x, y, WIDTH, WIDTH, Shape.CIRCLE);
		target = new Pair<>(pos, false);
		this.player = player;
		this.colors = colors;
	}
	
	/**
	 * @param mousePos   The position of the mouse
	 * @param allowHover Is the counter allowed to be hovered over
	 */
	public void update(Vector2D mousePos, boolean allowHover) {
		super.update(mousePos);
		this.allowHover = allowHover;
		
		if (atTarget() && targets.isEmpty()) {
			isMoving = false;
			return;
		}
		
		double dist = Vector2D.dist(pos, target.getKey());
		
		if (instantAnimate) {
			pos.set(targets.getLast().getKey());
			targets.clear();
		} else if (dist > SPEED) {
			// If more that speed away from the target
			// Move speed towards target
			pos.add(
				Vector2D.sub(target.getKey(), pos)
					.setMag(target.getValue() ? SPEED * 2 : SPEED)
			);
			isMoving = true;
		} else {// Less that speed away from target
			if (!atTarget()) {
				// So just move to target
				pos.set(target.getKey());
			}
			
			// Target is next target if exists
			if (!targets.isEmpty()) {
				target = targets.getFirst();
				targets.removeFirst();
			}
		}
	}
	
	public void draw(Graphics2D g, double interpolate) {
		super.draw(g, interpolate);
		int drawX = (int) drawPos.x, drawY = (int) drawPos.y;
		
		g.setColor(ColorMaterial.withAlpha(colors[9], 100));
		int shadowOffset = (int) Utility.mapWidth(3, 4);
		g.fillOval(drawX + shadowOffset, drawY + shadowOffset, width, height);
		
		int shade = 5 - ((allowHover && isMouseHovering) ? 2 : 0);
		g.setColor(colors[shade]);
		g.fillOval(drawX, drawY, width, height);
		
		
		/* Dots */
		
		g.setColor(colors[shade - 2]);
		g.fillOval(
			(drawX + width / 2) - width / 16,
			(drawY + width / 2) - width / 16,
			width / 8,
			height / 8
		);
		for (int i = 0; i < 4; i++) {
			int x = (int) ((width / 4) * Math.cos(dotOffset + ((Math.PI / 2) * i)));
			int y = (int) ((height / 4) * Math.sin(dotOffset + ((Math.PI / 2) * i)));
			g.fillOval(
				((drawX + width / 2) + x) - width / 16,
				((drawY + width / 2) + y) - width / 16,
				width / 8,
				height / 8
			);
		}
	}
	
	/**
	 * If the counter is at the current target
	 *
	 * @return True if at the current target position
	 */
	private boolean atTarget() {
		return pos.equals(target.getKey());
	}
	
	void setTarget(Vector2D target, boolean captured) {
		Pair<Vector2D, Boolean> pair = new Pair<>(target, captured);
		
		if (!instantAnimate && atTarget() && targets.isEmpty()) {
			this.target = pair;
		} else {
			targets.add(pair);
		}
	}
	
	boolean isMoving() {
		return isMoving;
		// return !atTarget();
	}
	
	public int getCurrentRouteIndex() {
		return currentRouteIndex;
	}
	
	void setCurrentRouteIndex(int currentRouteIndex) {
		this.currentRouteIndex = currentRouteIndex;
	}
	
	boolean isMouseHovering() {
		return allowHover && isMouseHovering;
	}
	
	public PlayerID getPlayer() {
		return player;
	}
}
