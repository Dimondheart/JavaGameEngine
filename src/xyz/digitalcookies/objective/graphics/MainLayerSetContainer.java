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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/** Handles multiple layers of rendering for a window.
 * Gets the drawing surface for the specified window layer.
 * @author Bryan Charles Bettis
 */
class MainLayerSetContainer extends JComponent
{
	/** */
	private static final long serialVersionUID = 1L;
	
	/** The primary layer set which is drawn to the screen. */
	private MainLayerSet mainLayers;
	
	/** Standard layer container for the specified window.
	 * @param window the window this LayerContainer is part of
	 * @param dims the initial dimensions of the layers
	 * @param numLayers the number of main layers to create
	 */
	public MainLayerSetContainer(Dimension dims, int numLayers)
	{
		// Setup the layers
		mainLayers = new MainLayerSet(numLayers);
		setPreferredSize(dims);
	}
	
	/** Gets the main layer set.
	 * @return the LayerSet for the main drawing layers
	 */
	public LayerSet getLayerSet()
	{
		return mainLayers;
	}
	
	@Override
	public void update(Graphics g)
	{
	}
	
	@Override
	public synchronized void paintComponent(Graphics g)
	{
		render((Graphics2D) g);
	}
	
	public void render(Graphics2D g)
	{
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// Clear the graphics context
		g.setColor(Color.black);
		g.fillRect(0, 0, mainLayers.getWidth(), mainLayers.getHeight());
		// Render the main layer set
		RenderEvent event = new RenderEvent((Graphics2D) g);
		mainLayers.render(event);
	}
	
	/** Resizes the main layer container.
	 * @param newDims the new dimensions
	 */
	protected synchronized void adjustSize(Dimension newDims)
	{
		// Resize the whole container
		this.setSize(newDims);
		// Adjust the main layer set
		mainLayers.resizeLayers(newDims);
	}
}
