package core.graphics.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import core.graphics.RenderEvent;
import core.graphics.TextDrawer;
import core.userinput.InputManager;

import static java.awt.event.MouseEvent.*;

/** A button which the user can interact with.
 * @author Bryan Bettis
 */
public class Button extends GUIObject
{
	/** The state of this button. */
	private ButtonState state;
	/** The text to display on the button. */
	private String text;
	/** The Font object for rendering this button's text. */
	private Font font;
	
	/** The different states a button can be in.
	 * @author Bryan Bettis
	 */
	public enum ButtonState
	{
		IDLE,
		PRESSED,
		HOVER,
		CLICKED
//		PRESSED_NO_HOVER
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
		this(x, y, width, height, text, TextDrawer.defFont);
	}
	
	public Button(int x, int y, int width, int height, String text, Font font)
	{
		state = ButtonState.IDLE;
		setPos(x, y);
		setDims(width, height);
		setText(text);
		setFont(font);
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
		// Mouse is hovering over, interact with button
		if (isMouseOver())
		{
			if (InputManager.getMS().justClicked(BUTTON1))
			{
				state = ButtonState.CLICKED;
			}
			else if (InputManager.getMS().isDown(BUTTON1))
			{
				state = ButtonState.PRESSED;
			}
			else
			{
				state = ButtonState.HOVER;
			}
		}
		// Otherwise set the button as idle
		else
		{
			state = ButtonState.IDLE;
		}
	}
	
	@Override
	public synchronized void clear()
	{
		state = ButtonState.IDLE;
	}
	
	/** Gets the text displayed on this button. */
	public synchronized String getText()
	{
		return text;
	}
	
	/** Adjust the text displayed on this button.
	 * @param newText the new text to display on this button
	 */
	public synchronized void setText(String newText)
	{
		text = newText;
	}
	
	/** Gets the font used for the label text on this button. */
	public synchronized Font getFont()
	{
		return font;
	}
	
	/** Adjust the font used to display text on this button.
	 * @param newFont the new Font object for displaying this button's label
	 */
	public synchronized void setFont(Font newFont)
	{
		font = newFont;
	}
	
	public boolean isMouseOver()
	{
		int mx = InputManager.getMS().getMouseX();
		int my = InputManager.getMS().getMouseY();
		if (
				mx < getX()
				|| mx > getX() + getWidth()
				|| my < getY()
				|| my > getY() + getHeight()
				)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public synchronized void render(RenderEvent event)
	{
		Graphics2D g = event.getContext();
		if (state.equals(ButtonState.PRESSED))
		{
			g.setColor(Color.darkGray);
		}
		else
		{
			g.setColor(Color.gray);
		}
		g.fillRect(x, y, width, height);
		if (state.equals(ButtonState.HOVER))
		{
			g.setColor(Color.darkGray);
			int[] textOffset = TextDrawer.getCenterOffsets(g, text, font);
			int tw = TextDrawer.getTextWidth(g, text, font);
			int th = TextDrawer.getTextHeight(g, text, font);
			int hx = getX() + getWidth()/2 - textOffset[0];
			int hy = getY() + getHeight()/2 - textOffset[1]*3/4;
			g.fillRect(hx, hy, tw, th);
		}
		g.setColor(Color.white);
		int[] centerCoords = {x + getWidth()/2, y + getHeight()/2};
		int[] drawCoords = TextDrawer.centerOverPoint(g, text, centerCoords, font);
		TextDrawer.drawText(g, text, drawCoords[0], drawCoords[1], font);
	}
}
