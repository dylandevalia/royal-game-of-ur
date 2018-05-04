package game.dylandevalia.royal_game_of_ur.states;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Framework;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.base.Background;
import game.dylandevalia.royal_game_of_ur.objects.base.Background.Node;
import game.dylandevalia.royal_game_of_ur.objects.ur.GameLogic;
import game.dylandevalia.royal_game_of_ur.objects.ur.Player.PlayerID;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.AI;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.DNA;
import game.dylandevalia.royal_game_of_ur.states.StateManager.GameState;
import game.dylandevalia.royal_game_of_ur.utility.Bundle;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import java.awt.Color;
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

public class GameUrSimulate implements IState {
	
	private StateManager stateManager;
	
	private final int gamesPerGeneration = 50;
	private final int noGenerations = 1000;
	private GameLogic[] games;
	private Background bg;
	private AI[] ais = new AI[gamesPerGeneration * 2];
	private int currentGame = 0, currentGeneration = 0;
	private double maxFitness = 0, maxMaxFitness = 0, mmfGeneration = 0;
	
	private int fade = 255;
	private boolean fadeDown = true;
	
	public void initialise(StateManager stateManager, Bundle bundle) {
		Log.SET_INFO();
		
		this.stateManager = stateManager;
		
		if (bundle != null) {
			bg = new Background(
				ColorMaterial.RED,
				(Node[]) bundle.get("nodes")
			);
		} else {
			bg = new Background(ColorMaterial.RED);
		}
		
		for (int i = 0; i < ais.length; i++) {
			ais[i] = new AI();
		}
		
		games = new GameLogic[gamesPerGeneration];
		for (int i = 0; i < games.length; i++) {
			games[i] = new GameLogic(false, ais[2 * i], ais[(2 * i) + 1]);
		}
	}
	
	public void onSet(Bundle bundle) {
	
	}
	
	public void update() {
		if (fadeDown) {
			if ((fade -= 5) < 0) {
				fade = 0;
			}
		} else {
			if ((fade += 10) > 255) {
				fade = 255;
				stateManager.loadState(GameState.MAIN_MENU);
				stateManager.setState(GameState.MAIN_MENU);
				stateManager.unloadState(GameState.GAME_UR_SIMULATE);
			}
		}
		
		bg.update();
		
		for (int i = 0; i < games.length; i++) {
			if (games[i].isWon()) {
				// If this game has already been checked, continue
				if (ais[i * 2].getFitness() >= 0) {
					continue;
				}
				
				ais[i * 2].setFitness(games[i].playerFitness(PlayerID.ONE));
				ais[(i * 2) + 1].setFitness(games[i].playerFitness(PlayerID.TWO));
				
				currentGame++;
				if (currentGame >= gamesPerGeneration) {
					
					currentGeneration++;
					if (currentGeneration >= noGenerations) {
						// Write AI attributes to file and close program
						writeToFile();
						System.exit(0);
					}
					
					// Cross breed new AIs
					naturalSelection();
					
					// Create new games with new AIs
					for (int j = 0; j < games.length; j++) {
						games[j] = new GameLogic(false, ais[j * 2], ais[(j * 2) + 1]);
					}
					
					currentGame = 0;
				}
			}
			
			games[i].update(Framework.getMousePos());
		}
	}
	
	private void naturalSelection() {
		ArrayList<AI> matingPool = new ArrayList<>();
		
		// Find max fitness value
		maxFitness = 0;
		for (AI ai : ais) {
			if (ai.getFitness() > maxFitness) {
				maxFitness = ai.getFitness();
			}
		}
		if (maxFitness > maxMaxFitness) {
			maxMaxFitness = maxFitness;
			mmfGeneration = currentGeneration;
		}
		
		for (AI ai : ais) {
			// Normalise fitness
			int n = ((int) Utility.map(ai.getFitness(), 0, maxFitness, 0, 100));
			
			// Add ai to mating pool n times
			for (int j = 0; j < n; j++) {
				matingPool.add(ai);
			}
		}
		
		AI[] newAis = new AI[ais.length];
		for (int i = 0; i < ais.length; i++) {
			// Pick two random parents from the mating pool
			DNA father = Utility.random(matingPool).getDna();
			DNA mother = Utility.random(matingPool).getDna();
			
			// Crossbreed and mutate child DNA
			DNA child = DNA.crossover(mother, father);
			child.mutate();
			
			// Apply new child DNA to an AI
			newAis[i] = new AI(child);
		}
		
		// Randomly select 2% AIs and give them random attributes
		for (int i = 0; i < 5 * (ais.length / 100); i++) {
			AI chosen = Utility.random(ais);
			chosen = new AI();
		}
		
		// Set new child AIs
		ais = newAis;
	}
	
	private void writeToFile() {
		String datetime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		try {
			PrintWriter w = new PrintWriter(""
				+ "data/GeneticAI_"
				+ "agents-" + ais.length
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
			for (AI ai : ais) {
				w.println(ai.getDNAValues() + "," + ai.getFitness());
			}
			
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
			"Current Game: " + (currentGame + 1) + " / " + gamesPerGeneration,
			100, 100
		);
		g.drawString(
			"Current Generation: " + (currentGeneration + 1 + " / " + noGenerations),
			100, 300
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
		
		g.setColor(new Color(255, 255, 255, fade));
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
	}
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			fadeDown = false;
		}
	}
}
