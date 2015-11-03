package main.input;

import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import main.graphics.Gfx;
import main.input.InputDevice;

/** Event listener for window-related events. */
public class WindowManager implements InputDevice, WindowListener, ComponentListener
{
	/** The window this manager is listening to. */
	private Window myWin;
	
	/** Constructor which takes a reference to the frame it will
	 * manage events for.
	 */
	public WindowManager(Window window)
	{
		myWin = window;
		myWin.addWindowListener(this);
		myWin.addComponentListener(this);
	}
	
	@Override
	public void poll()
	{
		// Not used
	}
	
	@Override
	public void clear()
	{
		// Not used
	}
	
	/* WindowListener Events */
	@Override
	public void windowClosing(WindowEvent e)
	{
		// If main window close btn hit, quit the program
		if (myWin == Gfx.getMainWin())
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
		// Main window disposed (not preferred quitting), exit the program
		if (myWin == Gfx.getMainWin())
		{
			System.exit(0);
		}
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
		focusRegained();
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
		focusLost();
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
	
	/* ComponentListener Events */
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
		Gfx.windowResized(myWin);
	}

	@Override
	public void componentShown(ComponentEvent e)
	{
	}
	
	/** Should be called when the window has lost focus. */
	private void focusLost()
	{
		// Main window; pause the game
		if (myWin == Gfx.getMainWin())
		{
			InputManager.pause();
		}
	}
	
	/** Should be called when the window has regained focus. */
	private void focusRegained()
	{
		// Main window; resume the game
		if (myWin == Gfx.getMainWin())
		{
			InputManager.resume();
		}
	}
}
