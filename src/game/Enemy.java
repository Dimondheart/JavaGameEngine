package game;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Random;

import core.entity.Entity;
import core.entity.EntityContainer;
import core.entity.EntityUpdateEvent;
import core.gamestate.GameState;
import core.graphics.FrameAnimator;
import core.graphics.TextDrawer;

@SuppressWarnings("javadoc")
public class Enemy implements core.entity.Entity, core.graphics.Renderer
{
	private static final int HEAL_RATE = 250;
	private static final int FIGHT_DISTANCE = 16;
	private static final int SPLIT_HEALTH = 48;
	/** The vitality of this enemy. */
	private int health;
	private long lastHeal = 0;
	private Random randNumGen;
	private FrameAnimator deathAnimator;
	private FrameAnimator lifeAnimator;
	private boolean dying = false;
	private boolean finishedDying = false;
	private long lastResistanceUpdate;
	private SimpleBody body;
	
	public Enemy(double x, double y, double vectorX, double vectorY, int health)
	{
		body = new SimpleBody(x, y, vectorX, vectorY);
		this.health = health;
		randNumGen = new Random();
		lifeAnimator = new FrameAnimator("birth", 60);
		lastResistanceUpdate = GameState.getClock().getMSTime();
	}
	
	public Enemy(double[] coords, double[] vector, int health)
	{
		this(coords[0], coords[1], vector[0], vector[1], health);
	}
	
	@Override
	public synchronized void render(core.graphics.RenderEvent e)
	{
		int[] drawCoords = {(int) body.getX(), (int) body.getY()};
//		e.getContext().setColor(Color.red);
//		e.getContext().drawOval(drawCoords[0]-32, drawCoords[1]-32, 64, 64);
		if (!dying)
		{
//			e.getContext().setColor(Color.white);
//			e.getContext().drawOval(drawCoords[0]-SPLIT_HEALTH/4, drawCoords[1]-SPLIT_HEALTH/4, SPLIT_HEALTH/2, SPLIT_HEALTH/2);
//			e.getContext().setColor(Color.orange);
//			e.getContext().drawOval(drawCoords[0]-FIGHT_DISTANCE, drawCoords[1]-FIGHT_DISTANCE, FIGHT_DISTANCE*2, FIGHT_DISTANCE*2);
			core.graphics.GfxManager.drawGraphic(
					e.getContext(),
					"planet6.png",
					drawCoords[0]-health/4,
					drawCoords[1]-health/4,
					health/2,
					health/2
					);
			lifeAnimator.renderAnimation(e, drawCoords[0], drawCoords[1]);
//			e.getContext().setColor(Color.white);
//			String toPrint = String.format("%.2f", body.getVectorX())
//					+ ","
//					+ String.format("%.2f", body.getVectorY());
//			TextDrawer.drawText(
//					e.getContext(),
//					toPrint,
//					(int) body.getX() - TextDrawer.getTextWidth(e.getContext(), toPrint)/2,
//					(int) body.getY() - TextDrawer.getTextHeight(e.getContext(), toPrint)
//					);
//			String toPrint2 = String.format("%.0f", body.getX())
//					+ ","
//					+ String.format("%.0f", body.getY());
//			TextDrawer.drawText(
//					e.getContext(),
//					toPrint2,
//					(int) body.getX() - TextDrawer.getTextWidth(e.getContext(), toPrint2)/2,
//					(int) body.getY()
//					); 
		}
		else
		{
			deathAnimator.renderAnimation(e, (int) body.getX(), (int) body.getY());
		}
//		TextDrawer.drawText(e.getContext(), Boolean.toString(dying), drawCoords);
	}
	
	public synchronized void update(EntityUpdateEvent event)
	{
		if (dying && deathAnimator.isAnimationDone())
		{
			finishedDying = true;
			return;
		}
		else if (health <= 0)
		{
			die();
			LinkedList<Enemy> nearEnemies = getNearEnemies(event.getEntities(), 32);
			for (Enemy ne : nearEnemies)
			{
				ne.doDamage(2);
			}
			return;
		}
		long currTime = core.gamestate.GameState.getClock().getMSTime();
		int maxX = core.graphics.GfxManager.getMainLayerSet().getLayerSetWidth();
		int maxY = core.graphics.GfxManager.getMainLayerSet().getLayerSetHeight();
		LinkedList<Enemy> nearEnemies = getNearEnemies(event.getEntities(), 8);
		for (Enemy ne : nearEnemies)
		{
			ne.doDamage(1);
		}
		if (currTime - lastHeal >= HEAL_RATE)
		{
			doDamage(-1);
			lastHeal = currTime;
		}
		if (body.getX() <= 0 || body.getY() >= maxX)
		{
			doDamage(4);
		}
		if (body.getY() <= 0 || body.getY() >= maxY)
		{
			doDamage(4);
		}
		if (body.getX() <= 0)
		{
			body.invertVectorX();
			body.setX(1);
		}
		else if (body.getX() >= maxX)
		{
			body.invertVectorX();
			body.setX(maxX - 1);
		}
		if (body.getY() <= 0)
		{
			body.invertVectorY();
			body.setY(1);
		}
		else if (body.getY() >= maxY)
		{
			body.invertVectorY();
			body.setY(maxY - 1);
		}
		body.move();
		if (lastResistanceUpdate - currTime >= 100)
		{
			lastResistanceUpdate = currTime;
			double newVX = body.getVectorX()*0.9;
			double newVY = body.getVectorY()*0.9;
			body.setVector(newVX, newVY);
		}
		if (shouldSplit())
		{
			event.getEntities().addEntity(split());
		}
	}
	
	public boolean shouldSplit()
	{
		return health >= 50;
	}
	
	public synchronized Enemy split()
	{
		double vXFactor = (randNumGen.nextInt(21) - 10) * .2;
		double vYFactor = (randNumGen.nextInt(21) - 10) * .2;
		double newVector[] = {body.getVectorX()*-vXFactor, body.getVectorY()*-vYFactor};
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
		return new Enemy(body.getPos(), newVector, health);
	}
	
	public synchronized double distTo(Enemy other)
	{
		return body.distTo((SimpleBody) other.getBody());
	}
	
	public synchronized void doDamage(int amount)
	{
		health -= amount;
		if (health < 0)
		{
			health = 0;
		}
	}
	
	public synchronized void die()
	{
		if (dying)
		{
			return;
		}
		dying = true;
		deathAnimator = new FrameAnimator("explosion", 125);
		core.sound.SoundManager.playSFX("sfx/explosion1.wav");
	}
	
	public synchronized boolean finishedDying()
	{
		return finishedDying;
	}
	
	public synchronized int getHealth()
	{
		return health;
	}
	
	public void adjVector(double dx, double dy)
	{
		body.changeVector(dx, dy);
	}

	public SimpleBody getBody()
	{
		return body;
	}

	@Override
	public boolean utilizesBody()
	{
		return true;
	}
	
	private LinkedList<Enemy> getNearEnemies(EntityContainer entities, int distance)
	{
		LinkedList<Enemy> nearby = new LinkedList<Enemy>();
		for (Entity other : entities.getEntities())
		{
			if (!(other instanceof Enemy))
			{
				continue;
			}
			if (other == this)
			{
				continue;
			}
			if (distTo((Enemy) other) <= distance)
			{
				nearby.add((Enemy) other);
			}
		}
		return nearby;
	}
}
