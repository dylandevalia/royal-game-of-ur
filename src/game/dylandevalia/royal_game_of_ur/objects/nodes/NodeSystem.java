package game.dylandevalia.royal_game_of_ur.objects.nodes;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Framework;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.nodes.QuadTree.Circle;
import game.dylandevalia.royal_game_of_ur.objects.nodes.QuadTree.Point;
import game.dylandevalia.royal_game_of_ur.objects.nodes.QuadTree.Rectangle;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class NodeSystem {
	
	private Node[] nodes;
	private QuadTree qtree;
	
	public NodeSystem() {
		nodes = new Node[(int) Utility.mapWidth(150, 300)];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(
				Utility.randBetween(-200, Window.WIDTH + 200),
				Utility.randBetween(-200, Window.HEIGHT + 200)
			);
		}
	}
	
	public void update() {
		qtree = new QuadTree(new Rectangle(
			Window.WIDTH / 2, Window.HEIGHT / 2,
			Window.WIDTH / 2 + 200, Window.HEIGHT / 2 + 200
		));
		
		for (Node n : nodes) {
			n.update();
			
			// Add nodes to quadtree
			Vector2D pos = n.getPos();
			qtree.insert(new Point(pos.x, pos.y, n));
		}
	}
	
	public void draw(Graphics2D g, double interpolate, boolean connectMouse) {
		for (Node n : nodes) {
			n.draw(g, interpolate, qtree, nodes.length);
		}
		
		if (connectMouse) {
			Vector2D mousePos = Framework.getMousePos();
			ArrayList<Point> points = qtree.query(new Circle(
				(int) mousePos.x, (int) mousePos.y, 300)
			);
			
			for (Point p : points) {
				Vector2D nPos = ((Node) p.getUserData()).getPos();
				
				double dist = Vector2D.dist(mousePos, nPos);
				g.setColor(
					ColorMaterial.withAlpha(
						ColorMaterial.INDIGO[2],
						(int) Utility.map(dist, 0, 300, 255, 0)
					)
				);
				g.drawLine(
					(int) mousePos.x, (int) mousePos.y,
					(int) nPos.x, (int) nPos.y
				);
			}
		}
		
		// qtree.draw(g);
		// g.drawOval((int) mousePos.x - 300, (int) mousePos.y - 300, 600, 600);
	}
}
