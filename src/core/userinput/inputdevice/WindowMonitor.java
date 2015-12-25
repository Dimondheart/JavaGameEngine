package core.userinput.inputdevice;

import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import core.graphics.GfxManager;
import core.userinput.InputManager;

/** Event listener for window-related events.
 * @author Bryan Bettis
 */
public class WindowMonitor implements InputDevice, WindowListener, ComponentListener
{
	/** The window this manager is listening to. */
	private Window myWin;
	
	/** Constructor which takes a reference to the frame it will
	 * manage events for.
	 */
	public WindowMonitor(Window window)
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
		if (myWin == GfxManager.getMainWin())
		{
			InputManager.quit();
		}
		else
		{
			closeWindow();
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
		if (myWin == GfxManager.getMainWin())
		{
			System.exit(0);
		}
		else
		{
			closeWindow();
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
		GfxManager.windowResized();
	}

	@Override
	public void componentShown(ComponentEvent e)
	{
	}
	
	/** Should be called when the window has lost focus. */
	private void focusLost()
	{
		// Main window; pause the game
		if (myWin == GfxManager.getMainWin())
		{
			InputManager.pause();
		}
	}
	
	/** Should be called when the window has regained focus. */
	private void focusRegained()
	{
		// Main window; resume the game
		if (myWin == GfxManager.getMainWin())
		{
			InputManager.resume();
		}
	}
	
	/** Safely destroys the window and all related info. */
	private void closeWindow()
	{
		// TODO Safely remove the window
	}
}
