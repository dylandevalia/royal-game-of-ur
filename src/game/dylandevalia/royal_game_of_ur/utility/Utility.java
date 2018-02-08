package game.dylandevalia.royal_game_of_ur.utility;

public class Utility {
	
	public static int randBetween(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}
	
	public static double randBetween(double min, double max) {
		return min + (Math.random() * ((max - min) + 1));
	}

//	public static int map(int n, int start1, int stop1, int start2, int stop2) {
//		return ((n - start1) / (stop1 - start1)) * (stop2 - start2) + start2;
//	}
	
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
