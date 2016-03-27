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

/** A thin layer over LayerSet, this provides a few changes to a layer
 * set needed for the main layer set to function properly. For example
 * this class removes the functionality of the position and dimensions
 * methods and adds a resizeLayers(dimensions) used to keep the main layer
 * set sized to fit the window/display area.
 * @author Bryan Charles Bettis
 */
class MainLayerSet extends LayerSet
{
	/** Basic constructor.
	 * @param numLayers the number of layers to setup
	 */
	public MainLayerSet(int numLayers)
	{
		super(numLayers);
		super.setVisible(true);
		super.setEnforceBounds(false);
	}
	
	@Override
	public void setPos(int x, int y)
	{
	}
	
	@Override
	public void setX(int x)
	{
	}
	
	@Override
	public void setY(int y)
	{
	}
	
	@Override
	public void setDims(int width, int height)
	{
	}
	
	@Override
	public void setWidth(int width)
	{
	}
	
	@Override
	public void setHeight(int height)
	{
	}
	
	@Override
	public void centerOver(int x, int y, boolean autoUpdate)
	{
	}
	
	@Override
	public void centerOverWindow(boolean center, boolean autoUpdate)
	{
	}
	
	@Override
	public void centerOverWindow(boolean center)
	{
	}
	
	@Override
	public void setVisible(boolean visible)
	{
	}
	
	@Override
	public void setEnforceBounds(boolean enforce)
	{
	}
	
	/** Resize the layers, used to only resize the main layers when the
	 * main window is resized.
	 * @param newDims the new dimensions of the main layers
	 */
	synchronized void resizeLayers(Dimension newDims)
	{
		super.setWidth(newDims.width);
		super.setHeight(newDims.height);
	}

}
