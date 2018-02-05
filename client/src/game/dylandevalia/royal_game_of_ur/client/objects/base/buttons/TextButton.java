package game.dylandevalia.royal_game_of_ur.client.objects.base.buttons;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class TextButton extends AbstractButton {
	
	/** The colour of the button if inactive */
	private static final Color inactiveColor = ColorMaterial.blueGrey;
	
	/** The message that the button will show */
	private String message;
	
	private Font font;
	
	private Alignment alignment;
	
	private int boundsX, boundsY;
	
	/** The colours that the button will turn for each state */
	private Color baseColor, hoverColor, textColor;
	
	/** The callback interface */
	private ButtonCallback callback;
	
	/** Is the button currently active */
	private boolean active = true;
	
	public TextButton(
		int x, int y, int boundsX, int boundsY,
		Font font,
		Alignment alignment,
		String message,
		Color baseColor, Color hoverColor, Color textColor
	) {
		super(x, y, -1, -1, Shape.RECTANGLE);
		
		this.message = message;
		
		this.boundsX = boundsX;
		this.boundsY = boundsY;
		
		this.font = font;
		this.alignment = alignment;
		
		this.baseColor = baseColor;
		this.hoverColor = hoverColor;
		this.textColor = textColor;
	}
	
	public void setOnClickListener(ButtonCallback callback) {
		this.callback = callback;
	}
	
	public void update(Vector2D mousePos) {
		if (width < 0 || height < 0) {
			return;
		}
		super.update(mousePos);
		isMouseHovering = isColliding(mousePos);
	}
	
	public void draw(Graphics2D g, double interpolate) {
		super.draw(g, interpolate);
		
		if (width < 0) {
			width = g.getFontMetrics(font).stringWidth(message);
		}
		if (height < 0) {
			height = g.getFontMetrics(font).getHeight();
		}
		
		// Sets colour depending on state (eg. hovering, normal, in-active)
		g.setColor(
			active ? (isMouseHovering ? hoverColor : baseColor)
				: inactiveColor
		);
		
		int x = getLeftEdge((int) drawPos.x);
		int y = getTopEdge((int) drawPos.y);
		
		g.fillRect(x, y, width + (boundsX * 2), height + (boundsY * 2));
		g.setColor(textColor);
		
		drawCenteredString(g);
	}
	
	/**
	 * Call this function when the button is pressed
	 * Runs callback function
	 */
	public void press() {
		if (active) {
			callback.callback();
		}
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public boolean isColliding(Vector2D other) {
		int x = getLeftEdge((int) pos.x);
		int y = getTopEdge((int) pos.y);
		
		return (other.x > x && other.x < x + width + (boundsX * 2))
			&& (other.y > y && other.y < y + height + (boundsY * 2));
	}
	
	private int getLeftEdge(int x) {
		switch (alignment) {
			case LEFT:
				break;
			case CENTER:
				x -= (width / 2) + boundsX;
				break;
			case RIGHT:
				x -= width + boundsX * 2;
				break;
		}
		return x;
	}
	
	private int getTopEdge(int y) {
		return y - (height / 2) - boundsY;
	}
	
	/**
	 * Draw a String centered in the middle of a Rectangle
	 *
	 * @param g    The Graphics instance
	 */
	private void drawCenteredString(Graphics2D g) {
		// Determine the X coordinate for the text
		int x = (int) drawPos.x;
		switch (alignment) {
			case LEFT:
				x += boundsX;
				break;
			case CENTER:
				x -= width / 2;
				break;
			case RIGHT:
				x -= width + boundsX;
				break;
		}
		
		// Determine the Y coordinate for the text
		int y = (int) drawPos.y + (height / 3);
		
		// Set the font
		g.setFont(font);
		// Draw the String
		g.drawString(message, x, y);
	}
	
	public enum Alignment {
		LEFT, CENTER, RIGHT
	}
	
	/**
	 * Interface which defines callback method
	 */
	public interface ButtonCallback {
		
		void callback();
	}
}
