package game;

import java.awt.Color;

import core.graphics.RenderEvent;

public class ClockTester extends SampleRenderer implements core.graphics.Renderer
{
	private long lastReset = 0;
	
	public ClockTester()
	{
		x = 100;
		y = 100;
	}
	
	@Override
	public void render(RenderEvent event)
	{
		event.getContext().setColor(Color.blue);
		event.getContext().fillOval(x-5, y-5, 10, 10);
	}
	
	@Override
	public void update()
	{
		long currTime = core.gamestate.GameStateManager.getClock().getTime();
		if (currTime - lastReset > 5)
		{
			x = 100;
			vector[0] = 1;
			lastReset = currTime;
		}
		if (x <= 0 || x >= 480)
		{
			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			vector[0] *= -1;
		}
		x += vector[0];
	}
}
