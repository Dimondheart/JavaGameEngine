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

import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import xyz.digitalcookies.objective.graphics.GraphicsManager;

/** Event listener for window-related events.
 * @author Bryan Charles Bettis
 */
public class WindowMonitor implements WindowListener, ComponentListener
{
	/** The window this manager is listening to. */
	private static Window myWin;
	
	/** Basic constructor. */
	WindowMonitor()
	{
	}
	
	/** Checks if this window is active/has input focus.
	 * @return true if window is in focus, false otherwise
	 */
	public static boolean isActive()
	{
		// No window has been set yet
		if (myWin == null)
		{
			return false;
		}
		return myWin.isActive();
	}
	
	@Override
	public void windowClosing(WindowEvent e)
	{
		// If main window close btn hit, quit the program
		if (myWin == GraphicsManager.getMainWin())
		{
			InputManager.quit();
		}
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
	}
	
	@Override
	public void windowClosed(WindowEvent e)
	{
		myWin = null;
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
		focusRegained();
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
		focusLost();
	}
	
	@Override
	public void windowDeiconified(WindowEvent e)
	{
		focusRegained();
	}
	
	@Override
	public void componentHidden(ComponentEvent e)
	{
	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		// Do any special sizing adjustments for the window
		if (myWin == GraphicsManager.getMainWin())
		{
			GraphicsManager.windowResized();
		}
	}

	@Override
	public void componentShown(ComponentEvent e)
	{
	}
	
	/** Setup different device-dependent stuff.
	 * @param window the window to listen to
	 */
	void setup(Window window)
	{
		clear();
		myWin = window;
		myWin.addWindowListener(this);
		myWin.addComponentListener(this);
	}
	
	/** Poll any window data that needs polled. */
	void poll()
	{
	}
	
	/** Clear any stored window data that needs cleared. */
	void clear()
	{
	}
	
	/** Should be called when the window has lost focus. */
	private void focusLost()
	{
		// Main window; pause the game
		if (myWin == GraphicsManager.getMainWin())
		{
			InputManager.pause();
		}
	}
	
	/** Should be called when the window has regained focus. */
	private void focusRegained()
	{
		// Main window; resume the game
		if (myWin == GraphicsManager.getMainWin())
		{
			InputManager.resume();
		}
	}
}
