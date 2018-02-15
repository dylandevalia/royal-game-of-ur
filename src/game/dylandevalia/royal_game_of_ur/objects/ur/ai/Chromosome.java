package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

class Chromosome {
	
	private double[] values;
	
	Chromosome(int noValues) {
		values = new double[noValues];
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
