package game;

import java.awt.Color;

import core.graphics.RenderEvent;
import core.graphics.Renderer;

public class LayerSetTesterObject implements Renderer
{
	private int x;
	private int y;
	private Color color;

	public LayerSetTesterObject(int x, int y, Color color)
	{
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	@Override
	public void render(RenderEvent event)
	{
		event.getContext().setColor(color);
		event.getContext().fillRect(x, y, 32, 32);
	}
}
