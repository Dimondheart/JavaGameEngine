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
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JFrame;

import xyz.digitalcookies.objective.Game;
import xyz.digitalcookies.objective.input.InputManager;
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
	/** Manages graphics loaded from files.
	 * TODO make this set-able by developers (custom resource format handling)
	 */
	private static GraphicsResources grm;
	/** The current average FPS. */
	private static double FPS = -1;
	
	/** Normal graphics system setup. */
	public GraphicsManager()
	{
		super(DEFAULT_RENDER_INTERVAL, "Graphics Manager Render Loop");
	}
	
	@Override
	protected void setupSystem(HashMap<String, Object> config)
	{
		System.out.println("Setting Up Graphics System...");
		TextDrawer.setDefaultFont(
				new Font(
						(String) config.get(Game.DEF_FONT),
						Font.PLAIN,
						(int) config.get(Game.DEF_FONT_SIZE)
						)
				); 
		// Cleanup any previous main window
		if (mainWin != null)
		{
			mainWin.dispose();
		}
		// Setup the main window
		mainWin = new JFrame((String) config.get(Game.MAIN_WIN_TITLE));
		// Setup the main layer container
		int width = (int) config.get(Game.INIT_WIN_WIDTH);
		int height = (int) config.get(Game.INIT_WIN_HEIGHT);
		Dimension mainWinDims = new Dimension(width, height);
		mainLayers = new MainLayerSetContainer(
				mainWinDims,
				(int) config.get(Game.NUM_LAYERS)
				);
		mainWin.add(mainLayers);
		// Create the graphics resource manager
		grm = new GraphicsResources();
		grm.initialize(
				(String) config.get(Game.GRAPHICS_RES_DIR),
				".png", ".bmp", ".jpg", ".jpeg"
				);
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
		if (InputManager.isQuitting())
		{
			return false;
		}
		mainLayers.repaint(0);
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
