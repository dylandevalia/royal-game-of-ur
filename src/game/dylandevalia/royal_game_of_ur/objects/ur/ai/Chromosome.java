package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

class Chromosome {
	
	private double[] values;
	
	Chromosome() {
	
	}
	
	Chromosome(double value) {
		values = new double[1];
		values[0] = value;
	}
	
	double getValue() {
		return values[0];
	}
	
	double getValue(int index) {
		return values[index];
	}
	
	void setValue(int index, double values) {
		this.values[index] = values;
	}
	
	int getLength() {
		return values.length;
	}
}
