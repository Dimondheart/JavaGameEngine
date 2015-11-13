package main.graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.LinkedList;

/** One layer in a LayerContainer. */
public class Layer
{
	/** If this layer is in use. */
	private boolean enabled = true;
	/** The renderers that render to this layer. */
	private LinkedList<Renderer> renderers;
	
	/** The normal constructor for a Layer.
	 * @param width of the layer
	 * @param height of the layer
	 */
	public Layer(Dimension dims)
	{
		renderers = new LinkedList<Renderer>();
		adjustSize(dims);
	}
	
	public synchronized void addRenderer(Renderer obj)
	{
		if (obj != null)
		{
			renderers.addLast(obj);
		}
	}
	
	public synchronized void removeRenderer(Renderer obj)
	{
		if (obj != null)
		{
			renderers.remove(obj);
		}
	}
	
	/** Draws this layer to the specified surface.
	 * @param g2 the graphics context to draw to
	 */
	public synchronized void flip(Graphics2D g2)
	{
		for (Renderer r : renderers)
		{
			r.render(g2);
		}
	}
	
	/** Remove all Renderer(s) from this layer. */
	public synchronized void clear()
	{
		renderers.clear();
	}
	
	/** Adjusts anything when the LayerContainer is resized. */
	public synchronized void adjustSize(Dimension newDims)
	{
	}
}
