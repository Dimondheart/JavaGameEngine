package game;

import java.awt.Color;

import core.entity.EntityUpdateEvent;
import core.graphics.FrameAnimator;
import core.graphics.RenderEvent;

public class Repulsor implements core.entity.Entity
{
	protected Color repulseColor = new Color(0,0,255,65);
	protected int repulseRadius = 64;
	protected FrameAnimator pulseAnimator;
	protected boolean isRepulsing;
	private int[] coords;
	private core.SimpleClock effectClock;
	private long lastCycleUpdate;
	protected long effectTime = 20;
	
	public Repulsor()
	{
		coords = new int[2];
		setCoords(0,0);
		pulseAnimator = new FrameAnimator("bluering", 60);
		effectClock = new core.SimpleClock(core.gamestate.GameState.getClock());
		effectClock.start();
		lastCycleUpdate = effectClock.getTime();
		activateRepulsion();
	}
	
	@Override
	public synchronized void render(RenderEvent event)
	{
		if (isRepulsing)
		{
			event.getContext().setColor(repulseColor);
			int x = coords[0]-repulseRadius;
			int y = coords[1]-repulseRadius;
			event.getContext().fillOval(x, y, repulseRadius*2, repulseRadius*2);
		}
		pulseAnimator.renderAnimation(event, coords[0], coords[1]);
	}
	
	public int getX()
	{
		return coords[0];
	}
	
	public int getY()
	{
		return coords[1];
	}
	
	public void setX(int x)
	{
		coords[0] = x;
	}
	
	public void setY(int y)
	{
		coords[1] = y;
	}
	
	public boolean isWithinAOE(int x, int y)
	{
		double dx = coords[0] - x;
		double dy = coords[1] - y;
		double sum = Math.pow(dx, 2.0) + Math.pow(dy, 2.0);
		double dist = Math.pow(sum, 0.5);
		return dist <= repulseRadius;
	}
	
	public boolean isRepulsing()
	{
		return isRepulsing && effectClock.getTime() - lastCycleUpdate >= effectTime;
	}
	
	protected void activateRepulsion()
	{
		if (isRepulsing)
		{
			return;
		}
		isRepulsing = true;
		pulseAnimator.setLooping(true);
	}
	
	protected void deactivateRepulsion()
	{
		if (!isRepulsing)
		{
			return;
		}
		isRepulsing = false;
		pulseAnimator.setLooping(false);
	}
	
	public void setCoords(int x, int y)
	{
		setX(x);
		setY(y);
	}
	
	@Override
	public synchronized void update(EntityUpdateEvent event)
	{
	}
	
	public synchronized void nextCycle()
	{
		if (effectClock.getTime() - lastCycleUpdate >= effectTime)
		{
			lastCycleUpdate = effectClock.getTime();
		}
	}

	@Override
	public boolean hasBody()
	{
		return true;
	}
}
