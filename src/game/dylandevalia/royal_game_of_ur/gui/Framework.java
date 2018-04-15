package game.dylandevalia.royal_game_of_ur.gui;

import game.dylandevalia.royal_game_of_ur.states.StateManager;
import game.dylandevalia.royal_game_of_ur.utility.Log;
import game.dylandevalia.royal_game_of_ur.utility.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The main logic of the window, implementing the objects loop and calls for the updates and draws
 * which are passed onto the state manager
 */
public class Framework extends Canvas {
	
	/* Constants */
	
	/** How often the objects should update a second */
	public static final double GAME_HERTZ = 30.0;
	
	/** The number of nanoseconds in a second */
	private static final long NS_A_SEC = 1000000000;
	
	/** How many times the objects should render a second */
	private static final double TARGET_FPS = 60.0;
	
	/** How many nanoseconds it should take to reach the target speed */
	private static final double TIME_BETWEEN_UPDATES = NS_A_SEC / GAME_HERTZ;
	
	/** How many nanoseconds it should take to render our target FPS */
	private static final double TARGET_TIME_BETWEEN_RENDERS = NS_A_SEC / TARGET_FPS;
	
	/** Maximum number of updates before forced render Set to {@code 1} for perfect rendering */
	private static final int MAX_UPDATES_BEFORE_RENDER = 5;
	
	/** Position of the mouse - updated in {@link #update()} */
	private static Vector2D mousePos = new Vector2D();
	
	/** Number of frames since start of loop */
	private static int frameCount = 0;
	
	/** The manager which handles the states of the program */
	private StateManager stateManager = new StateManager();
	
	
	/* Game updates */
	
	/** Should the objects loop run */
	private boolean runGame = true;
	
	/** Used to calculate positions for rendering (ie. deltaTime) */
	private double interpolate;
	
	
	Framework() {
		super();
		
		// Creates the main menu state and sets it to the active state
		stateManager.loadState(StateManager.GameState.MAIN_MENU);
		stateManager.setState(StateManager.GameState.MAIN_MENU);
		
		// Stats the objects loop in its own thread
		new Thread(this::gameLoop).start();
	}
	
	/**
	 * Simple getter to return the mouse position which is calculated in update. Returns a copy so
	 * functions don't accidentally change the value
	 *
	 * @return The mouse position
	 */
	public static Vector2D getMousePos() {
		return mousePos.copy();
	}
	
	/**
	 * Returns the number of frames since the start of the program
	 *
	 * @return Number of frames since beginning of canvas
	 */
	public static int getFrameCount() {
		return frameCount;
	}
	
	/**
	 * Runs the main objects (game) loop
	 */
	private void gameLoop() {
		AtomicInteger fps = new AtomicInteger();
		AtomicInteger frameCount = new AtomicInteger();
		
		// Last time the objects was updated/rendered
		double lastUpdateTime = System.nanoTime();
		long lastRenderTime = System.nanoTime();
		
		// Simple way to find fps
		AtomicInteger lastSecondTime = new AtomicInteger((int) (lastUpdateTime / NS_A_SEC));
		
		while (runGame) {
			
			
			/* Update objects */
			
			long now = System.nanoTime();
			AtomicInteger updateCount = new AtomicInteger();
			
			// Do as many updates as required - may need to play catchup
			while (
				now - lastUpdateTime > TIME_BETWEEN_UPDATES
					&& updateCount.get() < MAX_UPDATES_BEFORE_RENDER
				) {
				update();
				lastUpdateTime += TIME_BETWEEN_UPDATES;
				updateCount.getAndIncrement();
			}
			
			// If an update takes a long time, skip ahead to avoid lots of catchup
			if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
				lastUpdateTime = now - TIME_BETWEEN_UPDATES;
			}
			
			
			/* Render objects */
			
			// Calculate interpolation for smoother render
			interpolate = Math.min(1.0, (now - lastUpdateTime) / TIME_BETWEEN_UPDATES);
			repaint();
			lastRenderTime = now;
			frameCount.getAndIncrement();
			
			// Update frame
			AtomicInteger curSecond = new AtomicInteger((int) (lastUpdateTime / NS_A_SEC));
			if (curSecond.get() > lastSecondTime.get()) {
				Log.trace("objects loop", "FPS: " + fps);
				fps.set(frameCount.get());
				frameCount.set(0);
				lastSecondTime.set(curSecond.get());
			}
			
			// Yield CPU time so that we don't take up all the processing time
			while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
				&& now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
				Thread.yield();
				now = System.nanoTime();
			}
		}
	}
	
	/**
	 * Update state and mouse position Try catch used as location on screen might not have been
	 * instantiated yet
	 */
	private void update() {
		frameCount++;
		try {
			Point mouse = MouseInfo.getPointerInfo().getLocation();
			mousePos.set(
				mouse.x - getLocationOnScreen().x,
				mouse.y - getLocationOnScreen().y
			);
		} catch (IllegalComponentStateException | NullPointerException e) {
			Log.error("FRAMEWORK", "Couldn't get mouse position"/*, e*/);
		}
		stateManager.update();
	}
	
	/**
	 * Draw state Passes in interpolate value as well to allow smooth motion
	 *
	 * @param g The graphics2d object to draw onto
	 */
	@Override
	public void draw(Graphics2D g) {
		g.setColor(new Color(0, 0, 0));
		g.drawRect(0, 0, Window.WIDTH, Window.HEIGHT);
		stateManager.draw(g, interpolate);
	}
	
	@Override
	public void keyPressedFramework(KeyEvent e) {
		stateManager.keyPressed(e);
	}
	
	@Override
	public void keyReleasedFramework(KeyEvent e) {
		stateManager.keyReleased(e);
	}
	
	@Override
	public void mousePressedFramework(MouseEvent e) {
		stateManager.mousePressed(e);
	}
	
	@Override
	public void mouseReleasedFramework(MouseEvent e) {
		stateManager.mouseReleased(e);
	}
}
