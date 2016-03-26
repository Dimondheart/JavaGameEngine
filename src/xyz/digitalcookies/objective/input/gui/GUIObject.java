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

import xyz.digitalcookies.objective.graphics.BoundedRenderer;

/** Base class for individual GUI elements, such as a button or text box.
 * @author Bryan Charles Bettis
 */
public abstract class GUIObject extends BoundedRenderer implements GUIElement
{
	/** The fill color of this GUI Object. */
	private Color bgColor;
	/** The enabled state of this GUIObject. */
	private boolean isEnabled;
	
	public GUIObject()
	{
		setVisible(true);
		setEnabled(true);
	}
	
	@Override
	public boolean isEnabled()
	{
		return isEnabled;
	}
	
	@Override
	public void setEnabled(boolean enabled)
	{
		isEnabled = enabled;
		// Should not be enabled and not visible
		if (isEnabled)
		{
			setVisible(true);
		}
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
