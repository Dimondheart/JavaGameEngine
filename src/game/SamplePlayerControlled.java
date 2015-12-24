package game;

import java.awt.Color;
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
		e.getContext().setColor(Color.red);
		e.getContext().fillOval(x-5, y-5, 10, 10);
		// TODO Debug this
//		BufferedImage img = 
//				main.graphics.GfxManager.getResManager().getGraphic("asteroid.png");
//		main.graphics.GfxManager.drawGraphic(e.getContext(),img,x-5,y-5,10,10);
//		e.getContext().drawImage(img, x-5, y-5, 10, 10, null);
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
			core.sound.SoundManager.playSFX("bounce");
			moveX = 1;
		}
		else if (x >= 480)
		{
			core.sound.SoundManager.playSFX("bounce");
			moveX = -1;
		}
		if (y <= 0)
		{
			core.sound.SoundManager.playSFX("bounce");
			moveY = 1;
		}
		else if (y >= 270)
		{
			core.sound.SoundManager.playSFX("bounce");
			moveY = -1;
		}
		x += moveX;
		y += moveY;
	}
}
