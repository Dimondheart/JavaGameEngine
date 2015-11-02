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
	/** Number of milliseconds per cycle. */
	private long msPerCycle;
	/** Recorded start time. */
	private long start;
	/** Recorded stop time. */
	private long stop;
	/** Duration of the previous tick. */
	private long duration;

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
	}
	
	/** Finish the current cycle and start the next one. */
	public void nextCycle()
	{
		// Stop time (not literally, just get the current system time...)
		stop = System.currentTimeMillis();
		// Calculate tick duration
		duration = stop - start;
		// Processing finished before max cycle time, pause the thread
		if (duration < msPerCycle)
		{
			// Pause the thread to maintain stable CPS
			pauseThread(msPerCycle - duration);
		}
		// Start time for the next tick
		startCycle();
		// Calculate average CPS
		updateAvgCPS();
	}
	
	public double getAvgCPS()
	{
		return avgCPS;
	}

	/** Sets the start time for the next tick. */
	private void startCycle()
	{
		// Cycle start time
		start = System.currentTimeMillis();
	}
	
	private void updateAvgCPS()
	{
		// TODO Implement calculating this
		avgCPS = -1.0;
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
