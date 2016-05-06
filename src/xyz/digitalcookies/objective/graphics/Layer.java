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

import java.util.ArrayList;

import xyz.digitalcookies.objective.input.GUIMonitor;
import xyz.digitalcookies.objective.input.gui.GUIElement;

/** A single layer used for organizing screen drawing.
 * Each layer holds Renderer(s) that will be called in order once the layer
 * has been told to render itself to the specified graphics context.
 * @author Bryan Charles Bettis
 */
class Layer implements Renderer
{
	/** The Renderer(s) rendering to this layer. */
	private ArrayList<Renderer> renderers;
	/** The layer sets on this layer. */
	private ArrayList<LayerSet> layerSets;
	
	/** The normal constructor for a Layer. */
	public Layer()
	{
		renderers = new ArrayList<Renderer>();
		layerSets = new ArrayList<LayerSet>();
	}
	
	/** Add specified renderer to this layer.
	 * @param obj the Renderer to add
	 */
	public synchronized void addRenderer(Renderer obj)
	{
		// Don't add the same renderer more than once
		if (renderers.contains(obj))
		{
			return;
		}
		// Add to the list of renderers
		renderers.add(obj);
		// If it is also a layer set, add it to that list
		if (obj instanceof LayerSet)
		{
			layerSets.add((LayerSet) obj);
		}
		// Add GUI elements to the GUI manager
		if (obj instanceof GUIElement)
		{
			GUIMonitor.addGUIElement((GUIElement) obj);
		}
	}
	
	/** Remove the specified renderer from this layer.
	 * @param obj the Renderer to remove
	 */
	public synchronized void removeRenderer(Renderer obj)
	{
		renderers.remove(obj);
		// If it is also a layer set, remove it from that list too
		if (obj instanceof LayerSet)
		{
			layerSets.remove(obj);
		}
		// Remove GUI elements from the GUI manager
		if (obj instanceof GUIElement)
		{
			GUIMonitor.removeGUIElement((GUIElement) obj);
		}
	}
	
	/** Recursively removes the specified Renderer.
	 * @param obj the Renderer object to recursively remove
	 */
	public synchronized void recursiveRemoveRenderer(Renderer obj)
	{
		// Remove from this layer
		removeRenderer(obj);
		// Recursively remove from all sub layer sets
		layerSets.forEach(
				(LayerSet ls)->
				{
					ls.recursiveRemoveRenderer(obj);
				}
				);
	}
	
	@Override
	public synchronized void render(RenderEvent event)
	{
		renderers.forEach(
				(Renderer r)->
				{
					RenderEvent e2 = event.clone();
					r.render(e2);
					e2.getGC().dispose();
				}
				);
	}
	
	/** Remove all Renderers in this layer. */
	public synchronized void clear()
	{
		renderers.clear();
		layerSets.clear();
	}
}
