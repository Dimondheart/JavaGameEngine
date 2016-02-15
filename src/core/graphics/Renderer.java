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

/** Renderer is the base class for all classes that will be drawn to the
 * screen using the setup of this game engine. All classes that will be
 * directly drawing graphics to the main display should implement this class.
 * @author Bryan Charles Bettis
 */
public interface Renderer
{
	/** Called to draw a Renderer to the window.
	 * @param event the context information used to render
	 * @see RenderEvent
	 */
	public abstract void render(RenderEvent event);
	
	/** Cleans up a renderer before it is no longer used. This must be called
	 * when changing game states to remove Renderers that will not be needed
	 * in the new game state.
	 */
	public default void destroy()
	{
		GfxManager.getMainLayerSet().recursiveRemoveRenderer(this);
	}
}
