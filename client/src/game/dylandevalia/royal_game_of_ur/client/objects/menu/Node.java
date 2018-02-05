package game.dylandevalia.royal_game_of_ur.client.objects.menu;

import game.dylandevalia.royal_game_of_ur.client.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.client.gui.Window;
import game.dylandevalia.royal_game_of_ur.client.objects.base.BaseEntity;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;

public class Node extends BaseEntity {
	
	private Vector2D vel;
	
	public Node(int x, int y) {
		super(x, y, Utility.randBetween(0, 5));
		double maxSpeed = 0.5;
		vel = new Vector2D(
			Utility.randBetween(-maxSpeed, maxSpeed),
			Utility.randBetween(-maxSpeed, maxSpeed)
		);
	}
	
	@Override
	public void update() {
		super.update();
		
		pos.add(vel);
		
		if (pos.x < -200) {
			pos.x += Window.WIDTH + 399;
		} else if (pos.x > Window.WIDTH + 200) {
			pos.x -= Window.WIDTH + 399;
		}
		
		if (pos.y < -200) {
			pos.y += Window.HEIGHT + 399;
		} else if (pos.y > Window.HEIGHT + 200) {
			pos.y -= Window.HEIGHT + 399;
		}
	}
	
	public void draw(Graphics2D g, double interpolate, Node[] nodes, int i) {
		super.draw(g, interpolate);
		
		Color c = ColorMaterial.INDIGO[3];
		int noConnections = 0;
		// Check all nodes after this node's index
		for (int j = i + 1; j < nodes.length; j++) {
			Node n = nodes[j];
			
			double dist = Vector2D.dist(pos, n.getPos());
			
			if (dist < 200) {
				noConnections++;
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
		
		c = ColorMaterial.INDIGO[1];
		g.setColor(
			new Color(
				c.getRed(), c.getGreen(), c.getBlue(),
				(int) Utility.map(noConnections, 0, nodes.length, 150, 250)
			)
		);
		g.fillOval(
			(int) drawPos.x, (int) drawPos.y,
			width, height
		);
	}
}
