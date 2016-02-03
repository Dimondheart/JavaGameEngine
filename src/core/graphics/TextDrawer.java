package core.graphics;

import java.awt.Font;
import java.awt.Graphics2D;

/** Contains a bunch of methods used to draw text to a graphics context.
 * @author Bryan Bettis
 */
public class TextDrawer
{
	/** The default font. */
	public static final Font defFont = new Font("Dialog", Font.PLAIN, 12);
	
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
	
	public static int getTextHeight(Graphics2D g, String text)
	{
		return getTextHeight(g, text, defFont);
	}
	
	public static int getTextHeight(Graphics2D g, String text, Font font)
	{
		return (int) g.getFontMetrics(font).getLineMetrics(text, g).getHeight();
	}
	
	public static int getTextWidth(Graphics2D g, String text)
	{
		return getTextWidth(g, text, defFont);
	}
	
	public static int getTextWidth(Graphics2D g, String text, Font font)
	{
		return (int) g.getFontMetrics(font).getStringBounds(text, g).getWidth();
	}
	
	public static int[] getCenterOffsets(Graphics2D g, String text)
	{
		return getCenterOffsets(g, text, defFont);
	}
	
	public static int[] getCenterOffsets(Graphics2D g, String text, Font font)
	{
		int[] coords = new int[2];
		coords[0] = getTextWidth(g, text, font)/2;
		// Don't ask why *3/4 works, its some kind of internal Java magic
		coords[1] = getTextHeight(g, text, font)*3/4;
		return coords;
	}
	
	public static int[] centerOverPoint(Graphics2D g, String text, int[] centerOver)
	{
		return centerOverPoint(g, text, centerOver, defFont);
	}
	
	public static int[] centerOverPoint(Graphics2D g, String text, int[] centerOver, Font font)
	{
		int[] coords = new int[2];
		int[] offsets = getCenterOffsets(g, text, font);
		coords[0] = centerOver[0] - offsets[0];
		coords[1] = centerOver[1] - offsets[1];
		return coords;
	}
}
