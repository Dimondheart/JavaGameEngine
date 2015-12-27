package core.graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/** Handles the rendering thread.
 * @author Bryan Bettis
 */
public class GfxManager implements core.CustomRunnable
{
	/** The default interval for rendering. */
	public static final int DEFAULT_RENDER_INTERVAL = 16;
	/** Default dimensions for a new window. */
	public static final Dimension DEF_DIMS = new Dimension(480,270);
	/** Number of layers to create for the main window. */
	public static final int NUM_MAIN_LAYERS = 10;
	
	/** The main window frame. */
	private static JFrame mainWin;
	/** The layer container for the main window. */
	private static LayerContainer mainLayers;
	/** Manages graphics loaded from files. */
	private static GraphicsResources grm;
	/** The current average FPS. */
	private static double FPS;
	
	/** Thread for this system. */
	private Thread thread;
	/** Thread manager. */
	private core.ThreadClock clock;
	
	/** Normal graphics renderer setup. */
	public GfxManager()
	{
		System.out.println("Setting Up Graphics System...");
		// Setup the main window
		mainWin = new JFrame("Unnamed Java Game Engine");
		// Setup the main layer container
		mainLayers = new LayerContainer(mainWin, DEF_DIMS, NUM_MAIN_LAYERS);
		mainWin.add(mainLayers);
		// Create the graphics resource manager
		grm = new GraphicsResources();
		// Run at 62.5 FPS
		clock = new core.ThreadClock(DEFAULT_RENDER_INTERVAL);
	}
	
	@Override
	public void start()
	{
		System.out.println("Starting Graphics System...");
		mainWin.pack();
		mainWin.setVisible(true);
		thread = new Thread(this);
		thread.setName("Graphics Manager Render Loop");
		thread.start();
	}

	@Override
	public void run()
	{
		while (true)
		{
			FPS = clock.getAvgCPS();
			mainWin.repaint(16);
			clock.nextCycle();
		}
	}
	
	/** Get the average FPS. */
	public static double getAvgFPS()
	{
		return FPS;
	}
	
	/** Get the JFrame for the primary window.
	 * @return the JFrame of the main window
	 */
	public static synchronized JFrame getMainWin()
	{
		return mainWin;
	}
	
	/** Gets the object that stores loaded graphics.
	 * @return the object for accessing graphics files
	 */
	public static GraphicsResources getResManager()
	{
		return grm;
	}
	
	public static void drawGraphic(Graphics2D g, BufferedImage i, int x, int y, int width, int height)
	{
		g.drawImage(i,x,y,width,height,null);
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
