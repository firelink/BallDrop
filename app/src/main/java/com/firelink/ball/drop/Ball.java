package com.firelink.ball.drop;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

public class Ball
{
	public static final int HEMISPHERE_NORTH = 1;
	public static final int HEMISPHERE_SOUTH = 2;
	private static final int BOTTOM = 1;
	private static final int TOP = 0;
	private static final int NUM_POINTS = 200;

	private int numberOfBalls;
	private int ballEnergy;
	private float[] drawPointsTop;
	private float[] drawPointsBot;
	private Paint ballPaint;
	public Vector2 velocity;
	private float points[][];
	private int shaderColor;
	private int shaderDarkColor;
	private boolean shader;
	private boolean itHitSomething = false;
	private RectF ballRect;
	private double screenHeight;
	private double screenWidth;
	private float ballID;
	private int ballIndex;
	private int frameCount;
	private boolean done;
	private boolean paused;
	private double drawX;
	private double drawY;
	private double drawX2;
	private double drawY2;
	private Random rand;
	private boolean randBool;
	private Vector2 bV;
	Paint textPaint;
	Physics physx;

	Ball()
	{
		this(0, 0, 25, new Paint());
	}
	Ball(float x, float y, float r)
	{
		this(x, y, r, new Paint());
	}
	Ball(float x, float y, float r, Paint ballPaint)
	{
		bV = new Vector2(x, y, r);
		velocity = new Vector2(0, 0);
		this.ballPaint = ballPaint;

		drawPointsTop = new float[NUM_POINTS];
		drawPointsBot = new float[NUM_POINTS];

		points = new float[2][NUM_POINTS];

		numberOfBalls = 1;

		shader = false;

		physx = new Physics(Physics.EARTH_MASS, 5, r);

		ballRect = new RectF();

		ballID = (float)Math.random() * 9589123;

		frameCount = 0;
		ballEnergy = 1;
		done = false;
		paused = false;
		drawX = 0; drawY = 0;
		drawX2 = 0; drawY2 = 0;
		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		rand = new Random();
		randBool = rand.nextBoolean();

		//initPoints();
	}

	public float getBallID()
	{
		return ballID;
	}

	public void setBallIndex(int num)
	{
		this.ballIndex = num;
	}

	public int getBallIndex()
	{
		return ballIndex;
	}

	public void setScreenDimens(int w, int h)
	{
		this.screenHeight = h;
		this.screenWidth = w;

		physx.setScreenDimens(w, h);
	}

	public Physics getPhysics()
	{
		return physx;
	}
	
	public void destroy()
	{
		this.done = true;
	}

	public float checkCollissions(Object[] clip, String[] types)
	{
		if (paused)
		{}
		else
		{
			Object obj[] = clip;
			RectF rect = null;
			RectF rectf = null;
			Ball ball = null;
			Block block = null;
			boolean someCheck = false;
			int another[] = new int[2];
			another[0] = 10000;
			another[1] = 10000;

			for (int i = 1; i < obj.length; i++)
			{
				if (types[i] == "Block")
				{
					block = (Block)obj[i];
					
					if(block.isDone())
						continue;

					if (block.isACircle())
					{
						ball = block.getBall();

						if (!itHitSomething)
						{
							if (bV.y < ball.bV.y)
							{
								double dist = ball.bV.distance(bV);
								double sumRadii = (bV.r + ball.bV.r);

								if (dist <= sumRadii)
								{
									if(!block.isClipped())
									{
										return block.getID();
									}
										
									drawX = ((bV.x * ball.bV.r) + (ball.bV.x * bV.r)) / (bV.r + ball.bV.r);
									drawY = ((bV.y * ball.bV.r) + (ball.bV.y * bV.r)) / (bV.r + ball.bV.r);
									drawX2 = ball.bV.x;
									drawY2 = ball.bV.y;

									double tration = sumRadii - dist;
									Vector2 bVcpy = bV.copy();
									Vector2 N = bVcpy.subtract(ball.bV);
									N.multiply((float)(1 / dist));
									Vector2 P = N.multiply((float)tration);
									bV.add(P.x, P.y);

									Vector2 tan = new Vector2();
									tan.y = -(ball.bV.x - bV.x);
									tan.x = ball.bV.y - bV.y;
									tan.normalize();

									Vector2 relativeVelocity = 
										new Vector2(velocity.x - ball.velocity.x, 
													velocity.y - ball.velocity.y);

									float length = Vector2.dot(relativeVelocity, tan);
									tan.multiply(length);
									relativeVelocity.subtract(tan);

									velocity.x -= 1.5 * relativeVelocity.x;
									velocity.y -= 1.5 * relativeVelocity.y;


									itHitSomething = true;
									//Thread.sleep(2000);
									return block.getID();
								}
							}
							else if (bV.y > ball.bV.y)
							{
								double dist = ball.bV.distance(bV);
								double sumRadii = (bV.r + ball.bV.r);

								if (dist <= sumRadii)
								{
									
									if(!block.isClipped())
										return block.getID();
										
									drawX = ((bV.x * ball.bV.r) + (ball.bV.x * bV.r)) / (bV.r + ball.bV.r);
									drawY = ((bV.y * ball.bV.r) + (ball.bV.y * bV.r)) / (bV.r + ball.bV.r);
									drawX2 = ball.bV.x;
									drawY2 = ball.bV.y;

									double tration = sumRadii - dist;
									Vector2 bVcpy = bV.copy();
									Vector2 N = bVcpy.subtract(ball.bV);
									N.multiply((float)(1 / dist));
									Vector2 P = N.multiply((float)tration);
									bV.add(P.x, P.y);

									Vector2 tan = new Vector2();
									tan.y = -(ball.bV.x - bV.x);
									tan.x = ball.bV.y - bV.y;
									tan.normalize();

									Vector2 relativeVelocity = 
										new Vector2(velocity.x - ball.velocity.x, 
													velocity.y - ball.velocity.y);

									float length = Vector2.dot(relativeVelocity, tan);
									tan.multiply(length);
									relativeVelocity.subtract(tan);

									velocity.x -= 1.5 * relativeVelocity.x;
									velocity.y -= 1.5 * relativeVelocity.y;
									
									itHitSomething = true;
									//Thread.sleep(2000);
									return block.getID();
								}
							}
							else if (bV.y == ball.bV.y)
							{
								double distance = ((bV.x - ball.bV.x) * (bV.x - ball.bV.x) + (bV.y + velocity.y + 1 - ball.bV.y) * (bV.y + velocity.y + 1 - ball.bV.y));
								if (distance <= (Math.pow((bV.r + ball.bV.r), 2)))
								{
									if(!block.isClipped())
										return block.getID();
										
									bV.x = (float) (Math.sqrt((float) ((Math.pow(bV.r + ball.bV.r, 2)) - (Math.pow(bV.y - ball.bV.y, 2)))) + ball.bV.x);

									velocity.x *= -1;
									itHitSomething = true;
									return block.getID();
								}
							}
						}
						else
						{
							if (itHitSomething)
							{itHitSomething = false;}
						}
					}
					else
					{
						rect = block.getRect();

						if (RectF.intersects(ballRect, rect))
						{
							rectf = rect;

							if (!itHitSomething && !someCheck)
							{
								float ballLeft = ballRect.left;
								float ballRight = ballRect.right;
								float ballBottom = ballRect.bottom;
								float ballTop = ballRect.top;

								float buttRight = rect.right;
								float buttLeft = rect.left;
								float buttTop = rect.top;
								float buttBottom = rect.bottom;

								int diff[] = new int[4];

								diff[0] = (int) Math.abs(ballLeft - buttRight);
								diff[1] = (int) Math.abs(ballRight - buttLeft);
								diff[2] = (int) Math.abs(ballTop - buttBottom);
								diff[3] = (int) Math.abs(ballBottom - buttTop);
								int min = diff[0];
								for (int q = 1; q < 4; q++)
								{
									min = (min < diff[q]) ? min: diff[q];
								}

								if (min == diff[0] || min == diff[1])
								{
									if (min == diff[0])
									{
										bV.x = buttRight + bV.r + 1;
										if (ball != null)
											another[1] = 0;
									}
									else if (min == diff[1])
									{
										bV.x = buttLeft - bV.r - 1;
										if (ball != null)
											another[1] = 1;
									}

									velocity.x *= -1;
									itHitSomething = true;
									return block.getID();
								}
								else
								{
									if (min == diff[2])
									{
										bV.y = buttBottom + bV.r + 1;
										if (ball != null)
											another[1] = 2;
									}
									else if (min == diff[3])
									{
										bV.y = buttTop - bV.r - 1;
										if (ball != null)
											another[1] = 3;
									}


									velocity.y = -((velocity.y / 2) + ((int)((ballEnergy / screenHeight) * 5)));

									itHitSomething = true;
									return block.getID();
								}
							}
						}
						else
						{
							if (itHitSomething)
							{itHitSomething = false;}
						}
					}
				}
				else if (types[i] == "RectF")
				{
					rect = (RectF)obj[i];

					if (RectF.intersects(ballRect, rect))
					{
						rectf = rect;

						if (!itHitSomething && !someCheck)
						{
							float ballLeft = ballRect.left;
							float ballRight = ballRect.right;
							float ballBottom = ballRect.bottom;
							float ballTop = ballRect.top;

							float buttRight = rect.right;
							float buttLeft = rect.left;
							float buttTop = rect.top;
							float buttBottom = rect.bottom;

							int diff[] = new int[4];

							diff[0] = (int) Math.abs(ballLeft - buttRight);
							diff[1] = (int) Math.abs(ballRight - buttLeft);
							diff[2] = (int) Math.abs(ballTop - buttBottom);
							diff[3] = (int) Math.abs(ballBottom - buttTop);
							int min = diff[0];
							for (int q = 1; q < 4; q++)
							{
								min = (min < diff[q]) ? min: diff[q];
							}

							if (min == diff[0] || min == diff[1])
							{
								if (min == diff[0])
								{
									bV.x = buttRight + bV.r + 1;
									if (ball != null)
										another[1] = 0;
								}
								else if (min == diff[1])
								{
									bV.x = buttLeft - bV.r - 1;
									if (ball != null)
										another[1] = 1;
								}

								velocity.x *= -1;
								itHitSomething = true;
							}
							else
							{
								if (min == diff[2])
								{
									bV.y = buttBottom + bV.r + 1;
									if (ball != null)
										another[1] = 2;
								}
								else if (min == diff[3])
								{
									bV.y = buttTop - bV.r - 1;
									if (ball != null)
										another[1] = 3;
								}


								velocity.y = -((velocity.y / 2) + ((int)((ballEnergy / screenHeight) * 5)));

								itHitSomething = true;
							}

							if (buttLeft == buttRight)
							{
								if (0 == buttLeft)
									bV.x = bV.r + 1;
								else if (screenWidth == buttLeft)
									bV.x = (float) (screenWidth - bV.r - 1);
							}
							else if (buttTop == buttBottom)
							{
								if (0 == buttTop)
									bV.y = bV.r + 1;
								else if (buttTop == screenHeight)
									bV.y = (float) (screenHeight - bV.r - 1);
							}

							if ((buttBottom == buttTop) || (buttRight == buttLeft))
							{
								//ballEnergy/=10;
								//velocityX *= -1;
							}
							else
							{
								if (diff[0] < diff[1])
								{
									if (bV.x == buttRight + bV.r + 1)
										velocity.x *= -1;
									else if (bV.x == buttLeft - bV.r - 1)
										velocity.x *= -1;
									else
									{
										if (velocity.x < 0)
											velocity.x = velocity.x * -1;//1 + (int)((ballEnergy / screenHeight) * 10);
										else
											velocity.x = velocity.x * -1;//1 + (int)((ballEnergy / screenHeight) * 10);
									}
								}
								else if (diff[1] < diff[0])
								{
									velocity.x = velocity.x * -1;//-1 - (timeFalling) / 18;

								}
							}
						}
					}
					else
					{
						if (itHitSomething)
						{itHitSomething = false;}
					}
				}
			}
		}
		return 0;
	}

	public void setNumberOfBalls(int numberOfBalls)
	{
		this.numberOfBalls = numberOfBalls;
	}

	public int getNumberOfBalls()
	{
		return numberOfBalls;
	}

	public void set(int x, int y)
	{
		this.bV.x = x;
		this.bV.y = y;
	}

	public void updateBallRect()
	{
		ballRect.set((float)(bV.x - bV.r), 
					 (float)(bV.y - bV.r),
					 (float)(bV.x + bV.r), 
					 (float)(bV.y + bV.r));
	}

	public void addBall(int x, int y, int r)
	{
//		numberOfBalls++;
//		
//		Paint ballPaint = new Paint();
//		
//		this.x.put(numberOfBalls, (double)x);
//		this.y.put(numberOfBalls, (double)y);
//		this.r.put(numberOfBalls, r);
//		this.ballPaint.put(numberOfBalls, ballPaint);
//
//		this.velocityX.put(numberOfBalls, 0.0);
//		this.velocityY.put(numberOfBalls, 0.0);
//		
//		setShader(true);
	}

	public void setVelocity(int y, int x)
	{
		velocity.x = x;//-(int)(x * physx.getSinM());
		velocity.y = y;//(-speed * physx.getCosM());
	}

	public void updatePositions()
	{
		if (paused)
		{}
		else
		{
			if ((int)velocity.y == 0)
			{
				ballEnergy = (int)(screenHeight - bV.y - bV.r);
			}

			frameCount++;

			if ((int)velocity.y == 0 && ballEnergy > 0 && itHitSomething)
			{
				if (frameCount % 20 == 0)
				{

					if (randBool)
						velocity.x = -5;
					else
						velocity.x = 5;
				}
			}

			if (frameCount % 280 == 0)
				randBool = rand.nextBoolean();

			if (ballEnergy != 1)
			{			
				velocity.y += .5;
			}
			else if (1 == ballEnergy)
				done = true;

			bV.x += velocity.x;
			bV.y += velocity.y;


			updateBallRect();
			//calculateCollisionPoints();
		}
	}

	public void drawCollisionPoints(Canvas canvas)
	{
		Paint pointsPaint = new Paint();
		pointsPaint.setColor(Color.WHITE);

		canvas.drawPoints(drawPointsTop, pointsPaint);
		canvas.drawPoints(drawPointsBot, pointsPaint);
	}

	public void calculateCollisionPoints()
	{
		getPoints();
		//float[] drawPointsTop = new float[98];
		//float[] drawPointsBot = new float[98];

		for (int i = 0; i < 200; i += 2)
		{
			drawPointsTop[i] = points[TOP][i]; drawPointsTop[i + 1] = points[TOP][i + 1];
			drawPointsBot[i] = points[BOTTOM][i]; drawPointsBot[i + 1] = points[BOTTOM][i + 1];
		}

		//this.drawPointsTop = drawPointsTop;
		//this.drawPointsBot = drawPointsBot;
	}

	public float[][] getPoints()
	{
		int q = 0;
		for (double i = 0.0 - bV.r, j = 0; i <= bV.r; i += ((double)bV.r / 49.75), j += 2)
		{	
			points[BOTTOM][(int)j] = (float)(bV.x + i); 
			points[BOTTOM][(int)j + 1] = (float)findYBeta(bV.x + i, Ball.HEMISPHERE_SOUTH);
			points[TOP][(int)j] = (float)(bV.x + i); 
			points[TOP][(int)j + 1] = (float)findYBeta(bV.x + i, Ball.HEMISPHERE_NORTH);
			//Log.d("AndroidRuntime", i +"");
			q++;
		}

		//Log.d("AndroidRuntime", q+" jjjjj");
		return points;
	}

	public Point intersects(ClippedObject co)
	{

		return new Point(0, 0);
	}

	public Point intersects(Ball b)
	{

		return new Point(0, 0);
	}

	public Point intersects(int x, int y)
	{
		return new Point(0, 0);
	}


	public RectF getBallRect()
	{
		return this.ballRect;
	}

	public boolean isDone()
	{
		return this.done;
	}

	public void setShader(boolean shader)
	{
		this.shader = shader;

		if (this.shader)
		{
			int red = (int)(Math.random() * 155) + 100;
	        int green = (int)(Math.random() * 155) + 100;
	        int blue = (int)(Math.random() * 155) + 100;

	        shaderColor = 0xff000000 | red << 16 | green << 8 | blue;

	        shaderDarkColor =  0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;
	        RadialGradient gradient = new RadialGradient((int)bV.x, (int)bV.y - (int)(bV.r / 4),
														 (int)bV.r, shaderColor, shaderDarkColor, Shader.TileMode.CLAMP);

	        this.ballPaint.setShader(gradient);
		}
		else
		{
			this.ballPaint.setShader(null);
		}
	}

	public void setRadius(int r)
	{
		this.bV.r = r;
	}

	public double getRadius()
	{
		return bV.r;
	}

	public Paint getPaint()
	{
		return ballPaint;
	}

	public double getVelocityX()
	{
		return velocity.x;
	}

	public double getVelocityY()
	{
		return velocity.y;
	}

	public void setAntiAlias(boolean bool)
	{
		this.ballPaint.setAntiAlias(bool);
	}

	public double findYBeta(double x, int hemisphere)
	{
		double y = 0;
		double r = this.bV.r;

		x = this.bV.x - x;

		y = Math.sqrt(Math.pow(r, 2) - Math.pow(x, 2));

		if (Double.isNaN(y) == true)
		{y = 0;}

		if (hemisphere == Ball.HEMISPHERE_NORTH)
			return this.bV.y - y;
		else if (hemisphere == Ball.HEMISPHERE_SOUTH)
			return this.bV.y + y;

		return this.bV.y;
	}

	public void draw(Canvas c)
	{       
		if (shader)
		{
			int offSet = 0;
			double change = (c.getHeight()) / (bV.y);
			if (bV.y > c.getHeight() / 2)
			{offSet = 1;}
			else if (bV.y < c.getHeight() / 2)
			{offSet = -1;}
			RadialGradient gradient = new RadialGradient((int)bV.x, (int)((bV.y - (int)(bV.r / 4)) + ((change * 4))),
														 (int)bV.r, shaderColor, shaderDarkColor, Shader.TileMode.CLAMP);

			this.ballPaint.setShader(gradient);
		}

		if (done)
		{}
		else
		{
			c.drawCircle((float)bV.x, (float)bV.y, 
						 (float)bV.r, ballPaint);


			drawCollisionPoints(c);

			textPaint.setTextSize(24);
			//c.drawCircle((float)drawX, (float)drawY, 10, textPaint);
			//c.drawCircle((float)drawX2, (float)drawY2, 10, textPaint);
			c.drawLine((float)drawX, (float)drawY, (float)drawX2, (float)drawY2, textPaint);
			c.drawText(Math.toDegrees(physx.getM()) + " : " + physx.getM() + " : " + velocity.x + " : " + velocity.y + " : " + ballEnergy, (int)screenWidth / 2, (int)screenHeight / 2, textPaint);
		}
	}
}
