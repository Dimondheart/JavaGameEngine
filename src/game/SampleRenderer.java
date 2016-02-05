package game;

import core.graphics.RenderEvent;

/** A sample Renderer implementation.
* @author Bryan Charles Bettis
*/
public class SampleRenderer implements core.graphics.Renderer
{
	protected int x = 20;
	protected int y = 40;
	protected int[] vector;
	
	public SampleRenderer()
	{
		this(20, 40, -1, 1);
	}
	
	public SampleRenderer(int x, int y, int vectorx, int vectory)
	{
		vector = new int[2];
		this.x = x;
		this.y = y;
		vector[0] = vectorx;
		vector[1] = vectory;
	}
	
	@Override
	public synchronized void render(RenderEvent e)
	{
		core.graphics.GfxManager.drawGraphic(e.getContext(),"asteroid.png",x-6,y-6,12,12);
	}
	
	public synchronized void update()
	{
		if (x <= 0 || x >= 480)
		{
			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			vector[0] *= -1;
		}
		if (y <= 0 || y >= 270)
		{
			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			vector[1] *= -1;
		}
		x += vector[0];
		y += vector[1];
	}
}
