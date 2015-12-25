package core.userinput;

import static core.userinput.InputManagerEvent.Type;

import java.awt.Window;
import java.util.concurrent.ConcurrentLinkedDeque;

import core.userinput.inputdevice.Keyboard;
import core.userinput.inputdevice.Mouse;
import core.userinput.inputdevice.WindowMonitor;

/** Manages all input devices (keyboard, etc.) for the main window.
 * @author Bryan Bettis
 */
public class InputManager implements core.CustomRunnable
{
	/** Thread for this object. */
	private Thread thread;
	/** Thread controller for this object. */
	private core.ThreadClock clock;
	/** Event queue. */
	private static volatile ConcurrentLinkedDeque<InputManagerEvent> queue;
	/** The state the game is currently in. */
	private static volatile State state = State.NORMAL;
	/** The keyboard. */
	private static Keyboard keyboard;
	/** The mouse. */
	private static Mouse mouse;
	/** The main window event manager. */
	private static WindowMonitor win;
	
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
		System.out.println("Setting Up User Input System...");
		queue = new ConcurrentLinkedDeque<InputManagerEvent>();
		keyboard = new Keyboard(window);
		mouse = new Mouse(window);
		win = new WindowMonitor(window);
		clock = new core.ThreadClock(8);
	}
	
	@Override
	public void start()
	{
		System.out.println("Starting User Input System...");
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
								"Unrecognized InputManagerEvent Type: " +
										next.getType()
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
	
	/** Get the keyboard object. */
	public static Keyboard getKB()
	{
		return keyboard;
	}
	
	/** Get the mouse object. */
	public static Mouse getMS()
	{
		return mouse;
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
		mouse.poll();
		win.poll();
	}
	
	/** Does the clearing of input devices and any miscellaneous things to
	 * clear.
	 */
	private synchronized void doClear()
	{
		queue.clear();
		keyboard.clear();
		mouse.clear();
		win.clear();
	}
	
	/** Pause the game. */
	private void doPause()
	{
		doClear();
		setState(State.PAUSED);
		core.ProgramTimer.pause();
	}
	
	/** Resume the game to normal operation. */
	private void doResume()
	{
		setState(State.NORMAL);
		core.ProgramTimer.resume();
	}
	
	/** Quit the game. */
	private void doQuit()
	{
		doClear();
		setState(State.QUIT);
	}
}
