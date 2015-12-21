package main.input;

/** An event for the InputManager class.
 * @author Bryan Bettis
 */
public class InputManagerEvent
{
	/** What type of event this is. */
	private Type type;
	
	/** The different types of events. */
	public enum Type
	{
		POLL,
		CLEAR,
		PAUSE,
		RESUME,
		QUIT
	}
	
	/** Basic constructor. */
	public InputManagerEvent(Type type)
	{
		this.type = type;
	}
	
	public Type getType()
	{
		return type;
	}
}
