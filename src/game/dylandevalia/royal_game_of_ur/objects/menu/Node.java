package game.dylandevalia.royal_game_of_ur.objects.menu;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.base.BaseEntity;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;

public class Node extends BaseEntity {
	
	private Vector2D vel;
	
	public Node(int x, int y) {
		super(x, y, Utility.randBetween(0, 5));
		
		// Random velocity
		double maxSpeed = 0.5;
		vel = new Vector2D(
			Utility.randBetween(-maxSpeed, maxSpeed),
			Utility.randBetween(-maxSpeed, maxSpeed)
		);
	}
	
	@Override
	public void update() {
		super.update();
		
		// Update position
		pos.add(vel);
		
		// Wrap around screen
		int boundary = 200;
		if (pos.x < -boundary) {
			pos.x += Window.WIDTH + (boundary * 2);
		} else if (pos.x > Window.WIDTH + boundary) {
			pos.x -= Window.WIDTH + (boundary * 2);
		}
		
		if (pos.y < -boundary) {
			pos.y += Window.HEIGHT + (boundary * 2);
		} else if (pos.y > Window.HEIGHT + boundary) {
			pos.y -= Window.HEIGHT + (boundary * 2);
		}
	}
	
	public void draw(Graphics2D g, double interpolate, Node[] nodes, int i) {
		super.draw(g, interpolate);
		
		Color c = ColorMaterial.INDIGO[3];
		int noConnections = 0;
		// Check all nodes after this node's index
		for (int j = i + 1; j < nodes.length; j++) {
			Node n = nodes[j];
			
			// Get distance to
			double dist = Vector2D.dist(pos, n.getPos());
			if (dist < 200) {
				noConnections++;
				
				// Draw line more transparent the further away
				g.setColor(
					new Color(
						c.getRed(), c.getGreen(), c.getBlue(),
						(int) Utility.map(dist, 0, 200, 255, 0)
					)
				);
				g.drawLine(
					(int) pos.x + width / 2, (int) pos.y + height / 2,
					(int) n.getPos().x, (int) n.getPos().y
				);
			}
		}
		
		// More opaque the more connections
		c = ColorMaterial.INDIGO[1];
		g.setColor(
			new Color(
				c.getRed(), c.getGreen(), c.getBlue(),
				(int) Utility.map(noConnections, 0, nodes.length, 150, 250)
			)
		);
		// Wider the more connections -- edit: doesn't look all that great
		int w = width; // + noConnections;
		int h = height; // + noConnections;
		g.fillOval(
			(int) drawPos.x - (w / 2), (int) drawPos.y - (h / 2),
			w, h
		);
	}
}
