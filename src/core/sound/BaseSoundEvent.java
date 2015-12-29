package core.sound;

/** Used to represent all general sound events on a queue.
 * @author Bryan Bettis
 */
abstract class BaseSoundEvent
{
	/** The type specifier for a child class instance. */
	private Type type;
	
	/** The type specifiers for child classes.
	 * @author Bryan Bettis
	 */
	public enum Type
	{
		PLAY_SFX,
		PLAY_BGM,
		STOP_BGM,
		CHANGE_VOLUME
	}
	
	/** Basic constructor.
	 * @param type the Type of this sound event
	 */
	public BaseSoundEvent(Type type)
	{
		this.type = type;
	}
	
	/** Get what type of event this is.
	 * @return the Type of this event
	 */
	public Type getType()
	{
		return type;
	}
}
