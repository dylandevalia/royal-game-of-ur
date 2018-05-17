package game.dylandevalia.royal_game_of_ur.objects.base;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;

public class Background {
	
	private Node[] nodes;
	private Color[] colors, oldColors;
	private double fadeRatio = 1;
	
	public Background(Color[] colors, Node[] nodes) {
		this.nodes = nodes;
		this.colors = colors;
	}
	
	public Background(Color[] colors) {
		this.colors = colors;
		this.oldColors = colors;
		nodes = new Node[(int) Utility.mapWidth(150, 300)];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(
				Utility.randBetween(-200, Window.WIDTH + 200),
				Utility.randBetween(-200, Window.HEIGHT + 200)
			);
		}
	}
	
	public void update() {
		for (Node node : nodes) {
			node.update();
		}
	}
	
	public void draw(Graphics2D g, double interpolate) {
		drawGradient(g);
		
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].draw(g, interpolate, nodes, i);
		}
	}
	
	private void drawGradient(Graphics2D g) {
		fadeRatio = Utility.clamp(fadeRatio += 0.05, 0.0, 1.0);
		
		Paint oldPaint = g.getPaint();
		GradientPaint gradientPaint;
		int brightShade = 3;
		int darkShade = 9;
		int gradientBoundary = 100;
		
		if (fadeRatio < 1) {
			Color bright = lerpColor(fadeRatio, colors[brightShade], oldColors[brightShade]);
			Color dark = lerpColor(fadeRatio, colors[darkShade], oldColors[darkShade]);
			
			gradientPaint = new GradientPaint(
				-gradientBoundary, -gradientBoundary,
				bright,
				Window.WIDTH + gradientBoundary, Window.HEIGHT + gradientBoundary,
				dark
			);
		} else {
			gradientPaint = new GradientPaint(
				-gradientBoundary, -gradientBoundary,
				colors[brightShade],
				Window.WIDTH + gradientBoundary, Window.HEIGHT + gradientBoundary,
				colors[darkShade]
			);
		}
		
		g.setPaint(gradientPaint);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		g.setPaint(oldPaint);
	}
	
	private static Color lerpColor(double ratio, Color c1, Color c2) {
		return new Color(
			(int) Utility.lerp(
				ratio,
				c1.getRed(),
				c2.getRed()
			),
			(int) Utility.lerp(
				ratio,
				c1.getGreen(),
				c2.getGreen()
			),
			(int) Utility.lerp(
				ratio,
				c1.getBlue(),
				c2.getBlue()
			)
		);
	}
	
	public Node[] getNodes() {
		return nodes;
	}
	
	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}
	
	public void setColors(Color[] colors, boolean fade) {
		this.oldColors = this.colors;
		this.colors = colors;
		if (fade) {
			fadeRatio = 0;
		}
	}
	
	/**
	 * A node which draws lines to other nodes in range. Wraps around the screen if it goes off the
	 * edge
	 */
	public class Node extends BaseEntity {
		
		/** The velocity that the node travels at */
		private Vector2D vel;
		
		/**
		 * Creates a node at the given position
		 *
		 * @param x The x coordinate of the node's initial position
		 * @param y The y coordinate of the node's initial position
		 */
		Node(int x, int y) {
			super(x, y, Utility.randBetween(0, 5));
			
			// Random velocity
			double maxSpeed = 0.5;
			vel = new Vector2D(
				Utility.randBetween(-maxSpeed, maxSpeed),
				Utility.randBetween(-maxSpeed, maxSpeed)
			);
		}
		
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
			
			Color c = ColorMaterial.GREY[3];
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
						ColorMaterial.withAlpha(
							c,
							(int) Utility.map(dist, 0, 200, 127, 0)
						)
					);
					g.drawLine(
						(int) pos.x + width / 2, (int) pos.y + height / 2,
						(int) n.getPos().x, (int) n.getPos().y
					);
				}
			}
			
			// More opaque the more connections
			c = ColorMaterial.GREY[1];
			g.setColor(
				ColorMaterial.withAlpha(
					c,
					(int) Utility.map(noConnections, 0, nodes.length, 100, 200)
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
}
