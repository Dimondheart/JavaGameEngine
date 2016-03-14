package game;

import xyz.digitalcookies.objective.entity.Entity;
import xyz.digitalcookies.objective.entity.EntityUpdateEvent;
import xyz.digitalcookies.objective.graphics.GraphicsManager;
import xyz.digitalcookies.objective.graphics.RenderEvent;

@SuppressWarnings("javadoc")
public class Planet extends AstroObject implements xyz.digitalcookies.objective.entity.Entity
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
		GraphicsManager.drawGraphic(
				event.getContext(),
				GraphicsManager.getResManager().getRes("planet6.png"),
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
