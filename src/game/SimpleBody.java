package game;

import core.entity.Scene;

@SuppressWarnings("javadoc")
public class SimpleBody extends core.entity.entitymodule.Body
{
	private SimpleMap myMap;
	private double[] coords;
	private double[] vector;
	
	public SimpleBody()
	{
		this(0,0);
	}
	
	public SimpleBody(double x, double y)
	{
		this(x,y,0,0);
	}
	
	public SimpleBody(double x, double y, double vx, double vy)
	{
		coords = new double[2];
		vector = new double[2];
		setPos(x,y);
		setVector(vx, vy);
	}
	
	@Override
	public boolean setScene(Scene map)
	{
		if (!(map instanceof SimpleMap))
		{
			return false;
		}
		myMap = (SimpleMap) map;
		return true;
	}
	
	public double[] getPos()
	{
		return coords.clone();
	}
	
	public double getX()
	{
		return coords[0];
	}
	
	public double getY()
	{
		return coords[1];
	}
	
	public void setPos(double x, double y)
	{
		setX(x);
		setY(y);
	}
	
	public void setX(double x)
	{
		coords[0] = x;
	}
	
	public void setY(double y)
	{
		coords[1] = y;
	}
	
	public double getVectorX()
	{
		return vector[0];
	}
	
	public double getVectorY()
	{
		return vector[1];
	}
	
	public void setVector(double vx, double vy)
	{
		setVectorX(vx);
		setVectorY(vy);
	}
	
	public void changeVector(double dvx, double dvy)
	{
		setVector(getVectorX() + dvx, getVectorY() + dvy);
	}
	
	public void setVectorX(double vx)
	{
		vector[0] = vx;
	}
	
	public void setVectorY(double vy)
	{
		vector[1] = vy;
	}
	
	public void invertVectorX()
	{
		setVectorX(getVectorX() * -1);
	}
	
	public void invertVectorY()
	{
		setVectorY(getVectorY() * -1);
	}
	
	public void move()
	{
		coords[0] += vector[0];
		coords[1] += vector[1];
	}
	
	public double distTo(SimpleBody other)
	{
		double dx = getX() - other.getX();
		double dy = getY() - other.getY();
		double sum = Math.pow(dx, 2.0) + Math.pow(dy, 2.0);
		return Math.pow(sum, 0.5);
	}
}
