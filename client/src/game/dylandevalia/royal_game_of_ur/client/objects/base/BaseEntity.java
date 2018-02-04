package game.dylandevalia.royal_game_of_ur.client.objects.base;

import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Graphics2D;

public class BaseEntity {
	
	/** The current position of the entity */
	protected Vector2D pos = new Vector2D();
	
	/** The drawable position to be used in {@link #draw(Graphics2D, double)} */
	protected Vector2D drawPos = new Vector2D();
	
	/** The dimensions of the entity */
	protected int width, height;
	
	/**
	 * If the entity is currently on screen
	 * Calculated in {@link #isOnScreen(Vector2D)}
	 */
	protected boolean shouldDraw;
	
	/**
	 * The previous position of the entity used to
	 * calculate {@link #drawPos}
	 */
	private Vector2D lastPos = new Vector2D();
	
	protected BaseEntity(int x, int y, int width, int height) {
		this.pos.set(x, y);
		this.width = width;
		this.height = height;
	}
	
	protected BaseEntity(int x, int y, int size) {
		this(x, y, size, size);
	}
	
	protected void update() {
		this.lastPos = this.pos;
	}
	
	protected void draw(Graphics2D g, double interpolate) {
		calculateDrawPos(interpolate);
		shouldDraw = isOnScreen(drawPos);
	}
	
	public Vector2D getPos() {
		return pos;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	/**
	 * Uses the interpolation value to calculate a draw position
	 */
	private void calculateDrawPos(double interpolate) {
		if (pos == lastPos) {
			drawPos = pos;
		}
		drawPos.x = ((pos.x - lastPos.x) * interpolate + lastPos.x);
		drawPos.y = ((pos.y - lastPos.y) * interpolate + lastPos.y);
	}
	
	/**
	 * Checks if the drawable object is on the screen
	 */
	protected boolean isOnScreen(Vector2D pos) {
		return !(pos.x + width < 0 || pos.x > Window.WIDTH
			|| pos.y + height < 0 || pos.y > Window.HEIGHT);
	}
}
