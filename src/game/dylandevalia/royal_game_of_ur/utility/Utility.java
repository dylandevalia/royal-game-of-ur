package game.dylandevalia.royal_game_of_ur.utility;

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
	 * @return True 50% of the time
	 */
	public static boolean fiftyFifty() {
		return Math.random() < 0.5;
	}
	
	/**
	 * Remaps the given number from one linear range to another
	 * eg - map(5, 0, 10, 0, 20) = 10
	 * -- - map(8, 0, 19, 0 100) = 42.1052...(%)
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
}
