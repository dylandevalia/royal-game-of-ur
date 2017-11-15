package game.dylandevalia.royal_game_of_ur.utility;

import game.dylandevalia.royal_game_of_ur.utility.Die;

public class Dice {
	Die[] dice;
	
	public Dice(int noDice, int sides) {
		dice = new Die[noDice];
		for (Die die : dice) die = new Die(sides);
	}
	
	public int sum() {
		int sum = 0;
		for (Die die : dice) sum += die.roll();
		return sum;
	}
}
