package core.graphics;

import java.util.LinkedList;

/** A single layer used for organizing screen drawing.
 * Each layer holds Renderer(s) that will be called in order once the layer
 * has been told to render itself to the specified graphics context.
 * @author Bryan Bettis
 */
public class Layer implements Renderer
{
	/** The Renderer(s) rendering to this layer. */
	private LinkedList<Renderer> renderers;
	/** The layer sets on this layer. */
	private LinkedList<LayerSet> layerSets;
	
	/** The normal constructor for a Layer. */
	public Layer()
	{
		renderers = new LinkedList<Renderer>();
		layerSets = new LinkedList<LayerSet>();
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
		renderers.addLast(obj);
		// If it is also a layer set, add it to that list
		if (obj instanceof LayerSet)
		{
			layerSets.addLast((LayerSet) obj);
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
			layerSets.remove((LayerSet) obj);
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
		for (LayerSet ls : layerSets)
		{
			ls.recursiveRemoveRenderer(obj);
		}
	}
	
	@Override
	public synchronized void render(RenderEvent e)
	{
		// Draw each Renderer
		for (Renderer r : renderers)
		{
			r.render(e);
		}
	}
	
	/** Remove each Renderer in this layer. */
	public synchronized void clear()
	{
		renderers.clear();
	}
}
