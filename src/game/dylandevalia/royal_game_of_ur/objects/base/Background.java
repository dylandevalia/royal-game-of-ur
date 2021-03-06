package game.dylandevalia.royal_game_of_ur.objects.base;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;

/**
 * Draws a gradient background using a set of colours from the {@link ColorMaterial} library with a
 * lighter shade in the top left and darker in the bottom right. Also has nodes which glide and
 * connect with each other creating a network-styled effect
 */
public class Background {
	
	/** The array of nodes */
	private Node[] nodes;
	
	/** The colour of the background */
	private Color[] colors;
	
	/** The previous colour if changed - used for fade */
	private Color[] oldColors;
	
	/** Controls the fade between colours */
	private double fadeRatio = 1;
	
	/**
	 * Creates a background with a gradient with the given {@link ColorMaterial} {@link Color} array
	 * Uses the {@link Node} array given
	 *
	 * @param colors The {@link ColorMaterial} colour array
	 * @param nodes  The array of nodes for the background
	 */
	public Background(Color[] colors, Node[] nodes) {
		this.nodes = nodes;
		this.colors = colors;
	}
	
	/**
	 * Creates a background with a gradient with the given {@link ColorMaterial} {@link Color} array
	 * Generates a new array of nodes
	 *
	 * @param colors The {@link ColorMaterial} colour array
	 */
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
	
	/**
	 * Updates the nodes. Should be called in the state's update() method
	 */
	public void update() {
		for (Node node : nodes) {
			node.update();
		}
	}
	
	/**
	 * Draws the background. Should be called in the states's draw(Graphics2D, double) method
	 *
	 * @param g           The graphics object used to draw to the canvas
	 * @param interpolate The interpolation value passed in from the game engine
	 */
	public void draw(Graphics2D g, double interpolate) {
		drawGradient(g);
		
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].draw(g, interpolate, nodes, i);
		}
	}
	
	/**
	 * Draws the gradient the size of the window. If the colour has changed, fades to the new
	 * colours
	 *
	 * @param g The graphics object used to draw to the canvas
	 */
	private void drawGradient(Graphics2D g) {
		// Increment and fade ratio
		fadeRatio = Utility.clamp(fadeRatio += 0.05, 0.0, 1.0);
		
		// Set graphics paint type
		Paint oldPaint = g.getPaint();
		GradientPaint gradientPaint;
		
		// Constants
		final int brightShade = 3;
		final int darkShade = 9;
		final int gradientBoundary = 100;
		
		if (fadeRatio < 1) {
			// If currently fading
			
			Color bright = Utility.lerp(fadeRatio, colors[brightShade], oldColors[brightShade]);
			Color dark = Utility.lerp(fadeRatio, colors[darkShade], oldColors[darkShade]);
			
			gradientPaint = new GradientPaint(
				-gradientBoundary, -gradientBoundary,
				bright,
				Window.WIDTH + gradientBoundary, Window.HEIGHT + gradientBoundary,
				dark
			);
		} else {
			// Not fading
			
			gradientPaint = new GradientPaint(
				-gradientBoundary, -gradientBoundary,
				colors[brightShade],
				Window.WIDTH + gradientBoundary, Window.HEIGHT + gradientBoundary,
				colors[darkShade]
			);
		}
		
		// Set paint, draw, and finally set old paint back
		g.setPaint(gradientPaint);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		g.setPaint(oldPaint);
	}
	
	
	/* Getter and Setters */
	
	public Node[] getNodes() {
		return nodes;
	}
	
	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}
	
	public void setColors(Color[] colors, boolean fade) {
		this.oldColors = this.colors;
		this.colors = colors;
		
		// If fading between colours, set ratio to 0
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
