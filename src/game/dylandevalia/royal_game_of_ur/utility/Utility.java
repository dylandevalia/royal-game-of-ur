package game.dylandevalia.royal_game_of_ur.utility;

import game.dylandevalia.royal_game_of_ur.gui.Window;
import java.awt.Color;
import java.util.List;

public class Utility {
	
	/**
	 * Generate random integer in the given range (inclusive)
	 */
	public static int randBetween(int min, int max) {
		int range = Math.abs(max - min) + 1;
		return (int) (Math.random() * range) + (min <= max ? min : max);
	}
	
	/**
	 * Generate a random double in the given range (inclusive)
	 */
	public static double randBetween(double min, double max) {
		double range = Math.abs(max - min);
		return (Math.random() * range) + (min <= max ? min : max);
	}
	
	/**
	 * Returns a random item from the array
	 *
	 * @param array The array to get the random item
	 * @param <T>   The type of the array
	 * @return A random item of type T from the array
	 */
	public static <T> T random(T[] array) {
		return array[randBetween(0, array.length - 1)];
	}
	
	/**
	 * Returns a random item from the given list of type T
	 *
	 * @param list The list to get the random item from
	 * @param <T>  The object type that the list holds
	 * @return A random item of type T from the list
	 */
	public static <T> T random(List<T> list) {
		return list.get(randBetween(0, list.size() - 1));
	}
	
	/**
	 * @return True 50% of the time
	 */
	public static boolean fiftyFifty() {
		return Math.random() < 0.5;
	}
	
	/**
	 * Remaps the given number from one linear range to another eg - map(5, 0, 10, 0, 20) = 10 -- -
	 * map(8, 0, 19, 0 100) = 42.1052...(%)
	 *
	 * @param n      The number to remap
	 * @param start1 The initial range's minimum value
	 * @param stop1  The initial range's maximum value
	 * @param start2 The new range's minimum value
	 * @param stop2  The new range's maximum value
	 * @return The value mapped from the initial range to the new range
	 */
	public static double map(double n, double start1, double stop1, double start2, double stop2) {
		return ((n - start1) / (stop1 - start1)) * (stop2 - start2) + start2;
	}
	
	/**
	 * Maps a value based on the window's width
	 *
	 * @param sevenTwenty Value at 720p
	 * @param tenEighty   Value at 1080p
	 * @return The mapped value based on the window's width
	 */
	public static double mapWidth(double sevenTwenty, double tenEighty) {
		return map(Window.WIDTH, 1280, 1920, sevenTwenty, tenEighty);
	}
	
	/**
	 * Simple method which sleeps the thread and catches any errors thrown
	 *
	 * @param millis The amount of milliseconds to sleep the thread for
	 */
	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Log.error("COUNTER_CLUSTER", "Failed to sleep");
		}
	}
	
	/**
	 * Clamps the given value between the range of min to max. If the number if less than the
	 * minimum or greater than the maximum, it is set to the minimum and maximum value respectively.
	 * If the number is within the range, it just simply returned, unchanged.
	 *
	 * @param n   The number of clamp
	 * @param min The minimum value of the range
	 * @param max The maximum value of the range
	 * @return {@code min} if {@code n < min}, {@code max} if {@code n > max}, else {@code n}
	 */
	public static double clamp(double n, double min, double max) {
		if (n < min) {
			n = min;
		} else if (n > max) {
			n = max;
		}
		
		return n;
	}
	
	/**
	 * Clamps the given value between the range of min to max. If the number if less than the
	 * minimum or greater than the maximum, it is set to the minimum and maximum value respectively.
	 * If the number is within the range, it just simply returned, unchanged.
	 *
	 * @param n   The number of clamp
	 * @param min The minimum value of the range
	 * @param max The maximum value of the range
	 * @return {@code min} if {@code n < min}, {@code max} if {@code n > max}, else {@code n}
	 */
	public static int clamp(int n, int min, int max) {
		if (n < min) {
			n = min;
		} else if (n > max) {
			n = max;
		}
		
		return n;
	}
	
	/**
	 * Takes two numbers and linearly interpolates a middle values depending on the ratio value At
	 * {@code ratio = 0} returns {@code near} At {@code ratio = 0.5} returns {@code (near + far) /
	 * 2} (ie. mid point) At {@code ratio = 1} returns {@code far}
	 *
	 * @param ratio The ratio value -- min: 0, max: 1
	 * @param near  The first value when ratio is 0
	 * @param far   The second value when ratio is 1
	 */
	public static double lerp(double ratio, double near, double far) {
		return (int) (Math.abs((ratio * near)) + ((1 - ratio) * far));
	}
	
	public static Color lerp(double ratio, Color c1, Color c2) {
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
}
