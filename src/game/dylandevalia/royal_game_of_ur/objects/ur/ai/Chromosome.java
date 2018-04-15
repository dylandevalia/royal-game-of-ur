package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

/**
 * A single chromosome that holds an array of values. Can be used and access with a single value
 * also
 */
class Chromosome {
	
	private double[] values;
	
	Chromosome(int noValues) {
		values = new double[noValues];
	}
	
	Chromosome() {
		this(1);
	}
	
	double getValue() {
		return values[0];
	}
	
	double getValue(int index) {
		return values[index];
	}
	
	Chromosome setValue(double value) {
		values[0] = value;
		return this;
	}
	
	Chromosome setValue(int index, double values) {
		this.values[index] = values;
		return this;
	}
	
	int getLength() {
		return values.length;
	}
}
