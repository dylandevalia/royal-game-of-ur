package game.dylandevalia.royal_game_of_ur.states;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Framework;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.objects.menu.Node;
import game.dylandevalia.royal_game_of_ur.objects.ur.GameLogic;
import game.dylandevalia.royal_game_of_ur.objects.ur.Player.PlayerID;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.AI;
import game.dylandevalia.royal_game_of_ur.objects.ur.ai.DNA;
import game.dylandevalia.royal_game_of_ur.utility.Bundle;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GameUrSimulate extends AbstractState {
	
	private final int gamesPerGeneration = 5;
	private final int noGenerations = 1;
	private GameLogic[] game;
	private Node[] nodes;
	private AI[] ais = new AI[gamesPerGeneration * 2];
	private int currentGame = 0, currentGeneration = 0;
	
	@Override
	public void initialise(StateManager stateManager, Bundle bundle) {
		Log.SET_INFO();
		
		nodes = new Node[(int) Utility.mapWidth(150, 300)];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(
				Utility.randBetween(-200, Window.WIDTH + 200),
				Utility.randBetween(-200, Window.HEIGHT + 200)
			);
		}
		
		for (int i = 0; i < ais.length; i++) {
			ais[i] = new AI();
		}
		
		game = new GameLogic[gamesPerGeneration];
		for (int i = 0; i < game.length; i++) {
			game[i] = new GameLogic(false, ais[2 * i], ais[(2 * i) + 1]);
		}
		// game = new GameLogic(false, ais[0], ais[1]);
	}
	
	@Override
	public void update() {
		for (Node n : nodes) {
			n.update();
		}
		
		for (GameLogic g : game) {
			if (g.isWon()) {
				ais[currentGame * 2].setFitness(g.playerFitness(PlayerID.ONE));
				ais[(currentGame * 2) + 1].setFitness(g.playerFitness(PlayerID.TWO));
				// Log.info("AIS[" + (currentGame * 2) + "]", ais[currentGame * 2].getFitness());
				// Log.info("AIS[" + ((currentGame * 2) + 1) + "]",
				// 	ais[(currentGame * 2) + 1].getFitness());
				
				currentGame++;
				if (currentGame >= gamesPerGeneration) {
					
					currentGeneration++;
					if (currentGeneration >= noGenerations) {
						// int n = 0;
						// for (AI ai : ais) {
						// 	if (ai.getFitness() == 6) {
						// 		n++;
						// 	}
						// 	Log.info("AI", ai.toString());
						// }
						// Log.info("AI", "6s: " + n);
						
						// Write AI attributes to file and close program
						writeToFile();
						System.exit(0);
					}
					
					currentGame = 0;
				}
				
				naturalSelection();
				
				g = new GameLogic(
					false,
					ais[currentGame * 2], ais[(currentGame * 2) + 1]
				);
			}
			
			g.update(Framework.getMousePos());
		}
	}
	
	private void naturalSelection() {
		ArrayList<AI> matingPool = new ArrayList<>();
		for (AI ai : ais) {
			int n = (int) Math.floor(Utility.map(
				ai.getFitness(), -6, 6, 1, 100
			));
			for (int j = 0; j < n; j++) {
				matingPool.add(ai);
			}
		}
		
		AI[] newAis = new AI[ais.length];
		for (int i = 0; i < ais.length; i++) {
			// Pick two random parents from the mating pool
			DNA mother = matingPool.get(Utility.randBetween(0, matingPool.size() - 1)).getDna();
			DNA father = matingPool.get(Utility.randBetween(0, matingPool.size() - 1)).getDna();
			
			// Crossbreed and mutate child DNA
			DNA child = DNA.crossover(mother, father);
			child.mutate();
			
			// Apply new child DNA to an AI
			newAis[i] = new AI(child);
		}
		
		// Randomly select 2% AIs and give them random attributes
		for (int i = 0; i < 2 * (ais.length / 100); i++) {
			newAis[Utility.randBetween(0, newAis.length - 1)] = new AI();
		}
		
		// Set new child AIs
		ais = newAis;
	}
	
	private void writeToFile() {
		String datetime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		try {
			PrintWriter w = new PrintWriter("data/GeneticAI_" + datetime + ".csv", "UTF-8");
			
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
	
	@Override
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
		
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].draw(g, interpolate, nodes, i);
		}
		
		
		/* Text */
		
		g.setColor(ColorMaterial.GREY[2]);
		g.setFont(new Font("TimesRoman", Font.BOLD,
			(int) Utility.mapWidth(28, 56)
		));
		g.drawString("Current Game: " + (currentGame + 1) + " / " + gamesPerGeneration, 100, 100);
		g.drawString("Current Generation: " + (currentGeneration + 1 + " / " + noGenerations), 100,
			300);
	}
}
