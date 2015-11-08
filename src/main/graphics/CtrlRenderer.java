package main.graphics;

import java.awt.Graphics2D;

public class CtrlRenderer extends Renderer
{
	public CtrlRenderer()
	{
		super(9);
	}
	
	@Override
	public void render(Graphics2D g)
	{
		String line1 = "Use WASD to move";
		TextDrawer.drawText(g, line1, 2, 10, 24);
	}
}
