package game.dylandevalia.royal_game_of_ur.objects.ur.ai;

import game.dylandevalia.royal_game_of_ur.objects.ur.Counter;
import game.dylandevalia.royal_game_of_ur.objects.ur.GameLogic;
import game.dylandevalia.royal_game_of_ur.objects.ur.Player.PlayerID;
import game.dylandevalia.royal_game_of_ur.objects.ur.Tile;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.AIController.MoveState;
import game.dylandevalia.royal_game_of_ur.utility.Pair;
import java.util.ArrayList;

/**
 * The AI component of a player which is used to calculate the player's best available move
 */
public class AI {
	
	/** The AI's {@link DNA} */
	private DNA dna;
	
	private int fitness = 0;
	
	public AI() {
		dna = new DNA();
	}
	
	public AI(DNA dna) {
		this.dna = dna;
	}
	
	/**
	 * Calculates a move based on the AI's {@link DNA} and {@link Chromosome} values
	 *
	 * @param game  The game logic object which contains the rest of the game information
	 * @param moves The list of moves that the AI can make
	 * @return The best move according to the AI's DNA
	 */
	public Counter makeMove(
		GameLogic game,
		ArrayList<Pair<Counter, MoveState>> moves
	) {
		// TODO: Normalise sum by scenarios hit
		
		// If only one possible move, return counter
		if (moves.size() == 1) {
			return moves.get(0).getKey();
		}
		
		int[] noScenarios = new int[moves.size()];
		double[] scores = new double[moves.size()];
		// Pair<counter index, position>
		Pair<Integer, Integer> min, max = min = new Pair<>(
			0,
			moves.get(0).getKey().getCurrentRouteIndex()
		);
		PlayerID currentPlayer = moves.get(0).getKey().getPlayer();
		
		for (int i = 0; i < moves.size(); i++) {
			Counter counter = moves.get(i).getKey();
			MoveState ms = moves.get(i).getValue();
			scores[i] = 0;
			noScenarios[i] = 0;
			int currentPos = counter.getCurrentRouteIndex();
			int nextPos = currentPos + game.getCurrentRoll();
			
			
			/* Entering board segments*/
			
			if (currentPos < 0) {
				scores[i] += dna.getValue(DNA.ENTER_BOARD);
				noScenarios[i]++;
			} else if (
				currentPos < game.getBoard().getStartLen()
					&& nextPos >= game.getBoard().getStartLen()
					&& nextPos < game.getBoard().getMidLen()
				) {
				
				scores[i] += dna.getValue(DNA.ENTER_CENTER);
				noScenarios[i]++;
			} else if (
				currentPos < game.getBoard().getMidLen()
					&& nextPos >= game.getBoard().getMidLen()
					&& nextPos < game.getBoard().getRouteLength()
				) {
				scores[i] += dna.getValue(DNA.ENTER_END);
				noScenarios[i]++;
			}
			
			
			/* Rosette */
			
			if (game.getBoard().getTile(nextPos).isRosette()) {
				scores[i] += dna.getValue(DNA.ROSETTE);
				noScenarios[i]++;
			}
			
			
			/* Capture or exit board */
			
			if (ms == MoveState.CAPTURE) {
				scores[i] += dna.getValue(DNA.CAPTURE);
				noScenarios[i]++;
			} else if (ms == MoveState.END) {
				scores[i] += dna.getValue(DNA.EXIT_BOARD);
				noScenarios[i]++;
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
			
			for (int j = 1; j <= game.getDice().getNoDice(); j++) {
				if (currentPos - j < 0) {
					continue;
				}
				Tile t = game.getOtherPlayer().getRoute()[currentPos - j];
				
				Counter c = t.getCounter();
				if (c == null) {
					continue;
				}
				
				if (c.getPlayer() == currentPlayer) {
					scores[i] += dna.getValue(DNA.SPACES_AFTER_ENEMY_PRE, j - 1);
					noScenarios[i]++;
				}
			}
			
			for (int j = 1; j <= game.getDice().getNoDice(); j++) {
				if (nextPos - j < 0) {
					continue;
				}
				Tile t = game.getOtherPlayer().getRoute()[nextPos - j];
				
				Counter c = t.getCounter();
				if (c == null) {
					continue;
				}
				
				if (c.getPlayer() == currentPlayer) {
					scores[i] += dna.getValue(DNA.SPACES_AFTER_ENEMY_POST, j - 1);
					noScenarios[i]++;
				}
			}
			
			
			/* Counters on the board */
			// TODO: Can cache these values
			
			int friendly = 0, hostile = 0;
			for (int j = 0; j < game.getBoard().getNoTiles(); j++) {
				if (game.getBoard().getTile(j).hasCounter()) {
					Counter c = game.getBoard().getTile(j).getCounter();
					if (c.getPlayer() == currentPlayer) {
						friendly++;
					} else {
						hostile++;
					}
				}
			}
			
			scores[i] += dna.getValue(DNA.FRIENDLIES_ON_BOARD, friendly);
			noScenarios[i]++;
			scores[i] += dna.getValue(DNA.HOSTILES_ON_BOARD, hostile);
			noScenarios[i]++;
		}
		
		scores[min.getKey()] += dna.getValue(DNA.CLOSEST_PLACE);
		noScenarios[min.getKey()]++;
		scores[max.getKey()] += dna.getValue(DNA.FURTHEST_PLACE);
		noScenarios[max.getKey()]++;
		
		// Normalise values
		for (int i = 0; i < scores.length; i++) {
			if (noScenarios[i] > 0) {
				scores[i] /= noScenarios[i];
			}
		}
		
		// Pick highest score counter
		double maxScore = 0;
		int index = 0;
		for (int i = 0; i < scores.length; i++) {
			if (scores[i] > maxScore) {
				maxScore = scores[i];
				index = i;
			}
		}
		
		// Return highest scoring counter
		return moves.get(index).getKey();
	}
	
	public int getFitness() {
		return fitness;
	}
	
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}
	
	public DNA getDna() {
		return dna;
	}
	
	@Override
	public String toString() {
		return dna.toString();
	}
	
	public String getDNAValues() {
		return dna.getValues();
	}
}
