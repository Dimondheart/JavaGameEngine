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
public class LayerSet implements Renderer
{
	/** Total number of layers. */
	private int numLayers;
	/** List of the component layers. */
	private Layer[] layers;
	/** Width of the layers. */
	private int width;
	/** Height of the layers. */
	private int height;
	
	/** Basic constructor.
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
	
	/** Get the number of layers in this layer set.
	 * @return the number of layers in this set
	 */
	public int getNumLayers()
	{
		return numLayers;
	}
	
	/** Get the width of this set of layers.
	 * @return the width of a layer in this set
	 */
	public int getLayerSetWidth()
	{
		return width;
	}
	
	/** Get the height of this set of layers.
	 * @return the height of a layer in this set
	 */
	public int getLayerSetHeight()
	{
		return height;
	}
	
	/** Add the specified renderer to specified layer.
	 * @param obj the Renderer to add
	 * @param layer the layer to add the Renderer to
	 */
	public synchronized void addRenderer(Renderer obj, int layer)
	{
		layers[layer].addRenderer(obj);
	}
	
	/** Remove specified renderer from specified layer.
	 * @param obj the Renderer to remove
	 * @param layer the layer to remove the Renderer from
	 */
	public synchronized void removeRenderer(Renderer obj, int layer)
	{
		layers[layer].removeRenderer(obj);
	}
	
	/** Remove the specified renderer from all layers.
	 * @param obj the Renderer to hide
	 */
	public synchronized void removeRenderer(Renderer obj)
	{
		for (Layer l : layers)
		{
			l.removeRenderer(obj);
		}
	}
	
	/** Removes all Renderer(s) from all layers. */
	public synchronized void clearAllLayers()
	{
		// Clear each layer
		for (Layer layer : layers)
		{
			layer.clear();
		}
	}
	
	/** Clear only the specified layer.
	 * @param layer the index of the layer (starts at 0)
	 */
	public synchronized void clearLayer(int layer)
	{
		layers[layer].clear();
	}
	
	/** Removes the specified Renderer from the specified layer, and then
	 * calls this method on any layer sets inside that layer.
	 * @param obj the renderer to recursively remove
	 * @param layer the layer to recursively remove the renderer from
	 */
	public synchronized void recursiveRemoveRenderer(Renderer obj, int layer)
	{
		layers[layer].recursiveRemoveRenderer(obj);
	}
	
	/** Removes the specified Renderer from all layers, and also calls
	 * this method on any layer sets within the layers.
	 * @param obj the renderer to recursively remove
	 */
	public synchronized void recursiveRemoveRenderer(Renderer obj)
	{
		for (Layer layer : layers)
		{
			layer.recursiveRemoveRenderer(obj);
		}
	}
	
	/** Resizes all layers in this layer set. Calling this method on the
	 * main layer set will have no effect, as the main layers are adjusted
	 * to fit the entire display area/window.
	 * @param width the new width of the layers
	 * @param height the new height of the layers
	 */
	public synchronized void resizeLayers(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	@Override
	public void render(RenderEvent event)
	{
		// Render the layers
		for (int i = 0; i < numLayers; ++i)
		{
			RenderEvent e = event.clone();
			e.setLayer(i);
			layers[i].render(e);
		}
	}
}
