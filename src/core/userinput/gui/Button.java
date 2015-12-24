package core.userinput.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import core.graphics.RenderEvent;
import core.graphics.TextDrawer;

/** A button which the user can interact with.
 * @author Bryan Bettis
 */
public class Button extends GUIObject
{
	/** The state of this button. */
	private ButtonState state;
	/** The text to display on the button. */
	private String text;
	
	public enum ButtonState
	{
		IDLE,
		PRESSED,
		CLICKED
	}
	
	/** Takes arguments for position and dimensions.
	 * @param x the x coordinate
	 * @param y the y coordinate (screen coordinates)
	 * @param width the width of the button
	 * @param height the height of the button
	 */
	public Button(int x, int y, int width, int height)
	{
		this(x, y, width, height, "");
	}
	
	/** Takes arguments for position, dimensions, and initial text.
	 * @param x the x coordinate
	 * @param y the y coordinate (screen coordinates)
	 * @param width the width of the button
	 * @param height the height of the button
	 * @param text the text to display over the button
	 */
	public Button(int x, int y, int width, int height, String text)
	{
		state = ButtonState.IDLE;
		setPos(x, y);
		setDims(width, height);
		setText(text);
	}
	
	/** Get the current state of this button.
	 * @return the ButtonState of this button
	 */
	public synchronized ButtonState getState()
	{
		return state;
	}
	
	@Override
	public synchronized void update()
	{
		
	}
	
	/** Adjust the text displayed on this button.
	 * @param newText the new text to display on this button
	 */
	public synchronized void setText(String newText)
	{
		text = newText;
	}
	
	@Override
	public synchronized void render(RenderEvent event)
	{
		Graphics2D g = event.getContext();
		if (state == ButtonState.CLICKED)
		{
			g.setColor(Color.darkGray);
		}
		else
		{
			g.setColor(Color.gray);
		}
		g.fillRect(x, y, width, height);
		g.setColor(Color.white);
		TextDrawer.drawText(g, text, x, y);
	}
}
