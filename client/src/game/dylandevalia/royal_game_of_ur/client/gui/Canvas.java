package game.dylandevalia.royal_game_of_ur.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Abstract class which implements both the keyboard and mouse listeners as itself
 * Calls the child's draw function
 */
public abstract class Canvas extends JPanel implements KeyListener, MouseListener {
	// Array of key/mouse buttons and whether they are active or not
	private static boolean[] keyboardState = new boolean[525];
	private static boolean[] mouseState = new boolean[3];
	
	public Canvas() {
		setDoubleBuffered(true);
		setFocusable(true);
		setBackground(Color.BLACK);
		
		addKeyListener(this);
		addMouseListener(this);
	}
	
	public abstract void draw(Graphics2D graphics2D);
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g2d);
		
		// Calls method overridden by child
		draw(g2d);
	}
	
	/* ----------------- */
	/* Keyboard listener */
	/* ----------------- */
	
	/**
	 * Checks if a given key is being held down
	 * @param key   The keycode of the key to check
	 * @return  Whether the given key is held down
	 */
	public static boolean keyboardKeyState(int key) {
		return keyboardState[key];
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		keyboardState[e.getKeyCode()] = true;
		keyPressedFramework(e);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		keyboardState[e.getKeyCode()] = false;
		keyReleasedFramework(e);
	}
	
	@Override
	public void keyTyped(KeyEvent e) { }

	public abstract void keyPressedFramework(KeyEvent e);
	public abstract void keyReleasedFramework(KeyEvent e);
	
	/* -------------- */
	/* Mouse listener */
	/* -------------- */
	
	public static boolean mouseButtonState(int button) {
		return mouseState[button - 1];
	}
	
	private void mouseKeyStatus(MouseEvent e, boolean status) {
		switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				mouseState[0] = status;
				break;
			case MouseEvent.BUTTON2:
				mouseState[1] = status;
				break;
			case MouseEvent.BUTTON3:
				mouseState[2] = status;
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
	public void mouseClicked(MouseEvent e) { }
	
	@Override
	public void mouseEntered(MouseEvent e) { }
	
	@Override
	public void mouseExited(MouseEvent e) { }
	
	public abstract void mousePressedFramework(MouseEvent e);
	public abstract void mouseReleasedFramework(MouseEvent e);
}
