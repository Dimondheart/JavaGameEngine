package main.graphics;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import javax.swing.*;

/** One layer in a LayerContainer. */
public class Layer extends JPanel
{
	private static final long serialVersionUID = 1L;
	/** The dimensions for this layer. */
	private Dimension myDims;
	/** The buffered image for this surface. */
	private BufferedImage buffImg;
	/** The buffered image's surface for this layer. */
	private Graphics2D buffSurf;
	/** If this layer is in use. */
	private boolean enabled = false;
	/** The original composite of the surface to reset to after clearing
	 * using an AlphaComposite.
	 */
	private Composite oldComp = null;
	
	/** The normal constructor for a Layer.
	 * @param width of the layer
	 * @param height of the layer
	 */
	public Layer(Dimension dims)
	{
		adjustSize(dims);
	}
	
	/** Creates a new BufferedImage for this layer's graphics. */
	private void createNewBuffer()
	{
		buffImg = new BufferedImage(
				getW(),
				getH(),
				BufferedImage.TYPE_INT_ARGB
				);
		buffSurf = buffImg.createGraphics();
		// Initial setup of the original composite
		if (oldComp == null)
		{
			oldComp = buffSurf.getComposite();
		}
	}
	
	/** Gets the buffered surface of this frame to draw on.
	 * @return (Graphics2D) The buffered drawing surface
	 */
	public synchronized Graphics2D getDrawingSurface()
	{
		// Layer will be drawn to, enable this layer
		enabled = true;
		return buffSurf;
	}
	
	@Override
	public synchronized void paintComponent(Graphics g)
	{
		// Do absolutely nothing
	}
	
	/** Draws this layer to the specified surface.
	 * @param g2 the graphics context to draw to
	 */
	public synchronized void flip(Graphics2D g2)
	{
		if (enabled)
		{
			g2.drawImage(buffImg, 0, 0, getW(), getH(), null);
		}
	}
	
	/** Clears the buffer of this layer. */
	public synchronized void clear()
	{
		// Don't bother to clear an empty layer
		if (enabled)
		{
			// Change the composite
			buffSurf.setComposite(AlphaComposite.Clear);
			// Clear the surface
			buffSurf.fillRect(0, 0, getW(), getH());
			// Set the composite back to the original
			buffSurf.setComposite(oldComp);
			// Disable the layer
			enabled = false;
		}
	}
	
	/** Get the current layer width. */
	private int getW()
	{
		return myDims.width;
	}
	
	/** Get the current layer width. */
	private int getH()
	{
		return myDims.height;
	}
	
	/** Adjusts the dimensions of this layer. */
	public synchronized void adjustSize(Dimension newDims)
	{
		myDims = newDims;
		this.setSize(newDims);
		createNewBuffer();
	}
}
