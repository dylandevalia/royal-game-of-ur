package game.dylandevalia.royal_game_of_ur.utility;

/**
 * A 2 dimensional vector object.
 * Based on the <a href="https://p5js.org/">p5.js</a> and <a href="https://processing.org/">Processing</a> vector libraries
 */
public class Vector2D {
	
	/** The components of the vector */
	public double x, y;
	
	/**
	 * Create a vector with the initial given values
	 *
	 * @param x The x component
	 * @param y The y component
	 */
	public Vector2D(double x, double y) {
		set(x, y);
	}
	
	/**
	 * Create a vector at (0, 0)
	 */
	public Vector2D() {
		this(0, 0);
	}
	
	/**
	 * @return A vector at (0, 0)
	 */
	public static Vector2D ZERO() {
		return new Vector2D(0, 0);
	}
	
	/**
	 * @return A unit vector
	 */
	public static Vector2D UNIT() {
		return new Vector2D(1, 1);
	}
	
	/**
	 * @return A vector pointing up
	 */
	public static Vector2D UP() {
		return new Vector2D(0, -1);
	}
	
	/**
	 * @return A vector pointing down
	 */
	public static Vector2D DOWN() {
		return new Vector2D(0, 1);
	}
	
	/**
	 * @return A vector pointing left
	 */
	public static Vector2D LEFT() {
		return new Vector2D(-1, 0);
	}
	
	/**
	 * @return A vector pointing right
	 */
	public static Vector2D RIGHT() {
		return new Vector2D(1, 0);
	}
	
	/**
	 * Checks if two vectors are identical
	 *
	 * @param a The first vector
	 * @param b The second vector
	 * @return True if both vectors are the same
	 */
	public static boolean equals(Vector2D a, Vector2D b) {
		return a.equals(b);
	}
	
	/**
	 * Adds two vectors together
	 *
	 * @param a The first vector
	 * @param b The second vector
	 * @return a + b
	 */
	public static Vector2D add(Vector2D a, Vector2D b) {
		return a.copy().add(b);
	}
	
	/**
	 * Subtracts vector b from a
	 *
	 * @param a The first vector
	 * @param b The second vector
	 * @return b - a
	 */
	public static Vector2D sub(Vector2D a, Vector2D b) {
		return a.copy().sub(b);
	}
	
	/**
	 * Preforms a dot product on the two given vectors
	 *
	 * @param a The first vector
	 * @param b The second vector
	 * @return a . b
	 */
	public static double dot(Vector2D a, Vector2D b) {
		return a.copy().dot(b);
	}
	
	/**
	 * Calculates the distance between the two vectors
	 *
	 * @param a The first vector
	 * @param b The second vector
	 * @return The distance between the two vectors
	 */
	public static double dist(Vector2D a, Vector2D b) {
		return a.dist(b);
	}
	
	@Override
	public String toString() {
		return "(" + this.x + ",  " + this.y + ")";
	}
	
	/**
	 * Sets the value of this vector to the given values
	 *
	 * @param x The x component
	 * @param y The y component
	 * @return This vector with the new values
	 */
	public Vector2D set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	/**
	 * Sets this vector with the values of the other given vector
	 *
	 * @param other The vector to copy the values from
	 * @return This vector with the new values
	 */
	public Vector2D set(Vector2D other) {
		return set(other.x, other.y);
	}
	
	/**
	 * Creates a copy of this vector
	 *
	 * @return Another vector with the same values as this
	 */
	public Vector2D copy() {
		return new Vector2D(this.x, this.y);
	}
	
	/**
	 * Checks if the vector equals the values given
	 *
	 * @param x The x component
	 * @param y The y component
	 * @return True if the values given are the same as this vector's values
	 */
	public boolean equals(double x, double y) {
		return (this.x == x) && (this.y == y);
	}
	
	/**
	 * Checks if the given vector is the same as this vector
	 *
	 * @param other The other vector to check with this vector
	 * @return True if both vectors are the same
	 */
	public boolean equals(Vector2D other) {
		return equals(other.x, other.y);
	}
	
	/**
	 * Adds the values given to this vector's values
	 *
	 * @param x The x component
	 * @param y The y component
	 * @return This vector with the added components
	 */
	public Vector2D add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	/**
	 * Adds the given vector's values to this vector
	 *
	 * @param other The other vector to add numbers to
	 * @return This vector with the added values of the other vector
	 */
	public Vector2D add(Vector2D other) {
		return add(other.x, other.y);
	}
	
	/**
	 * Subtracts this vector with the given values
	 *
	 * @param x The x component
	 * @param y The y component
	 * @return This vector with the subtracted values
	 */
	public Vector2D sub(double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	/**
	 * Subtracts this vector with the values of the other given vector
	 *
	 * @param other The vector which values to subtract from this vector
	 * @return This vector with the subtracted values
	 */
	public Vector2D sub(Vector2D other) {
		return sub(other.x, other.y);
	}
	
	/**
	 * Multiplies both components with the given value
	 *
	 * @param n The value to multiply the components with
	 * @return This vector with the multiplied values
	 */
	public Vector2D mult(double n) {
		this.x *= n;
		this.y *= n;
		return this;
	}
	
	/**
	 * Divides both components with by the given value
	 *
	 * @param n The value to divide the components by
	 * @return This vector with the divided components
	 */
	public Vector2D div(double n) {
		this.x /= n;
		this.y /= n;
		return this;
	}
	
	/**
	 * Calculates the magnitude squared of this vector
	 *
	 * @return The magnitude squared of this vector
	 */
	public double magSquared() {
		return (this.x * this.x + this.y * this.y);
	}
	
	/**
	 * Calculates the magnitude of this vector
	 *
	 * @return The magnitude of this vector
	 */
	public double mag() {
		return Math.sqrt(magSquared());
	}
	
	/**
	 * Calculates the dot product of this vector with the given values
	 *
	 * @param x The x component
	 * @param y The y component
	 * @return The dot product of this vector and the given values
	 */
	public double dot(double x, double y) {
		return (this.x * x) + (this.y * y);
	}
	
	/**
	 * Preforms the dot product of this vector and the given vector
	 *
	 * @param other The vector to dot product with this vector
	 * @return The dot product of this vector and the given vector
	 */
	public double dot(Vector2D other) {
		return dot(other.x, other.y);
	}
	
	/**
	 * Calculates the distance between this vector and the given vector
	 *
	 * @param other The other vector to find the distance between
	 * @return The distance between this and the given vector
	 */
	public double dist(Vector2D other) {
		return other.copy().sub(this).mag();
	}
	
	/**
	 * Normalises this vector
	 *
	 * @return The normalised vector
	 */
	public Vector2D normalise() {
		return (this.mag() == 0) ? this : this.div(this.mag());
	}
	
	/**
	 * Limits this vector's magnitude to the given value
	 *
	 * @param max The maximum magnitude of the vector
	 * @return The vector capped at the given max value
	 */
	public Vector2D limit(double max) {
		if (this.magSquared() > max * max) {
			this.normalise();
			this.mult(max);
		}
		return this;
	}
	
	/**
	 * Sets the magnitude of this vector
	 *
	 * @param mag The magnitude to set this vector to
	 * @return This vector with the new magnitude
	 */
	public Vector2D setMag(double mag) {
		return this.normalise().mult(mag);
	}
	
	/**
	 * Calculates the heading angle of this vector
	 *
	 * @return The heading angle of this vector
	 */
	public double heading() {
		return Math.atan2(this.y, this.x);
	}
	
	/**
	 * Rotates this vector by the given angle around the origin (0, 0)
	 *
	 * @param angle The angle to rotate the vector by
	 * @return This vector after being rotated
	 */
	public Vector2D rotate(double angle) {
		double newHeading = this.heading() + angle;
		double mag = this.mag();
		this.x = Math.cos(newHeading) * mag;
		this.y = Math.sin(newHeading) * mag;
		return this;
	}
	
	/**
	 * Preforms a linear interpolation between this vector and the given values
	 * At {@code amount = 0} returns this vector
	 * At {@code amount = 0.5} returns the mid point
	 * At {@code amount = 1} returns the given values
	 *
	 * @param x      The x component
	 * @param y      The y component
	 * @param amount The amount to linearly interpolate between the two values
	 * @return The values between this vector and the given values depending on the amount
	 */
	public Vector2D lerp(double x, double y, double amount) {
		this.x += (x - this.x) * amount;
		this.y += (y - this.y) * amount;
		return this;
	}
	
	/**
	 * Preforms a linear interpolation between this vector and the given vector
	 * At {@code amount = 0} returns this vector
	 * At {@code amount = 0.5} returns the mid point
	 * At {@code amount = 1} returns the given vector
	 *
	 * @param other  The other vector
	 * @param amount The amount to linearly interpolate between the two values
	 * @return The values between this vector and the given values depending on the amount
	 */
	public Vector2D lerp(Vector2D other, double amount) {
		return lerp(other.x, other.y, amount);
	}
}
