package game.dylandevalia.royal_game_of_ur.utility;

import java.util.Random;

public class UrDice {
	private Dice dice = new Dice(4, 2);
	
	public void setNoDice(int noDice) {
		dice = new Dice(noDice, 2);
	}
	
	public int roll() {
		return dice.sum();
	}
	
	private class Dice {
		private Die[] dice;
		
		Dice(int noDice, int sides) {
			dice = new Die[noDice];
			for (int i = 0; i < noDice; i++) {
				dice[i] = new Die(sides);
				// Sleeps for one millisecond so they all have different seeds
				sleepForOneMilli();
			}
		}
		
		/**
		 * Rolls all the dice and returns the sum of the random numbers
		 *
		 * @return The sum of the all random rolls
		 */
		int sum() {
			int sum = 0;
			for (Die die : dice) {
				int roll = die.roll();
				sum += roll;
			}
			return sum;
		}
		
		/**
		 * Sleeps the thread for one millisecond
		 */
		private void sleepForOneMilli() {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Log.warn("Dice", "Couldn't sleep thread", e);
			}
		}
		
		/**
		 * A single die that can roll a random number between 0 and
		 * noSides - 1
		 */
		private class Die {
			private int noSides;
			private Random random;
			
			Die(int noSides) {
				this.noSides = noSides;
				random = new Random(System.currentTimeMillis());
			}
			
			int roll() {
				// Get a number from 1 to noSides
				return (random.nextInt(noSides));
			}
		}
	}
}
