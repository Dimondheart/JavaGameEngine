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

/** A Renderer is an object that can draw to the screen using the game
 * engine's setup.
 * TODO add more documentation here.
 * @author Bryan Charles Bettis
 */
public interface Renderer
{
	/** Called to draw a renderer to the window.
	 * @param event the context information used to render
	 * @see RenderEvent
	 */
	public abstract void render(RenderEvent event);
	
	/** The layer(s) this Renderer is currently showing on.
	 * TODO implement this, it currently only returns an empty array
	 * TODO make this be able to get what layers this is on from all
	 * 		layer sets and relate the layer set object to the layer indexes
	 * @return array of integers corresponding to layer indexes
	 */
	public default int[] getCurrentLayers()
	{
		int[] layers = {};
		return layers;
	}
	
	/** Cleans up a renderer before it is no longer used. */
	public default void destroy()
	{
		GfxManager.getMainLayerSet().recursiveRemoveRenderer(this);
	}
}
