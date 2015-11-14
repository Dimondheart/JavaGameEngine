package main.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;

import main.graphics.Renderer;
import main.graphics.TextDrawer;

public class CtrlRenderer extends Renderer
{
	private String displayText;
	public CtrlRenderer(String text)
	{
		super(9);
		displayText = text;
	}
	
	@Override
	public void render(Graphics2D g)
	{
		String line1 = "Controls: " + displayText;
		g.setColor(Color.white);
		TextDrawer.drawText(g, line1, 2, 10, 24);
	}
}
