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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/** Used to render an image to a graphics context with various adjustments.
 * @author Bryan Charles Bettis
 */
public class BufferedImageDrawer
{
	/** The x coordinate to draw at. */
	private int x = 0;
	private boolean xCentered = false;
	/** The y coordinate to draw at. */
	private int y = 0;
	private boolean yCentered = false;
	/** The width to draw at. */
	private int width = 0;
	/** The special width dimension. */
	private SpecialDimension specialWidth = SpecialDimension.ORIGINAL;
	/** The height to draw at. */
	private int height = 0;
	/** The special height dimension. */
	private SpecialDimension specialHeight = SpecialDimension.ORIGINAL;
	
	/** Different situations for adjusting the dimensions of an image
	 * before drawing it.
	 * @author Bryan Charles Bettis
	 */
	public enum SpecialDimension
	{
		/** No special dimension, use the specified value. */
		SPECIFIED,
		/** Indicates that a dimension of an image should not be
		 * altered, instead using the dimension of the original image.
		 */
		ORIGINAL,
		/** Indicates that a dimension of an image should be scaled
		 * to maintain the aspect ratio of the original image.
		 */
		SCALE
	}
	
	/** Basic constructor. */
	public BufferedImageDrawer()
	{
	}
	
	/** Get the currently specified x coordinate.
	 * @return the x coordinate
	 */
	public int getX()
	{
		return x;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public boolean isCenteringX()
	{
		return xCentered;
	}
	
	public void doCenterX(boolean center)
	{
		this.xCentered = center;
	}
	
	/** Draw an image to a graphics context. Fetch the image by name from
	 * the resource management system.
	 * @param name the image's graphics resource manager access name
	 * 		("/" separated path relative to the root graphics directory
	 * 		within the resource packs)
	 * @param g the graphics context to draw to
	 */
	public void draw(String name, Graphics2D g)
	{
		draw(GraphicsManager.getResManager().getRes(name), g);
	}
	
	/** Draw an image to a graphics context.
	 * @param img the buffered image to draw
	 * @param g the graphics context to draw to
	 */
	public void draw(BufferedImage img, Graphics2D g)
	{
	}
}
