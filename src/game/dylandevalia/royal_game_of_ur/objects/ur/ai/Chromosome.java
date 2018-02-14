package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

class Chromosome {
	
	private double[] values;
	
	Chromosome(double value) {
		values = new double[1];
		values[0] = value;
	}
	
	Chromosome(double[] values) {
		this.values = values;
	}
	
	double getValue() {
		return values[0];
	}
	
	void setValue(double value) {
		values[0] = value;
	}
	
	double getValue(int index) {
		return values[index];
	}
	
	double[] getValues() {
		return values;
	}
	
	void setValues(double[] values) {
		this.values = values;
	}
	
	void setValue(int index, double values) {
		this.values[index] = values;
	}
	
	int getLength() {
		return values.length;
	}
}
