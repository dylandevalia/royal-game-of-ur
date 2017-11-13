package game.dylandevalia.royal_game_of_ur.client.game.objects;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;

import java.awt.*;

public class Tile extends BaseEntity {
	private boolean rosette = false;
	
	public Tile(int x, int y) {
		super(x, y, Window.WIDTH / 10, Window.WIDTH / 10);
	}
	
	public Vector2D getMidPos(Counter counter) {
		return new Vector2D(pos.x + width / 2 - counter.width / 2, pos.y + height / 2 - counter.height / 2);
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
		
		if (rosette) {
			g.setColor(ColorMaterial.blue);
		} else {
			g.setColor(ColorMaterial.red);
		}
		g.fillRect((int) drawPos.x + boarderWidth, (int) drawPos.y + boarderWidth, width - boarderWidth * 2,
				height - boarderWidth * 2
		);
	}
}
