package main.input;

import java.awt.Window;
import java.awt.event.*;

/** Handles mouse input events.
 * @author Bryan Bettis
 */
public class Mouse implements InputDevice, MouseListener, MouseWheelListener
{
	/** Number of key values to be used. */
	private static final int NUM_BTNS = 4;
	
	/** States each button can be in. */
	private enum BtnState
		{
			RELEASED,  // Not down
			PRESSED,  // Down, but not first pressed this frame
			ONCE,  // Down for the first time
			CLICKED  // Button was pressed, then first released this frame
		}
		
	/** Current state of the mouse. */
	private static volatile boolean[] rawStates = null;
	
	/** Polled mouse state. */
	private static volatile BtnState[] processedStates = null;
	
	/** Constructor, takes a reference to the frame it should be added to. */
	public Mouse(Window win)
	{
		win.addMouseListener(this);
		win.addMouseWheelListener(this);
		clear();
	}
	
	/** Checks if the specified button is pressed down.
	 * @return True if the button is pressed
	 */
	public boolean isDown(int btnCode)
	{
		return (
				justPressed(btnCode) ||
				processedStates[btnCode] == BtnState.PRESSED
				);
	}
	
	/** Checks if the specified button is pressed down for the first time
	 * since the last poll.
	 * @return True if the button was first pressed during the last poll.
	 */
	public boolean justPressed(int btnCode)
	{
		return (processedStates[btnCode] == BtnState.ONCE);
	}
	
	/** Checks if the specified button was clicked (pressed and released)
	 * and the release was during the last poll.
	 * @return True if the button was released first during the last poll
	 */
	public boolean justClicked(int btnCode)
	{
		return (processedStates[btnCode] == BtnState.CLICKED);
	}
	
	@Override
	public synchronized void poll()
	{
		// For all used button IDs
		for (int i = 0; i < NUM_BTNS; ++i)
		{
			// Set the button state if it has been pressed
			if (rawStates[i])
			{
				// If key is down not but not the previous poll, set to once
				if (processedStates[i] == BtnState.RELEASED)
				{
					processedStates[i] = BtnState.ONCE;
				}
				// Otherwise set to pressed
				else
				{
					processedStates[i] = BtnState.PRESSED;
				}
			}
			// When the button has been "clicked" (first released)
			else if (processedStates[i] == BtnState.PRESSED)
			{
				processedStates[i] = BtnState.CLICKED;
			}
			// Otherwise set the key state to released
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
		for (int i = 0; i < NUM_BTNS; ++i)
		{
			processedStates[i] = BtnState.RELEASED;
		}
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
		// Check if btn is in range of used btns
		if (0 <= btnCode && btnCode < NUM_BTNS)
		{
			// Current btn set to pressed
			rawStates[btnCode] = true;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		// Get the btn's integer ID
		int btnCode = e.getButton();
		// Check if btn is in range of used btns
		if (0 <= btnCode && btnCode < NUM_BTNS)
		{
			// Current btn set to released
			rawStates[btnCode] = false;
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		// TODO Implement mouse wheel tracking
	}
}
