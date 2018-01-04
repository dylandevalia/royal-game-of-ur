package game.dylandevalia.royal_game_of_ur.client.game.entities.buttons;

import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;

public class TextButton extends AbstractButton {
	
	private String message;
	private Color baseColor, hoverColor;
	private ButtonCallback callback;
	
	public TextButton(
		int x, int y, int width, int height,
		String message,
		Color baseColor, Color hoverColor,
		ButtonCallback callback
	) {
		super(x, y, width, height, Shape.RECTANGLE);
		
		this.message = message;
		
		this.baseColor = baseColor;
		this.hoverColor = hoverColor;
		
		this.callback = callback;
	}
	
	public void update(Vector2D mousePos) {
		super.update(mousePos);
	}
	
	public void draw(Graphics2D g, double interpolate) {
		super.draw(g, interpolate);
		
		g.setColor(isMouseHovering ? hoverColor : baseColor);
		g.fillRect((int)drawPos.x, (int)drawPos.y, width, height);
	}
	
	public void press() {
		callback.callback();
	}
}



