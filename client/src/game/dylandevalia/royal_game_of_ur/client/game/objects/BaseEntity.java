package game.dylandevalia.royal_game_of_ur.client.game.objects;

import game.dylandevalia.royal_game_of_ur.utility.Vector2D;

import java.awt.*;

public class BaseEntity {
    protected Vector2D pos = new Vector2D();
    private Vector2D lastPos = new Vector2D();
    protected Vector2D drawPos = new Vector2D();
    
    protected void update() {
        this.lastPos = this.pos;
    }

    protected void draw(Graphics2D g2a, double interpolate) {
        calculateDrawPos(interpolate);
    }

    private void calculateDrawPos(double interpolate) {
        drawPos.x = ((pos.x - lastPos.x) * interpolate + lastPos.x);
        drawPos.y = ((pos.y - lastPos.y) * interpolate + lastPos.y);
    }
}
