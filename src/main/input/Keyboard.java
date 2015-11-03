package main.input;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

// TODO?: Rewrite this to work as an event dispatcher
/** Handles processing of keyboard events. */
public class Keyboard implements InputDevice, KeyListener
{
	/** Number of key values used. */
	private static final short KEY_COUNT = 256;
	
	/** Current state of the keys (pressed/released). */
	private static volatile boolean[] rawStates;
	/** Processed key states (KeyState). */
	private static volatile KeyState[] processedStates;
	
	/** States each key can be in. */
	private enum KeyState
	{
		RELEASED,  // Not down
		PRESSED,  // Down, but not first pressed this frame
		ONCE  // Down for the first time
	}
	
	/** Basic keyboard setup, takes argument for component to listen to. */
	public Keyboard(Component comp)
	{
		// Don't add if null was specified as the dispatcher
		if (comp != null)
		{
			comp.addKeyListener(this);
		}
		clear();
	}
	
	@Override
	public synchronized void poll()
	{
		// For all used key IDs
		for (int i = 0; i < KEY_COUNT; ++i)
		{
			// Set the key state if it has been pressed
			if (rawStates[i])
			{
				// If key is down not but not the previous frame, set to once
				if (processedStates[i] == KeyState.RELEASED)
				{
					processedStates[i] = KeyState.ONCE;
				}
				// Otherwise set to pressed
				else
				{
					processedStates[i] = KeyState.PRESSED;
				}
			}
			// Otherwise set the key state to released
			else
			{
				processedStates[i] = KeyState.RELEASED;
			}
		}
	}
	
	@Override
	public synchronized void clear()
	{
		// current pressed/released state of keys
		rawStates = new boolean[KEY_COUNT];
		// Key state beyond just pressed/released
		processedStates = new KeyState[KEY_COUNT];
		// Set all keys as released
		for (int i = 0; i < KEY_COUNT; ++i)
		{
			processedStates[i] = KeyState.RELEASED;
		}
	}
	
	/** Checks if the specified key is pressed down.
	 * @return True if the key is pressed
	 */
	public boolean isKeyDown(int keyCode)
	{
		return (
				isKeyDownOnce(keyCode) ||
				processedStates[keyCode] == KeyState.PRESSED
				);
	}
	
	/** Checks if the specified key is pressed down for the first time
	 * since the last poll.
	 * @return True if the key was first pressed during the last poll.
	 */
	public boolean isKeyDownOnce(int keyCode)
	{
		return (processedStates[keyCode] == KeyState.ONCE);
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		System.out.println("YEP");
		// Get the key's integer ID
		int keyCode = e.getKeyCode();
		// Check if key is in range of used keys
		if (keyCode >= 0 && keyCode < KEY_COUNT)
		{
			// Current key set to pressed
			rawStates[keyCode] = true;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		// Get the key's integer ID
		int keyCode = e.getKeyCode();
		// Check if key in range of used keys
		if (keyCode >= 0 && keyCode < KEY_COUNT)
		{
			// Set key as released
			rawStates[keyCode] = false;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		// Not used
	}
}
