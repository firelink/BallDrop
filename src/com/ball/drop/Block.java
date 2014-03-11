package com.ball.drop;

import android.graphics.Canvas;
import android.graphics.*;
import android.util.*;

public class Block 
{
	private int numberOfBlocks;
	private int r;
	private Paint blockPaint;
	public double x;
	public double y;
	public double velocityX;
	public double velocityY;
	private RectF blockRect;
	private float blockID;
	private double blockLeft;
	private double blockRight;
	private double blockUp;
	private double blockDown;
	private int clipNum;
	private Ball ball;
	private boolean circle;
	private boolean destructable;
	private boolean clipped;
	private boolean done;
	private int hitCount;
	Physics physx;
	//RectF rect = new RectF();
	//Ball ball = new Ball();
	
	Block(double x, double y, int r)
	{
		this(x, y, r, true);
	}
	Block(double x, double y, int r, boolean circle)
	{
		this.x = x;
		this.y = y;
		this.r = r;
		this.circle = circle;
		
		if(!circle)
		{
			this.blockLeft = x - r;
			this.blockRight = x + r;
			this.blockDown = y + r;
			this.blockUp = y - r;
		}
		
		this.velocityX = 0;
		this.velocityY = 0;
		this.blockPaint = new Paint();
		destructable = true;
		clipped = true;
		done = false;
		
		hitCount = 3;

		numberOfBlocks = 1;

		if(!circle)
		{
			blockRect = new RectF((float)blockLeft, (float)blockUp, (float)blockRight, (float)blockDown);
		}
		else
			ball = new Ball((float)x, (float)y, r, blockPaint);

		blockID = (float)Math.random() * 9589123;
	}
	
	public void updatePositions()
	{
		//x += velocityX;
		//y -= velocityY;
		
		if(!circle)
		{
			this.blockLeft = x;
			this.blockRight = x + r;
			this.blockDown = y + r;
			this.blockUp = y;

			updateBlockRect();
		}
	}
	
	public void updateBlockRect()
	{
		blockRect.set((float)(blockLeft), 
					 (float)(blockUp),
					 (float)(blockRight), 
					 (float)(blockDown));
	}
	
	public Ball getBall()
	{
		return ball;
	}
	
	public boolean isACircle()
	{
		return circle;
	}
	
	public void draw(Canvas c)
	{
		if(hitCount>0)
		{
			if(hitCount == 3)
				this.blockPaint.setColor(Color.GREEN);
			else if(hitCount == 2)
				this.blockPaint.setColor(Color.YELLOW);
			else if(hitCount == 1)
				this.blockPaint.setColor(Color.RED);
			
			if(done)
			{
				this.blockPaint.setColor(Color.MAGENTA);
				this.blockPaint.setStyle(Paint.Style.FILL);
			}
			
			if(circle)
				c.drawCircle((float)x, (float)y, r, blockPaint);
			else
				c.drawRect(blockRect, blockPaint);
				
			if(done)
			{
				this.blockPaint.setStyle(Paint.Style.STROKE);
				this.blockPaint.setColor(Color.BLUE);
				c.drawCircle((float)x, (float)y, r, blockPaint);
			}
			//if(circle){ball.draw(c);}
		}
	}
	
	public RectF getRect()
	{
		return blockRect;
	}
	
	public void gotHit()
	{
		if(!clipped)
			done = true;
			
		if(destructable)
			hitCount--;
	}
	
	public int getHitCount()
	{
		return hitCount;
	}
	
	public void setClipNum(int setter)
	{
		clipNum = setter;
	}
	
	public void setDestroyable(boolean d)
	{
		this.destructable = d;
	}
	
	public void setClipping(boolean c)
	{
		this.clipped = c;
		destructable = false;
	}
	
	public boolean isClipped()
	{
		return this.clipped;
	}
	
	public int getClipNum()
	{
		return clipNum;
	}
	
	public boolean isDone()
	{
		return done;
	}
	
	public float getID()
	{
		return blockID;
	}

}
