package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

import game.dylandevalia.royal_game_of_ur.utility.Utility;

public class DNA {
	
	public static final int ROSSETE = 0;
	
	public static final int ENTER_BOARD = 1;
	public static final int ENTER_CENTER = 2;
	public static final int EXIT_BOARD = 3;
	public static final int EXIT_CENTER = 4;
	
	public static final int FURTHEST_PLACE = 5;
	public static final int CLOSEST_PLACE = 6;
	
	public static final int SPACES_IN_FRONT_ENEMY = 7;
	
	public static final int COUNTER_ON_BOARD = 8;
	public static final int ENEMIES_ON_BOARD = 9;
	
	private Chromosome[] chromosomes = new Chromosome[10];
	private Crossover crossover;
	
	public DNA() {
		for (int i = 0; i < chromosomes.length; i++) {
			chromosomes[i] = new Chromosome(Math.random());
		}
		int len = Crossover.class.getEnumConstants().length;
		this.crossover = Crossover.class.getEnumConstants()[Utility.randBetween(0, len - 1)];
	}
	
	/**
	 * Cross-breeds two DNAs to make a new child DNA
	 * Randomly picks on of the crossover methods of the parent
	 *
	 * @param mum Parent DNA A
	 * @param dad Parent DNA B
	 * @return The child DNA of the parents
	 */
	public static DNA crossover(DNA mum, DNA dad) {
		// Pick one of the two parent's crossover methods
		Crossover cross;
		if (Utility.fiftyFifty()) {
			cross = mum.crossover;
		} else {
			cross = dad.crossover;
		}
		
		// Make child DNA and give it the chosen crossover method
		DNA child = new DNA();
		child.crossover = cross;
		
		// Pick two points for Crossover.SINGLE_POINT and Crossover.DOUBLE_POINT
		// methods
		int pointOne = Utility.randBetween(0, mum.chromosomes.length - 2);
		int pointTwo = Utility.randBetween(pointOne + 1, mum.chromosomes.length - 1);
		
		for (int i = 0; i < mum.chromosomes.length; i++) {
			for (int j = 0; j < mum.chromosomes[i].getLength(); j++) {
				child.chromosomes[i].setValue(j, crossover_helper(
					cross,
					mum.chromosomes[i].getValue(j),
					dad.chromosomes[i].getValue(j),
					i < pointOne,
					i < pointTwo
				));
			}
		}
		
		return child;
	}
	
	private static double crossover_helper(
		Crossover c,
		double mum, double dad,
		boolean beforePointOne, boolean beforePointTwo
	) {
		switch (c) {
			case AVERAGE:
				return (mum + dad) / 2;
			case SINGLE_POINT:
				return beforePointOne ? mum : dad;
			case DOUBLE_POINT:
				return (!beforePointOne && beforePointTwo) ? dad : mum;
			case FIFTY_FIFTY:
				return (Utility.fiftyFifty()) ? mum : dad;
			case WEIGHTED_FIFTY:
				return 0;
		}
		return 0;
	}
	
	public Chromosome getChromosomes(int index) {
		return chromosomes[index];
	}
	
	public void setChromosome(int index, double value) {
		this.chromosomes[index].setValue(value);
	}
	
	private enum Crossover {
		AVERAGE,
		SINGLE_POINT, DOUBLE_POINT,
		FIFTY_FIFTY, WEIGHTED_FIFTY
	}
	
	private enum Mutation {
	
	}
}
