package core;

/** A thin custom layer over the standard Runnable interface.
 * @author Bryan Bettis
 * @see Runnable
 */
public interface CustomRunnable extends Runnable
{
	/** Starts this custom runnable.  Create and start any threads in here. */
	public void start();
}
