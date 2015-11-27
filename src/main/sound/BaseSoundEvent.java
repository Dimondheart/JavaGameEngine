package main.sound;

/** Used to represent all general sound events on a queue. */
public abstract class BaseSoundEvent
{
	/** The type specifier for a child class instance. */
	private Type type;
	
	/** The type specifiers for child classes. */
	public enum Type
	{
		PLAY_SFX,
		PLAY_BGM,
		STOP_BGM,
		CHANGE_VOLUME
	}
	
	public BaseSoundEvent(Type type)
	{
		this.type = type;
	}
	
	/** Get what type of event this is. */
	public Type getType()
	{
		return type;
	}
}
