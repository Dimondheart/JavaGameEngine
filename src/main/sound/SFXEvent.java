package main.sound;

public class SFXEvent implements SoundEvent
{
	/** What type of sound event this is. */
	private Type type;
	/** The sound effect to play. */
	private String sfx;
	
	public SFXEvent(String sfx)
	{
		type = Type.PLAY_SFX;
		this.sfx = sfx;
	}
	
	/** Get the sound effect to play. */
	public String getSFX()
	{
		return sfx;
	}

	@Override
	public Type getType()
	{
		return type;
	}
}
