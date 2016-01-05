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
	
	/** Get the average FPS.
	 * @return the average FPS for rendering
	 */
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
	
	/** Draws a BufferedImage to the specified context.
	 * @param g the Graphics2D to draw the image to
	 * @param i the image to draw
	 * @param x the x coordinate of the left side
	 * @param y the y coordinate of the top side
	 * @param width the width to draw the image as
	 * @param height the height to draw the image as
	 */
	public static void drawGraphic(Graphics2D g, BufferedImage i, int x, int y, int width, int height)
	{
		g.drawImage(i,x,y,width,height,null);
	}
	
	/** Shows the specified Renderer on the specified layer.
	 * @param obj the Renderer to show
	 * @param layer the layer to show on
	 */
	public static synchronized void showRenderer(PrimaryRenderer obj, int layer)
	{
		mainLayers.showRenderer(obj, layer);
	}
	
	/** Hide (remove) the specified renderer from the specified layer.
	 * @param obj the Renderer to hide
	 * @param layer the layer to hide on
	 */
	public static synchronized void hideRenderer(PrimaryRenderer obj, int layer)
	{
		mainLayers.hideRenderer(obj, layer);
	}
	
	/** Hide (remove) the specified renderer from all layers.
	 * @param obj the Renderer to hide
	 */
	public static synchronized void hideRenderer(PrimaryRenderer obj)
	{
		mainLayers.hideRenderer(obj);
	}
	
	/** Clears all graphics data, like each Renderer, etc. */
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
