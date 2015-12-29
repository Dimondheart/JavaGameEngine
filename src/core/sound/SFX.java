package core.sound;

/** Represents a playing sound effect.
 * @author Bryan Bettis
 */
class SFX extends Sound
{
	/** Basic constructor.
	 * @param event the event for this sound effect
	 */
	public SFX(SFXEvent event)
	{
		super("sfx/" + event.getSFX());
	}
}
