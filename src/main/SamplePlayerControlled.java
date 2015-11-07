package main;

import main.input.InputManager;

import static java.awt.event.KeyEvent.*;
import static java.awt.event.MouseEvent.*;

import java.awt.Color;
import java.awt.Graphics2D;

public class SamplePlayerControlled extends main.graphics.Renderer
{
	private int x = 200;
	private int y = 135;
	
	public SamplePlayerControlled()
	{
		super(4);
	}
	
	@Override
	public void render(Graphics2D g)
	{
		g.setColor(Color.red);
		g.fillOval(x-5, y-5, 10, 10);
	}
	
	public synchronized void update()
	{
		int moveX = 0;
		int moveY = 0;
		if (InputManager.getKB().isDown(VK_A))
		{
			moveX -= 1;
		}
		if (InputManager.getKB().isDown(VK_D))
		{
			moveX += 1;
		}
		if (InputManager.getKB().isDown(VK_W))
		{
			moveY -= 1;
		}
		if (InputManager.getKB().isDown(VK_S))
		{
			moveY += 1;
		}
		if (x <= 0)
		{
			moveX = 1;
		}
		else if (x >= 480)
		{
			moveX = -1;
		}
		if (y <= 0)
		{
			moveY = 1;
		}
		else if (y >= 270)
		{
			moveY = -1;
		}
		x += moveX;
		y += moveY;
	}
}
