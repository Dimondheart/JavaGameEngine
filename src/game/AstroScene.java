package game;

import java.awt.Color;

import core.StopWatch;
import core.entity.EntityContainer;
import core.entity.EntityUpdateEvent;
import core.entity.SceneUpdateEvent;
import core.graphics.GfxManager;
import core.graphics.RenderEvent;

public class AstroScene extends core.entity.Scene implements core.graphics.Renderer
{
	protected double rf1;
	protected double rf2;
	protected double[] posOffset;
	protected EntityContainer astroObjects;
	
	public AstroScene()
	{
		super();
		timer = new StopWatch(core.gamestate.GameState.getClock());
		setScale(100.0, 1.0);
		posOffset = new double[2];
		setOffset(0,0);
		astroObjects = new EntityContainer();
		setActive(false);
	}
	
	public void setup(int sceneData)
	{
		switch (sceneData)
		{
			case 0:
				AstroBody b1 = new AstroBody();
				b1.setMass(5.972e24);
				b1.setRadius(6371.3929);
				b1.setPos(0, 0);
				astroObjects.addEntity(new Planet(b1, this));
				AstroBody b2 = new AstroBody();
				b2.setMass(7.34767309e22);
				b2.setRadius(1736.4822);
				b2.setPos(384472.282, 0);
				b2.setVector(0, 1.022);
				astroObjects.addEntity(new Planet(b2, this));
				break;
			case 1:
				AstroBody b3 = new AstroBody();
				b3.setMass(5e8);
				b3.setRadius(6000);
				b3.setPos(0, 0);
				b3.setVector(0, -4);
				astroObjects.addEntity(new Planet(b3, this));
				AstroBody b4 = new AstroBody();
				b4.setMass(5e8);
				b4.setRadius(6000);
				b4.setPos(13000, 0);
				b4.setVector(0, 4);
				astroObjects.addEntity(new Planet(b4, this));
				break;
			default:
				break;
		}
		timer.start();
	}
	
	public double getScale()
	{
		return rf2 / rf1;
	}
	
	public void zoomIn()
	{
		setScale(rf1-2, rf2);
	}
	
	public void zoomOut()
	{
		setScale(rf1+2, rf2);
	}
	
	public void zoomIn(double factor)
	{
		setScale(rf1-(2*factor), rf2);
	}
	
	public void zoomOut(double factor)
	{
		setScale(rf1+(2*factor), rf2);
	}
	
	public double getOffsetX()
	{
		return posOffset[0];
	}
	
	public double getOffsetY()
	{
		return posOffset[1];
	}
	
	public void clearOffset()
	{
		setOffset(0,0);
	}
	
	public void updateOffset(double dox, double doy)
	{
		updateOffsetX(dox);
		updateOffsetY(doy);
	}
	
	public void updateOffsetX(double dox)
	{
		setOffsetX(getOffsetX() + dox);
	}
	
	public void updateOffsetY(double doy)
	{
		setOffsetY(getOffsetY() + doy);
	}
	
	protected void setOffset(double ox, double oy)
	{
		setOffsetX(ox);
		setOffsetY(oy);
	}
	
	protected void setOffsetX(double ox)
	{
		posOffset[0] = ox;
	}
	
	protected void setOffsetY(double oy)
	{
		posOffset[1] = oy;
	}
	
	/** Original to New: ex 2:3 would be a *(3/2) increase in size. */
	protected void setScale(double v1, double v2)
	{
		if (v1 != 0 && v2 != 0)
		{
			rf1 = v1;
			rf2 = v2;
		}
	}

	@Override
	public void render(RenderEvent event)
	{
		if(!isActive())
		{
			return;
		}
		astroObjects.render(event);
		event.getContext().setColor(Color.white);
		// X-axis
		int[] xa = new int[3];
		xa[0] = (int) 0;
		xa[1] = (int) GfxManager.getMainLayerSet().getLayerSetWidth();
		xa[2] = (int) getOffsetY();
		event.getContext().drawLine(xa[0], xa[2], xa[1], xa[2]);
		int[] ya = new int[3];
		ya[0] = (int) 0;
		ya[1] = (int) GfxManager.getMainLayerSet().getLayerSetHeight();
		ya[2] = (int) getOffsetX();
		event.getContext().drawLine(ya[2], ya[0], ya[2], ya[1]);
	}
	
	@Override
	public void updateScene(SceneUpdateEvent event)
	{
		if (!isActive())
		{
			return;
		}
		EntityUpdateEvent entityEvent = new EntityUpdateEvent();
		entityEvent.setEntities(astroObjects);
		astroObjects.updateEntities(entityEvent);
	}
}
