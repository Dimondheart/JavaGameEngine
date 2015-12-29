package core.graphics;

import java.awt.Font;
import java.awt.Graphics2D;

/** Contains a bunch of methods used to draw text to a graphics context.
 * @author Bryan Bettis
 */
public class TextDrawer
{
	/** The default font. */
	private static Font defFont = new Font("Dialog", Font.PLAIN, 12);
	
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
		g.drawString(text, x, y+g.getFontMetrics().getAscent());
	}
}
