package game.dylandevalia.royal_game_of_ur.objects.ai;

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
	
	void setValue(double value) {
		values[0] = value;
	}
	
	void setValue(int index, double values) {
		this.values[index] = values;
	}
	
	int getLength() {
		return values.length;
	}
}
