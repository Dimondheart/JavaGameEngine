package core.userinput.inputdevice;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/** Handles processing of keyboard events.
 * @author Bryan Bettis
 */
public class Keyboard implements InputDevice, KeyListener
{
	/** Number of key values used. */
	private static final short KEY_COUNT = 256;
	
	/** Current state of the keys (pressed/released). */
	private static volatile boolean[] rawStates;
	/** Processed key states (KeyState). */
	private static volatile KeyState[] processedStates;
	
	/** States each key can be in.
	 * @author Bryan Bettis
	 */
	private enum KeyState
	{
		RELEASED,  // Not down
		PRESSED,  // Down, but not first pressed this poll
		ONCE  // Down for the first time
	}
	
	/** Basic keyboard setup, takes argument for component to listen to.
	 * @param comp the awt/swing component to listen to
	 */
	public Keyboard(Component comp)
	{
		// Don't add if null was specified as the dispatcher
		if (comp != null)
		{
			comp.addKeyListener(this);
		}
		clear();
	}
	
	/** Checks if the specified key is pressed down.
	 * @param keyCode the integer representing the key
	 * @return True if the key is pressed
	 * @see java.awt.event.KeyEvent
	 */
	public boolean isDown(int keyCode)
	{
		return (
				justPressed(keyCode) ||
				processedStates[keyCode] == KeyState.PRESSED
				);
	}
	
	/** Checks if the specified key is pressed down for the first time
	 * since the last poll.
	 * @param keyCode the integer representing the key
	 * @return True if the key was first pressed this cycle
	 * @see java.awt.event.KeyEvent
	 */
	public boolean justPressed(int keyCode)
	{
		return (processedStates[keyCode] == KeyState.ONCE);
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
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		// Get the key's integer ID
		int keyCode = e.getKeyCode();
		// Check if key is in range of used keys
		if (0 <= keyCode && keyCode < KEY_COUNT)
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
		if (0 <= keyCode && keyCode < KEY_COUNT)
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
