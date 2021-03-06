package game.dylandevalia.royal_game_of_ur.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import javax.swing.JPanel;

/**
 * Abstract class which implements both the keyboard and mouse listeners as itself Calls the child's
 * draw function
 */
public abstract class Canvas extends JPanel implements KeyListener, MouseListener {
	
	Canvas() {
		setDoubleBuffered(true);
		setFocusable(true);
		setBackground(Color.BLACK);
		
		addKeyListener(this);
		addMouseListener(this);
	}
	
	/**
	 * Call this class to draw to the canvas using the {@link Graphics2D} object
	 *
	 * @param g The graphics object used to draw to the canvas
	 */
	public abstract void draw(Graphics2D g);
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setBackground(Color.GREEN);
		// Calls method overridden by child
		draw(g2d);
	}
	
	
	/* Keyboard listener */
	
	/**
	 * A hashmap of all the keyCodes and whether they are currently held down
	 */
	private static HashMap<Integer, Boolean> keyboardCodeStates = new HashMap<>();
	
	private static HashMap<Character, Boolean> keyboardCharStates = new HashMap<>();
	
	/**
	 * Checks if a given key is being held down. Returns false on NullPointerException as key hasn't
	 * been pressed yet so there's no value for it in the map.
	 * <p>
	 * Use {@link KeyEvent}'s static members for its virtual keyboard. E.g {@code
	 * KeyEvent.VK_ESCAPE} for the escape key or {@code KeyEvent.VK_A} for the 'a' key
	 *
	 * @param key The keycode of the key to check
	 * @return Whether the given key is held down
	 */
	public static boolean getKeyState(int key) {
		try {
			return keyboardCodeStates.get(key);
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	/**
	 * Checks if a given key is being held down. Returns false on NullPointerException if a key is
	 * not yet registered with the hash map
	 *
	 * @param key The key char to check
	 * @return True if the given key is being pressed. False else
	 */
	public static boolean getKeyState(char key) {
		try {
			return keyboardCharStates.get(key);
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		keyboardCodeStates.put(e.getKeyCode(), true);
		keyboardCharStates.put(e.getKeyChar(), true);
		keyPressedFramework(e);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		keyboardCodeStates.put(e.getKeyCode(), false);
		keyboardCharStates.put(e.getKeyChar(), false);
		keyReleasedFramework(e);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	
	}
	
	/**
	 * Callback function called when a key is pressed
	 *
	 * @param e The {@link KeyEvent} when the key was pressed
	 */
	public abstract void keyPressedFramework(KeyEvent e);
	
	/**
	 * Callback function called when a key is released
	 *
	 * @param e The {@link KeyEvent} when the key was released
	 */
	public abstract void keyReleasedFramework(KeyEvent e);
	
	
	/* Mouse listener */
	
	private static boolean[] mouseStates = new boolean[3];
	
	/**
	 * Returns if the mouse button given is being pressed down 1 - Left click 2 - Middle click
	 * (scroll wheel) 3 - Right click
	 *
	 * @param button Value of the button to check
	 * @return Boolean if the mouse button is held down
	 */
	public static boolean mouseButtonState(int button) {
		return mouseStates[button - 1];
	}
	
	private void mouseKeyStatus(MouseEvent e, boolean status) {
		switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				mouseStates[0] = status;
				break;
			case MouseEvent.BUTTON2:
				mouseStates[1] = status;
				break;
			case MouseEvent.BUTTON3:
				mouseStates[2] = status;
				break;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		mouseKeyStatus(e, true);
		mousePressedFramework(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseKeyStatus(e, false);
		mouseReleasedFramework(e);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	/**
	 * Callback function called when a mouse button is pressed
	 *
	 * @param e The {@link MouseEvent} when the button was pressed
	 */
	public abstract void mousePressedFramework(MouseEvent e);
	
	/**
	 * Callback function called when a mouse button is released
	 *
	 * @param e The {@link KeyEvent} when the key was released
	 */
	public abstract void mouseReleasedFramework(MouseEvent e);
	
	
}
