package game;

import java.awt.Color;
import java.awt.image.BufferedImage;

import core.graphics.RenderEvent;

/** A sample Renderer implementation.
* @author Bryan Bettis
*/
public class SampleRenderer implements core.graphics.Renderer
{
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
		this.showOnLayer(4);
	}
	
	@Override
	public synchronized void render(RenderEvent e)
	{
//		e.getContext().setColor(renderColor);
//		e.getContext().fillOval(x-5, y-5, 10, 10);
		BufferedImage img;
		if (renderColor.equals(Color.blue))
		{
			img = core.graphics.GfxManager.getResManager().getRes("testfolder/bullet.png");
			core.graphics.GfxManager.drawGraphic(e.getContext(),img,x-3,y-6,6,12);
		}
		else
		{
			img = core.graphics.GfxManager.getResManager().getRes("asteroid.png");
			core.graphics.GfxManager.drawGraphic(e.getContext(),img,x-6,y-6,12,12);
		}
	}
	
	public synchronized void update()
	{
		if (x <= 0 || x >= 480)
		{
			core.sound.SoundManager.playSFX("bounce");
			vector[0] *= -1;
		}
		if (y <= 0 || y >= 270)
		{
			core.sound.SoundManager.playSFX("bounce");
			vector[1] *= -1;
		}
		x += vector[0];
		y += vector[1];
	}
}
