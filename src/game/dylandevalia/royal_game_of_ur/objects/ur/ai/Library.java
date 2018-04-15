package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

import game.dylandevalia.royal_game_of_ur.objects.ur.ai.DNA.CrossoverMethod;
import game.dylandevalia.royal_game_of_ur.utility.Utility;

public class Library {
	
	public static DNA thousand = new DNA(
		new Chromosome[]{
			new Chromosome().setValue(0.60144338),
			new Chromosome().setValue(0.52037024),
			new Chromosome().setValue(0.488966342),
			new Chromosome().setValue(0.423265514),
			new Chromosome().setValue(0.615562223),
			new Chromosome().setValue(0.618980456),
			new Chromosome().setValue(0.516302069),
			new Chromosome().setValue(0.426042467),
			
			new Chromosome(4)
				.setValue(0, 0.486712877)
				.setValue(1, 0.740478151)
				.setValue(2, 0.502770263)
				.setValue(3, 0.443259008),
			
			new Chromosome(4)
				.setValue(0, 0.469515113)
				.setValue(1, 0.570645719)
				.setValue(2, 0.526002198)
				.setValue(3, 0.475760949),
			
			new Chromosome(7)
				.setValue(0, 0.481394719)
				.setValue(1, 0.571627282)
				.setValue(2, 0.564610113)
				.setValue(3, 0.477490358)
				.setValue(4, 0.462273256)
				.setValue(5, 0.431268139)
				.setValue(6, 0.495770721),
			
			new Chromosome(7)
				.setValue(0, 0.483985421)
				.setValue(1, 0.483323985)
				.setValue(2, 0.518545685)
				.setValue(3, 0.481931342)
				.setValue(4, 0.490458504)
				.setValue(5, 0.518193902)
				.setValue(6, 0.51037361),
		},
		Utility.random(CrossoverMethod.values())
	);
	
	public static DNA bespoke = new DNA(
		new Chromosome[]{
			new Chromosome().setValue(0.9),
			new Chromosome().setValue(0.9),
			new Chromosome().setValue(0.6),
			new Chromosome().setValue(0.5),
			new Chromosome().setValue(0.7),
			new Chromosome().setValue(0.9),
			new Chromosome().setValue(0.5),
			new Chromosome().setValue(0.5),
			
			new Chromosome(4)
				.setValue(0, 0.1)
				.setValue(1, 0.6)
				.setValue(2, 0.4)
				.setValue(3, 0.1),
			
			new Chromosome(4)
				.setValue(0, 0.3)
				.setValue(1, 0.1)
				.setValue(2, 0.3)
				.setValue(3, 0.5),
			
			new Chromosome(7)
				.setValue(0, 0.9)
				.setValue(1, 0.8)
				.setValue(2, 0.7)
				.setValue(3, 0.6)
				.setValue(4, 0.5)
				.setValue(5, 0.4)
				.setValue(6, 0.3),
			
			new Chromosome(7)
				.setValue(0, 0.1)
				.setValue(1, 0.2)
				.setValue(2, 0.3)
				.setValue(3, 0.4)
				.setValue(4, 0.5)
				.setValue(5, 0.6)
				.setValue(6, 0.7),
		},
		Utility.random(CrossoverMethod.values())
	);
}
