package main;

/** A timing class used to pause time-based events when the game is
 * completely paused (i.e. InputManager is paused).
 * @author Bryan Bettis
 */
public class ProgramTimer
{
	/** Used to make sure this timer is only initialized once. */
	private static boolean isSetup = false;
	/** The system time from when the timer was last resumed. */
	private static long started;
	/** Elapsed time, not including when InputManager is paused. */
	private static long elapsed = 0;
	/** When the timer is paused and will not change until resumed. */
	private static boolean paused = false;

	/** Setup this timer. Repeated calls will have no effect. */
	public static synchronized void setup()
	{
		if (isSetup)
		{
			return;
		}
		else
		{
			started = System.currentTimeMillis();
			isSetup = true;
		}
	}

	/** Get the current program time. */
	public static synchronized long getTime()
	{
		if (paused)
		{
			return elapsed;
		}
		else
		{
			return elapsed + (System.currentTimeMillis() - started);
		}
	}
	
	/** Pause this timer. Repeated calls have no effect. */
	public static synchronized void pause()
	{
		if (paused)
		{
			return;
		}
		else
		{
			paused = true;
			elapsed += (System.currentTimeMillis() - started);
		}
	}
	
	/** Resume this timer. Repeated calls have no effect. */
	public static synchronized void resume()
	{
		if (paused)
		{
			paused = false;
			started = System.currentTimeMillis();
		}
		else
		{
			return;
		}
	}
}
