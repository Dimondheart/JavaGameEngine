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

package core.graphics;

import java.awt.Graphics2D;

/** An event class which contains context information used to render
 * something to the screen. This includes the graphics context
 * to render to and the layer number currently being rendered.
 * @author Bryan Charles Bettis
 */
public class RenderEvent
{
	/** The graphics context used for drawing. */
	private final Graphics2D g2;
	/** The Layer currently being drawn. */
	private final int layer;
	
	/** Basic render event constructor.
	 * @param g2 the graphics context
	 * @param layer the layer currently being rendered
	 */
	public RenderEvent (Graphics2D g2, int layer)
	{
		this.g2 = g2;
		this.layer = layer;
	}
	
	/** The graphics context to draw to.
	 * @return the Graphics2D context of this render event
	 */
	public Graphics2D getContext()
	{
		return g2;
	}
	
	/** The layer currently drawing to <b>in the most recent layer set.</b>
	 * @return the number corresponding to the layer currently being rendered
	 * @see LayerSet
	 */
	public int getLayer()
	{
		return layer;
	}
}
