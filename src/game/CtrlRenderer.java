package game;

import java.awt.Color;
import java.awt.Graphics2D;

import core.graphics.RenderEvent;
import core.graphics.Renderer;
import core.graphics.TextDrawer;

/** Renders controls text to the screen for testing purposes.
* @author Bryan Bettis
*/
public class CtrlRenderer implements Renderer
{
	private String[] displayText;
	
	public CtrlRenderer(String[] text)
	{
		displayText = text;
		// Show it on the highest layer
		showOnLayer(core.graphics.GfxManager.NUM_MAIN_LAYERS-1);
	}
	
	@Override
	public void render(RenderEvent e)
	{
		Graphics2D g = e.getContext();
		g.setColor(Color.white);
		TextDrawer.drawText(g, "Controls:", 2, 11, 12);
		for (int line = 0; line < displayText.length; ++line)
		{
			TextDrawer.drawText(g, displayText[line], 2, (line+1)*12+11, 12);
		}
	}
}
