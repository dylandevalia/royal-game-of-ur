package game.dylandevalia.royal_game_of_ur.objects.base.buttons;

import game.dylandevalia.royal_game_of_ur.utility.ICallback;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Button that has text in it. Size is determined by size of text and font
 * as well as {@link #paddingX} and {@link #paddingY} which adds area around
 * the text
 */
public class TextButton extends AbstractButton {
	
	/** The message that the button will show */
	private String message;
	/** The font that the button should use */
	private Font font;
	/** The {@link Alignment} of the button */
	private Alignment alignment;
	/** The padding around the text */
	private int paddingX, paddingY;
	
	/** The colours that the button will turn for each state */
	private Color baseColor, hoverColor, inactiveColor, textColor;
	
	/** The callback interface called when the button is pressed */
	private ICallback callback;
	
	/** Is the button currently active */
	private boolean active = true;
	
	/**
	 * @param x             The x coordinate of the centre where the button should be placed
	 * @param y             The y coordinate of the centre where the button should be placed
	 * @param paddingX      Padding to add left and right of the text
	 * @param paddingY      Padding to add top and bottom of the text
	 * @param font          The font that the button should use
	 * @param alignment     The alignment of the button
	 * @param message       The text message that the button should read
	 * @param baseColor     The colour that the button should be under normal conditions
	 * @param hoverColor    The colour that the button should be when the mouse is hovering over it
	 * @param inactiveColor The colour that the button should be when inactive
	 * @param textColor     The colour that the message should be
	 */
	public TextButton(
		int x, int y, int paddingX, int paddingY,
		Font font,
		Alignment alignment,
		String message,
		Color baseColor, Color hoverColor, Color inactiveColor,
		Color textColor
	) {
		super(x, y, -1, -1, Shape.RECTANGLE);
		
		this.message = message;
		
		this.paddingX = paddingX;
		this.paddingY = paddingY;
		
		this.font = font;
		this.alignment = alignment;
		
		this.baseColor = baseColor;
		this.hoverColor = hoverColor;
		this.inactiveColor = inactiveColor;
		this.textColor = textColor;
	}
	
	/**
	 * Used to set the callback method that the button will execute
	 * when pressed
	 *
	 * @param callback The callback method
	 */
	public void setOnClickListener(ICallback callback) {
		this.callback = callback;
	}
	
	/**
	 * On the first tick the button won't have a width or height as
	 * the {@link Graphics2D} object is needed to calculate the text dimensions
	 * so just return on the first tick
	 *
	 * @param mousePos The position of the mouse
	 */
	public void update(Vector2D mousePos) {
		if (width < 0 || height < 0) {
			return;
		}
		
		super.update(mousePos);
		isMouseHovering = isColliding(mousePos);
	}
	
	/**
	 * Calculates the text message dimensions and the draws the button
	 */
	public void draw(Graphics2D g, double interpolate) {
		super.draw(g, interpolate);
		
		width = g.getFontMetrics(font).stringWidth(message);
		height = g.getFontMetrics(font).getHeight();
		
		// Sets colour depending on state (eg. hovering, normal, in-active)
		g.setColor(
			active ? (isMouseHovering ? hoverColor : baseColor)
				: inactiveColor
		);
		
		int x = getLeftEdge((int) drawPos.x);
		int y = getTopEdge((int) drawPos.y);
		
		g.fillRect(x, y, width + (paddingX * 2), height + (paddingY * 2));
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
	
	/**
	 * Calculates if one position vector is colliding with the button
	 *
	 * @param other The other position
	 * @return True if the other position vector is colliding with the button
	 */
	@Override
	public boolean isColliding(Vector2D other) {
		int x = getLeftEdge((int) pos.x);
		int y = getTopEdge((int) pos.y);
		
		return (other.x > x && other.x < x + width + (paddingX * 2))
			&& (other.y > y && other.y < y + height + (paddingY * 2));
	}
	
	/**
	 * Used to calculate the left edge of the button depending on its
	 * {@link #alignment}
	 *
	 * @param x The x coordinate where the button should be positioned
	 * @return The x coordinate where the button should be drawn
	 */
	private int getLeftEdge(int x) {
		switch (alignment) {
			case LEFT:
				break;
			case CENTER:
				x -= (width / 2) + paddingX;
				break;
			case RIGHT:
				x -= width + paddingX * 2;
				break;
		}
		return x;
	}
	
	/**
	 * Calculates the top of the button based on the text size
	 *
	 * @param y The y coordinate where the button should be positioned
	 * @return The y coordinate where the button should be drawn
	 */
	private int getTopEdge(int y) {
		return y - (height / 2) - paddingY;
	}
	
	/**
	 * Draw a String centered in the middle of a Rectangle
	 *
	 * @param g The Graphics instance
	 */
	private void drawCenteredString(Graphics2D g) {
		// Determine the X coordinate for the text
		int x = (int) drawPos.x;
		switch (alignment) {
			case LEFT:
				x += paddingX;
				break;
			case CENTER:
				x -= width / 2;
				break;
			case RIGHT:
				x -= width + paddingX;
				break;
		}
		
		// Determine the Y coordinate for the text
		int y = (int) drawPos.y + (height / 3);
		
		// Set the font
		g.setFont(font);
		// Draw the String
		g.drawString(message, x, y);
	}
	
	/**
	 * Used to determine if the button should be drawn left or middle or right of
	 * the given position
	 */
	public enum Alignment {
		LEFT, CENTER, RIGHT
	}
}
