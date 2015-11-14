package main.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.util.LinkedList;
import javax.swing.JComponent;

/** Handles multiple layers of rendering for a window.
 * Gets the drawing surface for the specified window layer.
 * <br>For the primary window, there are 10 layers.
 * They should be used as follows:
 * <br>
 * <br>0-2: Background Layers
 * <br>3-6: Main Content Layers
 * <br>7-9: GUI Layers
 * <br>
 * <br> Using "sub-layers" (first drawn on a layer = lowest sub-layer)
 * should be preferred where possible.
 * @param window the window to get the layer from.
 * @param layer the index of the layer.
 * @return (Graphics2D) The graphics context to render to.
 * @author Bryan
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
	/** The list of new renderers to add. */
	private LinkedList<Renderer> addRenderers;
	/** The list of renderers to be removed. */
	private LinkedList<Renderer> remRenderers;
	
	/** Standard layer container for the specified window. */
	public LayerContainer(Window window, Dimension dims, int numLayers)
	{
		myWin = window;
		addRenderers = new LinkedList<Renderer>();
		remRenderers = new LinkedList<Renderer>();
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
	
	public synchronized void addRenderer(Renderer obj)
	{
		addRenderers.addLast(obj);
	}
	
	public synchronized void removeRenderer(Renderer obj)
	{
		remRenderers.addLast(obj);
	}
	
	@Override
	protected synchronized void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		// Clear the graphics context
		g2.setBackground(Color.black);
		g2.clearRect(0, 0, myWin.getWidth(), myWin.getHeight());
		// Add new renderers
		while (true)
		{
			if (addRenderers.isEmpty())
			{
				break;
			}
			Renderer newObj = addRenderers.poll();
			if (newObj == null)
			{
				continue;
			}
			layers[newObj.getLayer()].addRenderer(newObj);
		}
		// Remove destroyed renderers
		while (true)
		{
			if (remRenderers.isEmpty())
			{
				break;
			}
			Renderer newObj = remRenderers.poll();
			if (newObj == null)
			{
				continue;
			}
			layers[newObj.getLayer()].removeRenderer(newObj);
		}
		// Render the layers
		for (int i = 0; i < numLayers; ++i)
		{
			layers[i].flip(g2);
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
		// Remove any renderers queued for adding/removal
		addRenderers.clear();
		remRenderers.clear();
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
