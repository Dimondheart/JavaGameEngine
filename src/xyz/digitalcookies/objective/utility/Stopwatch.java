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

package xyz.digitalcookies.objective.utility;

/** A class for timing events. This clock uses ProgramTime
 * for getting the current time, so it is automatically paused when the
 * program is paused.
 * @author Bryan Charles Bettis
 */
public class Stopwatch
{
	/** Used to make sure this stopwatch is only initialized once. */
	private volatile boolean isStarted = false;
	/** The time from when the timer was last resumed. */
	private volatile long started = 0;
	/** Elapsed time, not including when parent is paused. */
	private volatile long elapsed = 0;
	/** When the timer is paused and will not change until resumed. */
	private volatile boolean paused = true;
	/** The clock that this clock uses to update itself. */
	private Stopwatch parentTime;
	
	/** Basic constructor. Uses the program time for updating its internal
	 * time.
	 */
	public Stopwatch()
	{
		this(null);
	}
	
	/** Takes an argument for a stopwatch to update this clock using (uses the
	 * program clock to update itself if specified clock is null.)
	 * @param parentTime the stopwatch to use to update this clock (this
	 * stopwatch pauses and resumes with the parent device.)
	 */
	public Stopwatch(Stopwatch parentTime)
	{
		this.parentTime = parentTime;
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
	
	/** Get the current time in seconds.
	 * @return the current time of this stopwatch in seconds
	 */
	public double getTimeSec()
	{
		return getTimeMS()/1000.0;
	}

	/** Get the current time in milliseconds.
	 * @return the current time of this stopwatch in milliseconds
	 */
	public double getTimeMS()
	{
		return getTimeNano()/1000000.0;
	}
	
	/** Get the current time in nanoseconds.
	 * @return the current time of this stopwatch in nanoseconds
	 */
	public synchronized long getTimeNano()
	{
		if (paused)
		{
			return elapsed;
		}
		// When the parent clock is the static program clock
		else if (parentTime == null)
		{
			return elapsed + (xyz.digitalcookies.objective.ProgramTime.getTimeNano() - started);
		}
		// Instantiated parent clocks
		else
		{
			return elapsed + (parentTime.getTimeNano() - started);
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
		else if (parentTime == null)
		{
			paused = true;
			elapsed += (xyz.digitalcookies.objective.ProgramTime.getTimeNano() - started);
		}
		// Instantiated parent clocks
		else
		{
			elapsed += (parentTime.getTimeNano() - started);
		}
	}
	
	/** Resume this clock. Repeated calls have no effect. */
	public synchronized void resume()
	{
		if (paused && isStarted)
		{
			paused = false;
			// When the parent clock is the static program clock
			if (parentTime == null)
			{
				started = xyz.digitalcookies.objective.ProgramTime.getTimeNano();
			}
			// Instantiated parent clocks
			else
			{
				started = parentTime.getTimeNano();
			}
		}
	}
}
