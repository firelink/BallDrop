package com.ball.drop;

import android.util.FloatMath;

public class Vector2 
{
	// Static temporary vector
	private final static Vector2 tmp = new Vector2();
	
	public float x;
	public float y;
	public float r;
	
	public Vector2()
	{}
	
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2(float x, float y, float r)
	{
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
	public Vector2(Vector2 v)
	{
		set(v);
	}
	
	public static float dot(Vector2 v1, Vector2 v2)
	{
		return v1.x * v2.x + v1.y * v2.y;
	}
	
	public void setRadius(float r)
	{
		this.r = r;
	}
	
	public Vector2 copy()
	{
		return new Vector2(this);
	}
	
	public float length()
	{
		return FloatMath.sqrt(x * x + y * y);
	}
	public float length2()
	{
		return (x * x + y * y);
	}
	
	public Vector2 set(Vector2 v)
	{
		x = v.x;
		y = v.y;
		return this;
	}
	public Vector2 set(float x, float y)
	{
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2 subtract(Vector2 v)
	{
		x -= v.x;
		y -= v.y;
		return this;
	}
	public Vector2 subtract(float x, float y)
	{
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vector2 normalize()
	{
		float length = length();
		
		if(length != 0)
		{
			x /= length;
			y /= length;
		}
		
		return this;
	}
	
	public Vector2 add(float x, float y)
	{
		this.x += x;
		this.y += y;
		return this;
	}
	
	public float dotProduct(Vector2 v)
	{
		return x * v.x + y * v.y;
	}
	
	public float dot(Vector2 v)
	{
		return x * v.x + y * v.y;
	}
	
	public Vector2 multiply(float scalar)
	{
		x *= scalar;
		y *= scalar;
		return this;
	}
	
	public Vector2 multiply(Vector2 scale)
	{
		x *= scale.x;
		y *= scale.y;
		return this;
	}
	
	public float distance(float x, float y)
	{
		float x_d = x - this.x;
		float y_d = y - this.y;
		return FloatMath.sqrt(x_d * x_d + y_d * y_d);
	}
	public float distance(Vector2 v)
	{
		float x_d = v.x - this.x;
		float y_d = v.y - this.y;
		return FloatMath.sqrt(x_d * x_d + y_d * y_d);
	}
	public float distance2(Vector2 v)
	{
		float x_d = v.x - x;
		float y_d = v.y - y;
		return x_d * x_d + y_d * y_d;
	}
	
	public String toString()
	{
		return "[" + x + ":" + y + "]";
	}
	
	public Vector2 tmp()
	{
		return tmp.set(this);
	}

}
