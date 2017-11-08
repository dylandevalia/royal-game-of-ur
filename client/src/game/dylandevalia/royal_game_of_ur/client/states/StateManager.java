package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.client.game.Game;
import game.dylandevalia.royal_game_of_ur.utility.Log;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Controls the creation, initialising, activating/swapping and destroying of states.
 * Passes functions onto the currently active state.
 * State objects are implemented from the 'State' interface
 * {@link State}
 */
public class StateManager {
	// Static to give all states a new id in array
	private static int stateIndexCounter = 0;
	
	/**
	 * Enum used to store states. Takes the state class in constructor and
	 * generates its own id from the static 'stateIndexCounter' to be used
	 * in the array of states 'loadedStates'
	 */
	public enum GameState {
		MAIN_MENU(MainMenu.class), PLAY(Play.class), PAUSE(Pause.class);
		
		private int index;
		private Class obj;
		GameState(Class obj) {
			this.index = stateIndexCounter++;
			this.obj = obj;
		}
		int getIndex() {
			return index;
		}
		Class getObj() {
			return obj;
		}
	}
	
	// Array of loaded states
	private final State[] loadedStates = new State[GameState.values().length];
	// The currently active state
	private State currentState;
	
	// Game object
	private Game game;
	
	public StateManager(Game game) {
		this.game = game;
	}
	
	/**
	 * Creates the state in the array and calls the state's initialise function
	 * @param state The states to be initialise
	 */
	public void initState(GameState state) {
		try {
			int index = state.getIndex();
			loadedStates[index] = (State) state.getObj().newInstance();
			loadedStates[index].initialise(game);
		} catch (Exception e) {
			Log.error("State manager", "Error trying to create new instance of state", e);
		}
	}
	
	/**
	 * Sets the given state as the active state
	 * @param state The state to become active
	 */
	public void setState(GameState state) {
		if (loadedStates[state.getIndex()] == null) {
			Log.error("State manager", "State not loaded!");
			return;
		}
		currentState = loadedStates[state.getIndex()];
	}
	
	/**
	 * Unloads the state from memory
	 * @param state The state to be deleted
	 */
	public void unloadState(GameState state) {
		loadedStates[state.getIndex()] = null;
	}
	
	/**
	 * Checks to see if the given states is loaded
	 * @param state The state to check if it's loaded
	 * @return  Boolean if the state is loaded
	 */
	public boolean isLoaded(GameState state) {
		return loadedStates[state.getIndex()] != null;
	}
	
	
	/*              Passers             */
	/* Calls the currently active state */
	
	public void initialise() {
		currentState.initialise(game);
	}
	
	public void update() {
		currentState.update();
	}
	
	public void draw(Graphics2D g2d, double interpolate) {
		currentState.draw(g2d, interpolate);
	}
	
	public void keyPressed(KeyEvent e) {
		currentState.keyPressed(e);
	}
	public void keyReleased(KeyEvent e) {
		currentState.keyReleased(e);
	}
	public void mousePressed(MouseEvent e) {
		currentState.mousePressed(e);
	}
	public void mouseReleased(MouseEvent e) {
		currentState.mouseReleased(e);
	}
}
