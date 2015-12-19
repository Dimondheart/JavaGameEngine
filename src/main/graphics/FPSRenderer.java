package main.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

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
		String toDraw = "Avg FPS: " + String.format("%.3f", GfxManager.getAvgFPS());
		g.setColor(Color.white);
		TextDrawer.drawText(g, toDraw, 2, 0);
	}
}
