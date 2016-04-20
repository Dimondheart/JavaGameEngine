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
import xyz.digitalcookies.objective.utility.Stopwatch;

/** This class is the base class for all scenes.
 * @author Bryan Charles Bettis
 */
public abstract class Scene implements Renderer
{
	/** A timer that can be used for updating the scene objects.
	 * Synchronized with the isUpdating state of this scene.
	 */
	private Stopwatch timer;
	/** If this scene should be updated or not. */
	private boolean doUpdate;
	/** If this scene should be rendered or not. */
	private boolean doRender;
	
	/** Generic constructor. */
	public Scene()
	{
		timer = new Stopwatch();
		timer.start();
		setUpdating(true);
		setRendering(false);
	}
	
	/** Update this scene (including objects it contains).
	 * @param event contains properties that the scene can use to affect
	 * updating.
	 */
	public abstract void updateScene(SceneUpdateEvent event);
	
	/** Check if this scene should update itself and the scene objects it
	 * contains. Subclasses should check this before performing updates.
	 * @return true if this scene should update when its update method
	 * 		is called
	 */
	public boolean isUpdating()
	{
		return doUpdate;
	}
	
	/** Set if this scene should update itself when its update method is
	 * called.
	 * @param update true if this scene should update when its update method
	 * 		is called
	 */
	public void setUpdating(boolean update)
	{
		if (doUpdate == update)
		{
			return;
		}
		doUpdate = update;
		if (isUpdating())
		{
			timer.resume();
		}
		else
		{
			timer.pause();
		}
	}
	
	/** Check if this scene should render itself and the scene objects it
	 * contains. Subclasses should check this before rendering.
	 * @return true if this scene should render when its render method is
	 * 		called
	 */
	public boolean isRendering()
	{
		return doRender;
	}
	
	/** Set if this scene should be rendered when its render method is
	 * called.
	 * @param doRender true if this scene should be rendered when its
	 * 		render method is called
	 */
	public void setRendering(boolean doRender)
	{
		this.doRender = doRender;
	}
	
	/** Get the timing object this scene can use to update itself and scene
	 * objects it contains.
	 * @return a timer that can be used to measure time when this scene
	 * 		is not paused
	 */
	public Stopwatch getTimer()
	{
		return timer;
	}
}
