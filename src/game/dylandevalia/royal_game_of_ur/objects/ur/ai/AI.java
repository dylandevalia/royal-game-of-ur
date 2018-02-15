package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

import game.dylandevalia.royal_game_of_ur.objects.ur.Board;
import game.dylandevalia.royal_game_of_ur.objects.ur.Counter;
import game.dylandevalia.royal_game_of_ur.objects.ur.Player.PlayerID;
import game.dylandevalia.royal_game_of_ur.objects.ur.Tile;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.AIController.MoveState;
import game.dylandevalia.royal_game_of_ur.utility.Pair;
import java.util.ArrayList;

public class AI {
	
	private DNA dna;
	
	public AI() {
		dna = new DNA();
	}
	
	public Counter makeMove(
		Board board,
		int diceRoll, int noDice,
		ArrayList<Pair<Counter, MoveState>> moves
	) {
		
		double[] scores = new double[moves.size()];
		Pair<Integer, Integer>
			min = new Pair<>(0, board.getRouteLength()),
			max = new Pair<>(0, -1);
		PlayerID currentPlayer = moves.get(0).getKey().getPlayer();
		
		for (int i = 0; i < moves.size(); i++) {
			Counter counter = moves.get(i).getKey();
			MoveState ms = moves.get(i).getValue();
			scores[i] = 0;
			int currentPos = counter.getCurrentRouteIndex();
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
			
			/* Spaces after enemy */
			// TODO: Check down correct route (enemy route)
			
			for (int j = 1; j <= noDice; j++) {
				Tile t = board.getTile(currentPos - j);
				if (t == null) {
					continue;
				}
				
				Counter c = t.getCounter();
				if (c == null) {
					continue;
				}
				
				if (c.getPlayer() == currentPlayer) {
					scores[i] += dna.getValue(DNA.SPACES_AFTER_ENEMY_PRE, j - 1);
				}
			}
			
			for (int j = 1; j <= noDice; j++) {
				Tile t = board.getTile(nextPos - j);
				if (t == null) {
					continue;
				}
				
				Counter c = t.getCounter();
				if (c == null) {
					continue;
				}
				
				if (c.getPlayer() == currentPlayer) {
					scores[i] += dna.getValue(DNA.SPACES_AFTER_ENEMY_POST, j - 1);
				}
			}
			
			/* Counters on the board */
			// TODO: Can cache these values
			
			int friendly = 0, hostile = 0;
			for (int j = 0; j < board.getNoTiles(); j++) {
				if (board.getTile(j).hasCounter()) {
					Counter c = board.getTile(j).getCounter();
					if (c.getPlayer() == currentPlayer) {
						friendly++;
					} else {
						hostile++;
					}
				}
			}
			
			scores[i] += dna.getValue(DNA.FRIENDLIES_ON_BOARD, friendly);
			scores[i] += dna.getValue(DNA.HOSTILES_ON_BOARD, hostile);
		}
		
		scores[min.getKey()] += dna.getValue(DNA.CLOSEST_PLACE);
		scores[max.getKey()] += dna.getValue(DNA.FURTHEST_PLACE);
		
		double maxScore = 0;
		int index = 0;
		for (int i = 0; i < scores.length; i++) {
			if (scores[i] > maxScore) {
				maxScore = scores[i];
				index = i;
			}
		}
		
		return moves.get(index).getKey();
	}
}
