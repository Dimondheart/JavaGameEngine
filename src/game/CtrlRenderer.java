package game;

import java.awt.Color;
import java.awt.Graphics2D;

import core.graphics.RenderEvent;
import core.graphics.Renderer;
import core.graphics.TextDrawer;

/** Renders controls text to the screen for testing purposes.
* @author Bryan Charles Bettis
*/
@SuppressWarnings("javadoc")
public class CtrlRenderer implements Renderer
{
	private String[] displayText;
	
	public CtrlRenderer(String[] text)
	{
		displayText = new String[text.length + 1];
		displayText[0] = "Controls:";
		for (int i = 1; i < displayText.length; ++i)
		{
			displayText[i] = text[i-1];
		}
	}
	
	@Override
	public void render(RenderEvent e)
	{
		Graphics2D g = e.getContext();
		g.setColor(Color.white);
		for (int line = 0; line < displayText.length; ++line)
		{
			String text = displayText[line];
			int y = (line+1)*TextDrawer.getTextHeight(g, text);
			TextDrawer.drawText(g, text, 0, y);
		}
	}
}
