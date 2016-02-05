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

package core.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;

import core.graphics.GfxManager;
import core.graphics.RenderEvent;
import core.graphics.PrimaryRenderer;
import core.graphics.TextDrawer;

/** A simple implementation of a Renderer, used to display the current average
 * FPS on the screen.
 * @author Bryan Charles Bettis
 */
public class FPSRenderer implements PrimaryRenderer
{
	/** Basic constructor. */
	public FPSRenderer()
	{
	}
	
	@Override
	public void render(RenderEvent e)
	{
		Graphics2D g = e.getContext();
		// Format the string to draw
		String toDraw = "Avg FPS: " + String.format("%.3f", GfxManager.getAvgFPS());
		g.setColor(Color.white);
		// Draw the string to the screen
		TextDrawer.drawText(g, toDraw, 0, 0);
	}
}
