package core.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;

import core.graphics.GfxManager;
import core.graphics.RenderEvent;
import core.graphics.Renderer;
import core.graphics.TextDrawer;

/** A simple implementation of a Renderer, used to display the current average
 * FPS on the screen.
 * @author Bryan Bettis
 */
public class FPSRenderer implements Renderer
{
	/** Basic constructor. */
	public FPSRenderer()
	{
	}
	
	@Override
	public void render(RenderEvent e)
	{
		Graphics2D g = e.getContext();
		// Format the string to draw
		String toDraw = "Avg FPS: " + String.format("%.3f", GfxManager.getAvgFPS());
		g.setColor(Color.white);
		// Draw the string to the screen
		TextDrawer.drawText(g, toDraw, 2, 0);
	}
}
