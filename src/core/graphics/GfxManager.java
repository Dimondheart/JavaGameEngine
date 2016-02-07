/** Copyright 2016 Bryan Charles Bettis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package core.graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import core.DeveloperSettings;

/** Handles the rendering thread.
 * @author Bryan Charles Bettis
 */
public class GfxManager extends core.Subsystem
{
	/** The default interval for rendering. */
	public static final int DEFAULT_RENDER_INTERVAL = 16;
	/** The index of the top layer. */
	public static final int TOP_LAYER_INDEX =
			(int)
			DeveloperSettings.getSetting("NUM_MAIN_LAYERS") - 1;
	
	/** The main window frame. */
	private static JFrame mainWin;
	/** The layer container for the main set of layers. */
	private static MainLayerSetContainer mainLayers;
	/** Manages graphics loaded from files. */
	private static GraphicsResources grm;
	/** The current average FPS. */
	private static double FPS;
	
	/** Normal graphics renderer setup. */
	public GfxManager()
	{
		super(DEFAULT_RENDER_INTERVAL, "Graphics Manager Render Loop");
		System.out.println("Setting Up Graphics System...");
		// Setup the main window
		mainWin = new JFrame("Unnamed Java Game Engine");
		// Setup the main layer container
		Integer numMainLayers =
				(Integer) DeveloperSettings.getSetting("NUM_MAIN_LAYERS");
		int width = 
				(int) DeveloperSettings.getSetting("INIT_MAIN_WIN_WIDTH");
		int height = 
				(int) DeveloperSettings.getSetting("INIT_MAIN_WIN_HEIGHT");
		Dimension mainWinDims = new Dimension(width, height);
		mainLayers = new MainLayerSetContainer(mainWin, mainWinDims, numMainLayers);
		mainWin.add(mainLayers);
		// Create the graphics resource manager
		grm = new GraphicsResources();
	}
	
	@Override
	public void startSystem()
	{
		System.out.println("Starting Graphics System...");
		mainWin.pack();
		mainWin.setVisible(true);
	}

	@Override
	public boolean runCycle()
	{
		FPS = clock.getAvgCPS();
		mainWin.repaint(16);
		return true;
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
	
	/** Gets the main layer set, which is drawn to the screen. To be able to
	 * draw to the screen, a subclass of Renderer (LayerSet, your own
	 * subclasses, etc.) must be added to this layer set, or to a different
	 * Renderer container (LayerSet, etc.) that has been/will be added to
	 * this main layer set.
	 * <br>
	 * <br>For the primary window, there are 10 layers.
	 * They "could" be used as follows:
	 * <br>
	 * <br>0-2: Background Layers
	 * <br>3-6: Main Content Layers
	 * <br>7-9: GUI Layers
	 * <br>
	 * <br>What you use each layer for is up to you, but setting your own
	 * standard usage for a game, like above for example, is recommended.
	 * @return the LayerSet for the main drawing layers
	 */
	public static LayerSet getMainLayerSet()
	{
		return mainLayers.getLayerSet();
	}
	
	/** Draws a pre-loaded image to the specified context.
	 * @param g the Graphics2D to draw the image to
	 * @param name the "pre-loaded name" of the image to draw
	 * @param x the x coordinate of the left side
	 * @param y the y coordinate of the top side
	 * @param width the width to draw the image as
	 * @param height the height to draw the image as
	 */
	public static void drawGraphic(Graphics2D g, String name, int x, int y, int width, int height)
	{
		BufferedImage img;
		img = core.graphics.GfxManager.getResManager().getRes(name);
		drawGraphic(g,img,x,y,width,height);
	}
	
	/** Draws a pre-loaded image to the specified context.
	 * @param g the Graphics2D to draw the image to
	 * @param name the "pre-loaded name" of the image to draw
	 * @param x the x coordinate of the left side
	 * @param y the y coordinate of the top side
	 */
	public static void drawGraphic(Graphics2D g, String name, int x, int y)
	{
		BufferedImage img;
		img = core.graphics.GfxManager.getResManager().getRes(name);
		drawGraphic(g,img,x,y,img.getWidth(),img.getHeight());
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
	
	/** Clears all graphics data, like each Renderer, etc. */
	public static synchronized void clearAll()
	{
		mainLayers.getLayerSet().clearAllLayers();
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
		// Adjust the size of the main layer set
		mainLayers.adjustSize(newSize);
		mainWin.revalidate();
	}
}
