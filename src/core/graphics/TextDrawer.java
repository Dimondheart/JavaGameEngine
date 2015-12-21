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
	
	/** Basic text drawing with default font and size. */
	public static void drawText(Graphics2D g, String text, int x, int y)
	{
		drawText(g, text, x, y, defFont);
	}
	
	/** Basic text drawing with default font and custom size. */
	public static void drawText(Graphics2D g, String text, int x, int y, int size)
	{
		Font font = defFont.deriveFont((float)size);
		drawText(g, text, x, y, font);
	}
	
	/** Draw text with a pre-made font. */
	public static void drawText(Graphics2D g, String text, int x, int y, Font font)
	{
		g.setFont(font);
		g.drawString(text, x, y+g.getFontMetrics().getAscent());
	}
}
