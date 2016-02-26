package game;

import java.util.LinkedList;

import core.entity.Entity;
import core.entity.EntityContainer;
import core.entity.EntityUpdateEvent;
import core.entity.entitymodule.Body;
import core.graphics.RenderEvent;

public class SimpleMap extends core.entity.Map
{
	private EntityContainer entities;
	
	private int width;
	private int height;
	
	public SimpleMap()
	{
		this(1,1);
	}
	
	public SimpleMap(int width, int height)
	{
		setWidth(width);
		setHeight(height);
		entities = new EntityContainer();
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setDims(int width, int height)
	{
		setWidth(width);
		setHeight(height);
	}
	
	public void setWidth(int width)
	{
		if (width <= 0)
		{
			return;
		}
		this.width = width;
	}
	
	public void setHeight(int height)
	{
		if (height <= 0)
		{
			return;
		}
		this.height = height;
	}
}
