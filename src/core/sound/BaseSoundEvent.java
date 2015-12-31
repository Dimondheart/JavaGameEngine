package core.sound;

/** Used to represent all general sound events on a queue.
 * @author Bryan Bettis
 */
abstract class BaseSoundEvent
{
	/** The time the sound effect was queued. */
	private long queueTime;
	
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
	public long getTimeQueued()
	{
		return queueTime - core.ProgramTimer.getTime();
	}
}
