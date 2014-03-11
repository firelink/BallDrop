package com.ball.drop;

public class Physics 
{
	public final static double EARTH_MASS = (double)((1.075) *  (Math.pow(10, 24)));
	public final static double EARTH_RADIUS = 6051000;
	
	private final static double GRAVITATIONAL_CONSTANT = (double)((6.67) * (Math.pow(10, -11)));
	
	private double mass1;
	private double mass2;
	private double radius;
	private double height;
	private double h;
	private double w;
	private double gravity;
	
	private double m = 0;
	private double b;
	private double y1;
	private double y2;
	private double x1;
	private double x2;
	
	
	
	Physics(double mass1, double mass2, double radius)
	{
		this.mass1 = mass1;
		this.mass2 = mass2;
		this.radius = radius;
		
		gravity = (double) ((GRAVITATIONAL_CONSTANT * mass1 * mass2) / 
				(Math.pow(radius, 2)));
	}
	
	public double findY(double x)
	{
		double y = y1 + getM() * (x - x1);
		
		return y;
				
				//y = y1 + m(x - x1)
	}
	
	public double findM(double y2, double y1, double x2, double x1)
	{
		this.y2 = y2;
		this.y1 = y1;
		this.x2 = x2;
		this.x1 = x1;
		
		m = ((y2 - y1) / (x2 - x1));
		
		return m;
	}
	
	public void setM(double m)
	{
		this.m = m;
	}
	
	public double getCotM()
	{	
		return Math.atan(Math.pow(getM(), -1));	
	}
	
	public double getSinM()
	{	
		return Math.sin(getCotM());	
	}
	
	public double getCosM()
	{	
		return Math.cos(getCotM());	
	}
	
	public int isMPositiveOrNegative()
	{
		if(getM() < 0){return -1;}
		else if(getM() > 0){return 1;}
		
		return 0;
	}
	
	public boolean isYIncreasing()
	{
		if(y2 > y1)
		{
			return true;
		}
		
		return false;
	}
	
	public void setScreenDimens(double w, double h)
	{
		this.w = w;
		this.h = h;
	}
	
	public double widthToMeter(double unit)
	{
		return (w / unit);
	}
	public double heightToMeter(double unit)
	{
		return (h / unit);
	}
	
	public double pixelToMeter(double unit)
	{
		double pixels = h*w;
		double convert = unit / pixels;
		
		return convert;
	}
	
	public double getM()
	{
		return m;
	}
	public void reverseM()
	{
		m *= -1;
	}
	
	public double getTheMass1()
	{
		return mass1;
	}
	public void setTheMass1(double m)
	{
		this.mass1 = m;
	}
	
	public double getTheMass2()
	{
		return mass2;
	}
	public void setTheMass2(double m)
	{
		this.mass2 = m;
	}
	
	public double getTheRadius()
	{
		return radius;
	}
	public void setTheRadius(double r)
	{
		this.radius = r;
	}
	
	public double getTheHeight()
	{
		return height;
	}
	public void setTheHeight(double h)
	{
		this.height = h;
	}
	
	public double getTheGravity()
	{
		return gravity;
	}
	public void setTheGravity(double g)
	{
		this.gravity = g;
	}

}
