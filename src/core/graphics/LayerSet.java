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
	
	/** Basic constructor. */
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
		for (int i = 0; i < numLayers; ++i)
		{
			layers[i].clear();
		}
	}
	
	/** Recursively removes the specified Renderer from all layers in this
	 * layer set, meaning if there are any layer sets within the layers of
	 * this layer set, then it will call this method on each of them. If used
	 * on the main layer set, this method will ensure a Renderer is completely
	 * removed from all <b>currently displayed</b> layers and layer sets.
	 * @param obj
	 */
	public synchronized void recursiveRemoveRenderer(Renderer obj)
	{
		for (Layer l : layers)
		{
			l.recursiveRemoveRenderer(obj);
		}
	}
	
	/** Resizes all layers in this layer set. Calling this method on the
	 * main layer set will have no effect, as the main layers are adjusted
	 * to fit the entire display area/window.
	 * @param newDims the new dimensions
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
			RenderEvent e = new RenderEvent(event.getContext(), i);
			layers[i].render(e);
		}
	}
}
