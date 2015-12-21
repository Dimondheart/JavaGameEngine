package main.sound;

/** An event used to play a sound effect.
* @author Bryan Bettis
*/
public class SFXEvent extends BaseSoundEvent
{
	/** The sound effect to play. */
	private String sfx;
	
	public SFXEvent(String sfx)
	{
		super(Type.PLAY_SFX);
		this.sfx = sfx;
	}
	
	/** Get the sound effect to play. */
	public String getSFX()
	{
		return sfx;
	}
}
