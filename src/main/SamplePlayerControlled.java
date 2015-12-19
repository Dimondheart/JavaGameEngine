package main;

import main.graphics.RenderEvent;
import main.input.InputManager;

import static java.awt.event.KeyEvent.*;

import java.awt.Color;

public class SamplePlayerControlled implements main.graphics.Renderer
{
	private int x = 200;
	private int y = 135;
	
	public SamplePlayerControlled()
	{
		this.show(4);
	}
	
	@Override
	public void render(RenderEvent e)
	{
		e.getContext().setColor(Color.red);
		e.getContext().fillOval(x-5, y-5, 10, 10);
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
			main.sound.SoundManager.playSFX("bounce");
			moveX = 1;
		}
		else if (x >= 480)
		{
			main.sound.SoundManager.playSFX("bounce");
			moveX = -1;
		}
		if (y <= 0)
		{
			main.sound.SoundManager.playSFX("bounce");
			moveY = 1;
		}
		else if (y >= 270)
		{
			main.sound.SoundManager.playSFX("bounce");
			moveY = -1;
		}
		x += moveX;
		y += moveY;
	}
}
