package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

public class Library {
	
	/** DNA of the most fit agent after 1000 generation of genetic evolution */
	public static DNA thousand_116 = new DNA(
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
		null // Utility.random(CrossoverMethod.values())
	);
	
	/** DNA of the most fit agent after 1000 generation of genetic evolution */
	public static DNA thousand_126 = new DNA(
		new Chromosome[]{
			new Chromosome().setValue(0.605758402),     // Rosette
			new Chromosome().setValue(0.523174481),     // Capture
			new Chromosome().setValue(0.500268245),     // Enter board
			new Chromosome().setValue(0.344588582),     // Enter centre
			new Chromosome().setValue(0.497346305),     // Enter end
			new Chromosome().setValue(0.674548541),     // Exit board
			new Chromosome().setValue(0.426701936),     // Furthest
			new Chromosome().setValue(0.493249610),     // Closest
			
			new Chromosome(4)                   // Spaces pre
				.setValue(0, 0.529455298)       // 1
				.setValue(1, 0.473901674)       // 2
				.setValue(2, 0.583292383)       // 3
				.setValue(3, 0.488080683),      // 4
			
			new Chromosome(4)                   // Spaces post
				.setValue(0, 0.467637820)       // 1
				.setValue(1, 0.575382447)       // 2
				.setValue(2, 0.554045752)       // 3
				.setValue(3, 0.524770723),      // 4
			
			new Chromosome(7)                   // Friendlies
				.setValue(0, 0.465120890)       // 0
				.setValue(1, 0.487829901)       // 1
				.setValue(2, 0.438037277)       // 2
				.setValue(3, 0.472117174)       // 3
				.setValue(4, 0.505623473)       // 4
				.setValue(5, 0.479678042)       // 5
				.setValue(6, 0.466345906),      // 6
			
			new Chromosome(7)                   // Hostiles
				.setValue(0, 0.432810145)       // 0
				.setValue(1, 0.535213707)       // 1
				.setValue(2, 0.502852158)       // 2
				.setValue(3, 0.455821194)       // 3
				.setValue(4, 0.554259602)       // 4
				.setValue(5, 0.499455278)       // 5
				.setValue(6, 0.513466143),      // 6
		},
		null // Utility.random(CrossoverMethod.values())
	);
	
	/** The most fit agent after 1000 generation of genetic evolution normalised between 0 and 1 */
	public static DNA thousandNormalised_1 = new DNA(
		new Chromosome[]{
			new Chromosome().setValue(0.893953587),     // Rosette
			new Chromosome().setValue(0.508743716),     // Capture
			new Chromosome().setValue(0.346916918),     // Enter board
			new Chromosome().setValue(0.051486916),     // Enter centre
			new Chromosome().setValue(0.894166836),     // Enter end
			new Chromosome().setValue(1.000000000),     // Exit board
			new Chromosome().setValue(0.462506467),     // Furthest
			new Chromosome().setValue(0.000000000),     // Closest
			
			new Chromosome(4)                   // Spaces pre
				.setValue(0, 0.300938533)       // 1
				.setValue(1, 0.314748887)       // 2
				.setValue(2, 0.419277375)       // 3
				.setValue(3, 0.402077413),      // 4
			
			new Chromosome(4)                   // Spaces post
				.setValue(0, 0.248520398)       // 1
				.setValue(1, 0.780454544)       // 2
				.setValue(2, 0.580995492)       // 3
				.setValue(3, 0.259642285),      // 4
			
			new Chromosome(7)                   // Friendlies
				.setValue(0, 0.301994657)       // 0
				.setValue(1, 0.465020696)       // 1
				.setValue(2, 0.641278876)       // 2
				.setValue(3, 0.326816464)       // 3
				.setValue(4, 0.260962700)       // 4
				.setValue(5, 0.267113558)       // 5
				.setValue(6, 0.24937336),      // 6
			
			new Chromosome(7)                   // Hostiles
				.setValue(0, 0.338081555)       // 0
				.setValue(1, 0.337793706)       // 1
				.setValue(2, 0.483821660)       // 2
				.setValue(3, 0.293224276)       // 3
				.setValue(4, 0.285118748)       // 4
				.setValue(5, 0.477385978)       // 5
				.setValue(6, 0.410467913),      // 6
		},
		null // Utility.random(CrossoverMethod.values())
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
		null // Utility.random(CrossoverMethod.values())
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
		null // Utility.random(CrossoverMethod.values())
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
		null // Utility.random(CrossoverMethod.values())
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
		null // Utility.random(CrossoverMethod.values())
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
		null // Utility.random(CrossoverMethod.values())
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
		null // Utility.random(CrossoverMethod.values())
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
		null // Utility.random(CrossoverMethod.values())
	);
}
