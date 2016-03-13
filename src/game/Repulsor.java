package game;

import java.awt.Color;
import java.util.Random;

import core.entity.Entity;
import core.entity.EntityUpdateEvent;
import core.gamestate.GameState;
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
	protected double health = 50;
	protected long lastDegradeUpdate;
	
	public Repulsor()
	{
		this(10,10,1.2,-0.5,50);
	}
	
	public Repulsor(double x, double y, double vectorX, double vectorY, double health)
	{
		body = new SimpleBody(x, y, vectorX, vectorY);
		pulseAnimator = new FrameAnimator("bluering", 60);
		effectClock = new core.StopWatch(core.gamestate.GameState.getClock());
		effectClock.start();
		lastCycleUpdate = effectClock.getTimeMS();
		pulseAnimator.setLooping(false);
		activateRepulsion();
		this.health = health;
		lastDegradeUpdate = GameState.getClock().getTimeMS();
	}
	
	public Repulsor(double[] coords, double[] vector, double health)
	{
		this(coords[0], coords[1], vector[0], vector[1], health);
	}
	
	@Override
	public void render(RenderEvent event)
	{
//		int x = ((int) body.getX())-outerPulseRadius;
//		int y = ((int) body.getY())-outerPulseRadius;
		event.getContext().setColor(Color.gray);
		event.getContext().fillOval(
				(int) (body.getX()-getHealth()/4),
				(int) (body.getY()-getHealth()/4),
				(int) getHealth()/2,
				(int) getHealth()/2
				);
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
	public void update(EntityUpdateEvent event)
	{
		int maxX = core.graphics.GfxManager.getMainLayerSet().getLayerSetWidth();
		int maxY = core.graphics.GfxManager.getMainLayerSet().getLayerSetHeight();
		if (health >= 100)
		{
			event.getEntities().addEntity(split());
		}
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
		long currTime = core.gamestate.GameState.getClock().getTimeMS();
		if (lastDegradeUpdate - currTime >= 10)
		{
			lastDegradeUpdate = currTime;
			if (health > 1)
			{
				doDamage(1);
			}
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
						e2.doDamage(0.5);
						doDamage(-0.1);
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
				else if (e instanceof Repulsor && !(e instanceof PlayerInteractor))
				{
					Repulsor e2 = (Repulsor) e;
					if (isWithinAOE((int) e2.getBody().getX(), (int) e2.getBody().getY()))
					{
						e2.doDamage(0.8);
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
	
	protected Entity split()
	{
		double newVector[] = {body.getVectorX(), body.getVectorY()};
		for (int i = 0; i < 2; ++i)
		{
			if (newVector[i] > -0.1 && newVector[i] <= 0.0)
			{
				newVector[i] -= 0.5;
			}
			else if (newVector[i] < 0.1 && newVector[i] >= 0.0)
			{
				newVector[i] += 0.5;
			}
		}
		body.setVector(-newVector[0], -newVector[1]);
		health *= 0.5;
//		core.sound.SoundManager.playSFX("sfx/pulse.wav");
		return new Repulsor(body.getPos(), newVector, health);
	}
	
	public double getHealth()
	{
		if (health < 1 && health > 0)
		{
			return 1;
		}
		return health;
	}
	
	public void doDamage(double damage)
	{
		health -= damage;
	}
	
	public void adjVector(double dx, double dy)
	{
		body.changeVector(dx, dy);
	}

	protected void nextCycle()
	{
		if (effectClock.getTimeMS() - lastCycleUpdate >= effectTime)
		{
			lastCycleUpdate = effectClock.getTimeMS();
		}
	}

	@Override
	public boolean utilizesBody()
	{
		return true;
	}
	
	public synchronized SimpleBody getBody()
	{
		return body;
	}
}
