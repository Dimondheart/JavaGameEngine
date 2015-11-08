package main;

/** Manages timing for the thread of an object that instantiates it. */
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
	 */
	public ThreadClock(int priority)
	{
		setSpeed(priority);
		startCycle();
	}
	
	/** Changes the cycle speed of this thread. */
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
	
	/** Get the average CPS of this ThreadClock. */
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

	/** Puts the thread to sleep for the specified number of milliseconds. */
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
