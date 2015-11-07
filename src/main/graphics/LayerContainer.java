package main.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import javax.swing.JComponent;

/** Handles multiple layers of rendering for a window. */
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
	
	/** Standard layer container for the specified window. */
	public LayerContainer(Window window, Dimension dims, int numLayers)
	{
		myWin = window;
		this.numLayers = numLayers;
		initDims = dims;
		// Setup the layers
		layers = new Layer[this.numLayers];
		for (int i = 0; i < this.numLayers; ++i)
		{
			layers[i] = new Layer(dims);
		}
		this.setPreferredSize(dims);
	}
	
	/** Gets the drawing surface for the specified layer.
	 * @param layer the index of the layer
	 * @return (Graphics2D) A reference to the drawing surface
	 */
	public Graphics2D getDrawingSurface(int layer)
	{
		return layers[layer].getDrawingSurface();
	}
	
	/** Get the container width. */
	public int getLCWidth()
	{
		return width;
	}
	
	/** Get the container height. */
	public int getLCHeight()
	{
		return height;
	}
	
	/** Return the horizontal scale factor. */
	public double getHorizScale()
	{
		return horizScale;
	}
	
	/** Return the vertical scale factor. */
	public double getVertScale()
	{
		return vertScale;
	}
	
	/** Clears the specified layer.
	 * @param layer the index of the layer.
	 */
	public synchronized void clearLayer(int layer)
	{
		layers[layer].clear();
	}
	
	/** Clears all layers. */
	public synchronized void clearAllLayers()
	{
		for (int i = 0; i < numLayers; ++i)
		{
			clearLayer(i);
		}
	}
	
	/** Clears only the specified layers. */
	public synchronized void clearLayers(int[] layersToClear)
	{
		for (int i : layersToClear)
		{
			clearLayer(i);
		}
	}
	
	/** Clears all layers starting with <tt>start</tt> layer and stopping with
	 * <tt>stop</tt> layer.
	 * @param start the first layer to clear
	 * @param stop the last layer to clear
	 */
	public synchronized void clearLayersInRange(int start, int stop)
	{
		for (int i = start; i <= stop; ++i)
		{
			clearLayer(i);
		}
	}
	
	@Override
	protected synchronized void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		// Clear the graphics context
		g2.setBackground(Color.black);
		g2.clearRect(0, 0, myWin.getWidth(), myWin.getHeight());
		// Render the layers
		for (int i = 0; i < numLayers; ++i)
		{
			// Draw only enabled layers
			if (layers[i].isEnabled())
			{
				layers[i].flip(g2);
			}
		}
	}
	
	/** Resizes all layers.
	 * @param newDims the new dimensions
	 */
	protected synchronized void adjustSize(Dimension newDims)
	{
		// Resize the whole container
		this.setSize(newDims);
		// Resize each layer
		for (int i = 0; i < numLayers; ++i)
		{
			layers[i].adjustSize(newDims);
		}
		updateScaleFactors();
	}
	
	/** Update the scale factors relative to the base dimensions. */
	private void updateScaleFactors()
	{
		horizScale = (double)getWidth()/initDims.getWidth();
		vertScale = (double)getHeight()/initDims.getHeight();
	}
}
