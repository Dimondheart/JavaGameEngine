package main.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import javax.swing.JComponent;

/** Handles multiple layers of rendering for a window.
 * Gets the drawing surface for the specified window layer.
 * <br>For the primary window, there are 10 layers.
 * They could be used as follows:
 * <br>
 * <br>0-2: Background Layers
 * <br>3-6: Main Content Layers
 * <br>7-9: GUI Layers
 * <br>
 * <br> Using "sub-layers" (first drawn on a layer = lowest sub-layer)
 * should be preferred where possible.
 */
public class LayerContainer extends JComponent
{
	private static final long serialVersionUID = 1L;
	/** Window this layer container is part of. */
	private Window myWin;
	/** Initial container dimensions. */
	private final Dimension initDims;
	/** Total number of layers. */
	private int numLayers;
	/** List of the component layers. */
	private Layer[] layers;
	/** Width of the layers. */
	private int width;
	/** Height of the layers. */
	private int height;
	/** The horizontal scale factor. */
	private double horizScale = 1.0;
	/** The vertical scale factor. */
	private double vertScale = 1.0;
	
	/** Standard layer container for the specified window.
	 * @param window the window this LayerContainer is part of
	 * @param dims the initial dimensions of the layers
	 * @param numLayers the number of layers to create
	 */
	public LayerContainer(Window window, Dimension dims, int numLayers)
	{
		myWin = window;
		this.numLayers = numLayers;
		initDims = dims;
		// Setup the layers
		layers = new Layer[this.numLayers];
		for (int i = 0; i < this.numLayers; ++i)
		{
			layers[i] = new Layer();
		}
		this.setPreferredSize(dims);
	}
	
	/** Get the container width.
	 * @return the width of the LayerContainer
	 */
	public int getLCWidth()
	{
		return width;
	}
	
	/** Get the container height.
	 * @return the height of the LayerContainer
	 */
	public int getLCHeight()
	{
		return height;
	}
	
	/** Return the horizontal scale factor.
	 * @return the horizontal scale factor
	 */
	public double getHorizScale()
	{
		return horizScale;
	}
	
	/** Return the vertical scale factor.
	 * @return the vertical scale factor
	 */
	public double getVertScale()
	{
		return vertScale;
	}
	
	/** Add specified renderer to specified layer. */
	public synchronized void showRenderer(Renderer obj, int layer)
	{
		layers[layer].addRenderer(obj);
	}
	
	/** Remove specified renderer from specified layer. */
	public synchronized void hideRenderer(Renderer obj, int layer)
	{
		layers[layer].removeRenderer(obj);
	}
	
	/** Remove the specified renderer from all layers. */
	public synchronized void hideRenderer(Renderer obj)
	{
		for (Layer l : layers)
		{
			l.removeRenderer(obj);
		}
	}
	
	@Override
	protected synchronized void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		// Clear the graphics context
		g2.setBackground(Color.black);
		g2.clearRect(0, 0, myWin.getWidth(), myWin.getHeight());
		// Update and render the layers
		for (int i = 0; i < numLayers; ++i)
		{
			RenderEvent e = new RenderEvent(g2, i);
			layers[i].flip(e);
		}
	}
	
	/** Resizes all layers.
	 * @param newDims the new dimensions
	 */
	protected synchronized void adjustSize(Dimension newDims)
	{
		// Resize the whole container
		this.setSize(newDims);
		// Update the scale factors for the two axes
		updateScaleFactors();
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
	
	/** Update the scale factors relative to the base dimensions. */
	private synchronized void updateScaleFactors()
	{
		horizScale = (double)getWidth()/initDims.getWidth();
		vertScale = (double)getHeight()/initDims.getHeight();
	}
}
