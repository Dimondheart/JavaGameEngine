package core.graphics;

import java.awt.Graphics2D;

/** An event class which contains context information used to render
 * something to the screen. This includes the graphics context
 * to render to and the layer number currently being rendered.
 * @author Bryan Bettis
 */
public class RenderEvent
{
	/** The graphics context used for drawing. */
	private final Graphics2D g2;
	/** The Layer currently being drawn. */
	private final int layer;
	
	/** Basic render event constructor.
	 * @param g2 the graphics context
	 * @param layer the layer currently being rendered
	 */
	public RenderEvent (Graphics2D g2, int layer)
	{
		this.g2 = g2;
		this.layer = layer;
	}
	
	/** The graphics context to draw to.
	 * @return the Graphics2D context of this render event
	 */
	public Graphics2D getContext()
	{
		return g2;
	}
	
	/** The layer currently drawing to.
	 * @return the number corresponding to the layer currently being rendered
	 */
	public int getLayer()
	{
		return layer;
	}
}
