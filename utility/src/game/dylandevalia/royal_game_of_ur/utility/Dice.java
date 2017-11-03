package game.dylandevalia.royal_game_of_ur.utility;

import java.util.Random;

public class Dice {
	private int noSides;
	private Random random;
	
	public Dice(int noSides) {
		this.noSides = noSides;
		random = new Random(System.currentTimeMillis());
	}
	
	public int roll() {
		// Get a number from 1 to noSides
		return (random.nextInt(noSides) + 1);
	}
}
