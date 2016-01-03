package core.sound;

/** An event used to play a sound effect.
* @author Bryan Bettis
*/
class SFXEvent extends BaseSoundEvent
{
	/** How long a sound effect can be queued before it is stale/old. */
	private static final long MAX_QUEUE_TIME = 200;
	
	/** The sound effect to play. */
	private String sfx;
	
	/** Basic constructor.
	 * @param sfx the name of the file
	 */
	public SFXEvent(String sfx)
	{
		this.sfx = sfx;
	}
	
	/** Get the sound effect to play.
	 * @return the name of the file
	 */
	public String getSFX()
	{
		return sfx;
	}
	
	@Override
	public boolean isStale()
	{
		return getTimeInQueue() >= MAX_QUEUE_TIME;
	}
}
