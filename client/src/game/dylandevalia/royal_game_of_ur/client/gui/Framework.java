package game.dylandevalia.royal_game_of_ur.client.gui;

import game.dylandevalia.royal_game_of_ur.client.Game;
import game.dylandevalia.royal_game_of_ur.client.states.StateManager;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Framework extends Canvas {
	public static int frameWidth, frameHeight;
	
	/* Constants */
	private static final long NS_A_SEC = 1000000000;
	private static final long MS_A_SEC = 1000000;
	
	/* Game updates */
	// Should the game loop run
	private boolean runGame = true;
	// How often the game should update a second
	private static final double GAME_HRETZ = 30.0;
	// How many times the game should render a second
	private static final double TARGET_FPS = 60.0;
	// How many nanoseconds it should take to reach the target speed
	private static final double TIME_BETWEEN_UPDATES = NS_A_SEC / GAME_HRETZ;
	// How many nanoseconds it should take to render our target FPS
	private static final double TARGET_TIME_BETWEEN_RENDERS = NS_A_SEC / TARGET_FPS;
	// Maximum number of updates before forced render
	// Set to `1` for perfect rendering
	private static final int MAX_UPDATES_BEFORE_RENDER = 5;
	// (ie. deltaTime) Used to calculate positions for rendering
	private double interpolate;
	
	private Game game = new Game();
	
	public Framework() {
		super();
		
		game.stateManager.initState(StateManager.GameState.MAIN_MENU);
		game.stateManager.setState(StateManager.GameState.MAIN_MENU);
		
		new Thread() {
			@Override
			public void run() {
				gameLoop();
			}
		}.start();
		
//		Window window = (Window) SwingUtilities.getWindowAncestor(this);
//		window.fullscreen = true;
	}
	
	private void gameLoop() {
		int fps = 60;
		int frameCount = 0;
		
		// Last time the game was updated/rendered
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();
		
		// Simple way to find fps
		int lastSecondTime = (int) (lastUpdateTime / NS_A_SEC);
		
//		setup();
		
		while (runGame) {
			/* Update game */
			
			double now = System.nanoTime();
			int updateCount = 0;
			
			// Do as many updates as required - may need to play catchup
			while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
				update();
				lastUpdateTime += TIME_BETWEEN_UPDATES;
				updateCount++;
			}
			
			// If an update takes a long time, skip ahead to avoid lots of catchup
			if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
				lastUpdateTime = now - TIME_BETWEEN_UPDATES;
			}
			
			/* Render game */
			
			// Calculate interpolation for smoother render
			interpolate = Math.min(1.0, (now - lastUpdateTime) / TIME_BETWEEN_UPDATES);
			repaint();
			lastRenderTime = now;
			
			// Update frame
			int curSecond = (int) (lastUpdateTime / NS_A_SEC);
			if (curSecond > lastSecondTime) {
				fps = frameCount;
				frameCount = 0;
				lastSecondTime = curSecond;
			}
			
			while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
					&& now - lastUpdateTime < TIME_BETWEEN_UPDATES)
			{
				Thread.yield();
				now = System.nanoTime();
			}
		}
	}
	
	public void update() {
		game.stateManager.update();
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		game.stateManager.draw(g2d, interpolate);
	}
	
	public void keyReleasedFramework(KeyEvent e) {
		game.stateManager.keyReleased(e);
	}
}
