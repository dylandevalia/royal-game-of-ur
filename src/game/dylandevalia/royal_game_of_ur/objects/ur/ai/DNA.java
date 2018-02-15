package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

import game.dylandevalia.royal_game_of_ur.utility.Utility;

public class DNA {
	
	/* Move weights */
	
	public static final int ROSETTE = 0;
	public static final int CAPTURE = 1;
	
	public static final int ENTER_BOARD = 2;
	public static final int ENTER_CENTER = 3;
	public static final int ENTER_END = 4;
	public static final int EXIT_BOARD = 5;
	
	public static final int FURTHEST_PLACE = 6;
	public static final int CLOSEST_PLACE = 7;
	
	public static final int SPACES_IN_FRONT_ENEMY = 8;
	
	public static final int COUNTER_ON_BOARD = 9;
	public static final int ENEMIES_ON_BOARD = 10;
	
	/* Meta values */
	
	public static final int MUTATION_CHANCE = 11;
	
	public static final int CHROMOSOME_LENGTH = 12;
	
	private Chromosome[] chromosomes = new Chromosome[CHROMOSOME_LENGTH];
	private Crossover crossover;
	
	public DNA() {
		for (int i = 0; i < chromosomes.length; i++) {
			chromosomes[i] = new Chromosome(Math.random());
		}
		int len = Crossover.values().length;
		this.crossover = Crossover.values()[Utility.randBetween(0, len - 1)];
	}
	
	private DNA(Chromosome[] chromosomes, Crossover crossoverMethod) {
		this.chromosomes = chromosomes;
		this.crossover = crossoverMethod;
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
		Crossover cross = (Utility.fiftyFifty() ? mum : dad).crossover;
		
		// Make child chromosomes and give it the chosen crossover method
		Chromosome[] child = new Chromosome[CHROMOSOME_LENGTH];
		
		// Pick two points for Crossover.SINGLE_POINT and Crossover.DOUBLE_POINT
		// methods
		int pointOne = Utility.randBetween(0, CHROMOSOME_LENGTH - 2);
		int pointTwo = Utility.randBetween(pointOne + 1, CHROMOSOME_LENGTH - 1);
		
		for (int i = 0; i < child.length; i++) {
			Chromosome c = child[i] = new Chromosome();
			for (int j = 0; j < c.getLength(); j++) {
				c.setValue(j, crossover_helper(
					cross,
					mum.chromosomes[i].getValue(j),
					dad.chromosomes[i].getValue(j),
					i < pointOne,
					i < pointTwo
				));
			}
		}
		
		return new DNA(child, cross);
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
			default:
				return 0;
		}
	}
	
	public double getValue(int moveState) {
		return chromosomes[moveState].getValue();
	}
	
	public double getValue(int moveState, int arg) {
		return chromosomes[moveState].getValue(arg);
	}
	
	private enum Crossover {
		AVERAGE,
		SINGLE_POINT, DOUBLE_POINT,
		FIFTY_FIFTY
	}
	
	private enum Mutation {
	
	}
}
