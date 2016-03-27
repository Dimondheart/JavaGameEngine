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

/** A set of layers that are rendered from the lowest layer to the highest
 * layer. Each layer stores Renderer objects in it, which are called to
 * render in the order they are added to a layer.
 * @author Bryan Charles Bettis
 */
public class LayerSet extends BoundedRenderer
{
	/** Total number of layers. */
	private int numLayers;
	/** List of the component layers. */
	private Layer[] layers;
	
	/** Standard constructor.
	 * @param numLayers the number of layers to setup
	 */
	public LayerSet(int numLayers)
	{
		this.numLayers = numLayers;
		// Setup the layers
		layers = new Layer[numLayers];
		for (int i = 0; i < numLayers; ++i)
		{
			layers[i] = new Layer();
		}
	}
	
	@Override
	public void render(RenderEvent event)
	{
		super.render(event);
		// Render the layers
		for (int i = 0; i < numLayers; ++i)
		{
			RenderEvent e = event.clone();
			e.setLayer(i);
			Layer layer = layers[i];
			synchronized(layer)
			{
				layer.render(e);
			}
		}
	}
	
	/** Get the number of layers in this layer set.
	 * @return the number of layers in this set
	 */
	public int getNumLayers()
	{
		return numLayers;
	}
	
	/** Add the specified renderer to specified layer.
	 * @param obj the Renderer to add
	 * @param layer the layer to add the Renderer to
	 */
	public void addRenderer(Renderer obj, int layer)
	{
		if (obj == null)
		{
			System.out.println(
					"WARNING: specified null when adding a "
					+ "renderer to a layer set."
					);
			Thread.dumpStack();
			return;
		}
		if (layer >= numLayers)
		{
			System.out.println(
					"ERROR: Attempted to add a renderer to an invalid layer"
					+ "index: "
					+ Integer.toString(layer)
					);
			Thread.dumpStack();
			return;
		}
		Layer l = layers[layer];
		synchronized(l)
		{
			l.addRenderer(obj);
		}
	}
	
	/** Add the specified renderer to all layers.
	 * @param obj the Renderer to add
	 */
	public void addRenderer(Renderer obj)
	{
		if (obj == null)
		{
			System.out.println(
					"WARNING: specified null when adding a "
					+ "renderer to a layer set."
					);
			Thread.dumpStack();
			return;
		}
		for (int i = 0; i < numLayers; ++i)
		{
			addRenderer(obj, i);
		}
	}
	
	/** Remove specified renderer from specified layer.
	 * @param obj the Renderer to remove
	 * @param layer the layer to remove the Renderer from
	 */
	public void removeRenderer(Renderer obj, int layer)
	{
		if (obj == null)
		{
			System.out.println(
					"WARNING: specified null when removing a "
					+ "renderer from a layer set."
					);
			Thread.dumpStack();
			return;
		}
		if (layer >= numLayers)
		{
			System.out.println(
					"ERROR: Attempted to remove a renderer from an invalid "
					+ "layer index: "
					+ Integer.toString(layer)
					);
			Thread.dumpStack();
			return;
		}
		Layer l = layers[layer];
		synchronized(l)
		{
			l.removeRenderer(obj);
		}
	}
	
	/** Remove the specified renderer from all layers.
	 * @param obj the Renderer to hide
	 */
	public void removeRenderer(Renderer obj)
	{
		if (obj == null)
		{
			System.out.println(
					"WARNING: specified null when removing a "
					+ "renderer from a layer set."
					);
			Thread.dumpStack();
			return;
		}
		for (int i = 0; i < numLayers; ++i)
		{
			removeRenderer(obj, i);
		}
	}
	
	/** Removes the specified Renderer from the specified layer, and then
	 * calls this method on any layer sets inside that layer.
	 * @param obj the renderer to recursively remove
	 * @param layer the layer to recursively remove the renderer from
	 */
	public void recursiveRemoveRenderer(Renderer obj, int layer)
	{
		if (obj == null)
		{
			System.out.println(
					"WARNING: specified null when recursively removing a "
					+ "renderer from a layer set."
					);
			Thread.dumpStack();
			return;
		}
		if (layer >= numLayers)
		{
			System.out.println(
					"ERROR: Attempted to recursively remove a renderer from an "
					+ "invalid layer index: "
					+ Integer.toString(layer)
					);
			Thread.dumpStack();
			return;
		}
		Layer l = layers[layer];
		synchronized(l)
		{
			l.recursiveRemoveRenderer(obj);
		}
	}
	
	/** Removes the specified Renderer from all layers, and also calls
	 * this method on any layer sets within the layers.
	 * @param obj the renderer to recursively remove
	 */
	public void recursiveRemoveRenderer(Renderer obj)
	{
		if (obj == null)
		{
			System.out.println(
					"WARNING: specified null when recursively removing a "
					+ "renderer from a layer set."
					);
			Thread.dumpStack();
			return;
		}
		for (int i = 0; i < numLayers; ++i)
		{
			recursiveRemoveRenderer(obj, i);
		}
	}
	
	/** Clear only the specified layer.
	 * @param layer the index of the layer (first index at 0)
	 */
	public void clearLayer(int layer)
	{
		if (layer >= numLayers)
		{
			System.out.println(
					"ERROR: Attempted to clear an invalid layer "
					+ "index: "
					+ Integer.toString(layer)
					);
			Thread.dumpStack();
			return;
		}
		Layer l = layers[layer];
		synchronized(l)
		{
			l.clear();
		}
	}
	
	/** Removes all Renderer(s) from all layers. */
	public void clearAllLayers()
	{
		for (int i = 0; i < numLayers; ++i)
		{
			clearLayer(i);
		}
	}
}
