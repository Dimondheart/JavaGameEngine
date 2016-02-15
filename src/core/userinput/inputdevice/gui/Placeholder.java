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

import core.graphics.RenderEvent;

/** A simple, invisible placeholder GUI element for occupying space (such as
 * in a GUIPanel.)
 * @author Bryan Charles Bettis
 */
public class Placeholder extends GUIObject
{
	/** Basic constructor.
	 * @param x the x coordinate of the upper left corner
	 * @param y the y coordinate of the upper left corner
	 * @param width the width of the placeholder
	 * @param height the height of the placeholder
	 */
	public Placeholder(int x, int y, int width, int height)
	{
		setPos(x, y);
		setDims(width, height);
	}
	
	@Override
	public void render(RenderEvent event)
	{
		// Placeholder element should not draw anything
	}

	@Override
	public void poll()
	{
		// Placeholder element should not respond to interaction
	}
}
