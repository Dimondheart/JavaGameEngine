package game;

import java.awt.image.BufferedImage;

import core.graphics.RenderEvent;
import core.userinput.InputManager;

import static java.awt.event.KeyEvent.*;

/** Sample player-controlled entity.
* @author Bryan Bettis
*/
public class SamplePlayerControlled implements core.graphics.Renderer
{
	private int x = 200;
	private int y = 135;
	
	public SamplePlayerControlled()
	{
		this.showOnLayer(4);
	}
	
	@Override
	public synchronized void render(RenderEvent e)
	{
//		e.getContext().setColor(java.awt.Color.red);
//		e.getContext().fillOval(x-5, y-5, 10, 10);
		BufferedImage img = 
				core.graphics.GfxManager.getResManager().getRes("lowqualityship.png");
		core.graphics.GfxManager.drawGraphic(e.getContext(),img,x-5,y-5,10,10);
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
			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			moveX = 1;
		}
		else if (x >= 480)
		{
			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			moveX = -1;
		}
		if (y <= 0)
		{
			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			moveY = 1;
		}
		else if (y >= 270)
		{
			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			moveY = -1;
		}
		x += moveX;
		y += moveY;
	}
}
