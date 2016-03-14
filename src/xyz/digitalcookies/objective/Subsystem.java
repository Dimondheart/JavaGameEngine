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

package xyz.digitalcookies.objective;

/** Subsystems are some of the core systems of this game engine which each
 * handle certain aspects of the engine, such as graphics or sounds. This class
 * is used internally; it is not part of the game engine API.
 * @author Bryan Charles Bettis
 */
public abstract class Subsystem implements Runnable
{
	/** The name of this system's thread. */
	protected String threadName;
	/** Thread manager. */
	protected xyz.digitalcookies.objective.ThreadManager clock;
	/** Thread for this system. */
	private Thread thread;
	/** Used to make this subsystem stop when running in threaded mode. */
	private boolean stopThread = false;
	
	/** Subclass-access only constructor.
	 * @param clockCycleInterval the interval this system will run at, when
	 * 		threaded
	 * @param threadName the name of this system's thread, when threaded
	 */
	protected Subsystem(int clockCycleInterval, String threadName)
	{
		clock = new xyz.digitalcookies.objective.ThreadManager(clockCycleInterval);
		this.threadName = threadName;
	}
	
	/** Does final setup that is needed for a system before starting it. */
	final void setup()
	{
		setupSystem();
	}
	
	/** Starts this subsystem in its own thread. */
	final void startThreaded()
	{
		startUnthreaded();
		thread = new Thread(this);
		thread.setName(threadName);
		thread.start();
	}
	
	/** Starts this subsystem. */
	final void startUnthreaded()
	{
		thread = null;
		startSystem();
	}
	
	/** Stops this subsystem. */
	final void stop()
	{
		stopThread();
		stopSystem();
	}
	
	/** Runs one cycle of this subsystem.
	 * @return true if this subsystem should keep running, this return value
	 * 		is only important to the subsystem, and only if it is running in
	 * 		threaded mode
	 */
	protected abstract boolean runCycle();
	
	/** Does final setup for the system. Subclasses should perform final setup
	 * here that will have to be run each time the game is restarted.
	 */
	protected abstract void setupSystem();
	
	/** Startup specific to each subsystem. */
	protected abstract void startSystem();
	
	/** Stops the system; cleans up any resources that would need to be
	 * released for the program to exit without calling System.exit().
	 */
	protected void stopSystem()
	{
	}
	
	@Override
	public final void run()
	{
		// Running in unthreaded mode
		if (thread == null)
		{
			runCycle();
			return;
		}
		// make sure this is reset
		stopThread = false;
		// Calls runCycle() each iteration & breaks when needed
		while (true)
		{
			clock.nextCycle();
			if (!runCycle() || stopThread)
			{
				break;
			}
		}
		stopThread = false;
	}
	
	/** Tell this subsystem to stop running if it is running in its
	 * own thread.
	 */
	final void stopThread()
	{
		stopThread = true;
	}
	
	/** Get the average number of cycles per second of this subsystem (only
	 * applicable if this system is running in running in threaded mode.)
	 * @return the average number of CPS, or 0 if this subsystem is not
	 * 		running in threaded mode
	 */
	protected double getAvgCPS()
	{
		return clock.getAvgCPS();
	}
}
