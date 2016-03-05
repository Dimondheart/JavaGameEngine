/** Copyright 2016 Bryan Charles Bettis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package core.userinput.inputdevice.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import core.graphics.RenderEvent;
import core.graphics.TextDrawer;
import core.userinput.InputManager;

import static java.awt.event.MouseEvent.*;

/** A button which the user can interact with.
 * @author Bryan Charles Bettis
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
	 * @author Bryan Charles Bettis
	 */
	public enum ButtonState
	{
		/** The button is not being interacted with or hovered over. */
		IDLE,
		/** The button is being pressed, but not released yet. */
		PRESSED,
		/** The mouse is hovering over the button, but has not clicked. */
		HOVER,
		/** The button has just been released. */
		CLICKED
//		/** The button was selected, but the mouse is no longer hovering over
//		 * this button.
//		 */
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
	
	/** Takes arguments for position, dimensions, and the text font.
	 * @param x the x coordinate
	 * @param y the y coordinate (screen coordinates)
	 * @param width the width of the button
	 * @param height the height of the button
	 * @param font the font to use to display the button text
	 */
	public Button(int x, int y, int width, int height, Font font)
	{
		this(x, y, width, height, "", font);
	}
	
	/** Takes arguments for position, dimensions, label, and label settings.
	 * @param x the x coordinate
	 * @param y the y coordinate (screen coordinates)
	 * @param width the width of the button
	 * @param height the height of the button
	 * @param text the text to display over the button
	 * @param font the font to use to display the button text
	 */
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
	
	/** Checks if this button is in the specified state.
	 * @param state the state to check if this button is in or not
	 * @return true if this button is in the specified state, false otherwise
	 */
	public synchronized boolean isInState(ButtonState state)
	{
		return getState().equals(state);
	}
	
	/** Determines if this button is currently being pressed. This is
	 * a convenience method for checking if this button is in the state
	 * ButtonState.PRESSED.
	 * @return true if this button is pressed, false otherwise
	 */
	public synchronized boolean isPressed()
	{
		return isInState(ButtonState.PRESSED);
	}
	
	/** Determines if this button was just released ("clicked"). This is
	 * a convenience method for checking if this button is in the state
	 * ButtonState.CLICKED.
	 * @return true if this button was first released last poll, false
	 * 		otherwise
	 */
	public synchronized boolean justReleased()
	{
		return isInState(ButtonState.CLICKED);
	}
	
	@Override
	public synchronized void poll()
	{
		// Mouse is hovering over, interact with button
		if (isMouseOver())
		{
			if (InputManager.getMS().justReleased(BUTTON1))
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
	
	/** Gets the text displayed on this button.
	 * @return the text being drawn over this button
	 */
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
	
	/** Gets the font used for the label text on this button.
	 * @return the font object used to draw this buttons label
	 */
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
	
	/** Check if the mouse is hovering over this button.
	 * @return true if the mouse is over this button, false otherwise
	 */
	public boolean isMouseOver()
	{
		int mx = InputManager.getMS().getUnpolledX();
		int my = InputManager.getMS().getUnpolledY();
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
		// Button pressed; adjust the background
		if (state.equals(ButtonState.PRESSED))
		{
			g.setColor(Color.darkGray);
		}
		else
		{
			g.setColor(Color.gray);
		}
		// Fill the button background
		g.fillRect(x, y, width, height);
		// Highlight the button center if hovering over
		if (state.equals(ButtonState.HOVER))
		{
			g.setColor(Color.darkGray);
			int tw = getWidth() - 8;
			int th = getHeight() - 8;
			int hx = getX() + 4;
			int hy = getY() + 4;
			g.fillRect(hx, hy, tw, th);
		}
		// Draw the text over the button
		g.setColor(Color.white);
		int[] centerCoords = {x + getWidth()/2, y + getHeight()/2};
		int[] drawCoords = TextDrawer.centerOverPoint(g, text, centerCoords, font);
		TextDrawer.drawText(g, text, drawCoords[0], drawCoords[1], font);
	}
}
