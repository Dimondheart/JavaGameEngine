package core;

/** A timing class used to pause time-based events when the game is
 * completely paused (i.e. InputManager is paused).
 * @author Bryan Bettis
 */
public class ProgramTimer
{
	/** Used to make sure this timer is only initialized once. */
	private static boolean isSetup = false;
	/** The system time from when the timer was last resumed. */
	private static long started = 0;
	/** Elapsed time, not including when InputManager is paused. */
	private static long elapsed = 0;
	/** When the timer is paused and will not change until resumed. */
	private static boolean paused = true;

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
			resume();
		}
	}

	/** Get the current program time.
	 * @return the current program time in milliseconds
	 */
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
		if (paused && isSetup)
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
