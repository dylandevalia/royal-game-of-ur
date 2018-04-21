package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

import game.dylandevalia.royal_game_of_ur.objects.ur.ai.DNA.CrossoverMethod;
import game.dylandevalia.royal_game_of_ur.utility.Utility;

public class Library {
	
	/** DNA of the most fit agent after 1000 generation of genetic evolution */
	public static DNA thousand = new DNA(
		new Chromosome[]{
			new Chromosome().setValue(0.601443380),     // Rosette
			new Chromosome().setValue(0.520370240),     // Capture
			new Chromosome().setValue(0.488966342),     // Enter board
			new Chromosome().setValue(0.423265514),     // Enter centre
			new Chromosome().setValue(0.615562223),     // Enter end
			new Chromosome().setValue(0.618980456),     // Exit board
			new Chromosome().setValue(0.516302069),     // Furthest
			new Chromosome().setValue(0.426042467),     // Closest
			
			new Chromosome(4)                   // Spaces pre
				.setValue(0, 0.486712877)       // 1
				.setValue(1, 0.740478151)       // 2
				.setValue(2, 0.502770263)       // 3
				.setValue(3, 0.443259008),      // 4
			
			new Chromosome(4)                   // Spaces post
				.setValue(0, 0.469515113)       // 1
				.setValue(1, 0.570645719)       // 2
				.setValue(2, 0.526002198)       // 3
				.setValue(3, 0.475760949),      // 4
			
			new Chromosome(7)                   // Friendlies
				.setValue(0, 0.481394719)       // 0
				.setValue(1, 0.571627282)       // 1
				.setValue(2, 0.564610113)       // 2
				.setValue(3, 0.477490358)       // 3
				.setValue(4, 0.462273256)       // 4
				.setValue(5, 0.431268139)       // 5
				.setValue(6, 0.495770721),      // 6
			
			new Chromosome(7)                   // Hostiles
				.setValue(0, 0.483985421)       // 0
				.setValue(1, 0.483323985)       // 1
				.setValue(2, 0.518545685)       // 2
				.setValue(3, 0.481931342)       // 3
				.setValue(4, 0.490458504)       // 4
				.setValue(5, 0.518193902)       // 5
				.setValue(6, 0.510373610),      // 6
		},
		Utility.random(CrossoverMethod.values())
	);
	
	/** The most fit agent after 1000 generation of genetic evolution normalised between 0 and 1 */
	public static DNA thousandNormalised = new DNA(
		new Chromosome[]{
			new Chromosome().setValue(0.893953587),     // Rosette
			new Chromosome().setValue(0.508743716),     // Capture
			new Chromosome().setValue(0.508743716),     // Enter board
			new Chromosome().setValue(0.346916918),     // Enter centre
			new Chromosome().setValue(0.346916918),     // Enter end
			new Chromosome().setValue(0.051486916),     // Exit board
			new Chromosome().setValue(0.051486916),     // Furthest
			new Chromosome().setValue(0.894166836),     // Closest
			
			new Chromosome(4)                   // Spaces pre
				.setValue(0, 0.894166836)       // 1
				.setValue(1, 1.000000000)       // 2
				.setValue(2, 1.000000000)       // 3
				.setValue(3, 0.462506467),      // 4
			
			new Chromosome(4)                   // Spaces post
				.setValue(0, 0.462506467)       // 1
				.setValue(1, 0.000000000)       // 2
				.setValue(2, 0.000000000)       // 3
				.setValue(3, 0.300938533),      // 4
			
			new Chromosome(7)                   // Friendlies
				.setValue(0, 0.300938533)       // 0
				.setValue(1, 0.314748887)       // 1
				.setValue(2, 0.314748887)       // 2
				.setValue(3, 0.419277375)       // 3
				.setValue(4, 0.419277375)       // 4
				.setValue(5, 0.402077413)       // 5
				.setValue(6, 0.402077413),      // 6
			
			new Chromosome(7)                   // Hostiles
				.setValue(0, 0.248520398)       // 0
				.setValue(1, 0.248520398)       // 1
				.setValue(2, 0.780454544)       // 2
				.setValue(3, 0.780454544)       // 3
				.setValue(4, 0.580995492)       // 4
				.setValue(5, 0.580995492)       // 5
				.setValue(6, 0.259642285),      // 6
		},
		Utility.random(CrossoverMethod.values())
	);
	
	
	/* Custom built */
	
	/** Template with all values at 0 used for creating other AI archetypes */
	public static DNA template = new DNA(
		new Chromosome[]{
			new Chromosome().setValue(0.0),     // Rosette
			new Chromosome().setValue(0.0),     // Capture
			new Chromosome().setValue(0.0),     // Enter board
			new Chromosome().setValue(0.0),     // Enter centre
			new Chromosome().setValue(0.0),     // Enter end
			new Chromosome().setValue(0.0),     // Exit board
			new Chromosome().setValue(0.0),     // Furthest
			new Chromosome().setValue(0.0),     // Closest
			
			new Chromosome(4)           // Spaces pre
				.setValue(0, 0.0)       // 1
				.setValue(1, 0.0)       // 2
				.setValue(2, 0.0)       // 3
				.setValue(3, 0.0),      // 4
			
			new Chromosome(4)           // Spaces post
				.setValue(0, 0.0)       // 1
				.setValue(1, 0.0)       // 2
				.setValue(2, 0.0)       // 3
				.setValue(3, 0.0),      // 4
			
			new Chromosome(7)           // Friendlies
				.setValue(0, 0.0)       // 0
				.setValue(1, 0.0)       // 1
				.setValue(2, 0.0)       // 2
				.setValue(3, 0.0)       // 3
				.setValue(4, 0.0)       // 4
				.setValue(5, 0.0)       // 5
				.setValue(6, 0.0),      // 6
			
			new Chromosome(7)           // Hostiles
				.setValue(0, 0.0)       // 0
				.setValue(1, 0.0)       // 1
				.setValue(2, 0.0)       // 2
				.setValue(3, 0.0)       // 3
				.setValue(4, 0.0)       // 4
				.setValue(5, 0.0)       // 5
				.setValue(6, 0.0),      // 6
		},
		Utility.random(CrossoverMethod.values())
	);
	
	/** Bespoke as to the values I believe to be pretty good */
	public static DNA bespoke = new DNA(
		new Chromosome[]{
			new Chromosome().setValue(0.9),     // Rosette
			new Chromosome().setValue(0.9),     // Capture
			new Chromosome().setValue(0.6),     // Enter board
			new Chromosome().setValue(0.5),     // Enter centre
			new Chromosome().setValue(0.7),     // Enter end
			new Chromosome().setValue(0.9),     // Exit board
			new Chromosome().setValue(0.5),     // Furthest
			new Chromosome().setValue(0.5),     // Closest
			
			new Chromosome(4)           // Spaces pre
				.setValue(0, 0.1)       // 1
				.setValue(1, 0.6)       // 2
				.setValue(2, 0.4)       // 3
				.setValue(3, 0.1),      // 4
			
			new Chromosome(4)           // Spaces post
				.setValue(0, 0.3)       // 1
				.setValue(1, 0.1)       // 2
				.setValue(2, 0.3)       // 3
				.setValue(3, 0.5),      // 4
			
			new Chromosome(7)           // Friendlies
				.setValue(0, 0.9)       // 0
				.setValue(1, 0.8)       // 1
				.setValue(2, 0.7)       // 2
				.setValue(3, 0.6)       // 3
				.setValue(4, 0.5)       // 4
				.setValue(5, 0.4)       // 5
				.setValue(6, 0.3),      // 6
			
			new Chromosome(7)           // Hostiles
				.setValue(0, 0.1)       // 0
				.setValue(1, 0.2)       // 1
				.setValue(2, 0.3)       // 2
				.setValue(3, 0.4)       // 3
				.setValue(4, 0.5)       // 4
				.setValue(5, 0.6)       // 5
				.setValue(6, 0.7),      // 6
		},
		Utility.random(CrossoverMethod.values())
	);
	
	/** Aggressive AI which will prioritize capturing enemy counters */
	public static DNA aggressive = new DNA(
		new Chromosome[]{
			new Chromosome().setValue(0.0),     // Rosette
			new Chromosome().setValue(1.0),     // Capture
			new Chromosome().setValue(0.0),     // Enter board
			new Chromosome().setValue(0.0),     // Enter centre
			new Chromosome().setValue(0.0),     // Enter end
			new Chromosome().setValue(0.0),     // Exit board
			new Chromosome().setValue(0.0),     // Furthest
			new Chromosome().setValue(0.0),     // Closest
			
			new Chromosome(4)           // Spaces pre
				.setValue(0, 0.0)       // 1
				.setValue(1, 0.0)       // 2
				.setValue(2, 0.0)       // 3
				.setValue(3, 0.0),      // 4
			
			new Chromosome(4)           // Spaces post
				.setValue(0, 0.0)       // 1
				.setValue(1, 0.0)       // 2
				.setValue(2, 0.0)       // 3
				.setValue(3, 0.0),      // 4
			
			new Chromosome(7)           // Friendlies
				.setValue(0, 0.0)       // 0
				.setValue(1, 0.0)       // 1
				.setValue(2, 0.0)       // 2
				.setValue(3, 0.0)       // 3
				.setValue(4, 0.0)       // 4
				.setValue(5, 0.0)       // 5
				.setValue(6, 0.0),      // 6
			
			new Chromosome(7)           // Hostiles
				.setValue(0, 0.0)       // 0
				.setValue(1, 0.1)       // 1
				.setValue(2, 0.2)       // 2
				.setValue(3, 0.4)       // 3
				.setValue(4, 0.6)       // 4
				.setValue(5, 0.8)       // 5
				.setValue(6, 1.0),      // 6
		},
		Utility.random(CrossoverMethod.values())
	);
	
	/** AI that tries to get a counter to the end before moving other counters */
	public static DNA justFinish = new DNA(
		new Chromosome[]{
			new Chromosome().setValue(0.0),     // Rosette
			new Chromosome().setValue(0.0),     // Capture
			new Chromosome().setValue(0.4),     // Enter board
			new Chromosome().setValue(0.6),     // Enter centre
			new Chromosome().setValue(0.8),     // Enter end
			new Chromosome().setValue(1.0),     // Exit board
			new Chromosome().setValue(1.0),     // Furthest
			new Chromosome().setValue(0.0),     // Closest
			
			new Chromosome(4)           // Spaces pre
				.setValue(0, 0.0)       // 1
				.setValue(1, 0.0)       // 2
				.setValue(2, 0.0)       // 3
				.setValue(3, 0.0),      // 4
			
			new Chromosome(4)           // Spaces post
				.setValue(0, 0.0)       // 1
				.setValue(1, 0.0)       // 2
				.setValue(2, 0.0)       // 3
				.setValue(3, 0.0),      // 4
			
			new Chromosome(7)           // Friendlies
				.setValue(0, 0.0)       // 0
				.setValue(1, 0.0)       // 1
				.setValue(2, 0.0)       // 2
				.setValue(3, 0.0)       // 3
				.setValue(4, 0.0)       // 4
				.setValue(5, 0.0)       // 5
				.setValue(6, 0.0),      // 6
			
			new Chromosome(7)           // Hostiles
				.setValue(0, 0.0)       // 0
				.setValue(1, 0.0)       // 1
				.setValue(2, 0.0)       // 2
				.setValue(3, 0.0)       // 3
				.setValue(4, 0.0)       // 4
				.setValue(5, 0.0)       // 5
				.setValue(6, 0.0),      // 6
		},
		Utility.random(CrossoverMethod.values())
	);
	
	/** Tries to move all its counter up the board together */
	public static DNA allTogether = new DNA(
		new Chromosome[]{
			new Chromosome().setValue(0.0),     // Rosette
			new Chromosome().setValue(0.0),     // Capture
			new Chromosome().setValue(0.2),     // Enter board
			new Chromosome().setValue(0.4),     // Enter centre
			new Chromosome().setValue(0.6),     // Enter end
			new Chromosome().setValue(0.8),     // Exit board
			new Chromosome().setValue(0.0),     // Furthest
			new Chromosome().setValue(1.0),     // Closest
			
			new Chromosome(4)           // Spaces pre
				.setValue(0, 0.0)       // 1
				.setValue(1, 0.0)       // 2
				.setValue(2, 0.0)       // 3
				.setValue(3, 0.0),      // 4
			
			new Chromosome(4)           // Spaces post
				.setValue(0, 0.0)       // 1
				.setValue(1, 0.0)       // 2
				.setValue(2, 0.0)       // 3
				.setValue(3, 0.0),      // 4
			
			new Chromosome(7)           // Friendlies
				.setValue(0, 0.0)       // 0
				.setValue(1, 0.0)       // 1
				.setValue(2, 0.0)       // 2
				.setValue(3, 0.0)       // 3
				.setValue(4, 0.0)       // 4
				.setValue(5, 0.0)       // 5
				.setValue(6, 0.0),      // 6
			
			new Chromosome(7)           // Hostiles
				.setValue(0, 0.0)       // 0
				.setValue(1, 0.0)       // 1
				.setValue(2, 0.0)       // 2
				.setValue(3, 0.0)       // 3
				.setValue(4, 0.0)       // 4
				.setValue(5, 0.0)       // 5
				.setValue(6, 0.0),      // 6
		},
		Utility.random(CrossoverMethod.values())
	);
	
	/** Tries to make sure it won't get captured by the enemy */
	public static DNA dodge = new DNA(
		new Chromosome[]{
			new Chromosome().setValue(0.5),     // Rosette
			new Chromosome().setValue(0.0),     // Capture
			new Chromosome().setValue(0.0),     // Enter board
			new Chromosome().setValue(0.0),     // Enter centre
			new Chromosome().setValue(0.0),     // Enter end
			new Chromosome().setValue(0.0),     // Exit board
			new Chromosome().setValue(0.0),     // Furthest
			new Chromosome().setValue(0.0),     // Closest
			
			new Chromosome(4)           // Spaces pre
				.setValue(0, 0.8)       // 1
				.setValue(1, 1.0)       // 2
				.setValue(2, 0.8)       // 3
				.setValue(3, 0.5),      // 4
			
			new Chromosome(4)           // Spaces post
				.setValue(0, 0.5)       // 1
				.setValue(1, 0.2)       // 2
				.setValue(2, 0.0)       // 3
				.setValue(3, 0.2),      // 4
			
			new Chromosome(7)           // Friendlies
				.setValue(0, 0.0)       // 0
				.setValue(1, 0.0)       // 1
				.setValue(2, 0.0)       // 2
				.setValue(3, 0.0)       // 3
				.setValue(4, 0.0)       // 4
				.setValue(5, 0.0)       // 5
				.setValue(6, 0.0),      // 6
			
			new Chromosome(7)           // Hostiles
				.setValue(0, 0.1)       // 0
				.setValue(1, 0.2)       // 1
				.setValue(2, 0.3)       // 2
				.setValue(3, 0.4)       // 3
				.setValue(4, 0.5)       // 4
				.setValue(5, 0.6)       // 5
				.setValue(6, 0.7),      // 6
		},
		Utility.random(CrossoverMethod.values())
	);
	
	/** Tries to make sure it won't get captured by the enemy */
	public static DNA boardControl = new DNA(
		new Chromosome[]{
			new Chromosome().setValue(1.0),     // Rosette
			new Chromosome().setValue(0.0),     // Capture
			new Chromosome().setValue(0.8),     // Enter board
			new Chromosome().setValue(0.6),     // Enter centre
			new Chromosome().setValue(0.6),     // Enter end
			new Chromosome().setValue(0.4),     // Exit board
			new Chromosome().setValue(0.0),     // Furthest
			new Chromosome().setValue(0.0),     // Closest
			
			new Chromosome(4)           // Spaces pre
				.setValue(0, 0.0)       // 1
				.setValue(1, 0.0)       // 2
				.setValue(2, 0.0)       // 3
				.setValue(3, 0.0),      // 4
			
			new Chromosome(4)           // Spaces post
				.setValue(0, 0.0)       // 1
				.setValue(1, 0.0)       // 2
				.setValue(2, 0.0)       // 3
				.setValue(3, 0.0),      // 4
			
			new Chromosome(7)           // Friendlies
				.setValue(0, 0.8)       // 0
				.setValue(1, 0.8)       // 1
				.setValue(2, 0.8)       // 2
				.setValue(3, 0.8)       // 3
				.setValue(4, 0.8)       // 4
				.setValue(5, 0.8)       // 5
				.setValue(6, 0.8),      // 6
			
			new Chromosome(7)           // Hostiles
				.setValue(0, 0.0)       // 0
				.setValue(1, 0.0)       // 1
				.setValue(2, 0.0)       // 2
				.setValue(3, 0.0)       // 3
				.setValue(4, 0.0)       // 4
				.setValue(5, 0.0)       // 5
				.setValue(6, 0.0),      // 6
		},
		Utility.random(CrossoverMethod.values())
	);
}
