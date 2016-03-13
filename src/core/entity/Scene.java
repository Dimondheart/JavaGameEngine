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

package core.entity;

import core.StopWatch;

/** This class is the base class for all scenes.
 * @author Bryan Charles Bettis
 */
public abstract class Scene
{
	protected StopWatch timer;
	private boolean isActive;
	
	public Scene()
	{
		timer = new StopWatch();
		timer.start();
		setActive(false);
	}
	
	public abstract void updateScene(SceneUpdateEvent event);
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public void setActive(boolean active)
	{
		isActive = active;
		if (isActive())
		{
			timer.resume();
		}
		else
		{
			timer.pause();
		}
	}
}
