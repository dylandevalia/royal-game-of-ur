package game.dylandevalia.royal_game_of_ur.client.states;

import game.dylandevalia.royal_game_of_ur.utility.Log;

import java.awt.*;
import java.awt.event.KeyEvent;

public class StateManager {
	private static int stateIndexCounter = 0;
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
	
	private final State[] loadedStates = new State[GameState.values().length];
	private State currentState;
	
	public void initState(GameState state) {
		try {
			int index = state.getIndex();
			loadedStates[index] = (State) state.getObj().newInstance();
			loadedStates[index].initialise();
		} catch (Exception e) {
			Log.error("State manager", "Error trying to create new instance of state", e);
		}
	}
	
	public void setState(GameState state) {
		if (loadedStates[state.getIndex()] == null) {
			Log.error("State manager", "State not loaded!");
			return;
		}
		currentState = loadedStates[state.getIndex()];
	}
	
	public void unloadState(GameState state) {
		loadedStates[state.getIndex()] = null;
	}
	
	public boolean isLoaded(GameState state) {
		return loadedStates[state.getIndex()] != null;
	}
	
	
	
	
	public void initialise() {
		currentState.initialise();
	}
	
	public void update() {
		currentState.update();
	}
	
	public void draw(Graphics2D g2d, double interpolate) {
		currentState.draw(g2d, interpolate);
	}
	
	public void keyReleased(KeyEvent event) {
		currentState.keyReleased(event);
	}
}
