package core.sound;

/** Represents a playing sound effect.
 * @author Bryan Bettis
 */
public class SFX extends Sound
{
	public SFX(SFXEvent event)
	{
		super("sfx/" + event.getSFX(), SoundManager.volume);
	}
}
