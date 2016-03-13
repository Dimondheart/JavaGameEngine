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

import java.awt.Font;
import java.awt.Graphics2D;

/** Contains a bunch of methods used to draw text to a graphics context.
 * @author Bryan Charles Bettis
 */
public class TextDrawer
{
	/** The default font. */
	public static final Font defFont = 
			(Font)
			core.DeveloperSettings.getSetting(core.DeveloperSettings.DEF_FONT);
	
	/** Basic text drawing with default font and size.
	 * @param g the surface to draw to
	 * @param text the text to draw
	 * @param x the left edge of the text
	 * @param y the baseline for the text
	 */
	public static void drawText(Graphics2D g, String text, int x, int y)
	{
		drawText(g, text, x, y, defFont);
	}
	
	/** Basic text drawing with default font and size.
	 * @param g the surface to draw to
	 * @param text the text to draw
	 * @param coords the x,y coordinates to draw the text at
	 */
	public static void drawText(Graphics2D g, String text, int[] coords)
	{
		drawText(g, text, coords[0], coords[1]);
	}
	
	/** Basic text drawing with default font and custom size.
	 * @param g the the surface to draw to
	 * @param text the text to draw
	 * @param x the left edge of the text
	 * @param y the baseline for the text
	 * @param size the font size
	 */
	public static void drawText(Graphics2D g, String text, int x, int y, int size)
	{
		Font font = defFont.deriveFont((float)size);
		drawText(g, text, x, y, font);
	}
	
	/** Basic text drawing with default font and custom size.
	 * @param g the the surface to draw to
	 * @param text the text to draw
	 * @param coords the coordinates to draw the text at
	 * @param size the font size
	 */
	public static void drawText(Graphics2D g, String text, int[] coords, int size)
	{
		drawText(g, text, coords[0], coords[1], size);
	}
	
	/** Draw text with a pre-made font.
	 * @param g the the surface to draw to
	 * @param text the text to draw
	 * @param x the left edge of the text
	 * @param y the baseline for the text
	 * @param font the font object to use
	 */
	public static void drawText(Graphics2D g, String text, int x, int y, Font font)
	{
		g.setFont(font);
		g.drawString(
				text,
				x,
				y + getTextHeight(g, text, font)
				);
	}
	
	/** Draw text with a pre-made font.
	 * @param g the the surface to draw to
	 * @param text the text to draw
	 * @param coords the coordinates to draw the text at
	 * @param font the font object to use
	 */
	public static void drawText(Graphics2D g, String text, int[] coords, Font font)
	{
		drawText(g, text, coords[0], coords[1], font);
	}
	
	/** Get the height of the specified string if it was drawn using the
	 * default font.
	 * @param g the graphics context (used to find the text size reliably)
	 * @param text the text to get the height of
	 * @return the height of the text if drawn, in pixels
	 */
	public static int getTextHeight(Graphics2D g, String text)
	{
		return getTextHeight(g, text, defFont);
	}
	
	/** Get the height of the specified string if it was drawn using the
	 * specified font.
	 * @param g the graphics context (used to find the text size reliably)
	 * @param text the text to get the height of
	 * @param font the font the text should be sized using
	 * @return the height of the text if drawn, in pixels
	 */
	public static int getTextHeight(Graphics2D g, String text, Font font)
	{
		return (int) g.getFontMetrics(font).getLineMetrics(text, g).getHeight();
	}
	
	/** Get the height of the specified string if it was drawn using the
	 * default font.
	 * @param g the graphics context (used to find the text size reliably)
	 * @param text the text to get the width of
	 * @return the width of the text if drawn, in pixels
	 */
	public static int getTextWidth(Graphics2D g, String text)
	{
		return getTextWidth(g, text, defFont);
	}
	
	/** Get the height of the specified string if it was drawn using the
	 * specified font.
	 * @param g the graphics context (used to find the text size reliably)
	 * @param text the text to get the width of
	 * @param font the font the text should be sized using
	 * @return the width of the text if drawn, in pixels
	 */
	public static int getTextWidth(Graphics2D g, String text, Font font)
	{
		return (int) g.getFontMetrics(font).getStringBounds(text, g).getWidth();
	}
	
	/** Get the offsets that would be used to center the specified string
	 * over a point, if the text was drawn in the default font.
	 * @param g the graphics context (used to find the text size reliably)
	 * @param text the text to find the offsets for
	 * @return an array containing the offsets ([x,y])
	 */
	public static int[] getCenterOffsets(Graphics2D g, String text)
	{
		return getCenterOffsets(g, text, defFont);
	}
	
	/** Get the offsets that would be used to center the specified string
	 * over a point, if the text was drawn in the specified font.
	 * @param g the graphics context (used to find the text size reliably)
	 * @param text the text to find the offsets for
	 * @param font the font the text should be sized using
	 * @return an array containing the offsets ([x,y])
	 */
	public static int[] getCenterOffsets(Graphics2D g, String text, Font font)
	{
		int[] coords = new int[2];
		coords[0] = getTextWidth(g, text, font)/2;
		// Don't ask why *3/4 works, its some kind of internal Java magic
		// Probably has something to do with baseline position
		// TODO figure out how to correctly find this offset
		coords[1] = getTextHeight(g, text, font)*3/4;
		return coords;
	}
	
	/** Get the coordinates that wound be needed to draw the specified text
	 * centered over the specified coordinates, in the default font.
	 * @param g the graphics context (used to find the text size reliably)
	 * @param text the text to center
	 * @param centerOver the [x,y] pair to center over
	 * @return the coordinates this text would need to be drawn at to center
	 * 		it over the specified coordinates
	 */
	public static int[] centerOverPoint(Graphics2D g, String text, int[] centerOver)
	{
		return centerOverPoint(g, text, centerOver, defFont);
	}
	
	/** Get the coordinates that wound be needed to draw the specified text
	 * centered over the specified coordinates, in the specified font.
	 * @param g the graphics context (used to find the text size reliably)
	 * @param text the text to center
	 * @param centerOver the [x,y] pair to center over
	 * @param font the font the text should be sized using
	 * @return the coordinates this text would need to be drawn at to center
	 * 		it over the specified coordinates
	 */
	public static int[] centerOverPoint(Graphics2D g, String text, int[] centerOver, Font font)
	{
		int[] coords = new int[2];
		int[] offsets = getCenterOffsets(g, text, font);
		coords[0] = centerOver[0] - offsets[0];
		coords[1] = centerOver[1] - offsets[1];
		return coords;
	}
}
