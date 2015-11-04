package main.graphics;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JFrame;

/** Handles the rendering thread. */
public class GfxManager implements main.CustomRunnable
{
	/** Default dimensions for a new window. */
	public static final Dimension DEF_DIMS = new Dimension(480,270);
	/** Number of layers to create for the main window. */
	public static final int NUM_MAIN_LAYERS = 14;
	
	/** The main window frame. */
	private static JFrame mainWin;
	/** The layer container for the main window. */
	private static LayerContainer mainLayers;
	/** Component maps for all windows. */
	private static volatile ConcurrentHashMap<Window, LinkedList<Component>> compMaps;
	/** Thread for this system. */
	private Thread thread;
	/** Thread manager. */
	private main.ThreadClock clock;
	
	/** Normal graphics renderer setup. */
	public GfxManager()
	{
		System.out.println("Setting Up Graphics System...");
		compMaps = new ConcurrentHashMap<Window, LinkedList<Component>>();
		mainWin = new JFrame("My Game Framework");
		mainLayers = new LayerContainer(mainWin, DEF_DIMS, NUM_MAIN_LAYERS);
		addComponent(mainWin, mainLayers);
		// Run at about 60 FPS
		clock = new main.ThreadClock(16);
	}
	
	@Override
	public void start()
	{
		System.out.println("Starting Graphics System...");
		mainWin.pack();
		mainWin.setVisible(true);
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run()
	{
		while (true)
		{
			mainWin.repaint();
			clock.nextCycle();
		}
	}
	
	/** Get the JFrame for the primary window. */
	public static synchronized JFrame getMainWin()
	{
		return mainWin;
	}
	
	/** Gets the drawing surface for the specified window layer.
	 * <br>For the primary window, there are 14 layers.
	 * They should be used as follows:
	 * <br>
	 * <br>0-3: Background Layers
	 * <br>4-9: Main Content Layers
	 * <br>10-13: GUI Layers
	 * <br>
	 * <br> Using "sub-layers" (first drawn on a layer = lowest sub-layer)
	 * should be preferred where possible.
	 * @param window the window to get the layer from.
	 * @param layer the index of the layer.
	 * @return (Graphics2D) The graphics context to render to.
	 */
	public static Graphics2D getLayerSurface(Window window, int layer)
	{
		// Find the layer container of the given window
		LayerContainer lc = findLayerContainer(window);
		// TODO Improve this for when no layer container is found
		return lc.getDrawingSurface(layer);
	}
	
	/** Clears the drawing surface of the specified layer.
	 * After clearing, any references to the drawing surface will need to be
	 * updated with {@link #getLayerSurface(int)}.
	 * @param layer the index of the layer
	 * @see {@link #getLayerSurface(int)}
	 */
	public static synchronized void clearLayer(Window window, int layer)
	{
		LayerContainer lc = findLayerContainer(window);
		if (lc != null)
		{
			lc.clearLayer(layer);
		}
	}
	
	/** Clears all layer drawing surfaces for the specified window. */
	public static synchronized void clearAllLayers(Window window)
	{
		LayerContainer lc = findLayerContainer(window);
		if (lc != null)
		{
			lc.clearAllLayers();
		}
	}
	
	/** Clears only the specified layers.
	 * @param layers the list of indexes of layers to clear
	 * @see {@link #getLayerSurface(int)}
	 */
	public static synchronized void clearLayers(Window window, int[] layers)
	{
		LayerContainer lc = findLayerContainer(window);
		if (lc != null)
		{
			lc.clearLayers(layers);
		}
	}
	
	/** Clears all layers starting with <tt>start</tt> layer and stopping with
	 * <tt>stop</tt> layer.
	 * @param start the first layer to clear
	 * @param stop the last layer to clear
	 */
	public static synchronized void clearLayersInRange(Window window, int start, int stop)
	{
		LayerContainer lc = findLayerContainer(window);
		if (lc != null)
		{
			lc.clearLayersInRange(start, stop);
		}
	}
	
	/** Handles any changes needed because of a resized window. */
	public static synchronized void windowResized(Window win)
	{
		// Get the new dimensions for content inside the border
		Dimension newSize = win.getSize();
		Insets mfi = win.getInsets();
		newSize.setSize(
				newSize.getWidth()-mfi.right-mfi.left,
				newSize.getHeight()-mfi.top-mfi.bottom
				);
		// Find any layer containers in the window
		LayerContainer lc = findLayerContainer(win);
		// Adjust the size of said layer container
		if (lc != null)
		{
			lc.adjustSize(newSize);
		}
		win.validate();
	}
	
	/** Add a component to the specified window and the component map. */
	private static synchronized void addComponent(Window window, Component component)
	{
		// Get components from compMap or get default value
		LinkedList<Component> compMap = compMaps.getOrDefault(
				window,
				new LinkedList<Component>()
				);
		// Add the component to the window's component map
		compMap.add(component);
		// Update the window's component map in compMap
		compMaps.put(window, compMap);
		// Actually add the component to the window
		window.add(component);
	}
	
	/** Gets the component map for the specified window. */
	private static synchronized LinkedList<Component> getCompMap(Window window)
	{
		return compMaps.getOrDefault(
				window,
				new LinkedList<Component>()
				);
	}
	
	/** Get the layer container from the specified window. */
	private static synchronized LayerContainer findLayerContainer(Window window)
	{
		// For each component in the window's component map...
		for (Component c : getCompMap(window))
		{
			// Try casting the component to a LayerContainer
			try
			{
				((LayerContainer) c).getHeight();
			}
			// If it fails to cast, try another component
			catch (java.lang.ClassCastException e)
			{
				continue;
			}
			// Cast succeeded, return the discovered layer container
			return (LayerContainer) c;
		}
		// No layer container found
		System.out.println(
				"Window does not contain a LayerContainer: " + window
				);
		return null;
	}
}
