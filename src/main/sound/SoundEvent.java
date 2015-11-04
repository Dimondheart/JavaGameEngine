package main.sound;

/** Used to represent all general sound events on a queue. */
public interface SoundEvent
{
	public enum Type
	{
		PLAY_SFX,
		PLAY_BGM,
		CHANGE_VOLUME
	}
	
	/** Get what type of event this is. */
	public Type getType();
}
