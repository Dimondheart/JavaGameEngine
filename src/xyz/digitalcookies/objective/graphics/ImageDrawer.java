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

/** TODO Document
 * TODO revamp this classes static methods
 * @author Bryan Charles Bettis
 */
public class ImageDrawer
{
	/** Constructor hidden to prevent instantiation. */
	private ImageDrawer()
	{
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
		img = GraphicsManager.getResManager().getRes(name);
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
		img = GraphicsManager.getResManager().getRes(name);
		drawGraphic(g,img,x,y,img.getWidth(),img.getHeight());
	}
	
	/** Draws a BufferedImage to the specified context, without resizing
	 * the image.
	 * @param g the Graphics2D context to draw the image to
	 * @param i the image to draw
	 * @param x the x coordinate of the top left corner
	 * @param y the y coordinate of the top left corner
	 */
	public static void drawGraphic(Graphics2D g, BufferedImage i, int x, int y)
	{
		drawGraphic(g,i,x,y,i.getWidth(),i.getHeight());
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
}
