package main.graphics;

import java.awt.Graphics2D;

public abstract class Renderer
{
	private int layer;
	
	public Renderer(int layer)
	{
		this.layer = layer;
		GfxManager.addRenderer(this);
	}
	
	public abstract void render(Graphics2D g);
	
	public void destroy()
	{
		GfxManager.removeRenderer(this);
	}
	
	public int getLayer()
	{
		return layer;
	}
}
