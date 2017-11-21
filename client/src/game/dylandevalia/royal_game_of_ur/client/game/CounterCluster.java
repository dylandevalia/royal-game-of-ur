package game.dylandevalia.royal_game_of_ur.client.game;

import game.dylandevalia.royal_game_of_ur.client.game.entities.Counter;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class CounterCluster {

	private ArrayList<Counter> counters = new ArrayList<>();
	private ArrayList<Vector2D> startPos = new ArrayList<>();

	private Vector2D initialPos;
	private boolean goLeft;

	public CounterCluster(Vector2D initialPos, boolean goLeft) {
		this.initialPos = initialPos;
		this.goLeft = goLeft;
	}

	public Counter addNew(boolean playerOne) {
		Vector2D nextPos = getNextPos();
		Counter counter = new Counter(
			(int) nextPos.x, (int) nextPos.y,
			playerOne
		);
		startPos.add(nextPos);
		counters.add(counter);

		return counter;
	}

	public void add(Counter counter) {
		counters.add(counter);
		Vector2D nextPos = getNextPos();
		startPos.add(nextPos);
		counter.setTarget(nextPos);
	}

	private Vector2D getNextPos() {
		return initialPos.copy().add(
			Counter.WIDTH * startPos.size() * (goLeft ? -1 : 1),
			0
		);
	}

	public void remove(Counter counter) {
		counters.remove(counter);
		startPos.remove(startPos.size() - 1);
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < counters.size(); i++) {
					sleep(200);
					counters.get(i).setTarget(startPos.get(i));
				}
			}

			private void sleep(int millis) {
				try {
					Thread.sleep(millis);
				} catch (InterruptedException e) {
					Log.error("Counter_Cluster", "Failed to sleep");
				}
			}
		}).start();
	}

	public void update(final Vector2D mousePos) {
		counters.forEach(counter -> counter.update(mousePos));
	}

	public void draw(final Graphics2D g, final double interpolate) {
		counters.forEach(counter -> counter.draw(g, interpolate));
	}
}
