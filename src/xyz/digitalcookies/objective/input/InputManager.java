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

import static xyz.digitalcookies.objective.input.InputManagerEvent.Type;

import java.awt.Window;
import java.util.concurrent.ConcurrentLinkedDeque;

import xyz.digitalcookies.objective.graphics.GraphicsManager;

/** Manages all input devices (keyboard, etc.) for the main window.
 * @author Bryan Charles Bettis
 */
public class InputManager extends xyz.digitalcookies.objective.Subsystem
{
	/** Event queue. */
	private static volatile ConcurrentLinkedDeque<InputManagerEvent> queue;
	/** The state the game is currently in. */
	private static volatile InputManagerState state = InputManagerState.NORMAL;
	/** The keyboard. */
	private static Keyboard keyboard;
	/** The mouse. */
	private static Mouse mouse;
	/** The main window event manager. */
	private static WindowMonitor window;
	/** Manages all the GUI object updates. */
	private static GUIMonitor gui;
	
	/** Different general states the game can be in.
	 * @author Bryan Charles Bettis
	 */
	private enum InputManagerState
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
	
	/** Basic constructor. */
	public InputManager()
	{
		super(8, "Input Manager Event Queue");
	}
	
	@Override
	protected void setupSystem()
	{
		System.out.println("Setting Up User Input System...");
		// Setup the event queue
		queue = new ConcurrentLinkedDeque<InputManagerEvent>();
		// Create main input device managers
		keyboard = new Keyboard();
		mouse = new Mouse();
		window = new WindowMonitor();
		gui = new GUIMonitor();
		state = InputManagerState.PAUSED;
		// Setup the main input device managers
		Window win = GraphicsManager.getMainWin();
		keyboard.setup(win);
		mouse.setup(win);
		window.setup(win);
		gui.setup();
	}
	
	@Override
	protected void startSystem()
	{
		System.out.println("Starting User Input System...");
	}
	
	@Override
	protected boolean runCycle()
	{
		// Stop processing new events if quitting soon
		if (isQuitting())
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
	
	/** Check if the game is running normally.
	 * @return true if the game is not paused, quitting, etc.
	 */
	public static synchronized boolean isRunning()
	{
		return state == InputManagerState.NORMAL;
	}
	
	/** Check if the game is paused, like when the game window is minimized.
	 * @return true if the game is paused
	 */
	public static synchronized boolean isPaused()
	{
		return state == InputManagerState.PAUSED;
	}
	
	/** Check if the game is in the process of quitting.
	 * @return true if the game is in the process of quitting
	 */
	public static synchronized boolean isQuitting()
	{
		return state == InputManagerState.QUIT;
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
		if (state != InputManagerState.PAUSED)
		{
			queueEventUnlessPrev(new InputManagerEvent(Type.PAUSE));
		}
	}
	
	/** Resume the game. */
	public static void resume()
	{
		// Queue unless already resumed
		if (state != InputManagerState.NORMAL)
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
	private static synchronized void setState(InputManagerState newState)
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
		setState(InputManagerState.PAUSED);
		xyz.digitalcookies.objective.ProgramTime.pause();
	}
	
	/** Resume the game to normal operation. */
	private void doResume()
	{
		setState(InputManagerState.NORMAL);
		xyz.digitalcookies.objective.ProgramTime.resume();
	}
	
	/** Quit the game. */
	private void doQuit()
	{
		doClear();
		setState(InputManagerState.QUIT);
	}
}
