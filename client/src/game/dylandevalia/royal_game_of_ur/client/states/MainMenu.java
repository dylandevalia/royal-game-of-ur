package game.dylandevalia.royal_game_of_ur.client.states;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MainMenu implements State {
	
	Box box;
	
	@Override
	public void initialise() {
		box = new Box(20,20);
	}
	
	@Override
	public void update() {
		box.update();
	}
	
	@Override
	public void draw(Graphics2D g2d, double interpolate) {
		g2d.setBackground(Color.CYAN);
		
		box.draw(g2d, interpolate);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	
	}
	
	private class Box {
		private int x, y;
		private int old_x, old_y;
		
		Box(int x, int y) {
			this.x = x;
			this.y = y;
			this.old_x = this.x;
			this.old_y = this.y;
		}
		
		void update() {
			this.old_x = this.x;
			this.old_y = this.y;
			
			this.x += 5;
			this.y += 5;
		}
		
		void draw(Graphics2D g2d, double interpolate) {
			int drawX = (int) ((x - old_x) * interpolate + old_x);
			int drawY = (int) ((y - old_y) * interpolate + old_y);
			
			g2d.setColor(Color.MAGENTA);
			g2d.drawRect(drawX, drawY , 20, 20);
		}
	}
}
