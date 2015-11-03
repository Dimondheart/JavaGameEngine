package main.graphics;

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
	/** If this layer is enabled or not. */
	private boolean enabled = false;
	
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
		// Disable this layer when it is reset
		enabled = false;
	}
	
	/** Gets the buffered surface of this frame to draw on.
	 * @return (Graphics2D) The buffered drawing surface
	 */
	public synchronized Graphics2D getDrawingSurface()
	{
		// Enable this layer
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
		g2.drawImage(buffImg, 0, 0, getW(), getH(), null);
	}
	
	/** Clears the buffer of this layer. */
	public synchronized void clear()
	{
		createNewBuffer();
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
	
	/** Gets if this layer is currently enabled or not. */
	public synchronized boolean isEnabled()
	{
		return enabled;
	}
}
