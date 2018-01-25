package game.dylandevalia.royal_game_of_ur.client.game;

import game.dylandevalia.royal_game_of_ur.client.game.Player.PlayerNames;
import game.dylandevalia.royal_game_of_ur.client.game.entities.Counter;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.util.ArrayList;

/**
 * Keeps an array-list of counters and position vectors for those counters
 */
public class CounterCluster {
	
	/** The array-list of counters */
	private ArrayList<Counter> counters = new ArrayList<>();
	
	/** The array-list of starting positions */
	private ArrayList<Vector2D> startPos = new ArrayList<>();
	
	/** The initial position where the counters should be displayed */
	private Vector2D initialPos;
	
	/** Whether the stack should extend left or right */
	private boolean goLeft;
	
	/**
	 * Constructor which takes the initial position and if the stack
	 * should go left or right
	 *
	 * @param initialPos The initial position for the first counter
	 * @param goLeft     Should the counters stack left or right
	 */
	public CounterCluster(Vector2D initialPos, boolean goLeft) {
		this.initialPos = initialPos;
		this.goLeft = goLeft;
	}
	
	/**
	 * Adds a new counter to the array-list and returns a reference to it
	 *
	 * @param player The player/owner of the counter ({@link PlayerNames})
	 * @return A reference to the newly created counter
	 */
	public Counter addNew(PlayerNames player) {
		Vector2D nextPos = getNextPos();
		Counter counter = new Counter(
			(int) nextPos.x, (int) nextPos.y,
			player
		);
		startPos.add(nextPos);
		counters.add(counter);
		
		return counter;
	}
	
	/**
	 * Adds an existing counter to the array-list
	 *
	 * @param counter Reference to the counter to add to the cluster
	 */
	public void add(Counter counter) {
		// Generate new position and add to end of startPos
		Vector2D nextPos = getNextPos();
		startPos.add(nextPos);
		// Add new counter to the front and set to initialPos
		counters.add(0, counter);
		counter.setTarget(initialPos.copy(), false);
		// Animate counters shifting backwards
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = startPos.size() - 1; i > 0; i--) {
					sleep(200);
					counters.get(i).setTarget(startPos.get(i), false);
				}
			}
		}).start();
	}
	
	/**
	 * Calculates the start position of a new counter
	 *
	 * @return The position vector of the starting position of the next counter
	 */
	private Vector2D getNextPos() {
		return initialPos.copy().add(
			Counter.WIDTH * startPos.size() * (goLeft ? -1 : 1),
			0
		);
	}
	
	/**
	 * Removes a counter from the array-list and moves all the other counters
	 * along to fill the gap <tt>What does this do</tt>
	 *
	 * @param counter The counter to remove from the cluster
	 */
	public void remove(Counter counter) {
		if (!counters.remove(counter)) {
			Log.error("COUNTER_CLUSTER", "Counter doesn't belong to this cluster");
		}
		// Removes the last position
		startPos.remove(startPos.size() - 1);
		// Moves counters along filling the gap in a nice animation
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < counters.size(); i++) {
					sleep(200);
					counters.get(i).setTarget(startPos.get(i), false);
				}
			}
		}).start();
	}
	
	public int getSize() {
		return counters.size();
	}
	
	/**
	 * Simple method which sleeps the thread and catches any errors thrown
	 *
	 * @param millis The amount of milliseconds to sleep the thread for
	 */
	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Log.error("COUNTER_CLUSTER", "Failed to sleep");
		}
	}
}
