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

package xyz.digitalcookies.objective.graphics;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import xyz.digitalcookies.objective.DevConfig;
import xyz.digitalcookies.objective.resources.GraphicsResources;

/** Handles the rendering thread.
 * @author Bryan Charles Bettis
 */
public class GraphicsManager extends xyz.digitalcookies.objective.Subsystem
{
	/** The default interval for rendering. */
	private static final int DEFAULT_RENDER_INTERVAL = 16;
	
	/** The main window frame. */
	private static JFrame mainWin;
	/** The layer container for the main set of layers. */
	private static MainLayerSetContainer mainLayers;
	/** Manages graphics loaded from files. */
	private static GraphicsResources grm;
	/** The current average FPS. */
	private static double FPS;
	
	/** Normal graphics system setup. */
	public GraphicsManager()
	{
		super(DEFAULT_RENDER_INTERVAL, "Graphics Manager Render Loop");
	}
	
	@Override
	protected void setupSystem()
	{
		System.out.println("Setting Up Graphics System...");
		TextDrawer.setDefaultFont((Font) DevConfig.getSetting(DevConfig.DEF_FONT)); 
		// Cleanup any previous main window
		if (mainWin != null)
		{
			mainWin.dispose();
		}
		// Setup the main window
		mainWin = new JFrame("Objective - Java Game Engine");
		// Setup the main layer container
		Integer numMainLayers =
				(Integer) DevConfig.getSetting(DevConfig.NUM_MAIN_LAYERS);
		int width = 
				(int) DevConfig.getSetting(DevConfig.INIT_MAIN_WIN_WIDTH);
		int height = 
				(int) DevConfig.getSetting(DevConfig.INIT_MAIN_WIN_HEIGHT);
		Dimension mainWinDims = new Dimension(width, height);
		mainLayers = new MainLayerSetContainer(
				mainWin,
				mainWinDims,
				numMainLayers
				);
		mainWin.add(mainLayers);
		// Create the graphics resource manager
		grm = new GraphicsResources();
		grm.initialize("graphics/", ".png", ".bmp", ".jpg", ".jpeg");
	}
	
	@Override
	protected void startSystem()
	{
		System.out.println("Starting Graphics System...");
		mainWin.pack();
		mainWin.setVisible(true);
	}
	
	@Override
	protected synchronized void stopSystem()
	{
		mainWin.dispose();
	}

	@Override
	public boolean runCycle()
	{
		// TODO Make FPS calculation actually reflect FPS
		FPS = getAvgCPS();
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
	 * <br>The number of layers in this layer set is specified in
	 * the properties file, as the setting NUM_MAIN_LAYERS.
	 * @return the LayerSet for the main drawing layers
	 */
	public static LayerSet getMainLayerSet()
	{
		return mainLayers.getLayerSet();
	}
	
	/** Draws a pre-loaded image to the specified graphics context.
	 * @param g the Graphics2D context to draw the image to
	 * @param name the "pre-loaded name" of the image to draw
	 * @param x the x coordinate of the top left corner
	 * @param y the y coordinate of the top left corner
	 * @param width the width to draw the image as
	 * @param height the height to draw the image as
	 */
	public static void drawGraphic(Graphics2D g, String name, int x, int y, int width, int height)
	{
		BufferedImage img;
		img = xyz.digitalcookies.objective.graphics.GraphicsManager.getResManager().getRes(name);
		drawGraphic(g,img,x,y,width,height);
	}
	
	/** Draws a pre-loaded image to the specified context, without resizing
	 * the image.
	 * @param g the Graphics2D context to draw the image to
	 * @param name the "pre-loaded name" of the image to draw
	 * @param x the x coordinate of the top left corner
	 * @param y the y coordinate of the top left corner
	 */
	public static void drawGraphic(Graphics2D g, String name, int x, int y)
	{
		BufferedImage img;
		img = xyz.digitalcookies.objective.graphics.GraphicsManager.getResManager().getRes(name);
		drawGraphic(g,img,x,y,img.getWidth(),img.getHeight());
	}
	
	/** Draws a BufferedImage to the specified context.
	 * @param g the Graphics2D context to draw the image to
	 * @param i the image to draw
	 * @param x the x coordinate of the top left corner
	 * @param y the y coordinate of the top left corner
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
