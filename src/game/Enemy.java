package game;

import java.util.Random;

import core.entity.EntityUpdateEvent;
import core.entity.entitymodule.Body;
import core.gamestate.GameState;
import core.graphics.FrameAnimator;

@SuppressWarnings("javadoc")
public class Enemy implements core.entity.Entity, core.graphics.Renderer
{
	private static final int HEAL_RATE = 250;
	/** The vitality of this enemy. */
	private int health;
	private long lastHeal = 0;
	private Random randNumGen;
	private FrameAnimator deathAnimator;
	private FrameAnimator lifeAnimator;
	private boolean dying = false;
	private long lastResistanceUpdate;
	private SimpleBody body;
	
	public Enemy(double x, double y, double vectorX, double vectorY, int health)
	{
		body = new SimpleBody(x, y, vectorX, vectorY);
		this.health = health;
		randNumGen = new Random();
		lifeAnimator = new FrameAnimator("bluering", 60);
		lastResistanceUpdate = GameState.getClock().getTime();
	}
	
	public Enemy(double[] coords, double[] vector, int health)
	{
		this(coords[0], coords[1], vector[0], vector[1], health);
	}
	
	@Override
	public synchronized void render(core.graphics.RenderEvent e)
	{
		if (deathAnimator == null)
		{
			int[] drawCoords = {(int) body.getX(), (int) body.getY()};
			core.graphics.GfxManager.drawGraphic(
					e.getContext(),
					"planet6.png",
					drawCoords[0]-health/4,
					drawCoords[1]-health/4,
					health/2,
					health/2
					);
//			core.graphics.GfxManager.drawGraphic(
//					e.getContext(),
//					"asteroid.png",
//					drawCoords[0]-health/4,
//					drawCoords[1]-health/4,
//					health/2,
//					health/2
//					);
			lifeAnimator.renderAnimation(e, drawCoords[0], drawCoords[1]);
//			e.getContext().setColor(Color.white);
//			String toDraw = Integer.toString(health);
//			int[] textCoords = TextDrawer.centerOverPoint(e.getContext(), toDraw, drawCoords);
//			TextDrawer.drawText(e.getContext(), toDraw, textCoords);
		}
		else
		{
			deathAnimator.renderAnimation(e, (int) body.getX(), (int) body.getY());
		}
	}
	
	public synchronized void update(EntityUpdateEvent event)
	{
		System.out.println("UPDATING");
		long currTime = core.gamestate.GameState.getClock().getTime();
		int maxX = core.graphics.GfxManager.getMainLayerSet().getLayerSetWidth();
		int maxY = core.graphics.GfxManager.getMainLayerSet().getLayerSetHeight();
		if (health <= 0)
		{
			health = 0;
			return;
		}
		if (body.getX() <= 0 || body.getY() >= maxX)
		{
//			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			body.invertVectorX();
			health -=5;
		}
		if (body.getY() <= 0 || body.getY() >= maxY)
		{
			body.invertVectorY();
			health -=5;
		}
		if (body.getX() <= 0)
		{
			body.setX(1);
		}
		else if (body.getX() >= maxX)
		{
			body.setX(maxX - 1);
		}
		if (body.getY() <= 0)
		{
			body.setY(1);
		}
		else if (body.getY() >= maxY)
		{
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
		if (currTime - lastHeal >= HEAL_RATE)
		{
			health += 1;
			lastHeal = currTime;
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
		core.sound.SoundManager.playSFX("sfx/pulse.wav");
		return new Enemy(body.getPos(), newVector, health);
	}
	
	public synchronized double distTo(Enemy other)
	{
		return body.distTo((SimpleBody) other.getBody());
	}
	
	public synchronized void doDamage(int amount)
	{
		health -= amount;
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
	
	public synchronized boolean isDead()
	{
		return health <= 0;
	}
	
	public synchronized boolean finishedDying()
	{
		if (!dying)
		{
			return false;
		}
		return deathAnimator.isAnimationDone();
	}
	
	public int getHealth()
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
	public boolean hasBody()
	{
		return true;
	}
}
