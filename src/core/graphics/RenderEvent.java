package core.graphics;

import java.awt.Graphics2D;

/** An event class that contains information for a Renderer to draw itself
 * to the screen.
 * @author Bryan Bettis
 */
public class RenderEvent
{
	/** The graphics context used for drawing. */
	private final Graphics2D g2;
	/** The Layer currently being drawn. */
	private final int layer;
	
	/** Basic constructor. */
	public RenderEvent (Graphics2D g2, int layer)
	{
		this.g2 = g2;
		this.layer = layer;
	}
	
	/** The graphics context to draw to. */
	public Graphics2D getContext()
	{
		return g2;
	}
	
	/** The layer currently drawing to. */
	public int getLayer()
	{
		return layer;
	}
}
