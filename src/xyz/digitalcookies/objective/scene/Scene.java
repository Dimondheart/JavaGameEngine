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

package xyz.digitalcookies.objective.scene;

import xyz.digitalcookies.objective.graphics.Renderer;
import xyz.digitalcookies.objective.utility.StopWatch;

/** This class is the base class for all scenes.
 * @author Bryan Charles Bettis
 */
public abstract class Scene implements Renderer
{
	/** The timing device, is paused and resumed when this scene
	 * is activated or deactivated.
	 */
	private StopWatch timer;
	/** If this scene is paused or not; affects updating
	 * of this scene.
	 */
	private boolean isPaused;
	private boolean doRender;
	/** The x-offset of this scene, in pixels. */
	private int offsetX;
	/** The y-offset of this scene, in pixels. */
	private int offsetY;
	private double pixelsPerUnit;
	
	public Scene()
	{
		timer = new StopWatch();
		timer.start();
		setPaused(true);
		setRendering(false);
		setOffset(0, 0);
		setScale(1);
	}
	
	public abstract void updateScene(SceneUpdateEvent event);
	
	public boolean isPaused()
	{
		return isPaused;
	}
	
	public void setPaused(boolean pause)
	{
		if (isPaused == pause)
		{
			return;
		}
		isPaused = pause;
		if (isPaused())
		{
			timer.pause();
		}
		else
		{
			timer.resume();
		}
	}
	
	public boolean isRendering()
	{
		return doRender;
	}
	
	public void setRendering(boolean doRender)
	{
		this.doRender = doRender;
	}
	
	public StopWatch getTimer()
	{
		return timer;
	}
	
	public double getOffsetX()
	{
		return offsetX;
	}
	
	public double getOffsetY()
	{
		return offsetY;
	}
	
	public double getScale()
	{
		return 1 / pixelsPerUnit;
	}
	
	public void zoom(double units)
	{
		if (units > 0)
		{
			setScale(pixelsPerUnit*Math.pow(0.9, units));
		}
		else if (units < 0)
		{
			setScale(pixelsPerUnit*Math.pow(1.1, -units));
		}
	}
	
	protected void setOffset(int ox, int oy)
	{
		offsetX = ox;
		offsetY = oy;
	}
	
	protected void setScale(double pixelsPerUnit)
	{
		if (pixelsPerUnit == 0)
		{
			return;
		}
		this.pixelsPerUnit = pixelsPerUnit;
	}
}