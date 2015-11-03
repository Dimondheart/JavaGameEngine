package main.input;

import java.awt.Window;
import java.util.LinkedList;

import main.input.Keyboard;
//import main.input.Mouse;
//import main.input.WindowManager;

import static main.input.InputManagerEvent.Type;

/** Manages all input devices (keyboard, etc.) for the main window. */
public class InputManager implements main.CustomRunnable
{
	/** Thread for this object. */
	private Thread thread;
	/** Thread controller for this object. */
	private main.ThreadClock clock;
	/** Event queue. */
	private static volatile LinkedList<InputManagerEvent> queue;
	/** The state the game is currently in. */
	private static volatile State state = State.NORMAL;
	/** The keyboard. */
	private static Keyboard keyboard;
//	/** The mouse. */
//	private static Mouse mouse;
	/** The main window event manager. */
	private static WindowManager win;
	
	/** Different basic states the game can be in. */
	public enum State
	{
		NORMAL,
		PAUSED,
		QUIT
	}
	
	/** Normal input setup. */
	public InputManager(Window window)
	{
		queue = new LinkedList<InputManagerEvent>();
		keyboard = new Keyboard(window);
//		mouse = new Mouse(window);
		win = new WindowManager(window);
		clock = new main.ThreadClock(8);
	}
	
	@Override
	public void start()
	{
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			// Get the next event
			InputManagerEvent next = queue.poll();
			// If there is a next event, do it
			if (next != null)
			{
				// Do the event
				switch(next.getType())
				{
					case POLL:
						doPoll();
						break;
					case CLEAR:
						doClear();
						break;
					case PAUSE:
						doPause();
						break;
					case RESUME:
						doResume();
						break;
					case QUIT:
						doQuit();
					default:
						break;
				}
			}
			clock.nextCycle();
		}
	}
	
	/** Check if an event of the specified type is currently queued. */
	private static synchronized boolean isInQueue(Type type)
	{
		if (queue.size() == 0)
		{
			return false;
		}
		for (InputManagerEvent e : queue)
		{
			if (e.getType() == type)
			{
				return true;
			}
		}
		return false;
	}
	
	/** Get the current general state of the game. */
	public static synchronized State getState()
	{
		return state;
	}
	
	/** Update state information for all input devices. */
	public static synchronized void poll()
	{
		// Only queue one poll event at a time
		if (isInQueue(Type.POLL))
		{
			return;
		}
		// Queue new poll event
		queue.add(new InputManagerEvent(Type.POLL));
	}
	
	/** Clear all buffered/stored data in input devices. */
	public static synchronized void clear()
	{
		// Don't queue multiple clear events in a row
		if (queue.isEmpty() || queue.getLast().getType() == Type.CLEAR)
		{
			return;
		}
		// Queue clear event
		queue.add(new InputManagerEvent(Type.CLEAR));
	}
	
	/** Pause the game. */
	public static synchronized void pause()
	{
		queue.add(new InputManagerEvent(Type.PAUSE));
	}
	
	/** Resume the game. */
	public static synchronized void resume()
	{
		queue.add(new InputManagerEvent(Type.RESUME));
	}
	
	/** Quit the program. */
	public static synchronized void quit()
	{
		queue.add(new InputManagerEvent(Type.QUIT));
	}
	
	/** Does the polling of all input devices. */
	private synchronized void doPoll()
	{
		keyboard.poll();
//		mouse.poll();
		win.poll();
	}
	
	/** Does the clearing of input devices. */
	private void doClear()
	{
		keyboard.clear();
//		mouse.clear();
		win.clear();
	}
	
	/** Pause the game. */
	private void doPause()
	{
		doClear();
		queue.clear();
		System.out.println("Paused");
		state = State.PAUSED;
	}
	
	/** Resume the game to normal operation. */
	private void doResume()
	{
		System.out.println("Resumed");
		state = State.NORMAL;
	}
	
	/** Quit the game. */
	private void doQuit()
	{
		queue.clear();
		System.out.println("Quitted (FYI: Not a real word)");
		state = State.QUIT;
	}
}
