package game.dylandevalia.royal_game_of_ur.client.game.objects;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;

import java.awt.*;

public class Tile extends BaseEntity {
	public static final int WIDTH = Window.WIDTH / 10;
	
	private boolean rosette = false;
	
	public Tile(int x, int y) {
		super(x, y, WIDTH, WIDTH);
	}
	
	public boolean isRosette() {
		return rosette;
	}
	
	public void setRosette(boolean rosette) {
		this.rosette = rosette;
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
