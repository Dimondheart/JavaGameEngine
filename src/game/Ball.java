package game;

import core.graphics.RenderEvent;

import java.awt.Color;

public class Ball implements core.graphics.Renderer
{
	private double x;
	private double y;
	private double vx;
	private double vy;
	
	public Ball(double x, double y, double vx, double vy)
	{
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
	}
	
	@Override
	public void render(RenderEvent event)
	{
		event.getContext().setColor(Color.white);
		event.getContext().fillOval((int) x-6, (int) y-6, 12, 12);
	}
	
	public void update()
	{
		x += vx;
		y += vy;
		if (x <= 0)
		{
			x = 1;
			vx *= -1;
		}
		else if (x >= 480)
		{
			x = 479;
			vx *= -1;
		}
		if (y <= 0)
		{
			y = 1;
			vy *= -1;
		}
		else if (y >= 270)
		{
			y = 269;
			vy *= -1;
		}
	}
}
