package main;

import java.awt.Color;
import java.awt.Graphics2D;

public class SampleRenderer2 extends main.graphics.Renderer
{
	private static final long serialVersionUID = 1L;
	
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
		super(layer);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.renderColor = color;
	}

	@Override
	public void render(Graphics2D g)
	{
		g.setColor(renderColor);
		g.fillRect(x, y, width, height);
	}

}
