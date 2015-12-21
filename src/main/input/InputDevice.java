package main.input;

/** Interface for all input devices.
 * @author Bryan Bettis
 */
public interface InputDevice
{
	/** Update processed input data. */
	void poll();
	/** Clear all stored input data. */
	void clear();
}
