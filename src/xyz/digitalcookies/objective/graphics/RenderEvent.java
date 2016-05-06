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

package xyz.digitalcookies.objective.graphics;

import java.awt.Graphics2D;

import xyz.digitalcookies.objective.EngineEvent;

/** An event class which contains context information used to render
 * something to the screen. This includes the graphics context
 * to draw to.
 * @author Bryan Charles Bettis
 */
public class RenderEvent extends EngineEvent implements Cloneable
{
	/** The graphics context used for drawing. */
	private Graphics2D g;
	
	/** Basic render event constructor.
	 * @param g the graphics context
	 */
	public RenderEvent(Graphics2D g)
	{
		setGC(g);
	}
	
	/** The graphics context to draw to.
	 * @return the Graphics2D context of this render event
	 */
	public Graphics2D getGC()
	{
		return g;
	}
	
	/** Set the graphics context.
	 * @param g the new graphics context
	 */
	public void setGC(Graphics2D g)
	{
		this.g = g;
	}
	
	/** Create a copy of this render event. The new event will have its own
	 * graphics context and other property, so modification of the
	 * new event will not affect the original event (unless the properties
	 * are linked elsewhere.)
	 */
	@Override
	public RenderEvent clone()
	{
		try
		{
			RenderEvent newEvent = (RenderEvent) super.clone();
			newEvent.setGC((Graphics2D) getGC().create());
			return newEvent;
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			System.out.println("INTERNAL ERROR: Unable to clone RenderEvent.");
			return null;
		}
	}
}
