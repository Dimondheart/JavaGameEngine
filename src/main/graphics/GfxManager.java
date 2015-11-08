package main.graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;
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
	private static double FPS;
	
	/** Normal graphics renderer setup. */
	public GfxManager()
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

	@SuppressWarnings("unused")
	@Override
	public void run()
	{
		FPSRenderer fpsR = new FPSRenderer();
		CtrlRenderer controls = new CtrlRenderer();
		while (true)
		{
			FPS = clock.getAvgCPS();
			clearLayers();
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
	
	/** Gets the drawing surface for the specified window layer.
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
	 */
	public static Graphics2D getLayerSurface(int layer)
	{
		return mainLayers.getDrawingSurface(layer);
	}
	
	/** Clears all layers' drawing surfaces. */
	private static synchronized void clearLayers()
	{
		mainLayers.clearLayers();
	}
	
	public static synchronized void addRenderer(Renderer obj)
	{
		mainLayers.addRenderer(obj);
	}
	
	public static synchronized void removeRenderer(Renderer obj)
	{
		mainLayers.removeRenderer(obj);
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
		// Adjust the size of said layer container
		mainLayers.adjustSize(newSize);
		mainWin.revalidate();
	}
}
