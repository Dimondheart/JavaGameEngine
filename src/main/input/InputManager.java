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
	/** The keyboard. */
	private static Keyboard keyboard;
//	/** The mouse. */
//	private static Mouse mouse;
//	/** The main window event manager. */
//	private static WindowManager window;
	
	/** Normal input setup. */
	public InputManager(Window window)
	{
		queue = new LinkedList<InputManagerEvent>();
		keyboard = new Keyboard(window);
//		mouse = new Mouse(window);
//		window = new WindowManager(window);
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
					default:
						break;
				}
			}
			clock.nextCycle();
		}
	}
	
	public static boolean isInQueue(Type type)
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
	
	/** Update state information for all input devices. */
	public static void poll()
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
	public static void clear()
	{
		// Don't queue multiple clear events in a row
		if (queue.isEmpty() || queue.getLast().getType() == Type.CLEAR)
		{
			return;
		}
		// Queue clear event
		queue.add(new InputManagerEvent(Type.CLEAR));
	}
	
	/** Does the polling of all input devices. */
	private synchronized void doPoll()
	{
		System.out.println("Polling...");
		keyboard.poll();
//		mouse.poll();
//		window.poll();
	}
	
	/** Does the clearing of input devices. */
	private void doClear()
	{
		// TODO Implement
		System.out.println("Clearing...");
		keyboard.clear();
//		mouse.clear();
//		window.clear();
	}
}
