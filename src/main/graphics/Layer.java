package main.graphics;

import java.awt.Graphics2D;
import java.util.LinkedList;

/** One layer in a LayerContainer. */
public class Layer
{
	/** The renderers that render to this layer. */
	private LinkedList<Renderer> renderers;
	/** The list of new renderers to add. */
	private LinkedList<Renderer> addRenderers;
	/** The list of renderers to be removed. */
	private LinkedList<Renderer> remRenderers;
	
	/** The normal constructor for a Layer. */
	public Layer()
	{
		renderers = new LinkedList<Renderer>();
		addRenderers = new LinkedList<Renderer>();
		remRenderers = new LinkedList<Renderer>();
	}
	
	/** Add specified renderer to this layer. */
	public synchronized void addRenderer(Renderer obj)
	{
		addRenderers.add(obj);
	}
	
	/** Remove specified renderer from this layer. */
	public synchronized void removeRenderer(Renderer obj)
	{
		remRenderers.add(obj);
	}
	
	/** Update and draw this layer to the specified surface.
	 * @param g2 the graphics context to draw to
	 */
	public synchronized void flip(Graphics2D g2)
	{
		// Add new renderers
		while (true)
		{
			if (addRenderers.isEmpty())
			{
				break;
			}
			Renderer r = addRenderers.poll();
			if (r != null)
			{
				renderers.addLast(r);
			}
		}
		// Remove destroyed renderers
		while (true)
		{
			if (remRenderers.isEmpty())
			{
				break;
			}
			Renderer r = remRenderers.poll();
			if (r != null)
			{
				renderers.remove(r);
			}
		}
		// Draw all renderers
		for (Renderer r : renderers)
		{
			r.render(g2);
		}
	}
	
	/** Remove all Renderer's from this layer. */
	public synchronized void clear()
	{
		renderers.clear();
		addRenderers.clear();
		remRenderers.clear();
	}
}
