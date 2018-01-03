package game.dylandevalia.royal_game_of_ur.client.game.entities;

import game.dylandevalia.royal_game_of_ur.utility.Vector2D;

public abstract class Button extends BaseEntity {
	
	private Shape shape;
	protected boolean isMouseHovering;
	
	public Button(int x, int y, int width, int height, Shape shape) {
		super(x, y, width, height);
		this.shape = shape;
	}
	
	public void update(Vector2D mousePos) {
		super.update();
		
		isMouseHovering = isColliding(mousePos);
	}
	
	/**
	 * Checks if the given position vector is intersecting with the object
	 *
	 * @param other The other position
	 * @return True if the other vector is in the object's bounds
	 */
	public boolean isColliding(Vector2D other) {
		switch (shape) {
			case SQUARE:
				return !(
					(other.x < pos.x)
						|| (other.x > pos.x + width)
						|| (other.y < pos.y)
						|| (other.y > pos.y + height)
				);
			case CIRCLE:
//				Vector2D center = new Vector2D(width / 2, height / 2);
//				double a = other.x - center.x;
//				double b = other.y - center.y;
//				double rx = width / 2.0;
//				double ry = height / 2.0;
//				return (
//					((a * a) / (rx * rx))
//						+ ((b * b) / (ry * ry))
//						< width / 2
//				);
				return (
					Vector2D.dist(
						Vector2D.add(
							pos, new Vector2D(width / 2, width / 2)
						),
						other)
						< width / 2
				);
		}
		return false;
	}
	
	public enum Shape {
		SQUARE, CIRCLE
	}
}
