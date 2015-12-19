package main;

import java.awt.Color;

import main.graphics.RenderEvent;

public class SampleRenderer2 implements main.graphics.Renderer
{
	private int x;
	private int y;
	private int width;
	private int height;
	private Color renderColor;
	
	public SampleRenderer2()
	{
		this(180, 0, 20, 270, Color.darkGray, 5);
	}
	
	public SampleRenderer2(int x, int y, int width, int height, Color color, int layer)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.renderColor = color;
		this.show(layer);
	}

	@Override
	public void render(RenderEvent e)
	{
		e.getContext().setColor(renderColor);
		e.getContext().fillRect(x, y, width, height);
	}

}
