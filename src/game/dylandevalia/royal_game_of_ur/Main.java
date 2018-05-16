package game.dylandevalia.royal_game_of_ur;

import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import javax.swing.SwingUtilities;

public class Main {
	
	public static void main(String[] args) {
		// roundRobin();
		// System.exit(0);
		Log.SET_DEBUG();
		
		// Start running the gui in its own thread
		SwingUtilities.invokeLater(Window::new);
	}
	
	private static void roundRobin() {
		int noGames = 0;
		
		int[] teams = new int[10];
		for (int i = 0; i < teams.length; i++) {
			teams[i] = i + 1;
		}
		
		int noRounds = teams.length - 1;
		int halfSize = teams.length / 2;
		
		int[] rotating = new int[teams.length - 1];
		System.arraycopy(teams, 1, rotating, 0, rotating.length);
		
		int teamsRotating = rotating.length;
		
		for (int round = 0; round < noRounds; round++) {
			System.out.println("Round: " + (round + 1) + " / " + (noRounds));
			
			int teamIdx = round % teamsRotating;
			// System.out.println("    " + teams[0] + " vs " + rotating[teamIdx]);
			System.out.printf("    %3d vs %-3d\n", teams[0], rotating[teamIdx]);
			noGames++;
			
			for (int idx = 1; idx < halfSize; idx++) {
				int teamOne = (round + teamsRotating - idx) % teamsRotating;
				int teamTwo = (round + idx) % teamsRotating;
				// System.out.println("    " + rotating[teamOne] + " vs " + rotating[teamTwo]);
				System.out.printf("    %3d vs %-3d\n", rotating[teamOne], rotating[teamTwo]);
				noGames++;
			}
		}
		
		System.out.println("\nNumber of Games: " + noGames);
	}
}
