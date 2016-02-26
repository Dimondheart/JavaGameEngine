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

/** A class for timing events. This clock uses the core.ProgramClock
 * for getting the current time, so it is automatically paused when the
 * game is paused.
 * @author Bryan Charles Bettis
 */
public class SimpleClock
{
	/** Used to make sure this clock is only initialized once. */
	private boolean isStarted = false;
	/** The system time from when the timer was last resumed. */
	private long started = 0;
	/** Elapsed time, not including when InputManager is paused. */
	private long elapsed = 0;
	/** When the timer is paused and will not change until resumed. */
	private boolean paused = true;
	/** The clock that this clock uses to update itself. */
	private SimpleClock parentClock;
	
	/** Basic constructor. Uses the ProgramTimer for updating its internal
	 * time.
	 */
	public SimpleClock()
	{
		this(null);
	}
	
	/** Takes an argument for a clock to update this clock using (uses the
	 * program clock to update itself if specified clock is null.)
	 * @param parentClock the clock to use to update this clock (this clock
	 * 		pauses and resumes with the parent clock.)
	 */
	public SimpleClock(SimpleClock parentClock)
	{
		this.parentClock = parentClock;
	}
	
	/** Start this clock. Repeated calls will have no effect. */
	public synchronized void start()
	{
		if (isStarted)
		{
			return;
		}
		else
		{
			isStarted = true;
			resume();
		}
	}

	/** Get the current time.
	 * @return the current clock time in milliseconds
	 */
	public synchronized long getTime()
	{
		if (paused)
		{
			return elapsed;
		}
		// When the parent clock is the static program clock
		else if (parentClock == null)
		{
			return elapsed + (core.ProgramClock.getTime() - started);
		}
		// Instantiated parent clocks
		else
		{
			return elapsed + (parentClock.getTime() - started);
		}
	}
	
	/** Pause this clock. Repeated calls have no effect. */
	public synchronized void pause()
	{
		if (paused)
		{
			return;
		}
		// When the parent clock is the static program clock
		else if (parentClock == null)
		{
			paused = true;
			elapsed += (core.ProgramClock.getTime() - started);
		}
		// Instantiated parent clocks
		else
		{
			elapsed += (parentClock.getTime() - started);
		}
	}
	
	/** Resume this clock. Repeated calls have no effect. */
	public synchronized void resume()
	{
		if (paused && isStarted)
		{
			paused = false;
			// When the parent clock is the static program clock
			if (parentClock == null)
			{
				started = core.ProgramClock.getTime();
			}
			// Instantiated parent clocks
			else
			{
				started = parentClock.getTime();
			}
		}
	}
}
