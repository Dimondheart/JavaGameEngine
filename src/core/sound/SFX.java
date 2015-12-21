package core.sound;

/** Represents a playing sound effect.
 * @author Bryan Bettis
 */
public class SFX extends Sound
{
	public SFX(String sfx, Volume volume)
	{
		super("sfx/" + sfx, volume);
	}
}
