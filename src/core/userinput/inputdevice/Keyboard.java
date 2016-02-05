/** Copyright 2016 Bryan Charles Bettis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package core.userinput.inputdevice;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

/** Handles processing of keyboard events.
 * @author Bryan Charles Bettis
 */
public class Keyboard implements InputDevice, KeyListener
{
	/** Number of key values used. */
	private static final short KEY_COUNT = 256;
	
	/** Current state of the keys (pressed/released). */
	private volatile boolean[] rawStates;
	/** Processed key states (KeyState). */
	private volatile KeyState[] processedStates;
	
	/** States each key can be in.
	 * @author Bryan Charles Bettis
	 */
	private enum KeyState
	{
		/** Key is not pressed. */
		RELEASED,
		/** Key pressed, but not first pressed this poll (see ONCE). */
		PRESSED,
		/** Key just pressed (will change to PRESSED on the next poll). */
		ONCE
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
				processedStates[keyCode].equals(KeyState.PRESSED)
				|| justPressed(keyCode)
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
		return processedStates[keyCode].equals(KeyState.ONCE);
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
				if (processedStates[i].equals(KeyState.RELEASED))
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
		Arrays.fill(processedStates, KeyState.RELEASED);
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		// Get the key's integer ID
		int keyCode = e.getKeyCode();
		// Check if key is in range of used keys
		if (keyCode > KEY_COUNT || keyCode < 0)
		{
			return;
		}
		// Update key
		rawStates[keyCode] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		// Get the key's integer ID
		int keyCode = e.getKeyCode();
		// Check if key in range of used keys
		if (keyCode > KEY_COUNT || keyCode < 0)
		{
			return;
		}
		// Update key
		rawStates[keyCode] = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		// Not used
	}
}
