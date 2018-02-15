package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

import game.dylandevalia.royal_game_of_ur.objects.ur.Board;
import game.dylandevalia.royal_game_of_ur.objects.ur.Counter;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.AIController.MoveState;
import game.dylandevalia.royal_game_of_ur.utility.Pair;
import java.util.ArrayList;

public class AI {
	
	private DNA dna;
	
	public AI() {
		dna = new DNA();
	}
	
	public Counter makeMove(Board board, int diceRoll, ArrayList<Pair<Counter, MoveState>> moves) {
		double[] scores = new double[moves.size()];
		Pair<Integer, Integer>
			min = new Pair<>(0, board.getRouteLength()),
			max = new Pair<>(0, -1);
		
		for (int i = 0; i < moves.size(); i++) {
			Counter counter = moves.get(i).getKey();
			MoveState ms = moves.get(i).getValue();
			scores[i] = 0;
			int currentPos = counter.currentRouteIndex;
			int nextPos = currentPos + diceRoll;
			
			/* Entering board segments*/
			
			if (currentPos < 0) {
				scores[i] += dna.getValue(DNA.ENTER_BOARD);
			} else if (
				currentPos < board.getStartLen()
					&& nextPos >= board.getStartLen()
					&& nextPos < board.getMidLen()
				) {
				scores[i] += dna.getValue(DNA.ENTER_CENTER);
			} else if (
				currentPos < board.getMidLen()
					&& nextPos >= board.getMidLen()
					&& nextPos < board.getRouteLength()
				) {
				scores[i] += dna.getValue(DNA.ENTER_END);
			}
			
			/* Rosette */
			
			if (board.getTile(nextPos).isRosette()) {
				scores[i] += dna.getValue(DNA.ROSETTE);
			}
			
			/* Capture or exit board */
			
			if (ms == MoveState.CAPTURE) {
				scores[i] += dna.getValue(DNA.CAPTURE);
			} else if (ms == MoveState.END) {
				scores[i] += dna.getValue(DNA.EXIT_BOARD);
			}
			
			/* Min max positions */
			
			if (nextPos < min.getValue()) {
				min.setKey(i);
				min.setValue(nextPos);
			}
			
			if (nextPos > max.getValue()) {
				max.setKey(i);
				max.setValue(nextPos);
			}
		}
		
		scores[min.getKey()] += dna.getValue(DNA.CLOSEST_PLACE);
		scores[max.getKey()] += dna.getValue(DNA.FURTHEST_PLACE);
		
		return moves.get(0).getKey();
	}
}
