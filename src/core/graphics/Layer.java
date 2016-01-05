package core.graphics;

import java.util.LinkedList;

import core.graphics.gui.GUIObject;

/** A single layer used for organizing screen drawing.
 * Each layer holds Renderer(s) that will be called in order once the layer
 * has been told to render itself to the specified graphics context.
 * @author Bryan Bettis
 */
public class Layer
{
	/** The Renderer(s) rendering to this layer. */
	private LinkedList<PrimaryRenderer> renderers;
	/** The GUI objects on this layer. */
	private LinkedList<GUIObject> guiObjects;
	
	/** The normal constructor for a Layer. */
	public Layer()
	{
		renderers = new LinkedList<PrimaryRenderer>();
		guiObjects = new LinkedList<GUIObject>();
	}
	
	/** Add specified renderer to this layer.
	 * @param obj the Renderer to add
	 */
	public synchronized void addRenderer(PrimaryRenderer obj)
	{
		// Don't add the same renderer more than once
		if (renderers.contains(obj))
		{
			return;
		}
		renderers.addLast(obj);
		// Add GUI elements also to the list of GUI objects
		if (obj instanceof GUIObject)
		{
			guiObjects.add((GUIObject) obj);
		}
	}
	
	/** Remove specified renderer from this layer.
	 * @param obj the Renderer to remove
	 */
	public synchronized void removeRenderer(PrimaryRenderer obj)
	{
		renderers.remove(obj);
		// Remove any GUI elements from the GUI object list
		if (obj instanceof GUIObject)
		{
			guiObjects.remove((GUIObject) obj);
		}
	}
	
	/** Update and draw this layer to the specified surface.
	 * @param e the event object to pass to each Renderer
	 */
	public synchronized void flip(RenderEvent e)
	{
		// Draw each Renderer
		for (PrimaryRenderer r : renderers)
		{
			r.render(e);
		}
	}
	
	/** Remove all Renderer's from this layer. */
	public synchronized void clear()
	{
		renderers.clear();
		guiObjects.clear();
	}
}
