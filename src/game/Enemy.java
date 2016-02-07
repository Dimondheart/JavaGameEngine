package game;

import java.awt.Color;
import java.util.Random;

import core.graphics.TextDrawer;

@SuppressWarnings("javadoc")
public class Enemy implements core.graphics.Renderer
{
	private static final int HEAL_RATE = 250;
	/** Where this enemy is located. */
	private double[] coords;
	private double[] vector;
	/** The vitality of this enemy. */
	private int health;
	private long lastHeal = 0;
	private Random randNumGen;
	
	public Enemy(double x, double y, double vectorX, double vectorY, int health)
	{
		coords = new double[2];
		vector = new double[2];
		coords[0] = x;
		coords[1] = y;
		vector[0] = vectorX;
		vector[1] = vectorY;
		this.health = health;
		randNumGen = new Random();
	}
	
	public Enemy(double[] coords, double[] vector, int health)
	{
		this(coords[0], coords[1], vector[0], vector[1], health);
	}
	
	@Override
	public synchronized void render(core.graphics.RenderEvent e)
	{
		int[] drawCoords = {(int) coords[0], (int) coords[1]};
		core.graphics.GfxManager.drawGraphic(
				e.getContext(),
				"asteroid.png",
				drawCoords[0]-6,
				(int) drawCoords[1]-6,
				12,
				12
				);
		e.getContext().setColor(Color.white);
		String toDraw = Integer.toString(health);
		int[] textCoords = TextDrawer.centerOverPoint(e.getContext(), toDraw, drawCoords);
		TextDrawer.drawText(e.getContext(), toDraw, textCoords);
	}
	
	public synchronized void update()
	{
		long currTime = core.gamestate.GameState.getClock().getTime();
		if (health <= 0)
		{
			return;
		}
		if (coords[0] <= 0 || coords[0] >= 480)
		{
			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			vector[0] *= -1;
			health -=2;
		}
		if (coords[1] <= 0 || coords[1] >= 270)
		{
			core.sound.SoundManager.playSFX("sfx/bounce.wav");
			vector[1] *= -1;
			health -=2;
		}
		if (coords[0] <= 0)
		{
			coords[0] = 1;
		}
		else if (coords[0] >= 480)
		{
			coords[0] = 479;
		}
		if (coords[1] <= 0)
		{
			coords[1] = 1;
		}
		else if (coords[1] >= 270)
		{
			coords[1] = 269;
		}
		coords[0] += vector[0];
		coords[1] += vector[1];
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
		double newVector[] = {vector[0]*-vXFactor, vector[1]*-vYFactor};
		for (int i = 0; i < 2; ++i)
		{
			if (newVector[i] > 2.0)
			{
				newVector[i] = 2.0;
			}
			else if (newVector[i] < -2.0)
			{
				newVector[i] = -2.0;
			}
			else if (newVector[i] > -0.1 && newVector[i] <= 0.0)
			{
				newVector[i] -= 0.5;
			}
			else if (newVector[i] > -0.1 && newVector[i] <= 0.0)
			{
				newVector[i] -= 0.5;
			}
			else if (newVector[i] < 0.1 && newVector[i] >= 0.0)
			{
				newVector[i] += 0.5;
			}
		}
		health *= 0.5;
		return new Enemy(coords, newVector, health);
	}
}
