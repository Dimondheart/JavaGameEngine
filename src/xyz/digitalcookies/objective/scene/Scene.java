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

import xyz.digitalcookies.objective.graphics.RenderEvent;
import xyz.digitalcookies.objective.graphics.Renderer;
import xyz.digitalcookies.objective.utility.Stopwatch;

/** This class is the base class for all scenes.
 * @author Bryan Charles Bettis
 */
public abstract class Scene implements Renderer
{
	/** The name of the event property that indicates how much time has
	 * elapsed since the last update, excluding any amount of time the
	 * scene was paused since the last update. This property is
	 * handled automatically by the scene base class before the child
	 * class is called to update.
	 */
	public static final String UPDATE_ELAPSED = "elapsed";
	
	/** A timer that can be used for updating the scene.
	 * Paused/resumed along with this scene.
	 */
	private Stopwatch timer;
	/** If this scene is paused or not. */
	private boolean isPaused;
	/** If this scene should be rendered or not. */
	private boolean isVisible;
	/** The last time an update was performed, in seconds. */
	private double lastUpdate;
	
	/** Generic constructor. */
	public Scene()
	{
		timer = new Stopwatch();
		timer.start();
		timer.pause();
		setPaused(true);
		setVisible(false);
		lastUpdate = timer.getTimeSec();
	}
	
	@Override
	public final void render(RenderEvent event)
	{
		if (!isVisible())
		{
			return;
		}
		renderScene(event);
	}
	
	/** This method should perform updates for a scene that will take place
	 * when the scene is called to update.
	 * @param event an object containing primarily developer defined
	 * 		properties that can be used by the scene to perform updates
	 */
	protected abstract void updateScene(SceneUpdateEvent event);
	
	/** Render the components and contents of this scene.
	 * @param event the event containing the graphics context, etc.
	 */
	protected abstract void renderScene(RenderEvent event);
	
	/** Update this scene. Automatically returns if the scene is
	 * paused.
	 * @param event an object containing properties that can be used
	 * 		by the scene to perform updates, and various properties will
	 * 		be added, such as how long has passed since the scene was last
	 * 		resumed or last updated
	 */
	public final void update(SceneUpdateEvent event)
	{
		if (isPaused())
		{
			return;
		}
		// Get the current time of the scene timer
		double currTime = getTimer().getTimeSec();
		// Set the elapsed time in the event
		event.setProperty(Scene.UPDATE_ELAPSED, currTime - lastUpdate);
		// Perform 'actual' updates
		updateScene(event);
		// Update the time of the most recent update
		lastUpdate = currTime;
	}
	
	/** Check if this scene is paused, meaning its internal timer is paused
	 * and will return from the update method without performing scene
	 * updates.
	 * @return false if this scene will update when its update method
	 * 		is called
	 */
	public boolean isPaused()
	{
		return isPaused;
	}
	
	/** Set if this scene should be paused, meaning scene updates will not be
	 * performed while paused, even if the update method is called.
	 * @param pause false if this scene should update when its update method
	 * 		is called
	 */
	public void setPaused(boolean pause)
	{
		// Don't change anything if already set to specified value
		if (isPaused == pause)
		{
			return;
		}
		isPaused = pause;
		// Pause the scene timer
		if (isPaused())
		{
			getTimer().pause();
		}
		// Resume the scene timer
		else
		{
			getTimer().resume();
		}
		// Update the last update to reflect the current time
		lastUpdate = getTimer().getTimeSec();
	}
	
	/** Check if this scene is visible, meaning it will perform rendering
	 * when its render method is called.
	 * @return true if this scene should render when its render method is
	 * 		called
	 */
	public boolean isVisible()
	{
		return isVisible;
	}
	
	/** Set if this scene should render when its render method is
	 * called.
	 * @param visible true if this scene should be rendered when its
	 * 		render method is called
	 */
	public void setVisible(boolean visible)
	{
		this.isVisible = visible;
	}
	
	/** Get the timing object this scene can use to update itself and scene
	 * objects it contains.
	 * @return a timer that can be used to measure elapsed time when this scene
	 * 		is not paused
	 */
	public Stopwatch getTimer()
	{
		return timer;
	}
}
