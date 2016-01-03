package core.sound;

/** Used to represent all general sound events on a queue.
 * @author Bryan Bettis
 */
abstract class BaseSoundEvent
{
	/** The time the sound effect was queued. */
	private final long queueTime;
	
	/** Basic constructor.
	 * @param type the Type of this sound event
	 */
	public BaseSoundEvent()
	{
		this.queueTime = core.ProgramTimer.getTime();
	}
	
	/** How long this event has been queued.
	 * @return the ProgramTimer time that this event has been queued
	 */
	public final long getTimeInQueue()
	{
		return core.ProgramTimer.getTime() - queueTime;
	}
	
	/** Determines if this event has met any conditions that make
	 * it "stale"/old.
	 * @return if this event is old/stale
	 */
	public boolean isStale()
	{
		return false;
	}
}
