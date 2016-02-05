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

/** Manages timing for the thread of an object that instantiates it.
 * @author Bryan Charles Bettis
 */
public class ThreadClock
{
	/** Preset, low speed cycling. */
	public static final int LOW_CPS = 10;
	/** Preset, medium speed cycling. */
	public static final int MEDIUM_CPS = 25;
	/** Preset, high speed cycling. */
	public static final int HIGH_CPS = 50;
	
	/** Actual average CPS. */
	private double avgCPS;
	/** Average CPS of the previous cycle. */
	private double prevAvgCPS;
	/** Number of milliseconds per cycle. */
	private long msPerCycle;
	/** Recorded start time. */
	private long start;

	/** Standard ThreadClock, defaults to MEDIUM_CPS speed. */
	public ThreadClock()
	{
		// Default priority
		this(MEDIUM_CPS);
	}
	
	/** Constructor, takes a specified speed setting. Can be one of the 
	 * presets/constants ending in '_CPS'.
	 * @param interval the number of milliseconds per cycle
	 */
	public ThreadClock(int interval)
	{
		setSpeed(interval);
		startCycle();
	}
	
	/** Changes the cycle speed of this thread.
	 * @param interval the milliseconds per cycle
	 */
	public void setSpeed(int interval)
	{
		msPerCycle = (long)interval;
		// Reset average CPS values
		avgCPS = 1000.000 / (double)msPerCycle;
		prevAvgCPS = 1000.000 / (double)msPerCycle;
	}
	
	/** Finish the current cycle and start the next one. */
	public void nextCycle()
	{
		// Stop time (not literally, just get the current system time...)
		long stop = System.currentTimeMillis();
		// Calculate tick duration
		long duration = stop - start;
		// Processing finished before max cycle time, pause the thread
		if (duration < msPerCycle)
		{
			// Pause the thread to maintain stable CPS
			pauseThread(msPerCycle - duration);
		}
		// Start time for the next tick
		startCycle();
		// Calculate average CPS
		updateAvgCPS(duration);
	}
	
	/** Get the average CPS of this ThreadClock
	 * @return the smoothed average number of cycles per second
	 */
	public double getAvgCPS()
	{
		return avgCPS;
	}

	/** Sets the start time for the next cycle. */
	private void startCycle()
	{
		// Cycle start time
		start = System.currentTimeMillis();
	}
	
	private void updateAvgCPS(long duration)
	{
		// Set the previous avg CPS as avg CPS from previous cycle
		prevAvgCPS = avgCPS;
		// The instantaneous CPS for this cycle
		double instantCPS;
		// Calculate instantaneous CPS
		if (duration <= msPerCycle)
		{
			// Cycle duration plus thread paused time
			instantCPS = 1000.000 / (double)msPerCycle;
		}
		else
		{
			instantCPS = 1000.000 / (double)duration;
		}
		// Update the average CPS based on previous and instantaneous
		avgCPS = (prevAvgCPS * 0.75) + (instantCPS * 0.25);
	}

	/** Puts the thread to sleep for the specified number of milliseconds.
	 * @param millisec the number of milliseconds to pause
	 */
	public void pauseThread(long millisec)
	{
		try
		{
			Thread.sleep(millisec);
		}
		catch (InterruptedException e)
		{
		}
	}
}
