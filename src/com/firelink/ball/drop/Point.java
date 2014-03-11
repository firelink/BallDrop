package com.firelink.ball.drop;

public class Point
{
	
	public double y;
	public double x;
	
	Point(double x, double y)
	{
		this.setX(x);
		this.setY(y);
	}
	Point()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public void set(double x, double y)
	{
		this.setX(x);
		this.setY(y);
	}
	
	public double getY() 
	{
		return y;
	}
	
	public void setY(double y) 
	{
		this.y = y;
	}
	
	public double getX() 
	{
		return x;
	}
	
	public void setX(double x) 
	{
		this.x = x;
	}
}
