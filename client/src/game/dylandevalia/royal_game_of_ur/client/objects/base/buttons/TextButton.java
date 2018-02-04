package game.dylandevalia.royal_game_of_ur.client.objects.base.buttons;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class TextButton extends AbstractButton {
	
	/** The colour of the button if inactive */
	private static final Color inactiveColor = ColorMaterial.blueGrey;
	
	/** The message that the button will show */
	private String message;
	
	/** The colours that the button will turn for each state */
	private Color baseColor, hoverColor, textColor;
	
	/** The callback interface */
	private ButtonCallback callback;
	
	/** Is the button currently active */
	private boolean active = true;
	
	public TextButton(
		int x, int y, int width, int height,
		String message,
		Color baseColor, Color hoverColor, Color textColor
	) {
		super(x, y, width, height, Shape.RECTANGLE);
		
		this.message = message;
		
		this.baseColor = baseColor;
		this.hoverColor = hoverColor;
		this.textColor = textColor;
	}
	
	public void setOnClickListener(ButtonCallback callback) {
		this.callback = callback;
	}
	
	public void update(Vector2D mousePos) {
		super.update(mousePos);
	}
	
	public void draw(Graphics2D g, double interpolate) {
		super.draw(g, interpolate);
		
		// Sets colour depending on state (eg. hovering, normal, in-active)
		g.setColor(
			active ? (isMouseHovering ? hoverColor : baseColor)
				: inactiveColor
		);
		g.fillRect((int) drawPos.x, (int) drawPos.y, width, height);
		g.setColor(textColor);
		drawCenteredString(g, message, new Font("TimesRoman", Font.BOLD, 28));
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
	 * Draw a String centered in the middle of a Rectangle
	 *
	 * @param g    The Graphics instance
	 * @param text The String to draw
	 * @param font The Font to use
	 */
	private void drawCenteredString(Graphics2D g, String text, Font font) {
		// Get the FontMetrics
		FontMetrics metrics = g.getFontMetrics(font);
		// Determine the X coordinate for the text
		int x = (int) pos.x + (width - metrics.stringWidth(text)) / 2;
		// Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
		int y = (int) pos.y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
		// Set the font
		g.setFont(font);
		// Draw the String
		g.drawString(text, x, y);
	}
	
	/**
	 * Interface which defines callback method
	 */
	public interface ButtonCallback {
		
		void callback();
	}
}
