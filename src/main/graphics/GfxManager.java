package main.graphics;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JFrame;

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
	/** Thread for this system. */
	private Thread thread;
	/** Thread manager. */
	private main.ThreadClock clock;
	/** The current average FPS. */
	private static double FPS;
	private static FPSRenderer fpsR;
	
	/** Normal graphics renderer setup. */
	public GfxManager()
	{
		System.out.println("Setting Up Graphics System...");
		mainWin = new JFrame("My Game Framework");
		mainLayers = new LayerContainer(mainWin, DEF_DIMS, NUM_MAIN_LAYERS);
		mainWin.add(mainLayers);
		// Draw the average FPS to the screen
		fpsR = new FPSRenderer();
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

	@SuppressWarnings("unused")
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
	
	/** Adds the specified Renderer to be rendered. */
	public static synchronized void addRenderer(Renderer obj)
	{
		mainLayers.addRenderer(obj);
	}
	
	/** Remove the specified renderer from being rendered. */
	public static synchronized void removeRenderer(Renderer obj)
	{
		mainLayers.removeRenderer(obj);
	}
	
	/** Clears all graphics data, like renderers, etc. */
	public static synchronized void clearAll()
	{
		mainLayers.clearAllLayers();
		// Re-add the FPS text renderer
		addRenderer(fpsR);
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
