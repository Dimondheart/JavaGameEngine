package main;

import java.awt.Color;
import java.awt.Graphics2D;

import main.graphics.RenderEvent;

public class SampleRenderer implements main.graphics.Renderer
{
	private static final long serialVersionUID = 1L;
	
	private int x = 20;
	private int y = 40;
	private int[] vector;
	private Color renderColor;
	
	public SampleRenderer()
	{
		this(20, 40, -1, 1, Color.blue);
	}
	
	public SampleRenderer(int x, int y, int vectorx, int vectory, Color color)
	{
		vector = new int[2];
		this.x = x;
		this.y = y;
		vector[0] = vectorx;
		vector[1] = vectory;
		renderColor = color;
		this.show(4);
	}
	
	@Override
	public synchronized void render(RenderEvent e)
	{
		e.getContext().setColor(renderColor);
		e.getContext().fillOval(x-5, y-5, 10, 10);
	}
	
	public synchronized void update()
	{
		if (x <= 0 || x >= 480)
		{
			main.sound.SoundManager.playSFX("bounce");
			vector[0] *= -1;
		}
		if (y <= 0 || y >= 270)
		{
			main.sound.SoundManager.playSFX("bounce");
			vector[1] *= -1;
		}
		x += vector[0];
		y += vector[1];
	}
}
