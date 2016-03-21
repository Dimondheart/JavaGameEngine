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

package xyz.digitalcookies.objective.input;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

/** Handles processing of keyboard events.
 * @author Bryan Charles Bettis
 */
public class Keyboard implements KeyListener
{
	/** Number of key values used. */
	private static final short KEY_COUNT = 256;
	
	/** Current state of the keys (pressed/released). */
	private static volatile boolean[] rawStates =
			new boolean[KEY_COUNT];
	/** Processed key states (KeyState). */
	private static volatile KeyState[] processedStates =
			new KeyState[KEY_COUNT];
	
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
	
	/** Basic constructor. */
	Keyboard()
	{
	}
	
	/** Checks if the specified key is pressed down.
	 * @param keyCode the integer representing the key
	 * @return True if the key is pressed
	 * @see java.awt.event.KeyEvent
	 */
	public static boolean isDown(int keyCode)
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
	public static boolean justPressed(int keyCode)
	{
		return processedStates[keyCode].equals(KeyState.ONCE);
	}
	
	/** Checks if the specified key was clicked (pressed and released),
	 * and the release was during the last poll.
	 * @param keyCode the integer representing the key
	 * @return True if the key was released first during the last poll
	 * @see java.awt.event.KeyEvent
	 */
	public static boolean justReleased(int keyCode)
	{
		return (processedStates[keyCode].equals(KeyState.ONCE));
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
	
	/** Setup different device-dependent stuff.
	 * @param comp the component to listen to
	 */
	void setup(Component comp)
	{
		clear();
		comp.addKeyListener(this);
	}
	
	synchronized void poll()
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
	
	synchronized void clear()
	{
		// Set all keys as released
		Arrays.fill(rawStates, false);
		Arrays.fill(processedStates, KeyState.RELEASED);
	}
}
