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

package core.userinput;

import static core.userinput.InputManagerEvent.Type;

import java.awt.Window;
import java.util.concurrent.ConcurrentLinkedDeque;

import core.userinput.inputdevice.GUIMonitor;
import core.userinput.inputdevice.Keyboard;
import core.userinput.inputdevice.Mouse;
import core.userinput.inputdevice.WindowMonitor;

/** Manages all input devices (keyboard, etc.) for the main window.
 * @author Bryan Charles Bettis
 */
public class InputManager extends core.Subsystem
{
	/** Event queue. */
	private static volatile ConcurrentLinkedDeque<InputManagerEvent> queue;
	/** The state the game is currently in. */
	private static volatile State state = State.NORMAL;
	/** The keyboard. */
	private static Keyboard keyboard;
	/** The mouse. */
	private static Mouse mouse;
	/** The main window event manager. */
	private static WindowMonitor window;
	/** Manages all the GUI object updates. */
	private static GUIMonitor gui;
	
	/** Different basic states the game can be in.
	 * @author Bryan Charles Bettis
	 */
	public enum State
	{
		/** The game is running normally. */
		NORMAL,
		/** The game has been paused because the main window was
		 * minimized/iconified.
		 */
		PAUSED,
		/** When the game has been quit, either because the main window
		 * was closed or InputManager.quit() has been called.
		 */
		QUIT
	}
	
	/** Normal input setup.
	 * @param win the main window
	 */
	public InputManager(Window win)
	{
		super(8, "Input Manager Event Queue");
		System.out.println("Setting Up User Input System...");
		// Setup the event queue
		queue = new ConcurrentLinkedDeque<InputManagerEvent>();
		// Setup the input devices
		keyboard = new Keyboard(win);
		mouse = new Mouse(win);
		window = new WindowMonitor(win);
		gui = new GUIMonitor();
	}
	
	@Override
	public void startSystem()
	{
		System.out.println("Starting User Input System...");
	}
	
	@Override
	public boolean runCycle()
	{
		// Stop processing new events if quitting soon
		if (getState() == State.QUIT)
		{
			return false;
		}
		// Get the next event
		InputManagerEvent next = queue.poll();
		// If there is a next event, do it
		if (next != null)
		{
			// Do the event event
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
		return true;
	}
	
	/** Get the current general state of the game.
	 * @return the State of the game
	 */
	public static synchronized State getState()
	{
		return state;
	}
	
	/** Get the keyboard object.
	 * @return the Keyboard object
	 */
	public static Keyboard getKB()
	{
		return keyboard;
	}
	
	/** Get the mouse object.
	 * @return the Mouse object
	 */
	public static Mouse getMS()
	{
		return mouse;
	}
	
	/** Gets the object that monitors the main window.
	 * @return the object that manages/monitors the main window
	 */
	public static WindowMonitor getWin()
	{
		return window;
	}
	
	/** Gets the manager of graphical user input devices, such as on-screen
	 * buttons.
	 * @return the GUI manager object
	 */
	public static GUIMonitor getGUI()
	{
		return gui;
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
	
	/** Change the current state.
	 * @param newState the new general game state State
	 */
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
	
	/** Queue the specified event unless it is the same type as the previous
	 * queued event.
	 * @param e the event to queue
	 */
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
		window.poll();
		gui.poll();
	}
	
	/** Does the clearing of input devices, etc. */
	private synchronized void doClear()
	{
		queue.clear();
		keyboard.clear();
		mouse.clear();
		window.clear();
		gui.clear();
	}
	
	/** Pause the game. */
	private void doPause()
	{
		doClear();
		setState(State.PAUSED);
		core.ProgramClock.pause();
	}
	
	/** Resume the game to normal operation. */
	private void doResume()
	{
		setState(State.NORMAL);
		core.ProgramClock.resume();
	}
	
	/** Quit the game. */
	private void doQuit()
	{
		doClear();
		setState(State.QUIT);
	}
}
