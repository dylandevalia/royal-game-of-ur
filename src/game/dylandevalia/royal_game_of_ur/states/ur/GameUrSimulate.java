package game.dylandevalia.royal_game_of_ur.states.ur;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.base.Background;
import game.dylandevalia.royal_game_of_ur.objects.base.Fade;
import game.dylandevalia.royal_game_of_ur.objects.ur.GameLogic;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.AI;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.DNA;
import game.dylandevalia.royal_game_of_ur.states.AbstractState;
import game.dylandevalia.royal_game_of_ur.states.StateManager.GameState;
import game.dylandevalia.royal_game_of_ur.utility.Bundle;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The state which simulates the genetic algorithm of the Royal Game of Ur
 */
public class GameUrSimulate extends AbstractState {
	
	/** The number of agents to simulate */
	private final int noAgents = 100;
	
	/** The number of generations the simulations should go on for */
	private final int noGenerations = 1000;
	
	/** The array of agents */
	private AI[] agents = new AI[noAgents];
	
	/** The number of rounds there will be each generation */
	private final int noRounds = noAgents - 1;
	
	/** The number of teams that rotate each round */
	private final int noTeamsRotating = noAgents - 1;
	
	/** The array of rotating teams */
	private AI[] rotating = new AI[noTeamsRotating];
	
	/** The array of games being played each round */
	private GameLogic[] games;
	
	/** Variables that track the current state of the simulation */
	private int gamesCompleted = 0, currentRound = 0, currentGeneration = 0;
	
	/** Keeps track of the max fitness */
	private double maxFitness = 0, maxMaxFitness = 0, mmfGeneration = 0;
	
	@Override
	public void initialise(Bundle bundle) {
		// Log.SET_INFO();
		Log.SET_WARN();
		
		// Set background
		if (bundle != null) {
			bg = new Background(
				ColorMaterial.RED,
				(Background.Node[]) bundle.get("nodes")
			);
		} else {
			bg = new Background(ColorMaterial.RED);
		}
		
		// Set fade in
		fade = new Fade(ColorMaterial.GREY[0], ColorMaterial.GREY[0], 5, true);
		
		// Check that there are an even number of agents
		if (noAgents % 2 != 0) {
			throw new IllegalArgumentException("Number of agents must be even");
		}
		
		// Generate new random AIs
		for (int i = 0; i < agents.length; i++) {
			agents[i] = new AI();
		}
		
		// Copy all but first AI to rotating array
		System.arraycopy(agents, 1, rotating, 0, rotating.length);
		
		// Generate the first set of games
		generateNextGames();
	}
	
	@Override
	public void update() {
		bg.update();
		
		// Loop through each game
		for (GameLogic game : games) {
			// Check if the game has been won
			if (game.isWon()) {
				
				// Check if game has already been processed
				if (game.isProcessed()) {
					continue;
				}
				
				// Calculate fitness of agents and set game as processed
				game.calculatePlayerFitnesses();
				game.setProcessed(true);
				
				gamesCompleted++;
				if (gamesCompleted >= games.length) {
					// All games have been completed
					
					currentRound++;
					if (currentRound >= noRounds) {
						// All rounds have been completed
						
						currentGeneration++;
						if (currentGeneration >= noGenerations) {
							// All generations have been completed
							
							// Write AI attributes to file and close program
							writeToFile();
							System.exit(0);
						}
						
						Log.info("SIM", "New generation");
						naturalSelection();
						currentRound = 0;
					}
					
					Log.info("SIM", "New round");
					generateNextGames();
					gamesCompleted = 0;
				}
			}
			
			game.update(Vector2D.ZERO());
		}
	}
	
	/**
	 * Generates a new array of games by rotating the agents and who they are playing against
	 */
	private void generateNextGames() {
		int currentRotationAgent = currentRound % noTeamsRotating;
		
		games = new GameLogic[noAgents / 2];
		
		games[0] = new GameLogic(false, agents[0], rotating[currentRotationAgent]);
		Log.info("SIM", "0 vs " + (currentRotationAgent + 1));
		
		int halfSize = noAgents / 2;
		for (int i = 1; i < halfSize; i++) {
			int agentOne = (currentRound + noTeamsRotating - i) % noTeamsRotating;
			int agentTwo = (currentRound + i) % noTeamsRotating;
			
			games[i] = new GameLogic(false, rotating[agentOne], rotating[agentTwo]);
			Log.info("SIM", (agentOne + 1) + " vs " + (agentTwo + 1));
		}
	}
	
	/**
	 * Performs the natural selection process by creating a new set of AIs. The fitter agents of the
	 * current generation have a greater chance of being chosen as parents for the new children
	 */
	private void naturalSelection() {
		ArrayList<AI> matingPool = new ArrayList<>();
		
		// Find max fitness value
		maxFitness = 0;
		for (AI ai : agents) {
			if (ai.getFitness() > maxFitness) {
				maxFitness = ai.getFitness();
			}
		}
		if (maxFitness > maxMaxFitness) {
			maxMaxFitness = maxFitness;
			mmfGeneration = currentGeneration;
		}
		
		for (AI ai : agents) {
			// Normalise fitness
			int n = ((int) Utility.map(ai.getFitness(), 0, maxFitness, 0, 100));
			
			// Add ai to mating pool n times
			for (int j = 0; j < n; j++) {
				matingPool.add(ai);
			}
		}
		
		AI[] newAis = new AI[agents.length];
		for (int i = 0; i < agents.length; i++) {
			// Pick two random parents from the mating pool
			DNA father = Utility.random(matingPool).getDna();
			DNA mother = Utility.random(matingPool).getDna();
			
			// Crossbreed and mutate child DNA
			DNA child = DNA.crossover(mother, father);
			child.mutate();
			
			// Apply new child DNA to an AI
			newAis[i] = new AI(child);
		}
		
		// Randomly select 5% AIs and give them random attributes
		for (int i = 0; i < 5 * (agents.length / 100); i++) {
			AI chosen = Utility.random(agents);
			chosen = new AI();
		}
		
		// Set new child AIs
		agents = newAis;
	}
	
	/**
	 * Writes all the AIs' DNA data to a {@code .csv} file
	 */
	private void writeToFile() {
		String datetime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		try {
			PrintWriter w = new PrintWriter(""
				+ "data/GeneticAI_"
				+ "agents-" + agents.length
				+ "_generations-" + noGenerations
				+ "_" + datetime +
				".csv",
				"UTF-8"
			);
			
			// Headers
			w.println(""
				+ "rosette,"
				+ "capture,"
				+ "enter board,"
				+ "enter centre,"
				+ "enter end,"
				+ "exit board,"
				+ "furthest,"
				+ "closest,"
				+ "spaces pre (1),"
				+ "spaces pre (2),"
				+ "spaces pre (3),"
				+ "spaces pre (4),"
				+ "spaces post (1),"
				+ "spaces post (2),"
				+ "spaces post (3),"
				+ "spaces post (4),"
				+ "friendlies (0),"
				+ "friendlies (1),"
				+ "friendlies (2),"
				+ "friendlies (3),"
				+ "friendlies (4),"
				+ "friendlies (5),"
				+ "friendlies (6),"
				+ "hostiles (0),"
				+ "hostiles (1),"
				+ "hostiles (2),"
				+ "hostiles (3),"
				+ "hostiles (4),"
				+ "hostiles (5),"
				+ "hostiles (6),"
				+ "fitness"
			);
			
			// Values
			for (AI ai : agents) {
				w.println(ai.getDNAValues() + "," + ai.getFitness());
			}
			
			// Close file i/o
			w.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g, double interpolate) {
		/* Background */
		
		GradientPaint gradientPaint = new GradientPaint(
			-100, -100,
			ColorMaterial.RED[4],
			Window.WIDTH + 100, Window.HEIGHT + 100,
			ColorMaterial.RED[9]
		);
		g.setPaint(gradientPaint);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		bg.draw(g, interpolate);
		
		
		/* Text */
		
		g.setColor(ColorMaterial.GREY[2]);
		g.setFont(new Font("TimesRoman", Font.BOLD,
			(int) Utility.mapWidth(28, 56)
		));
		
		g.drawString(
			"Games completed: " + (gamesCompleted) + " / " + games.length,
			100, 100
		);
		g.drawString(
			"Current round: " + (currentRound + 1) + " / " + noRounds,
			100, 150
		);
		g.drawString(
			"Current Generation: " + (currentGeneration + 1) + " / " + noGenerations,
			100, 200
		);
		g.drawString(
			"Max Fitness: " + maxFitness,
			100, 500
		);
		g.drawString(
			"Max Max Fitness: " + maxMaxFitness,
			100, 550
		);
		g.drawString(
			"MMF Generation: " + mmfGeneration,
			100, 600
		);
		
		fade.draw(g);
	}
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			fade.out();
			fade.setCallback(() -> {
				stateManager.loadAndSetState(GameState.MENU_MAIN);
				stateManager.unloadState(GameState.GAME_UR_SIMULATE);
			});
		}
	}
}
