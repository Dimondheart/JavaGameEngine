package main.graphics;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Window;

import javax.swing.JFrame;

/** Handles the rendering thread. */
public class Gfx implements main.CustomRunnable
{
	/** Number of layers to create for the main window. */
	public static final int NUM_MAIN_LAYERS = 14;
	/** Default dimensions for a new window. */
	public static final Dimension DEF_DIMS = new Dimension(480,270);
	
	/** The main window frame. */
	private static JFrame mainWin;
	/** The layer container for the main window. */
	private static LayerContainer mainLayers;
	/** Thread for this system. */
	private Thread thread;
	/** Thread manager. */
	private main.ThreadClock clock;
	
	/** Normal graphics renderer setup. */
	public Gfx()
	{
		System.out.println("Setting Up Graphics System...");
		mainWin = new JFrame("My Game Framework");
		mainLayers = new LayerContainer(mainWin, DEF_DIMS, NUM_MAIN_LAYERS);
		mainWin.add(mainLayers);
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
			clock.nextCycle();
		}
	}
	
	/** Gets the drawing surface for the specified layer and container.
	 * <br>For the primary window, there are 14 layers.
	 * They should be used as follows:
	 * <br>
	 * <br>0-3: Background Layers
	 * <br>4-9: Main Content Layers
	 * <br>10-13: GUI Layers
	 * <br>
	 * <br> Using "sub-layers" (drawing stuff within a layer in order) should
	 * be preferred where possible. Use layers to simplify graphics operations,
	 * for example rendering a tile grid at the same time as the entities
	 * in that tile grid, or rendering background animations over a
	 * background image that doesn't change.
	 * @param layer the index of the layer.
	 * @return (Graphics2D) The graphics context to render to.
	 */
	public static Graphics2D getLayerSurface(Window window, int layer)
	{
		// Find the layer container of the given window
		LayerContainer lc = findLayerContainer(window);
		return lc.getDrawingSurface(layer);
	}
	
	/** Get the layer container from the specified window. */
	public static LayerContainer findLayerContainer(Window window)
	{
		Component[] comps = window.getComponents();
		for (Component c : comps)
		{
			((LayerContainer) c).getHeight();
			return (LayerContainer) c;
		}
		return null;
	}

	/** Get the JFrame for the primary window. */
	public static synchronized JFrame getMainWin()
	{
		return mainWin;
	}
}
