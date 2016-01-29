package core.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import javax.swing.JComponent;

/** Handles multiple layers of rendering for a window.
 * Gets the drawing surface for the specified window layer.
 * @author Bryan Bettis
 */
public class MainLayerSetContainer extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	/** Window this layer container is part of. */
	private Window myWin;
//	/** Initial container dimensions. */
//	private final Dimension initDims;
	/** The primary layer set which is drawn to the screen. */
	private MainLayerSet mainLayers;
	
	/** Standard layer container for the specified window.
	 * @param window the window this LayerContainer is part of
	 * @param dims the initial dimensions of the layers
	 * @param numLayers the number of main layers to create
	 */
	public MainLayerSetContainer(Window window, Dimension dims, int numLayers)
	{
		myWin = window;
//		initDims = dims;
		// Setup the layers
		mainLayers = new MainLayerSet(numLayers);
		setPreferredSize(dims);
	}
	
	/** Gets the main layer set.
	 * @return the LayerSet for the main drawing layers
	 */
	public LayerSet getLayerSet()
	{
		return mainLayers;
	}
	
	@Override
	public synchronized void update(Graphics g)
	{
	}
	
	@Override
	public synchronized void paintComponent(Graphics g)
	{
		// TODO add a buffer or buffer flipping strategy
		Graphics2D g2 = (Graphics2D)g;
		// Clear the graphics context
		g2.setBackground(Color.black);
		g2.clearRect(0, 0, myWin.getWidth(), myWin.getHeight());
		// Render the main layer container
		mainLayers.render(new RenderEvent(g2, -1));
	}
	
	/** Resizes the main layer container.
	 * @param newDims the new dimensions
	 */
	protected synchronized void adjustSize(Dimension newDims)
	{
		// Resize the whole container
		this.setSize(newDims);
		// Adjust the main layer set
		mainLayers.resizeLayers(newDims);
	}
}
