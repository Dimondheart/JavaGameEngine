package main.graphics;

import java.awt.Graphics2D;
import java.io.Serializable;

/** The base class for any object that will be rendered to the screen. */
public abstract class Renderer implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** What layer this renderer is drawing to. */
	private int layer;
	
	/** Basic constructor. */
	public Renderer(int layer)
	{
		setLayer(layer);
		GfxManager.addRenderer(this);
	}
	
	/** Called in the render loop to draw this renderer to the screen. */
	public abstract void render(Graphics2D g);
	
	/** Removes an references to a renderer so it can be completely
	 * removed.
	 */
	public void destroy()
	{
		GfxManager.removeRenderer(this);
	}
	
	/** Change the layer that this renderer is in. */
	public void changeLayer(int newLayer)
	{
		if (layer == newLayer)
		{
			return;
		}
		GfxManager.moveRenderer(this, layer, newLayer);
		setLayer(newLayer);
	}
	
	/** Set what layer this renderer is in. */
	private void setLayer(int newLayer)
	{
		layer = newLayer;
	}
	
	/** Get what layer this renderer is in. */
	public int getLayer()
	{
		return layer;
	}
}
