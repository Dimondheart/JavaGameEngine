package main.input;

/** Interface for all input devices. */
public interface InputDevice
{
	/** Update processed input data. */
	void poll();
	/** Clear all stored input data. */
	void clear();
}
