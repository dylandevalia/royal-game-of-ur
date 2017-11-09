package game.dylandevalia.royal_game_of_ur.client.game.objects;

import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;

import java.awt.*;

public class BaseEntity {
    protected Vector2D pos = new Vector2D();
    private Vector2D lastPos = new Vector2D();
    protected Vector2D drawPos = new Vector2D();
    protected double width, height;
    protected boolean onScreen;
    
    protected void update() {
        this.lastPos = this.pos;
    }

    protected void draw(Graphics2D g2a, double interpolate) {
        calculateDrawPos(interpolate);
	    isOnScreen();
    }

    private void calculateDrawPos(double interpolate) {
        drawPos.x = ((pos.x - lastPos.x) * interpolate + lastPos.x);
        drawPos.y = ((pos.y - lastPos.y) * interpolate + lastPos.y);
    }
    
    private void isOnScreen() {
	    onScreen =
			    !(drawPos.x + width < 0 || drawPos.x > Window.WIDTH
				|| drawPos.y + height < 0 || drawPos.y > Window.HEIGHT);
    }
}
