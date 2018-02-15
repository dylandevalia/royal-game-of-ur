package game.dylandevalia.royal_game_of_ur.utility;

import java.util.Random;

public class UrDice {
	
	/** Array of dice */
	private Die[] dice;
	
	public UrDice(int noDice) {
		dice = new Die[noDice];
		for (int i = 0; i < noDice; i++) {
			dice[i] = new Die();
			// Sleep for one ms so they get different random seeds
			Utility.sleep(1);
		}
	}
	
	/**
	 * Rolls the dice and returns the sum of the dice
	 *
	 * @return The sum of the rolled dice
	 */
	public int roll() {
		int roll = 0;
		
		for (Die die : dice) {
			roll += die.roll();
		}
		
		return roll;
	}
	
	/**
	 * Gets all the individual rolls of the dice
	 *
	 * @return The rolls of the individual dice
	 */
	public int[] getRolls() {
		int[] rolls = new int[dice.length];
		
		for (int i = 0; i < dice.length; i++) {
			rolls[i] = dice[i].value;
		}
		
		return rolls;
	}
	
	/**
	 * Die class which can produce a random number between 0 and 1
	 */
	private class Die {
		
		private int value;
		private Random random;
		
		Die() {
			random = new Random(System.nanoTime());
		}
		
		int roll() {
			value = random.nextInt(2);
			return value;
		}
	}
}
