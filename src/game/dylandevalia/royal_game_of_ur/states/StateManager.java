package game.dylandevalia.royal_game_of_ur.states;

import game.dylandevalia.royal_game_of_ur.utility.Log;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Controls the creation, initialising, activating/swapping and destroying of states.
 * Passes functions onto the currently active state.
 * IState objects are implemented from the 'IState' interface
 * {@link IState}
 */
public class StateManager {
	
	// Static to give all states a new id in array
	private static int stateIndexCounter = 0;
	// Array of loaded states
	private final IState[] loadedStates = new IState[GameState.values().length];
	// The currently active state
	private IState currentState;
	
	/**
	 * Creates the state in the array and calls the state's initialise function
	 *
	 * @param state The states to be initialise
	 */
	public void loadState(GameState state) {
		try {
			int index = state.getIndex();
			loadedStates[index] = (IState) state.getObj().newInstance();
			loadedStates[index].initialise(this);
			Log.info("STATE MANAGER", "Loaded " + state);
		} catch (Exception e) {
			Log.error("STATE MANAGER", "Error trying to create new instance of state", e);
		}
	}
	
	/**
	 * Sets the given state as the active state
	 *
	 * @param state The state to become active
	 */
	public void setState(GameState state) {
		if (loadedStates[state.getIndex()] == null) {
			Log.error("STATE MANAGER", "State not loaded!");
			return;
		}
		currentState = loadedStates[state.getIndex()];
		Log.info("STATE MANAGER", "Set " + state);
	}
	
	/**
	 * Unloads the state from memory
	 *
	 * @param state The state to be deleted
	 */
	public void unloadState(GameState state) {
		loadedStates[state.getIndex()] = null;
		Log.info("STATE MANAGER", "Unloaded " + state);
	}
	
	/**
	 * Checks to see if the given states is loaded
	 *
	 * @param state The state to check if it's loaded
	 * @return Boolean if the state is loaded
	 */
	public boolean isLoaded(GameState state) {
		return loadedStates[state.getIndex()] != null;
	}
	
	public void reinitialise() {
		currentState.initialise(this);
		Log.info("STATE MANAGER", "Re-Initialised " + currentState);
	}
	
	/*              Passers             */
	/* Calls the currently active state */
	
	public void update() {
		currentState.update();
	}
	
	public void draw(Graphics2D g, double interpolate) {
		currentState.draw(g, interpolate);
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
	
	/**
	 * Enum used to store states. Takes the state class in constructor and
	 * generates its own id from the static {@code stateIndexCounter} to be used
	 * in the array of states {@code loadedStates}
	 *
	 * @see #stateIndexCounter
	 * @see IState
	 */
	public enum GameState {
		MAIN_MENU(MainMenu.class), GAME_UR(Game_Ur.class), PAUSE(Pause.class);
		
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
}
