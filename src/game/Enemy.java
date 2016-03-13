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
	protected static final int HEAL_RATE = 250;
	protected static final int FIGHT_DISTANCE = 16;
	protected static final int SPLIT_HEALTH = 48;
	/** The vitality of this enemy. */
	protected double health;
	protected long lastHeal = 0;
	protected Random randNumGen;
	protected FrameAnimator deathAnimator;
	protected FrameAnimator lifeAnimator;
	protected boolean dying = false;
	protected boolean finishedDying = false;
	protected long lastResistanceUpdate;
	protected SimpleBody body;
	
	public Enemy(double x, double y, double vectorX, double vectorY, double health)
	{
		body = new SimpleBody(x, y, vectorX, vectorY);
		this.health = health;
		randNumGen = new Random();
		lifeAnimator = new FrameAnimator("birth", 60);
		lastResistanceUpdate = GameState.getClock().getTimeMS();
	}
	
	public Enemy(double[] coords, double[] vector, double health)
	{
		this(coords[0], coords[1], vector[0], vector[1], health);
	}
	
	@Override
	public void render(core.graphics.RenderEvent e)
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
					drawCoords[0]-(int) getHealth()/4,
					drawCoords[1]-(int) getHealth()/4,
					(int) getHealth()/2,
					(int) getHealth()/2
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
	
	public void update(EntityUpdateEvent event)
	{
		if (dying && deathAnimator.isAnimationDone())
		{
			finishedDying = true;
			return;
		}
		else if (health <= 0)
		{
			die();
			return;
		}
		long currTime = core.gamestate.GameState.getClock().getTimeMS();
		int maxX = core.graphics.GfxManager.getMainLayerSet().getLayerSetWidth();
		int maxY = core.graphics.GfxManager.getMainLayerSet().getLayerSetHeight();
		LinkedList<Enemy> nearEnemies = getNearEnemies(event.getEntities(), 8);
		for (Enemy ne : nearEnemies)
		{
			ne.doDamage(-0.05);
		}
		LinkedList<Enemy> pack = getNearEnemies(event.getEntities(), 64);
		if (pack.size() < 15)
		{
			for (Enemy ne : pack)
			{
				if (distTo(ne) >= getHealth() && ne.getHealth() >= 15)
				{
					double dx = ne.getBody().getX() - getBody().getX();
					double dy = ne.getBody().getY() - getBody().getY();
					if (dx >= 0.1)
					{
						dx = 0.1;
					}
					else if (dx <= -0.1)
					{
						dx = -0.1;
					}
					else
					{
						dx *= -1;
					}
					if (dy >= 0.1)
					{
						dy = 0.1;
					}
					else if (dy <= -0.1)
					{
						dy = -0.1;
					}
					else
					{
						dy *= -1;
					}
					adjVector(dx/10, dy/10);
					if (getBody().getVectorX() > 2)
					{
						getBody().setVectorX(2);
					}
					else if (getBody().getVectorX() < -2)
					{
						getBody().setVectorX(-2);
					}
					if (getBody().getVectorY() > 2)
					{
						getBody().setVectorY(1);
					}
					else if (getBody().getVectorY() < -2)
					{
						getBody().setVectorY(-2);
					}
				}
			}
		}
		else
		{
			doDamage(0.1);
		}
		if (currTime - lastHeal >= HEAL_RATE && health < SPLIT_HEALTH + 10)
		{
			doDamage(-0.25);
			lastHeal = currTime;
		}
//		if (body.getX() <= 0 || body.getY() >= maxX)
//		{
//			doDamage(4);
//		}
//		if (body.getY() <= 0 || body.getY() >= maxY)
//		{
//			doDamage(4);
//		}
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
	
	public Entity split()
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
	
	public double distTo(Enemy other)
	{
		return body.distTo((SimpleBody) other.getBody());
	}
	
	public void doDamage(double amount)
	{
		health -= amount;
		if (health < 0)
		{
			health = 0;
		}
		else if (health > 47)
		{
			health = 47;
		}
	}
	
	public void die()
	{
		if (dying)
		{
			return;
		}
		dying = true;
		deathAnimator = new FrameAnimator("explosion", 125);
		core.sound.SoundManager.playSFX("sfx/explosion1.wav");
	}
	
	public boolean finishedDying()
	{
		return finishedDying;
	}
	
	public double getHealth()
	{
		if (health < 1 && health > 0)
		{
			return 1;
		}
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
