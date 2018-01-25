package game.dylandevalia.royal_game_of_ur.client.game.entities.buttons;

import game.dylandevalia.royal_game_of_ur.client.game.entities.BaseEntity;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;

public abstract class AbstractButton extends BaseEntity {
	
	/** Is the mouse hovering over this button */
	protected boolean isMouseHovering;
	
	/**
	 * The shape pattern of the object
	 *
	 * @see Shape
	 */
	private Shape shape;
	
	public AbstractButton(int x, int y, int width, int height, Shape shape) {
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
			case RECTANGLE:
				return !(
					(other.x < pos.x)
						|| (other.x > pos.x + width)
						|| (other.y < pos.y)
						|| (other.y > pos.y + height)
				);
			case CIRCLE:
				// True if the distance between the middle of the object and
				// 'other' is less than the radius
				return (
					Vector2D.dist(
						Vector2D.add(
							pos, new Vector2D(width / 2, width / 2)
						),
						other
					) < width / 2
				);
		}
		return false;
	}
	
	/**
	 * Enum to define the shape of the button
	 */
	public enum Shape {
		RECTANGLE, CIRCLE
	}
}
