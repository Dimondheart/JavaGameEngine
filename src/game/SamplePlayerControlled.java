package game;

import core.graphics.FrameAnimator;
import core.graphics.RenderEvent;
import core.graphics.Renderer;
import core.graphics.TextDrawer;
import core.userinput.InputManager;

import static java.awt.event.KeyEvent.*;

import java.awt.Color;

/** Sample player-controlled entity.
* @author Bryan Charles Bettis
*/
@SuppressWarnings("javadoc")
public class SamplePlayerControlled implements Renderer
{
	private double x = 200;
	private double y = 135;
	private double speed = 1;
	private FrameAnimator animate;
	
	public SamplePlayerControlled()
	{
		animate = new FrameAnimator("arrowship",150);
		animate.setLooping(true);
	}
	
	@Override
	public synchronized void render(RenderEvent e)
	{
		if (e.getLayer() == 9)
		{
			e.getContext().setColor(Color.white);
			e.getContext().fillOval((int)x-1, (int)y-1, 3, 3);
			String speedText = "Speed: " + Double.toString(speed);
			TextDrawer.drawText(e.getContext(), speedText, (int)x + 5, (int)y);;
		}
		else
		{
			animate.renderAnimation(e, (int)x, (int)y);
		}
	}
	
	public synchronized void update()
	{
		double moveX = 0;
		double moveY = 0;
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
		if (x < 0)
		{
			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			x = 0;
			moveX = 1;
		}
		else if (x > 480)
		{
			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			x = 480;
			moveX = -1;
		}
		if (y < 0)
		{
			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			y = 0;
			moveY = 1;
		}
		else if (y > 270)
		{
			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			y = 270;
			moveY = -1;
		}
		
		animate.setAnimationSet("basic");
		if (moveX < 0)
		{
			if (moveY < 0)
			{
				animate.setAnimationSet("leftup");
			}
			else if (moveY > 0)
			{
				animate.setAnimationSet("leftdown");
			}
			else
			{
				animate.setAnimationSet("left");
			}
		}
		else if (moveX > 0)
		{
			if (moveY < 0)
			{
				animate.setAnimationSet("rightup");
			}
			else if (moveY > 0)
			{
				animate.setAnimationSet("rightdown");
			}
			else
			{
				animate.setAnimationSet("right");
			}
		}
		else
		{
			if (moveY < 0)
			{
				animate.setAnimationSet("up");
			}
			else if (moveY > 0)
			{
				animate.setAnimationSet("down");
			}
		}
		int scrollWheelChange = InputManager.getMS().getWheelChange();
		if (scrollWheelChange > 0)
		{
			speed *= 2;
			if (speed > 16)
			{
				speed = 16;
			}
		}
		else if (scrollWheelChange < 0)
		{
			speed *= 0.5;
			if (speed < 1.0/16.0)
			{
				speed = 1.0/16.0;
			}
		}
		x += moveX*speed;
		y += moveY*speed;
	}
	
	public double getSpeed()
	{
		return speed;
	}
}
