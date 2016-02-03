package core.userinput.inputdevice;

import java.awt.Window;
import java.awt.event.*;
import java.util.Arrays;

import core.DynamicSettings;

/** Handles mouse input events.
 * @author Bryan Bettis
 */
public class Mouse implements InputDevice, MouseListener, MouseWheelListener
{
	/** Number of button values to use. */
	private static final int NUM_BTNS = 10;
		
	/** Raw/Unprocessed state updates to the mouse buttons. */
	private volatile boolean[] rawStates;
	/** Processed button states. */
	private volatile BtnState[] processedStates;
	/** The change in the scroll wheel between polls. */
	private volatile int rawScrollChange = 0;
	/** The change in the scroll wheel, updated each poll. */
	private volatile int processedScrollChange = 0;
	
	/** States each button can be in.
	 * @author Bryan Bettis
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
	
	/** Constructor, takes a reference to the window/frame it should be added to.
	 * @param win the window to listen to
	 */
	public Mouse(Window win)
	{
		clear();
		win.addMouseListener(this);
		win.addMouseWheelListener(this);
	}
	
	/** Checks if the specified button is pressed down.
	 * @param btnCode the integer representing the button
	 * @return True if the button is pressed
	 * @see java.awt.event.MouseEvent
	 */
	public boolean isDown(int btnCode)
	{
		return (
				processedStates[btnCode].equals(BtnState.PRESSED)
				|| justPressed(btnCode)
				);
	}
	
	/** Checks if the specified button is pressed down for the first time
	 * since the last poll.
	 * @param btnCode the integer representing the button
	 * @return True if the button was first pressed during the last poll.
	 * @see java.awt.event.MouseEvent
	 */
	public boolean justPressed(int btnCode)
	{
		return (processedStates[btnCode].equals(BtnState.ONCE));
	}
	
	/** Checks if the specified button was clicked (pressed and released)
	 * and the release was during the last poll.
	 * @param btnCode the integer representing the button
	 * @return True if the button was released first during the last poll
	 * @see java.awt.event.MouseEvent
	 */
	public boolean justClicked(int btnCode)
	{
		return (processedStates[btnCode].equals(BtnState.CLICKED));
	}
	
	/** Get how much the mouse wheel changed between the two previous polls
	 * (does not change between polls, it is updated at each poll).
	 * @return the number of scroll wheel ticks moved
	 */
	public int getWheelChange()
	{
		return processedScrollChange;
	}
	
	@Override
	public synchronized void poll()
	{
		// Update the scroll wheel
		if ((boolean) DynamicSettings.getSetting("INVERT_SCROLL_WHEEL"))
		{
			processedScrollChange = -rawScrollChange;
		}
		else
		{
			processedScrollChange = rawScrollChange;
		}
		rawScrollChange = 0;
		// Update button processed values
		for (int i = 0; i < NUM_BTNS; ++i)
		{
			// Button is currently depressed
			if (rawStates[i])
			{
				// Key was just pressed
				if (processedStates[i].equals(BtnState.RELEASED))
				{
					processedStates[i] = BtnState.ONCE;
				}
				// Key has been pressed for a while
				else
				{
					processedStates[i] = BtnState.PRESSED;
				}
			}
			// The button has been "clicked" (just released)
			else if (processedStates[i].equals(BtnState.PRESSED))
			{
				processedStates[i] = BtnState.CLICKED;
			}
			// Otherwise key is released
			else
			{
				processedStates[i] = BtnState.RELEASED;
			}
		}
	}
	
	@Override
	public synchronized void clear()
	{
		// current pressed/released state of keys
		rawStates = new boolean[NUM_BTNS];
		// Key state beyond just pressed/released
		processedStates = new BtnState[NUM_BTNS];
		// Set all keys as released
		Arrays.fill(processedStates, BtnState.RELEASED);
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		// Not used
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
		// Not used
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		// Not used
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		// Get the btn's integer ID
		int btnCode = e.getButton();
		// Ignore button IDs outside our used range
		if (btnCode > NUM_BTNS || btnCode < 0)
		{
			return;
		}
		// Update the button
		rawStates[btnCode] = true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		// Get the btn's integer ID
		int btnCode = e.getButton();
		// Check if btn is in range of used btns
		if (btnCode > NUM_BTNS || btnCode < 0)
		{
			return;
		}
		// Update the button
		rawStates[btnCode] = false;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		rawScrollChange += e.getWheelRotation();
	}
}
