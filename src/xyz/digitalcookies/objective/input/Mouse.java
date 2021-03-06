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
import java.awt.event.*;
import java.util.Arrays;

import xyz.digitalcookies.objective.Settings;
import xyz.digitalcookies.objective.graphics.GraphicsManager;

/** Handles mouse input events.
 * @author Bryan Charles Bettis
 */
public class Mouse implements MouseListener, MouseWheelListener, MouseMotionListener
{
	/** Number of button values used. */
	private static final int BUTTONS_USED = 10;
		
	/** Raw/Unprocessed state updates to the mouse buttons. */
	private static volatile boolean[] unpolledStates;
	/** Processed button states. */
	private static volatile BtnState[] polledStates;
	/** The change in the scroll wheel between polls. */
	private static volatile int unpolledScrollChange = 0;
	/** The change in the scroll wheel, updated each poll. */
	private static volatile int polledScrollChange = 0;
	/** The x coordinate of the mouse cursor. */
	private static volatile int unpolledX = 0;
	/** The y coordinate of the mouse cursor. */
	private static volatile int unpolledY = 0;
	
	/** States each button can be in.
	 * @author Bryan Charles Bettis
	 */
	private enum BtnState
	{
		/** When a button is not depressed and was not recently released. */
		RELEASED,
		/** When a button has been depressed for a while (when depressed a 
		 * button will first go to ONCE, then shortly after this state).
		 */
		PRESSED,
		/** When a button was recently depressed (shortly after entering this
		 * state a button will change to PRESSED).
		 */
		ONCE,
		/** When a button was recently released (shortly after entering this
		 * state a button will change to RELEASED).
		 */
		CLICKED
	}
	
	/** Basic constructor. */
	Mouse()
	{
	}
	
	/** Checks if the specified button is pressed down.
	 * @param btnCode the integer representing the button
	 * @return True if the button is pressed
	 * @see java.awt.event.MouseEvent
	 */
	public static boolean isDown(int btnCode)
	{
		return (
				polledStates[btnCode].equals(BtnState.PRESSED)
				|| justPressed(btnCode)
				);
	}
	
	/** Checks if the specified button is pressed down for the first time
	 * since the last poll.
	 * @param btnCode the integer representing the button
	 * @return True if the button was first pressed during the last poll.
	 * @see java.awt.event.MouseEvent
	 */
	public static boolean justPressed(int btnCode)
	{
		return (polledStates[btnCode].equals(BtnState.ONCE));
	}
	
	/** Checks if the specified button was clicked (pressed and released)
	 * and the release was during the last poll.
	 * @param btnCode the integer representing the button
	 * @return True if the button was released first during the last poll
	 * @see java.awt.event.MouseEvent
	 */
	public static boolean justReleased(int btnCode)
	{
		return (polledStates[btnCode].equals(BtnState.CLICKED));
	}
	
	/** Get how much the mouse wheel changed between the two previous polls
	 * (does not change between polls, it is updated at each poll).
	 * @return the number of scroll wheel ticks moved
	 */
	public static int getWheelChange()
	{
		return polledScrollChange;
	}
	
	/** Gets the x position of the mouse cursor. The coordinates are
	 * constrained to the edges of the main window.
	 * @return the x position of the mouse over the game window
	 */
	public static int getUnpolledX()
	{
		return unpolledX;
	}
	
	/** Gets the y position of the mouse cursor. The coordinates are
	 * constrained to the edges of the main window.
	 * @return the y position of the mouse over the game window
	 */
	public static int getUnpolledY()
	{
		return unpolledY;
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		// Get the btn's integer ID
		int btnCode = e.getButton();
		// Ignore button IDs outside our used range
		if (btnCode > BUTTONS_USED || btnCode < 0)
		{
			return;
		}
		// Update the button
		unpolledStates[btnCode] = true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		// Get the btn's integer ID
		int btnCode = e.getButton();
		// Check if btn is in range of used btns
		if (btnCode > BUTTONS_USED || btnCode < 0)
		{
			return;
		}
		// Update the button
		unpolledStates[btnCode] = false;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		unpolledScrollChange += e.getWheelRotation();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		updateCursorPos(e);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		updateCursorPos(e);
	}
	
	/** Setup different device-dependent stuff.
	 * @param comp the component to listen to
	 */
	void setup(Component comp)
	{
		clear();
		comp.addMouseListener(this);
		comp.addMouseWheelListener(this);
		comp.addMouseMotionListener(this);
	}
	
	/** Polls all the different mouse data. */
	synchronized void poll()
	{
		// Update the scroll wheel
		if ((boolean) Settings.getSetting("INVERT_SCROLL_WHEEL"))
		{
			polledScrollChange = -unpolledScrollChange;
		}
		else
		{
			polledScrollChange = unpolledScrollChange;
		}
		unpolledScrollChange = 0;
		// Update button processed values
		for (int i = 0; i < BUTTONS_USED; ++i)
		{
			// Button is currently depressed
			if (unpolledStates[i])
			{
				// Key was just pressed
				if (polledStates[i].equals(BtnState.RELEASED))
				{
					polledStates[i] = BtnState.ONCE;
				}
				// Key has been pressed for a while
				else
				{
					polledStates[i] = BtnState.PRESSED;
				}
			}
			// The button has been "clicked" (just released)
			else if (polledStates[i].equals(BtnState.PRESSED) || polledStates[i].equals(BtnState.ONCE))
			{
				polledStates[i] = BtnState.CLICKED;
			}
			// Otherwise key is released
			else
			{
				polledStates[i] = BtnState.RELEASED;
			}
		}
	}
	
	/** Clears all stored input data. */
	synchronized void clear()
	{
		polledScrollChange = 0;
		unpolledScrollChange = 0;
		// current pressed/released state of keys
		unpolledStates = new boolean[BUTTONS_USED];
		// Key state beyond just pressed/released
		polledStates = new BtnState[BUTTONS_USED];
		// Init all keys as released
		Arrays.fill(polledStates, BtnState.RELEASED);
	}
	
	/** Update the position of the mouse cursor.
	 * @param e the mouse movement event
	 */
	private void updateCursorPos(MouseEvent e)
	{
		int newX = e.getX() - GraphicsManager.getMainWin().getInsets().left;
		int newY = e.getY() - GraphicsManager.getMainWin().getInsets().top;
		// Limit the x coordinate to inside the main window
		if (newX < 0)
		{
			newX = 0;
		}
		else if (newX > GraphicsManager.getMainLayerSet().getWidth())
		{
			newX = GraphicsManager.getMainLayerSet().getWidth();
		}
		// Limit the x coordinate to inside the main window
		if (newY < 0)
		{
			newY = 0;
		}
		else if (newY > GraphicsManager.getMainLayerSet().getHeight())
		{
			newY = GraphicsManager.getMainLayerSet().getHeight();
		}
		// Update the unpolled mouse position values
		unpolledX = newX;
		unpolledY = newY;
	}
}
