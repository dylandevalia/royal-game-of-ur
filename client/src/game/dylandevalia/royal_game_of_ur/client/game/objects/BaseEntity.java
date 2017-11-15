package game.dylandevalia.royal_game_of_ur.client.game.objects;

import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;

import java.awt.*;

public class BaseEntity {
	protected Vector2D pos = new Vector2D();
	private Vector2D lastPos = new Vector2D();
	protected Vector2D drawPos = new Vector2D();
	protected int width, height;
	protected boolean onScreen;
	
	protected BaseEntity(int x, int y, int width, int height) {
		this.pos.set(x, y);
		this.width = width;
		this.height = height;
	}
	
	protected void update() {
		this.lastPos = this.pos;
	}
	
	protected void draw(Graphics2D g, double interpolate) {
		calculateDrawPos(interpolate);
		isOnScreen();
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
	
	private void calculateDrawPos(double interpolate) {
		if (pos == lastPos) {
			drawPos = pos;
		}
		drawPos.x = ((pos.x - lastPos.x) * interpolate + lastPos.x);
		drawPos.y = ((pos.y - lastPos.y) * interpolate + lastPos.y);
	}
	
	private void isOnScreen() {
		onScreen =
				!(drawPos.x + width < 0 || drawPos.x > Window.WIDTH
						|| drawPos.y + height < 0 || drawPos.y > Window.HEIGHT);
	}
}
