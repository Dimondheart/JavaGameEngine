package core.graphics;

import java.util.LinkedList;

/** A single layer used for organizing screen drawing.
 * Each layer holds Renderer(s) that will be called in order once the layer
 * has been told to render itself to the specified graphics context.
 * @author Bryan Bettis
 */
public class Layer
{
	/** The Renderer(s) rendering to this layer. */
	private LinkedList<Renderer> renderers;
	
	/** The normal constructor for a Layer. */
	public Layer()
	{
		renderers = new LinkedList<Renderer>();
	}
	
	/** Add specified renderer to this layer. */
	public synchronized void addRenderer(Renderer obj)
	{
		renderers.addLast(obj);
	}
	
	/** Remove specified renderer from this layer. */
	public synchronized void removeRenderer(Renderer obj)
	{
		renderers.remove(obj);
	}
	
	/** Update and draw this layer to the specified surface.
	 * @param g2 the graphics context to draw to
	 */
	public synchronized void flip(RenderEvent e)
	{
		// Draw all renderers
		for (Renderer r : renderers)
		{
			r.render(e);
		}
	}
	
	/** Remove all Renderer's from this layer. */
	public synchronized void clear()
	{
		renderers.clear();
	}
}