package core.userinput;

/** An event for the InputManager class.
 * @author Bryan Bettis
 */
class InputManagerEvent
{
	/** What type of event this is. */
	private Type type;
	
	/** The different types of events.
	 * @author Bryan Bettis
	 */
	public enum Type
	{
		POLL,
		CLEAR,
		PAUSE,
		RESUME,
		QUIT
	}
	
	/** Basic constructor.
	 * @param type the Type of this event
	 */
	public InputManagerEvent(Type type)
	{
		this.type = type;
	}
	
	/** Gets the type of this event.
	 * @return the Type of this event
	 */
	public Type getType()
	{
		return type;
	}
}
