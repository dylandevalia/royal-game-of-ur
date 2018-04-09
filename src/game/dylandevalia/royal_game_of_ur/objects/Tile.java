package game.dylandevalia.royal_game_of_ur.objects;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.ai.AIController.MoveState;
import game.dylandevalia.royal_game_of_ur.objects.base.BaseEntity;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * An individual tile that makes up the {@link Board}
 */
public class Tile extends BaseEntity {
	
	/** Colour used when mouse hovers over a counter with an allowed move */
	private static final Color hoverAllowed = ColorMaterial.withAlpha(ColorMaterial.GREY[2], 100);
	/** Colour used when mouse hovers over a counter with a blocked move */
	private static final Color hoverBlocked = ColorMaterial.withAlpha(ColorMaterial.red, 100);
	/** Colour used when mouse hovers over a counter that will capture another counter */
	private static final Color hoverCapture = ColorMaterial.withAlpha(ColorMaterial.blue, 100);
	/** Colour used when mouse hovers over a counter that will move off the board */
	private static final Color hoverEnd = ColorMaterial.withAlpha(ColorMaterial.green, 100);
	
	/** The width of the tile */
	public static int WIDTH = Window.WIDTH / 10;
	
	/** If the tile is a rosette tile */
	private boolean rosette = false;
	
	/** Reference to the counter on the tile */
	private Counter counter = null;
	
	/** The current MoveState used to inform if a hover effect should be applied */
	private MoveState hoverMode = null;
	
	Tile(int x, int y) {
		super(x, y, WIDTH, WIDTH);
	}
	
	public boolean isRosette() {
		return rosette;
	}
	
	public void setRosette(boolean rosette) {
		this.rosette = rosette;
	}
	
	public boolean hasCounter() {
		return counter != null;
	}
	
	public Counter getCounter() {
		return counter;
	}
	
	public void setCounter(Counter counter) {
		this.counter = counter;
	}
	
	public void setHoverMode(MoveState hoverMode) {
		this.hoverMode = hoverMode;
	}
	
	@Override
	public void update() {
		super.update();
		hoverMode = null;
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		super.draw(g, interpolate);
		
		g.setColor(ColorMaterial.BROWN[9]);
		g.fillRect((int) drawPos.x, (int) drawPos.y, width, height);
		
		if (rosette) {
			g.setColor(ColorMaterial.BROWN[6]);
		} else {
			g.setColor(ColorMaterial.BROWN[3]);
		}
		
		drawInnerTile(g);
		
		// If no hover effect needed, return
		if (hoverMode == null) {
			return;
		}
		
		switch (hoverMode) {
			case EMPTY:
				g.setColor(hoverAllowed);
				drawInnerTile(g);
				break;
			case END:
				g.setColor(hoverEnd);
				drawInnerTile(g);
				break;
			case BLOCKED:
				g.setColor(hoverBlocked);
				drawInnerTile(g);
				break;
			case CAPTURE:
				g.setColor(hoverCapture);
				drawInnerTile(g);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Draws the tile without the border
	 */
	private void drawInnerTile(Graphics2D g) {
		final int boarderWidth = (int) Utility.mapWidth(1, 2);
		g.fillRect(
			(int) drawPos.x + boarderWidth,
			(int) drawPos.y + boarderWidth,
			width - boarderWidth * 2,
			height - boarderWidth * 2
		);
	}
}
