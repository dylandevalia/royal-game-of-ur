package game.dylandevalia.royal_game_of_ur.objects.ur;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.base.BaseEntity;
import java.awt.Graphics2D;

public class Tile extends BaseEntity {
	
	public static int WIDTH = Window.WIDTH / 10;
	
	/** If the tile is a rosette tile */
	private boolean rosette = false;
	
	/** Reference to the counter on the tile */
	private Counter counter = null;
	
	public Tile(int x, int y) {
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
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void draw(Graphics2D g, double interpolate) {
		super.draw(g, interpolate);
		final int boarderWidth = 2;
		
		g.setColor(ColorMaterial.GREY[7]);
		g.fillRect((int) drawPos.x, (int) drawPos.y, width, height);
		
		g.setColor(rosette ? ColorMaterial.blue : ColorMaterial.red);
		g.fillRect(
			(int) drawPos.x + boarderWidth,
			(int) drawPos.y + boarderWidth,
			width - boarderWidth * 2,
			height - boarderWidth * 2
		);
	}
}
