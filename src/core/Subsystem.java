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

package core;

/** Subsystems are some of the core systems of this game engine which each
 * handle certain aspects of the engine, such as graphics or sounds.
 * @author Bryan Charles Bettis
 * @see Runnable
 */
public abstract class Subsystem implements Runnable
{
	/** Thread for this system. */
	protected Thread thread;
	/** The name of this system's thread. */
	protected String threadName;
	/** Thread manager. */
	protected core.ThreadManager clock;
	/** Used to make this subsystem stop when running in threaded mode. */
	private boolean stopThread = false;
	
	/** Subclass-access only constructor.
	 * @param clockCyclInterval the interval this system will run at, when
	 * 		threaded
	 * @param threadName the name of this system's thread, when threaded
	 */
	protected Subsystem(int clockCyclInterval, String threadName)
	{
		clock = new core.ThreadManager(clockCyclInterval);
		this.threadName = threadName;
	}
	
	/** Does final setup that is needed for a system before starting it. */
	public void setup()
	{
		setupSystem();
	}
	
	/** Starts this subsystem in its own thread. */
	public final void startThreaded()
	{
		startUnthreaded();
		thread = new Thread(this);
		thread.setName(threadName);
		thread.start();
	}
	
	/** Starts this subsystem. */
	public final void startUnthreaded()
	{
		startSystem();
	}
	
	/** Runs one cycle of this subsystem.
	 * @return true if this subsystem should keep running, this return value
	 * 		is only important to the subsystem, and only if it is running in
	 * 		threaded mode
	 */
	public abstract boolean runCycle();
	
	/** Does final setup for the system. Subclasses should perform final setup
	 * here that will have to be run each time the game is restarted.
	 */
	protected abstract void setupSystem();
	
	/** Startup specific to each subsystem. */
	protected abstract void startSystem();
	
	@Override
	public final void run()
	{
		stopThread = false;
		while (true)
		{
			clock.nextCycle();
			boolean continueRunning = runCycle();
			if (!continueRunning || stopThread)
			{
				break;
			}
		}
		stopThread = false;
	}
	
	/** Tell this subsystem to stop running if it is running in its
	 * own thread.
	 */
	public final void stopThread()
	{
		stopThread = true;
	}
}
