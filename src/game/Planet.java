package game;

import java.awt.image.BufferedImage;

import core.entity.Entity;
import core.entity.EntityUpdateEvent;
import core.graphics.GfxManager;
import core.graphics.RenderEvent;

public class Planet extends AstroObject implements core.entity.Entity
{
	protected AstroBody body;
	
	public Planet(AstroBody body, AstroScene map)
	{
		setBody(body);
		this.body.setScene(map);
	}
	
	@Override
	public void render(RenderEvent event)
	{
		double x = body.getScreenX();
		double y = body.getScreenY();
		double r = body.getScreenRadius();
		GfxManager.drawGraphic(
				event.getContext(),
				GfxManager.getResManager().getRes("planet6.png"),
				(int) (x-r),
				(int) (y-r),
				(int) (r*2),
				(int) (r*2)
				);
	}
	
	@Override
	public void update(EntityUpdateEvent event)
	{
		for (Entity e : event.getEntities().getEntities())
		{
			if (e == this)
			{
				continue;
			}
			else if (e instanceof Planet)
			{
				Planet e2 = (Planet) e;
				getBody().applyAcceleration(e2.getBody());
			}
		}
		body.move();
	}
	
	@Override
	public boolean utilizesBody()
	{
		return true;
	}
	
	@Override
	public void setBody(AstroBody body)
	{
		this.body = body;
	}
	
	@Override
	public AstroBody getBody()
	{
		return body;
	}
}
