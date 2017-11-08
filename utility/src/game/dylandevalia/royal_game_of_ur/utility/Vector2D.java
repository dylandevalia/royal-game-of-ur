package game.dylandevalia.royal_game_of_ur.utility;

public class Vector2D {
	public double x, y;
	
	public Vector2D(double x, double y) {
		set(x, y);
	}
	public Vector2D() {
		this(0, 0);
	}
	
	public static final Vector2D ZERO = new Vector2D(0, 0);
	public static final Vector2D UNIT = new Vector2D(1, 1);
	public static final Vector2D UP = new Vector2D(0, -1);
	public static final Vector2D DOWN = new Vector2D(0, 1);
	public static final Vector2D LEFT = new Vector2D(-1, 0);
	public static final Vector2D RIGHT = new Vector2D(1, 0);
	
	@Override
	public String toString() {
		return "x: " + this.x + ", y: " + this.y;
	}
	
	public Vector2D set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	public Vector2D set(Vector2D other) {
		return set(other.x, other.y);
	}
	
	public Vector2D copy() {
		return new Vector2D(this.x, this.y);
	}
	
	public boolean equals(double x, double y) {
		return (this.x == x) && (this.y == y);
	}
	public boolean equals(Vector2D other) {
		return equals(other.x, other.y);
	}
	public static boolean equals(Vector2D a, Vector2D b) {
		return a.equals(b);
	}
	
	public Vector2D add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}
	public Vector2D add(Vector2D other) {
		return add(other.x, other.y);
	}
	public static Vector2D add(Vector2D a, Vector2D b) {
		return a.copy().add(b);
	}
	
	public Vector2D sub(double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	public Vector2D sub(Vector2D other) {
		return sub(other.x, other.y);
	}
	public static Vector2D sub(Vector2D a, Vector2D b) {
		return a.copy().sub(b);
	}
	
	public Vector2D mult(double n) {
		this.x *= n;
		this.y *= n;
		return this;
	}
	
	public Vector2D div(double n) {
		this.x /= n;
		this.y /= n;
		return this;
	}
	
	public double magSquared() {
		return (this.x * this.x + this.y * this.y);
	}
	public double mag() {
		return Math.sqrt(magSquared());
	}
	
	public double dot(double x, double y) {
		return (this.x * x) + (this.y * y);
	}
	public double dot(Vector2D other) {
		return dot(other.x, other.y);
	}
	public static double dot(Vector2D a, Vector2D b) {
		return a.copy().dot(b);
	}
	
	public double dist(Vector2D other) {
		return other.copy().sub(this).mag();
	}
	public static double dist(Vector2D a, Vector2D b) {
		return a.dist(b);
	}
	
	
	public Vector2D normalise() {
		return this.mag() == 0 ? this : this.div(this.mag());
	}
	
	public Vector2D limit(double max) {
		if (this.magSquared() > max*max) {
			this.normalise();
			this.mult(max);
		}
		return this;
	}
	
	public Vector2D setMag(double mag) {
		return this.normalise().mult(mag);
	}
	
	public double heading() {
		return Math.atan2(this.y, this.x);
	}
	
	public Vector2D rotate(double angle) {
		double newHeading = this.heading() + angle;
		double mag = this.mag();
		this.x = Math.cos(newHeading) * mag;
		this.y = Math.sin(newHeading) * mag;
		return this;
	}
	
	public Vector2D lerp(double x, double y, double amount) {
		this.x += (x - this.x) * amount;
		this.y += (y - this.y) * amount;
		return this;
	}
	public Vector2D lerp(Vector2D other, double amount) {
		return lerp(other.x, other.y, amount);
	}
}
