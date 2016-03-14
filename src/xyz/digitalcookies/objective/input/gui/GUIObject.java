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

package xyz.digitalcookies.objective.input.gui;

import java.awt.Color;

import xyz.digitalcookies.objective.graphics.Renderer;

/** Base class for user inputs that are rendered on the screen, and interaction
 * with these inputs is through simple input devices such as a mouse.
 * @author Bryan Charles Bettis
 */
public abstract class GUIObject implements Renderer
{
	/** The x coordinate of this object. */
	protected int x;
	/** The y coordinate of this object. */
	protected int y;
	/** The width of this object. */
	protected int width;
	/** The height of this object. */
	protected int height;
	/** The fill color of this GUI Object. */
	private Color bgColor;
	
	/** Update the GUI object. */
	public abstract void poll();
	
	/** Clear stored data for this GUI object. */
	public void clear()
	{
	}
	
	/** Update the position of this element.
	 * @param x the new x coordinate
	 * @param y the new y coordinate
	 */
	public void setPos(int x, int y)
	{
		setX(x);
		setY(y);
	}
	
	/** Update the x coordinate of this element.
	 * @param x the new x coordinate
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	
	/** Update the y coordinate of this element.
	 * @param y the new y coordinate
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	
	/** Update the dimensions of this element.
	 * @param width the new width
	 * @param height the new height
	 */
	public void setDims(int width, int height)
	{
		setWidth(width);
		setHeight(height);
	}
	
	/** Update the width of this element.
	 * @param width the new width
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	/** Update the height of this element.
	 * @param height the new height
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	/** Get the x coordinate.
	 * @return the screen x coordinate
	 */
	public int getX()
	{
		return x;
	}
	
	/** Get the y coordinate.
	 * @return the screen y coordinate
	 */
	public int getY()
	{
		return y;
	}
	
	/** Get the width.
	 * @return the width on the screen
	 */
	public int getWidth()
	{
		return width;
	}
	
	/** Get the height.
	 * @return the height on the screen
	 */
	public int getHeight()
	{
		return height;
	}
	
	/** Gets the current background fill color.
	 * @return the current background fill color
	 */
	public synchronized Color getBGColor()
	{
		return bgColor;
	}
	
	/** Set the background fill color.
	 * @param color the Color to fill the background with
	 */
	public synchronized void setBGColor(Color color)
	{
		bgColor = color;
	}
}
