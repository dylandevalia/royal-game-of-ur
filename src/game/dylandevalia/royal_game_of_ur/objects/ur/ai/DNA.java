package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

import game.dylandevalia.royal_game_of_ur.states.Game_Ur;
import game.dylandevalia.royal_game_of_ur.utility.Utility;

public class DNA {
	
	/* Move weights */
	
	/** Landed on a rosette */
	static final int ROSETTE = 0;
	/** Captures an enemy */
	static final int CAPTURE = 1;
	
	/** Enters the board from off-board */
	static final int ENTER_BOARD = 2;
	/** Enters the central (shared) row */
	static final int ENTER_CENTER = 3;
	/** Enters the end row */
	static final int ENTER_END = 4;
	/** Exits the board (won) */
	static final int EXIT_BOARD = 5;
	
	/** Counter moves the furthest up the board */
	static final int FURTHEST_PLACE = 6;
	/** Moves the least up the board */
	static final int CLOSEST_PLACE = 7;
	
	private static final int SINGLE_CHROMOSOME = 8;
	
	/** Amount of spaces in front of an enemy before move */
	static final int SPACES_AFTER_ENEMY_PRE = 8;
	/** Amount of spaces in front of an enemy after move */
	static final int SPACES_AFTER_ENEMY_POST = 9;
	
	/** The number of friendly counters on the board */
	static final int FRIENDLIES_ON_BOARD = 10;
	/** The number of hostile counters on the board */
	static final int HOSTILES_ON_BOARD = 11;
	
	/* Meta values */
	
	public static final int MUTATION_CHANCE = 12;
	
	private static final int CHROMOSOME_LENGTH = 13;
	
	private Chromosome[] chromosomes;
	private Crossover crossover;
	
	DNA() {
		chromosomes = initialiseChromosomes();
		
		int len = Crossover.values().length;
		this.crossover = Crossover.values()[Utility.randBetween(0, len - 1)];
	}
	
	private DNA(Chromosome[] chromosomes, Crossover crossoverMethod) {
		this.chromosomes = chromosomes;
		this.crossover = crossoverMethod;
	}
	
	private static Chromosome[] initialiseChromosomes() {
		Chromosome[] chromosomes = new Chromosome[CHROMOSOME_LENGTH];
		
		for (int i = 0; i < SINGLE_CHROMOSOME; i++) {
			chromosomes[i] = new Chromosome(1);
			chromosomes[i].setValue(Math.random());
		}
		
		chromosomes[SPACES_AFTER_ENEMY_PRE] = new Chromosome(Game_Ur.noDice);
		chromosomes[SPACES_AFTER_ENEMY_POST] = new Chromosome(Game_Ur.noDice);
		for (int i = 0; i < Game_Ur.noDice; i++) {
			chromosomes[SPACES_AFTER_ENEMY_PRE].setValue(i, Math.random());
			chromosomes[SPACES_AFTER_ENEMY_POST].setValue(i, Math.random());
		}
		
		chromosomes[FRIENDLIES_ON_BOARD] = new Chromosome(Game_Ur.noCounters);
		chromosomes[HOSTILES_ON_BOARD] = new Chromosome(Game_Ur.noCounters);
		for (int i = 0; i < Game_Ur.noCounters; i++) {
			chromosomes[FRIENDLIES_ON_BOARD].setValue(i, Math.random());
			chromosomes[HOSTILES_ON_BOARD].setValue(i, Math.random());
		}
		
		return chromosomes;
	}
	
	/**
	 * Cross-breeds two DNAs to make a new child DNA Randomly picks on of the crossover methods of
	 * the parent
	 *
	 * @param mum Parent DNA A
	 * @param dad Parent DNA B
	 * @return The child DNA of the parents
	 */
	public static DNA crossover(DNA mum, DNA dad) {
		// Pick one of the two parent's crossover methods
		Crossover cross = (Utility.fiftyFifty() ? mum : dad).crossover;
		
		// Make child chromosomes and give it the chosen crossover method
		Chromosome[] child = initialiseChromosomes();
		
		// Pick two points for Crossover.SINGLE_POINT and Crossover.DOUBLE_POINT
		// methods
		int pointOne = Utility.randBetween(0, CHROMOSOME_LENGTH - 2);
		int pointTwo = Utility.randBetween(pointOne + 1, CHROMOSOME_LENGTH - 1);
		
		// Go through every chromosome and sub-chromosome and cross
		for (int i = 0; i < child.length; i++) {
			Chromosome c = child[i];
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
	
	double getValue(int moveState) {
		return chromosomes[moveState].getValue();
	}
	
	double getValue(int moveState, int arg) {
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
