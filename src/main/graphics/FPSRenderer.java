package main.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

public class FPSRenderer extends Renderer
{
	public FPSRenderer()
	{
		super(9);
	}
	
	@Override
	public void render(Graphics2D g)
	{
		String toDraw = "Avg FPS: " + String.format("%.3f", GfxManager.getAvgFPS());
		g.setColor(Color.white);
		TextDrawer.drawText(g, toDraw, 2, 0);
	}
}
