package game.dylandevalia.royal_game_of_ur.objects.nodes;

import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class QuadTree {
	
	public static class Point extends Vector2D {
		
		Object userData;
		
		Point(double x, double y, Object userData) {
			super(x, y);
			this.userData = userData;
		}
		
		Object getUserData() {
			return userData;
		}
	}
	
	private abstract static class Shape {
		
		int x, y, w, h;
		
		abstract boolean contains(Point point);
		
		abstract boolean intersects(Shape range);
	}
	
	public static class Rectangle extends Shape {
		
		Rectangle(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
		
		public boolean contains(Point point) {
			return point.x >= x - w
				&& point.x <= x + w
				&& point.y >= y - h
				&& point.y <= y + h;
		}
		
		public boolean intersects(Shape range) {
			return !(range.x - range.w > x + w
				|| range.x + range.w < x - w
				|| range.y - range.h > y + h
				|| range.y + range.h < y - h);
		}
	}
	
	public static class Circle extends Shape {
		
		private int rSquared;
		
		Circle(int x, int y, int r) {
			this.x = x;
			this.y = y;
			this.w = r;
			this.h = r;
			this.rSquared = r * r;
		}
		
		boolean contains(Point point) {
			if (point == null) {
				Log.debug("QUADTREE", "Circle -> contains() -> point == null");
				return false;
			}
			
			double d = Math.pow((point.x - x), 2) + Math.pow((point.y - y), 2);
			return d <= rSquared;
		}
		
		boolean intersects(Shape range) {
			int xDist = Math.abs(range.x - x);
			int yDist = Math.abs(range.y - y);
			
			int r = w; // Could use this.h too
			
			// No intersection
			if (xDist > (r + range.w) || yDist > r + range.h) {
				return false;
			}
			
			// Intersects within circle
			if (xDist <= range.w || yDist <= range.h) {
				return true;
			}
			
			// Intersects on the edge of the circle
			double edges = Math.pow(xDist - range.w, 2) + Math.pow(yDist - range.h, 2);
			return edges <= this.rSquared;
		}
	}
	
	
	private static final int NODE_CAPACITY = 4;
	private Rectangle boundary;
	private ArrayList<Point> points = new ArrayList<>();
	private boolean subdivided = false;
	
	private QuadTree northEast = null;
	private QuadTree southEast = null;
	private QuadTree southWest = null;
	private QuadTree northWest = null;
	
	QuadTree(Rectangle boundary) {
		this.boundary = boundary;
	}
	
	boolean insert(Point point) {
		// If point is not in boundary
		if (!boundary.contains(point)) {
			return false;
		}
		
		if (points.size() < NODE_CAPACITY) {
			points.add(point);
			return true;
		}
		
		// If not subdivided
		if (northEast == null) {
			subdivide();
		}
		
		if (northEast.insert(point)) {
			return true;
		}
		if (southEast.insert(point)) {
			return true;
		}
		if (southWest.insert(point)) {
			return true;
		}
		return northWest.insert(point);
		
	}
	
	private void subdivide() {
		int x = boundary.x;
		int y = boundary.y;
		int w = boundary.w / 2;
		int h = boundary.h / 2;
		
		Rectangle ne = new Rectangle(x + w, y - h, w, h);
		northEast = new QuadTree(ne);
		Rectangle se = new Rectangle(x + w, y + h, w, h);
		southEast = new QuadTree(se);
		Rectangle sw = new Rectangle(x - w, y + h, w, h);
		southWest = new QuadTree(sw);
		Rectangle nw = new Rectangle(x - w, y - h, w, h);
		northWest = new QuadTree(nw);
		
		subdivided = true;
	}
	
	ArrayList<Point> query(Shape range) {
		return query(range, new ArrayList<>());
	}
	
	private ArrayList<Point> query(Shape range, ArrayList<Point> found) {
		if (!range.intersects(boundary)) {
			return found;
		}
		
		// Search through points that this boundary holds
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			if (range.contains(p)) {
				found.add(p);
			}
		}
		
		if (subdivided) {
			found = northEast.query(range, found);
			found = southEast.query(range, found);
			found = southWest.query(range, found);
			found = northWest.query(range, found);
		}
		
		return found;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.drawRect(
			boundary.x - boundary.w, boundary.y - boundary.h,
			boundary.w * 2, boundary.h * 2
		);
		
		if (northEast != null) {
			northEast.draw(g);
			southEast.draw(g);
			southWest.draw(g);
			northWest.draw(g);
		}
	}
}
