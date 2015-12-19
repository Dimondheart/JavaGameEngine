package main.graphics;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JFrame;

import main.graphics.resources.GfxResourceManager;

/** Handles the rendering thread. */
public class GfxManager implements main.CustomRunnable
{
	/** Default dimensions for a new window. */
	public static final Dimension DEF_DIMS = new Dimension(480,270);
	/** Number of layers to create for the main window. */
	public static final int NUM_MAIN_LAYERS = 10;
	
	/** The main window frame. */
	private static JFrame mainWin;
	/** The layer container for the main window. */
	private static LayerContainer mainLayers;
	/** Manages graphics loaded from files. */
	private static GfxResourceManager grm;
	/** The current average FPS. */
	private static double FPS;
	/** Renders the FPS to the screen. */
	private static FPSRenderer fpsR;
	
	/** Thread for this system. */
	private Thread thread;
	/** Thread manager. */
	private main.ThreadClock clock;
	
	/** Normal graphics renderer setup. */
	public GfxManager()
	{
		System.out.println("Setting Up Graphics System...");
		mainWin = new JFrame("My Game Framework");
		mainLayers = new LayerContainer(mainWin, DEF_DIMS, NUM_MAIN_LAYERS);
		mainWin.add(mainLayers);
		// Draw the average FPS to the screen
		fpsR = new FPSRenderer();
		fpsR.show(9);
		// Create the graphics resource manager
		grm = new GfxResourceManager();
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
			FPS = clock.getAvgCPS();
			mainWin.repaint();
			clock.nextCycle();
		}
	}
	
	/** Get the average FPS. */
	public static double getAvgFPS()
	{
		return FPS;
	}
	
	/** Get the JFrame for the primary window. */
	public static synchronized JFrame getMainWin()
	{
		return mainWin;
	}
	
	/** Gets the object that stores loaded graphics. */
	public static synchronized GfxResourceManager getResManager()
	{
		return grm;
	}
	
	/** Shows the specified Renderer on the specified layer. */
	public static synchronized void showRenderer(Renderer obj, int layer)
	{
		mainLayers.showRenderer(obj, layer);
	}
	
	/** Hide (remove) the specified renderer from the specified layer. */
	public static synchronized void hideRenderer(Renderer obj, int layer)
	{
		mainLayers.hideRenderer(obj, layer);
	}
	
	/** Hide (remove) the specified renderer from all layers. */
	public static synchronized void hideRenderer(Renderer obj)
	{
		mainLayers.hideRenderer(obj);
	}
	
	/** Clears all graphics data, like renderers, etc. */
	public static synchronized void clearAll()
	{
		mainLayers.clearAllLayers();
		// Re-add the FPS text renderer
		showRenderer(fpsR,9);
	}
	
	/** Handles any changes needed because of a resized window. */
	public static synchronized void windowResized()
	{
		// Get the new dimensions for content inside the border
		Dimension newSize = mainWin.getSize();
		Insets mfi = mainWin.getInsets();
		newSize.setSize(
				newSize.getWidth()-mfi.right-mfi.left,
				newSize.getHeight()-mfi.top-mfi.bottom
				);
		// Adjust the size of the layer container
		mainLayers.adjustSize(newSize);
		mainWin.revalidate();
	}
}
