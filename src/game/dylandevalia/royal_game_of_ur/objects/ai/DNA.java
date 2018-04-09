package game.dylandevalia.royal_game_of_ur.objects.ai;

import game.dylandevalia.royal_game_of_ur.states.GameUr;
import game.dylandevalia.royal_game_of_ur.utility.Utility;

/**
 * Holds an {@link AI}'s chromosomes and has crossover and mutation methods for genetic evolution
 */
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
	
	/** Amount of spaces in front of an enemy before move */
	static final int SPACES_AFTER_ENEMY_PRE = 8;
	/** Amount of spaces in front of an enemy after move */
	static final int SPACES_AFTER_ENEMY_POST = 9;
	
	/** The number of friendly counters on the board */
	static final int FRIENDLIES_ON_BOARD = 10;
	/** The number of hostile counters on the board */
	static final int HOSTILES_ON_BOARD = 11;
	
	/** The chance a chromosome will mutate */
	private static final int MUTATION_CHANCE = 12;
	
	/* Meta values */
	/** All chromosomes before this only have a single value */
	private static final int SINGLE_CHROMOSOME = 8;
	/** The number of chromosomes */
	private static final int CHROMOSOME_LENGTH = 13;
	
	/** The array of chromosomes */
	private Chromosome[] chromosomes;
	/** The DNAs method to preform crossover */
	private CrossoverMethod crossoverMethod;
	
	/**
	 * Used to create a brand new DNA structure with random values
	 */
	DNA() {
		chromosomes = initialiseChromosomes();
		
		int len = CrossoverMethod.values().length;
		this.crossoverMethod = CrossoverMethod.values()[Utility.randBetween(0, len - 1)];
	}
	
	/**
	 * Uses to create a DNA structure with premade values
	 *
	 * @param chromosomes     The array of chromosomes. Use {@link #initialiseChromosomes()} to
	 *                        generate correct structure
	 * @param crossoverMethod The method that the DNA will use to preform crossover
	 */
	private DNA(Chromosome[] chromosomes, CrossoverMethod crossoverMethod) {
		this.chromosomes = chromosomes;
		this.crossoverMethod = crossoverMethod;
	}
	
	/**
	 * Creates an array of chromosomes with the correct structure that all DNAs will use
	 *
	 * @return An array of Chromosomes initialised with random values (double 0-1)
	 */
	private static Chromosome[] initialiseChromosomes() {
		Chromosome[] chromosomes = new Chromosome[CHROMOSOME_LENGTH];
		
		for (int i = 0; i < SINGLE_CHROMOSOME; i++) {
			chromosomes[i] = new Chromosome();
			chromosomes[i].setValue(Math.random());
		}
		
		chromosomes[SPACES_AFTER_ENEMY_PRE] = new Chromosome(GameUr.noDice);
		chromosomes[SPACES_AFTER_ENEMY_POST] = new Chromosome(GameUr.noDice);
		for (int i = 0; i < GameUr.noDice; i++) {
			chromosomes[SPACES_AFTER_ENEMY_PRE].setValue(i, Math.random());
			chromosomes[SPACES_AFTER_ENEMY_POST].setValue(i, Math.random());
		}
		
		chromosomes[FRIENDLIES_ON_BOARD] = new Chromosome(GameUr.noCounters + 1);
		chromosomes[HOSTILES_ON_BOARD] = new Chromosome(GameUr.noCounters + 1);
		for (int i = 0; i < GameUr.noCounters + 1; i++) {
			chromosomes[FRIENDLIES_ON_BOARD].setValue(i, Math.random());
			chromosomes[HOSTILES_ON_BOARD].setValue(i, Math.random());
		}
		
		chromosomes[MUTATION_CHANCE] = new Chromosome();
		chromosomes[MUTATION_CHANCE].setValue(Math.random());
		
		return chromosomes;
	}
	
	/**
	 * Cross-breeds two DNAs to make a new child DNA Randomly picks on of the crossoverMethod
	 * methods of the parent
	 *
	 * @param mum Parent DNA A
	 * @param dad Parent DNA B
	 * @return The child DNA of the parents
	 */
	public static DNA crossover(DNA mum, DNA dad) {
		// Pick one of the two parent's crossoverMethod methods
		CrossoverMethod cross = (Utility.fiftyFifty() ? mum : dad).crossoverMethod;
		
		// Make child chromosomes and give it the chosen crossoverMethod method
		Chromosome[] child = initialiseChromosomes();
		
		// Pick two points for CrossoverMethod.SINGLE_POINT and CrossoverMethod.DOUBLE_POINT
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
	
	/**
	 * Small helper function used by {@link #crossover(DNA, DNA)} Used to crossover two values
	 * depending on the crossover method
	 *
	 * @param c              The crossover method to use
	 * @param mum            The first value to crossover
	 * @param dad            The second value to crossover
	 * @param beforePointOne Some random point in the length of the chromosome size
	 * @param beforePointTwo Another random point <em>after</em> {@code beforePointOne} and before
	 *                       chromosome size
	 * @return The crossover value
	 */
	private static double crossover_helper(
		CrossoverMethod c,
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
	
	/**
	 * Randomly mutates the {@link DNA} based on its {@link #chromosomes}[{@link #MUTATION_CHANCE}]
	 */
	public void mutate() {
		for (Chromosome chromosome : chromosomes) {
			for (int j = 0; j < chromosome.getLength(); j++) {
				// If mutation chance successful, change to new random value
				if (Math.random() < 0.01 /*chromosomes[MUTATION_CHANCE].getValue()*/) {
					chromosome.setValue(j, Math.random());
				}
			}
		}
	}
	
	double getValue(int chromosome) {
		return chromosomes[chromosome].getValue();
	}
	
	double getValue(int chromosome, int arg) {
		return chromosomes[chromosome].getValue(arg);
	}
	
	@Override
	public String toString() {
		return "\n        rosette: " + chromosomes[ROSETTE].getValue()
			+ "\n        capture: " + chromosomes[CAPTURE].getValue()
			+ "\n    enter board: " + chromosomes[ENTER_BOARD].getValue()
			+ "\n   enter centre: " + chromosomes[ENTER_CENTER].getValue()
			+ "\n      enter end: " + chromosomes[ENTER_END].getValue()
			+ "\n     exit board: " + chromosomes[EXIT_BOARD].getValue()
			+ "\n       furthest: " + chromosomes[FURTHEST_PLACE].getValue()
			+ "\n        closest: " + chromosomes[CLOSEST_PLACE].getValue()
			+ "\n spaces pre (1): " + chromosomes[SPACES_AFTER_ENEMY_PRE].getValue(0)
			+ "\n spaces pre (2): " + chromosomes[SPACES_AFTER_ENEMY_PRE].getValue(1)
			+ "\n spaces pre (3): " + chromosomes[SPACES_AFTER_ENEMY_PRE].getValue(2)
			+ "\n spaces pre (4): " + chromosomes[SPACES_AFTER_ENEMY_PRE].getValue(3)
			+ "\nspaces post (1): " + chromosomes[SPACES_AFTER_ENEMY_POST].getValue(0)
			+ "\nspaces post (2): " + chromosomes[SPACES_AFTER_ENEMY_POST].getValue(1)
			+ "\nspaces post (3): " + chromosomes[SPACES_AFTER_ENEMY_POST].getValue(2)
			+ "\nspaces post (4): " + chromosomes[SPACES_AFTER_ENEMY_POST].getValue(3)
			+ "\n friendlies (0): " + chromosomes[FRIENDLIES_ON_BOARD].getValue(0)
			+ "\n friendlies (1): " + chromosomes[FRIENDLIES_ON_BOARD].getValue(1)
			+ "\n friendlies (2): " + chromosomes[FRIENDLIES_ON_BOARD].getValue(2)
			+ "\n friendlies (3): " + chromosomes[FRIENDLIES_ON_BOARD].getValue(3)
			+ "\n friendlies (4): " + chromosomes[FRIENDLIES_ON_BOARD].getValue(4)
			+ "\n friendlies (5): " + chromosomes[FRIENDLIES_ON_BOARD].getValue(5)
			+ "\n friendlies (6): " + chromosomes[FRIENDLIES_ON_BOARD].getValue(6)
			+ "\n   hostiles (0): " + chromosomes[HOSTILES_ON_BOARD].getValue(0)
			+ "\n   hostiles (1): " + chromosomes[HOSTILES_ON_BOARD].getValue(1)
			+ "\n   hostiles (2): " + chromosomes[HOSTILES_ON_BOARD].getValue(2)
			+ "\n   hostiles (3): " + chromosomes[HOSTILES_ON_BOARD].getValue(3)
			+ "\n   hostiles (4): " + chromosomes[HOSTILES_ON_BOARD].getValue(4)
			+ "\n   hostiles (5): " + chromosomes[HOSTILES_ON_BOARD].getValue(5)
			+ "\n   hostiles (6): " + chromosomes[HOSTILES_ON_BOARD].getValue(6);
	}
	
	public String getValues() {
		return "" + chromosomes[ROSETTE].getValue()
			+ "," + chromosomes[CAPTURE].getValue()
			+ "," + chromosomes[ENTER_BOARD].getValue()
			+ "," + chromosomes[ENTER_CENTER].getValue()
			+ "," + chromosomes[ENTER_END].getValue()
			+ "," + chromosomes[EXIT_BOARD].getValue()
			+ "," + chromosomes[FURTHEST_PLACE].getValue()
			+ "," + chromosomes[CLOSEST_PLACE].getValue()
			+ "," + chromosomes[SPACES_AFTER_ENEMY_PRE].getValue(0)
			+ "," + chromosomes[SPACES_AFTER_ENEMY_PRE].getValue(1)
			+ "," + chromosomes[SPACES_AFTER_ENEMY_PRE].getValue(2)
			+ "," + chromosomes[SPACES_AFTER_ENEMY_PRE].getValue(3)
			+ "," + chromosomes[SPACES_AFTER_ENEMY_POST].getValue(0)
			+ "," + chromosomes[SPACES_AFTER_ENEMY_POST].getValue(1)
			+ "," + chromosomes[SPACES_AFTER_ENEMY_POST].getValue(2)
			+ "," + chromosomes[SPACES_AFTER_ENEMY_POST].getValue(3)
			+ "," + chromosomes[FRIENDLIES_ON_BOARD].getValue(0)
			+ "," + chromosomes[FRIENDLIES_ON_BOARD].getValue(1)
			+ "," + chromosomes[FRIENDLIES_ON_BOARD].getValue(2)
			+ "," + chromosomes[FRIENDLIES_ON_BOARD].getValue(3)
			+ "," + chromosomes[FRIENDLIES_ON_BOARD].getValue(4)
			+ "," + chromosomes[FRIENDLIES_ON_BOARD].getValue(5)
			+ "," + chromosomes[FRIENDLIES_ON_BOARD].getValue(6)
			+ "," + chromosomes[HOSTILES_ON_BOARD].getValue(0)
			+ "," + chromosomes[HOSTILES_ON_BOARD].getValue(1)
			+ "," + chromosomes[HOSTILES_ON_BOARD].getValue(2)
			+ "," + chromosomes[HOSTILES_ON_BOARD].getValue(3)
			+ "," + chromosomes[HOSTILES_ON_BOARD].getValue(4)
			+ "," + chromosomes[HOSTILES_ON_BOARD].getValue(5)
			+ "," + chromosomes[HOSTILES_ON_BOARD].getValue(6);
	}
	
	/**
	 * The list of methods that a DNA can crossover
	 */
	private enum CrossoverMethod {
		AVERAGE,
		SINGLE_POINT, DOUBLE_POINT,
		FIFTY_FIFTY
	}
}
