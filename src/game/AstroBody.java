package game;

import java.util.LinkedList;

import core.entity.Scene;

public class AstroBody extends core.entity.entitymodule.Body
{
	public static final double G = 6.674e-11;
	protected AstroScene map;
	/** In kilometers relative to the astronomical origin. */
	protected double[] coords;
	/** In kilometers/second relative to the astronomical origin. */
	protected double[] vector;
	/** In kilograms. */
	protected double mass;
	/** In kilometers. The exact or approximate radius (estimated for stars,
	 * for example.)
	 */
	protected double radius;
	protected long lastMovement;
	protected LinkedList<Double> xAccel;
	protected LinkedList<Double> yAccel;
	
	public AstroBody()
	{
		xAccel = new LinkedList<Double>();
		yAccel = new LinkedList<Double>();
		lastMovement = core.gamestate.GameState.getClock().getTimeNano();
		coords = new double[2];
		vector = new double[2];
		setMass(1.0);
	}
	
	public void setPos(double x, double y)
	{
		setX(x);
		setY(y);
	}
	
	public double getX()
	{
		return coords[0];
	}
	
	public double getScreenX()
	{
//		System.out.println(getX()*getScene().getScale());
		return getX()*getScene().getScale() + getScene().getOffsetX();
	}
	
	public void setX(double x)
	{
		coords[0] = x;
	}
	
	public double getY()
	{
		return coords[1];
	}
	
	public double getScreenY()
	{
		return getY()*getScene().getScale() + getScene().getOffsetY();
	}
	
	public void setY(double y)
	{
		coords[1] = y;
	}
	
	
	public void setVector(double vx, double vy)
	{
		setVectorX(vx);
		setVectorY(vy);
	}
	
	public double getVectorX()
	{
		return vector[0];
	}
	
	public void setVectorX(double vx)
	{
		vector[0] = vx;
	}
	
	public double getVectorY()
	{
		return vector[1];
	}
	
	public void setVectorY(double vy)
	{
		vector[1] = vy;
	}
	
	public double getMass()
	{
		return mass;
	}
	
	public void setMass(double mass)
	{
		this.mass = mass;
	}
	
	public double getRadius()
	{
		return radius;
	}
	
	public double getScreenRadius()
	{
//		System.out.println(getRadius()*getScene().getScale());
		return getRadius()*getScene().getScale();
	}
	
	public void setRadius(double radius)
	{
		this.radius = radius;
	}

	@Override
	public boolean setScene(Scene map)
	{
		if (map instanceof AstroScene)
		{
			this.map = (AstroScene) map;
			return true;
		}
		return false;
	}
	
	public AstroScene getScene()
	{
		return this.map;
	}
	
	/** Check if the specified point is inside this body. */
	public boolean hasCollided(double x, double y)
	{
		return distance(x, y, false) <= 0;
	}
	
	protected double distance(double x, double y, boolean toCenter)
	{
		double absDist = 
				Math.pow(
						Math.pow(
								x-getX(),
								2.0
								)
						+ Math.pow(
								y-getY(),
								2.0
								),
						0.5
						);
		
		if (toCenter)
		{
			return absDist;
		}
		else
		{
			return absDist - getRadius();
		}
	}
	
	public void applyAcceleration(AstroBody other)
	{
		double dx = getX() - other.getX();
		double dy = getY() - other.getY();
		double distance = distance(other.getX(), other.getY(), true)*1000;
		double force = G*getMass()*other.getMass()/Math.pow(distance, 2.0);
		double accel = force/getMass();
		double ratio = accel/distance;
		double xAccel = -dx*1000.0*ratio;
		double yAccel = -dy*1000.0*ratio;
		this.xAccel.add(xAccel/1000.0);
		this.yAccel.add(yAccel/1000.0);
//		System.out.print("a:");
//		System.out.print(accel);
//		System.out.print("ax:");
//		System.out.println(xAccel);
	}
	
	public void move()
	{
//		System.out.println(core.gamestate.GameState.getClock().getTimeNano()/1000000.0/1000.0);
		long currTime = core.gamestate.GameState.getClock().getTimeNano();
		double elapsed = (currTime - lastMovement)/1000000.0/1000.0;
		double newVX = getVectorX();
		double newVY = getVectorY();
		for (Double ax : xAccel)
		{
			newVX += (ax*elapsed);
		}
		for (Double ay : yAccel)
		{
			newVY += (ay*elapsed);
		}
		xAccel.clear();
		yAccel.clear();
		setVector(newVX, newVY);
		double newX = getX() + getVectorX()*elapsed;
		double newY = getY() + getVectorY()*elapsed;
//		System.out.print("Elapsed:");
//		System.out.print(elapsed);
//		System.out.print(getX());
//		System.out.print(",dx:");
//		System.out.print(getVectorX()*elapsed);
		System.out.print("vx:" + String.format("%.8f", getVectorX()));
		System.out.println(",vy:" + String.format("%.8f", getVectorY()));
		setX(getX() + getVectorX()*elapsed);
		setY(getY() + getVectorY()*elapsed);
		lastMovement = currTime;
	}
}
