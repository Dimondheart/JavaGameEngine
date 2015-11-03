package main.input;

import java.awt.Window;
import java.util.concurrent.ConcurrentLinkedDeque;

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
	private static volatile ConcurrentLinkedDeque<InputManagerEvent> queue;
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
		queue = new ConcurrentLinkedDeque<InputManagerEvent>();
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
			// Stop processing new events if quitting soon
			if (getState() == State.QUIT)
			{
				break;
			}
			// Get the next event
			InputManagerEvent next = queue.poll();
			// If there is a next event, do it
			if (next != null)
			{
				/* Debuging stuff */
//				System.out.print("InputManager Queue: ");
//				System.out.print(next.getType());
//				System.out.print(", ");
//				for (InputManagerEvent e : queue)
//				{
//					System.out.print(e.getType());
//					System.out.print(", ");
//				}
//				System.out.println("");
				/* End debugging stuff */
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
						break;
					default:
						System.out.println(
								"Unrecognized Event Type: " + next.getType()
								);
						break;
				}
			}
			clock.nextCycle();
		}
	}
	
	/** Get the current general state of the game. */
	public static synchronized State getState()
	{
		return state;
	}
	
	/** Update processed state information for all input devices. */
	public static void poll()
	{
		queueEventUnlessPrev(new InputManagerEvent(Type.POLL));
	}
	
	/** Clear all buffered/stored data in input devices. */
	public static void clear()
	{
		queueEventUnlessPrev(new InputManagerEvent(Type.CLEAR));
	}
	
	/** Pause the game. */
	public static void pause()
	{
		// Queue unless already paused
		if (getState() != State.PAUSED)
		{
			queueEventUnlessPrev(new InputManagerEvent(Type.PAUSE));
		}
	}
	
	/** Resume the game. */
	public static void resume()
	{
		// Queue unless already resumed
		if (getState() != State.NORMAL)
		{
			queueEventUnlessPrev(new InputManagerEvent(Type.RESUME));
		}
	}
	
	/** Quit the program. */
	public static void quit()
	{
		queueEventOnce(new InputManagerEvent(Type.QUIT));
	}
	
	/** Change the current state in a synchronized way. */
	private static synchronized void setState(State newState)
	{
		state = newState;
	}
	
	/** Check if an event of the specified type is currently queued.
	 * @param type the InputManagerEvent.Type of event to look for
	 * @return <tt>false</tt> if queue is empty or has no events of
	 * the specified type, <tt>true</tt> otherwise.
	 */
	private static boolean isInQueue(Type type)
	{
		if (queue.isEmpty())
		{
			return false;
		}
		// Check if any queued events are of specified type
		for (InputManagerEvent e : queue)
		{
			if (e.getType() == type)
			{
				return true;
			}
		}
		// No events of specified type in queue
		return false;
	}
	
	/** Checks if the last event in queue is of the specified type.
	 * False if the queue is empty.
	 * @param type the InputManagerEvent.Type of event to look for
	 * @return <tt>false</tt> if queue is empty or last event is not of
	 * the specified type, <tt>true</tt> otherwise.
	 */
	private static boolean isLastInQueue(Type type)
	{
		// See what the last event in the queue is
		InputManagerEvent last = queue.peekLast();
		// Check if queue is empty or last is not specified type
		if (last == null || last.getType() != type)
		{
			return false;
		}
		// Last event is of specified type
		return true;
	}
	
	/** Add the specified event to the queue.
	 * @param e the main.input.InputManagerEvent object
	 */
	private static synchronized void queueEvent(InputManagerEvent e)
	{
		queue.add(e);
	}
	
	/** Queue the specified event, unless an event of the same type is
	 * already in the queue.
	 * @param e the event object to queue
	 */
	private static synchronized void queueEventOnce(InputManagerEvent e)
	{
		// Return if similar event already in queue
		if (isInQueue(e.getType()))
		{
			return;
		}
		// Add the event
		queueEvent(e);
	}
	
	private static synchronized void queueEventUnlessPrev(InputManagerEvent e)
	{
		// Return if the current last element in queue is the same type
		if (isLastInQueue(e.getType()))
		{
			return;
		}
		// Add the event
		queueEvent(e);
	}
	
	/** Does the polling of all input devices. */
	private synchronized void doPoll()
	{
		keyboard.poll();
//		mouse.poll();
		win.poll();
	}
	
	/** Does the clearing of input devices. */
	private synchronized void doClear()
	{
		queue.clear();
		keyboard.clear();
//		mouse.clear();
		win.clear();
	}
	
	/** Pause the game. */
	private void doPause()
	{
		doClear();
		System.out.println("Paused");
		setState(State.PAUSED);
	}
	
	/** Resume the game to normal operation. */
	private void doResume()
	{
		System.out.println("Resumed");
		setState(State.NORMAL);
	}
	
	/** Quit the game. */
	private void doQuit()
	{
		doClear();
		System.out.println("'Quitted'");
		setState(State.QUIT);
	}
}
