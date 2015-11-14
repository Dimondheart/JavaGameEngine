package main.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;

import main.graphics.Renderer;
import main.graphics.TextDrawer;

public class CtrlRenderer extends Renderer
{
	private String[] displayText;
	
	public CtrlRenderer(String[] text)
	{
		super(9);
		displayText = text;
	}
	
	@Override
	public void render(Graphics2D g)
	{
		g.setColor(Color.white);
		TextDrawer.drawText(g, "Controls:", 2, 11, 12);
		for (int line = 0; line < displayText.length; ++line)
		{
			TextDrawer.drawText(g, displayText[line], 2, (line+1)*12+11, 12);
		}
	}
}
