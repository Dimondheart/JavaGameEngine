package game;

import java.awt.Color;

import core.entity.Entity;
import core.entity.EntityUpdateEvent;
import core.graphics.FrameAnimator;
import core.graphics.RenderEvent;
import core.graphics.TextDrawer;

@SuppressWarnings("javadoc")
public class Repulsor implements core.entity.Entity
{
	protected Color repulseColor = new Color(0,0,255,65);
	protected int outerPulseRadius = 64;
	protected int innerPulseRadius = 32;
	protected FrameAnimator pulseAnimator;
	protected boolean isRepulsing = false;
	private core.StopWatch effectClock;
	private long lastCycleUpdate;
	protected long effectTime = 20;
	protected SimpleBody body;
	protected boolean autoRepulse = true;
	protected int health = 50;
	
	public Repulsor()
	{
		body = new SimpleBody(0, 0);
		pulseAnimator = new FrameAnimator("bluering", 60);
		effectClock = new core.StopWatch(core.gamestate.GameState.getClock());
		effectClock.start();
		lastCycleUpdate = effectClock.getMSTime();
		pulseAnimator.setLooping(false);
		activateRepulsion();
	}
	
	@Override
	public synchronized void render(RenderEvent event)
	{
//		int x = ((int) body.getX())-outerPulseRadius;
//		int y = ((int) body.getY())-outerPulseRadius;
		event.getContext().setColor(Color.gray);
		event.getContext().fillOval((int) body.getX()-health/4, (int) body.getY()-health/4, health/2, health/2);
		pulseAnimator.renderAnimation(event, (int) body.getX(), (int) body.getY());
//		event.getContext().drawOval(x, y, outerPulseRadius*2, outerPulseRadius*2);
//		event.getContext().drawOval(((int) body.getX())-innerPulseRadius, ((int) body.getY())-innerPulseRadius, innerPulseRadius*2, innerPulseRadius*2);

		//		event.getContext().setColor(Color.white);
//		String toPrint = String.format("%.2f", body.getVectorX())
//				+ ","
//				+ String.format("%.2f", body.getVectorY());
//		TextDrawer.drawText(
//				event.getContext(),
//				toPrint,
//				(int) body.getX() - TextDrawer.getTextWidth(event.getContext(), toPrint)/2,
//				(int) body.getY() - TextDrawer.getTextHeight(event.getContext(), toPrint)
//				);
//		String toPrint2 = String.format("%.0f", body.getX())
//				+ ","
//				+ String.format("%.0f", body.getY());
//		TextDrawer.drawText(
//				event.getContext(),
//				toPrint2,
//				(int) body.getX() - TextDrawer.getTextWidth(event.getContext(), toPrint2)/2,
//				(int) body.getY()
//				); 
	}
	
	public boolean isWithinAOE(int x, int y)
	{
		double dx = ((int) body.getX()) - x;
		double dy = ((int) body.getY()) - y;
		double sum = Math.pow(dx, 2.0) + Math.pow(dy, 2.0);
		double dist = Math.pow(sum, 0.5);
		if (dist > outerPulseRadius || dist < innerPulseRadius)
		{
			return false;
		}
		return true;
	}
	
	protected void activateRepulsion()
	{
		isRepulsing = true;
		pulseAnimator.setFrame(1);
	}
	
	protected void deactivateRepulsion()
	{
		isRepulsing = false;
	}
	
	@Override
	public synchronized void update(EntityUpdateEvent event)
	{
		int maxX = core.graphics.GfxManager.getMainLayerSet().getLayerSetWidth();
		int maxY = core.graphics.GfxManager.getMainLayerSet().getLayerSetHeight();
		if (pulseAnimator.isAnimationDone())
		{
			deactivateRepulsion();
			if (autoRepulse)
			{
				activateRepulsion();
			}
		}
		else
		{
			if (pulseAnimator.getFrame() >= 17)
			{
				outerPulseRadius = 0;
				innerPulseRadius = 0;
			}
			else
			{
				outerPulseRadius = (pulseAnimator.getFrame()+4) * 5;
				if (outerPulseRadius > 80)
				{
					outerPulseRadius = 80;
				}
				innerPulseRadius = outerPulseRadius - 45;
				if (innerPulseRadius < 0)
				{
					innerPulseRadius = 0;
				}
			}
		}
		if (health <= 0)
		{
			health = 0;
			return;
		}
		if (body.getX() <= 0)
		{
			body.invertVectorX();
			body.setX(1);
			health -= 2;
		}
		else if (body.getX() >= maxX)
		{
			body.invertVectorX();
			body.setX(maxX - 1);
			health -= 2;
		}
		if (body.getY() <= 0)
		{
			body.invertVectorY();
			body.setY(1);
			health -= 2;
		}
		else if (body.getY() >= maxY)
		{
			body.invertVectorY();
			body.setY(maxY - 1);
			health -= 2;
		}
		body.move();
		if (isRepulsing)
		{
			for (Entity e : event.getEntities().getEntities())
			{
				if (e == this)
				{
					continue;
				}
				else if (e instanceof Enemy)
				{
					Enemy e2 = (Enemy) e;
					if (isWithinAOE((int) e2.getBody().getX(), (int) e2.getBody().getY()))
					{
						double dx = 0.1;
						double dy = 0.1;
						if (((int) body.getX()) > e2.getBody().getX())
						{
							dx *= -1.0;
						}
						if(((int) body.getY()) > e2.getBody().getY())
						{
							dy *= -1.0;
						}
						e2.adjVector(dx, dy);
					}
				}
				else if (e instanceof Repulsor)
				{
					Repulsor e2 = (Repulsor) e;
					if (isWithinAOE((int) e2.getBody().getX(), (int) e2.getBody().getY()))
					{
						double dx = 0.2;
						double dy = 0.2;
						if (((int) body.getX()) > e2.getBody().getX())
						{
							dx *= -1.0;
						}
						if(((int) body.getY()) > e2.getBody().getY())
						{
							dy *= -1.0;
						}
						e2.adjVector(dx, dy);
					}
				}
			}
		}
	}
	
	public void adjVector(double dx, double dy)
	{
		body.changeVector(dx, dy);
	}

	public synchronized void nextCycle()
	{
		if (effectClock.getMSTime() - lastCycleUpdate >= effectTime)
		{
			lastCycleUpdate = effectClock.getMSTime();
		}
	}

	@Override
	public boolean utilizesBody()
	{
		return true;
	}
	
	public SimpleBody getBody()
	{
		return body;
	}
}
