package com.ball.drop;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.util.*;
import android.view.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;

public class BallView extends SurfaceView 
implements SurfaceHolder.Callback 
{

	private int BALL_RADIUS = 0;
//	private final static float MAX_HEIGHT = 50;
	
	private boolean isGameStarted;
	private SparseArray<Boolean> ballMoved;
	private SparseArray<Integer> ballObjectNum;
	private SparseArray<Ball> ball;
	private ClippedObject clipjects;
	private double interval;
	private double frameCount;

	private double totalTimeElapsed;

	private Paint textPaint;

	private int screenWidth;
	private int screenHeight;

	private Paint backgroundPaint;

	private DecimalFormat decMat;

	private BallThread ballThread;

	private SoundPool soundPool;
	private int soundInt;

	private double x = 0, y = 0;
	private boolean threadPaused;
	private int buttonLeft;
	private int buttonTop;
	private int buttonRight;
	private int buttonBottom;
	private Paint buttonTextPaint;
	private Paint buttonPaint;
	private String buttonText;
	private RectF buttonRect;
	private int pause;
	private Object[] CURRENT_CLIPPED_OBJECTS;
	private String[] CLIPPED_OBJECT_TYPES;

	private Block[] blocks;
	
	int diff[] = new int[4];

	private float[] pointsBot;
	private float[] pointsTop;

	private int count = 0;
	
	private SparseArray<RectF> platform;
	private Bitmap launcher;
	private Paint launcherPaint;
	private double launcherX;
	private double launcherY;
	private RectF launchButtonRect;
	private Paint launchPaint;
	private String launchText;
	private Paint launchTextPaint;
	private boolean slowMo;
	int numberOfPlatforms;
	
	Context thisC;
	Activity activity;
	String someDebug;

	public BallView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		thisC = context;

		getHolder().addCallback(this);

		textPaint = new Paint();

		//soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		//soundInt = soundPool.load(context, R.raw.doot, 1);

		backgroundPaint = new Paint();
		backgroundPaint.setColor(Color.WHITE);

		decMat = new DecimalFormat("0.00");

		totalTimeElapsed = 0.0;

		buttonTextPaint = new Paint();
		buttonTextPaint.setColor(Color.WHITE);
		buttonPaint = new Paint();
		buttonPaint.setColor(Color.BLACK);
		buttonText = "Pause Game";
		buttonRect = new RectF();

		ballMoved = new SparseArray<Boolean>();
		
		clipjects = new ClippedObject();
		
		ball = new SparseArray<Ball>();
		ballObjectNum = new SparseArray<Integer>();
		
		interval = 0; 
		frameCount = 0;
		someDebug = "this is debug";
		launcherX = 0;
		launcherY = 0;
		launchButtonRect = new RectF();
		launchPaint = new Paint();
		launchText = "Launch Ball";
		launchTextPaint = new Paint();
		slowMo = false;

	}
	
	public Bitmap loadLauncher() 
	{
        Bitmap defautBitmap = null;
        try
		{
			InputStream ims = thisC.getAssets().open("launcher.png");
			defautBitmap = BitmapFactory.decodeStream(ims);
		}
		catch (IOException e)
		{}
		double h = defautBitmap.getHeight();
		double w = defautBitmap.getWidth();
		double ratio = w/h;
		defautBitmap = Bitmap.createScaledBitmap(defautBitmap, screenWidth/20, (screenWidth/20 * (int)ratio), false);
		
		drop();
		
        return defautBitmap;

    }
	
	public void disposeAssets() 
	{
        launcher.recycle();
        launcher = null;

    }

	public void drop()
	{

		isGameStarted = true;
		for (int i = 1; i <= ball.size(); i++)
		{
			if (ballMoved.get(i, false))
			{
		
				isGameStarted = true;


				ballMoved.put(i, false);
			}
		}

		count = 0;
	}

	public void updatePositions(double elapsedTimeMS)
	{
		interval = elapsedTimeMS / 1000;
		
		if (slowMo)
		{try
			{
				Thread.sleep(0);
			}
			catch (InterruptedException e)
			{}}
	
		float collID = 0;

		for (int i = 1; i <= ball.size(); i++)
		{
			
			if(ball.get(i, null) == null)
				continue;
				
			if(ball.get(i).isDone())
			{
				ball.put(i, null);
				continue;
			}
			

			ball.get(i).updatePositions();

			initRects();

			collID = ball.get(i).checkCollissions(CURRENT_CLIPPED_OBJECTS, CLIPPED_OBJECT_TYPES);
		
			for(int j = 0; j < blocks.length; j++)
			{
				if(blocks[j] != null)
				{

					if(collID == blocks[j].getID())
					{
						blocks[j].gotHit();
					}
			
					if(blocks[j].getHitCount() == 0)
					{
						clipjects.updateObject((blocks[j].getClipNum()-1), new RectF(0,0,0,0), "null");
						CURRENT_CLIPPED_OBJECTS = clipjects.retrieveObjects();
						CLIPPED_OBJECT_TYPES = clipjects.retrieveObjectTypes();
						blocks[j] = null;
						Log.d("AndroidRuntime", "hi");
					}
				}
			}	
		}
	}

	public void launchBall()
	{
		Ball ball = new Ball((int)(launcherX + launcher.getWidth()/2), (int)(launcherY + launcher.getHeight()), BALL_RADIUS);
		
		ball.setShader(true);
		ball.setAntiAlias(true);
		ball.setScreenDimens(screenWidth, screenHeight);
		
		for(int i = 1; i <= this.ball.size(); i++)
		{
			if(this.ball.get(i, null) == null)
			{
				this.ball.put(i, ball);

				this.ball.get(i).setBallIndex(i);

				ballMoved.put(i, false);
				
				return;
			}
		}
		
		this.ball.put(this.ball.size()+1, ball);
		
		this.ball.get(this.ball.size()).setBallIndex(this.ball.size());

		ballMoved.put(this.ball.size(), false);

	}

	private void initRects()
	{
		for(int i = 1; i <= ball.size(); i++)
		{
			if(ball.get(i, null) == null)
				continue;
				
			ball.get(i).updateBallRect();
		}
	}

	public void touchReceptor(MotionEvent e)
	{	
	
/*
		initRects();
		for (int i = 1; i <= ball.size(); i++)
		{
			if(ball.get(i, null) == null)
				continue;
				
			count = 0;
			
			for (int j = 1; j <= ball.size(); j++)
			{
				if(ball.get(i, null) == null)
					continue;
					
				if (i == j)
				{continue;}
				else if (ballMoved.get(j))
				{count++;}
			}

			if (count == 0 && RectF.intersects(ball.get(i).getBallRect(), 
											   new RectF(e.getX() - ball.get(i).getRadius() * 2, e.getY() - ball.get(i).getRadius() * 2, 
														 e.getX() + ball.get(i).getRadius() * 2, e.getY() + ball.get(i).getRadius() * 2)))
			{
				ball.get(i).set((int)(e.getX()), (int)(e.getY()));
				//ball.calculatePoints(i);

				//ball.get(i).setLaunchPoints(decConvert(e.getX()), decConvert(e.getY()));
				
				ball.get(i).setVelocity(0, 0);

				if (!someCheck)
					someCheck = true;

				ballMoved.put(i, true);
			}
		}
*/
	}

	public void buttonPressed(MotionEvent e)
	{
		double dx = Math.pow(e.getX() - screenWidth, 2);
		double dy = Math.pow(e.getY() - screenHeight, 2);
		
		if(dx+dy < Math.pow(buttonRect.right - buttonRect.left, 2))
		{
			if (e.getAction() == MotionEvent.ACTION_DOWN)
			{
				buttonPaint.setColor(Color.GRAY);
				//buttonTextPaint.setColor(Color.BLACK);

				LinearGradient grad = new LinearGradient(buttonRight - (buttonRight - buttonLeft), buttonTop,
														 buttonRight - (buttonRight - buttonLeft), buttonBottom, Color.BLACK, Color.WHITE, Shader.TileMode.CLAMP);

				buttonPaint.setARGB(50, 255, 255, 255);

				if (buttonText.equals("Pause Game"))
				{buttonText = "Unpause"; pauseGame();}
				else
				{buttonText = "Pause Game"; resumeGame();}
			}
			else if (e.getAction() == MotionEvent.ACTION_UP)
			{
				buttonPaint.setColor(Color.BLACK);
				buttonTextPaint.setColor(Color.WHITE);

				LinearGradient grad = new LinearGradient(buttonRight - (buttonRight - buttonLeft), buttonTop,
														 buttonRight - (buttonRight - buttonLeft), buttonBottom, Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP);

				buttonPaint.setARGB(25, 255, 255, 255);
			}
		}
		else
		{
			dx = Math.pow(e.getX() - 0, 2);
			dy = Math.pow(e.getY() - screenHeight, 2);

			if(dx+dy < Math.pow(launchButtonRect.right - launchButtonRect.left, 2))
			{
				if (e.getAction() == MotionEvent.ACTION_DOWN)
				{
					launchPaint.setARGB(50, 255, 255, 255);

					launchBall();
				}
				else if (e.getAction() == MotionEvent.ACTION_UP)
				{
					//drop();
					launchPaint.setARGB(25, 255, 255, 255);
				}
			}
			else
				launcherX = e.getX() - launcher.getWidth()/2;
		}
	}

	public void drawGame(Canvas canvas)
	{
		pause = 0;
		backgroundPaint.setColor(Color.BLACK);
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

		//Debug Information
		//drawDebug(canvas);
		
//		canvas.drawText(someDebug, 500, 500, textPaint);

		canvas.drawLine((float)launcherX + launcher.getWidth()/2, 0, (float)launcherX + launcher.getWidth()/2, screenHeight, textPaint); 
		canvas.drawBitmap(launcher, (float)launcherX, (float)launcherY, launcherPaint);

		for(int i = 0; i < blocks.length; i++)
		{
			if(blocks[i] != null)
			{
				blocks[i].draw(canvas);	
			}
		}
		
		for(int i = 1; i <= ball.size(); i++)
		{
			if(ball.get(i, null) == null)
				continue;

			ball.get(i).draw(canvas);
		}
		
//		canvas.drawPoint(ball.x, ball.y, textPaint);
		//textPaint.setColor(Color.YELLOW);
		//canvas.drawCircle((float)ball.x, (float)ball.y, (float)15, textPaint);
		//textPaint.setColor(Color.BLACK);
		textPaint.setColor(Color.WHITE);
	
		canvas.drawText(ball.size() + " : " + (int)(frameCount/interval), screenWidth/3, screenHeight/3, textPaint);
//		canvas.drawText("Objects: " + ball.size(), screenWidth/2, screenHeight/3, textPaint);

		
		//ball.drawCollisionPoints(canvas);

		//textPaint.setColor(Color.BLACK);
		//canvas.drawPoints(ball.findPoints(Ball.HEMISPHERE_TOP_RIGHT), textPaint);
		//canvas.drawPoints(ball.findPoints(Ball.HEMISPHERE_BOTTOM_RIGHT), textPaint);
//		
		//canvas.drawPoints(ball.findPoints(Ball.HEMISPHERE_TOP_LEFT), textPaint);
		//canvas.drawPoints(ball.findPoints(Ball.HEMISPHERE_BOTTOM_LEFT), textPaint);

		//canvas.drawRect(buttonRect, buttonPaint);
		//canvas.drawRect(launchButtonRect, launchPaint);
		canvas.drawCircle(0, screenHeight, launchButtonRect.right - launchButtonRect.left, launchPaint);
		canvas.drawCircle(screenWidth, screenHeight, buttonRect.right - buttonRect.left, buttonPaint);
		
		
		canvas.drawText(buttonText, screenWidth - (buttonRight - buttonLeft)/2 - buttonText.length()/2, 
						screenHeight - (buttonBottom - buttonTop)/2 - buttonTextPaint.getTextSize(), buttonTextPaint);
						
		canvas.drawText(launchText, (launchButtonRect.right - launchButtonRect.left)/2 - launchText.length()/2 * launchTextPaint.getTextSize(), 
						screenHeight - (launchButtonRect.bottom - launchButtonRect.top)/2 - launchTextPaint.getTextSize(), launchTextPaint);

		if (1 == pause)
		{stopGame();}

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) 
	{
		super.onSizeChanged(w, h, oldw, oldh);

		screenWidth = w;
		screenHeight = h;

		textPaint.setTextSize(w / 20);
		textPaint.setAntiAlias(true);

		//physx.setScreenDimens(w, h);
		//ballHeightRatioW = (float) physx.widthToMeter(MAX_HEIGHT);
		//ballHeightRatioH = (float) physx.heightToMeter(MAX_HEIGHT);
		
		BALL_RADIUS = screenWidth/100;
//		ballHeightOffset = BALL_RADIUS / ballHeightRatioH;

//		Ball ball = new Ball((w / 2 - BALL_RADIUS), 
//				 		(h/5 + BALL_RADIUS),
//				 		BALL_RADIUS);

		//ball.calculateCollisionPoints();

//		ball.setShader(true);
//		ball.setAntiAlias(true);
//		ball.setScreenDimens(w, h);
		
//		this.ball.put(1, ball);
//		this.ball.get(1).setBallIndex(1);
		
/*		int blockSize = 0;
		for(int j = screenHeight/5, q = 0; j < screenHeight-screenHeight/8; j+=(screenHeight/8), q++)
		{
			int m = screenWidth/10;
			if(q % 2 == 0)
				m = screenWidth/5;

			for(int i = m; i < screenWidth - m; i+=(screenWidth/25))
			{
				blockSize++;
			}
		}

		blocks = new Block[blockSize];
		int s = 0;
		for(int j = screenHeight/5, q = 0; j < screenHeight-screenHeight/8; j+=(screenHeight/8), q++)
		{
			int m = screenWidth/10;
			if(q % 2 == 0)
				m = screenWidth/5;
				
			for(int i = m; i < screenWidth - m; i+=(screenWidth/25))
			{
				Block block = new Block(i, j, screenWidth/200, true);
				blocks[s] = block;
				s++;
			}
		}
*/
		blocks = new Block[2];
		blocks[0] = new Block(screenWidth/3, screenHeight/2, screenWidth/50);
		blocks[0].setDestroyable(false);
		blocks[1] = new Block(screenWidth - screenWidth/3, screenHeight/2, screenWidth/50);
		blocks[1].setClipping(false);
		x = h;
		
//		Log.d("Debugging", "You made it this far");

		buttonLeft = screenWidth - screenWidth/5;
		buttonTop = screenHeight - screenHeight/5;
		buttonRight = screenWidth - screenWidth/40;
		buttonBottom = screenHeight - screenHeight/30;

		buttonTextPaint.setTextSize(w / 75);

		buttonRect.set(buttonLeft, buttonTop, buttonRight, buttonBottom);
		
		launchButtonRect.set(screenWidth/40, screenHeight - screenHeight/5, screenWidth/5, screenHeight - screenHeight/30); 
		
		launchTextPaint.setTextSize(w / 75);
		launchTextPaint.setColor(Color.WHITE);
		
		//clipjects = new ClippedObject();
//		clipjects.addObject(buttonRect);
		clipjects.addObject(0, 0, 0, screenHeight);
		clipjects.addObject(0, 0, screenWidth, 0);
		clipjects.addObject(screenWidth, 0, screenWidth, screenHeight);
		clipjects.addObject(0, screenHeight, screenWidth, screenHeight);
	
		for(int i = 0; i < blocks.length; i++)
		{
			clipjects.addObject(blocks[i]);	
			blocks[i].setClipNum(clipjects.size());
		}
/*		for(int i = 0; i < blocks.length; i++)
		{
			clipjects.addObject(blocks[i].getBall());
			blocks[i].setClipNum(clipjects.size());
		}
*/		
//		ballObjectNum.put(1, clipjects.addObject(this.ball.get(1)));

		CURRENT_CLIPPED_OBJECTS = clipjects.retrieveObjects();
		CLIPPED_OBJECT_TYPES = clipjects.retrieveObjectTypes();

		//buttonPaint.setShader(grad);
		//Color buttonColor = new Color();
		//buttonColor.argb(175, 255, 255, 255);
		buttonPaint.setARGB(25, 255, 255, 255);
		launchPaint = new Paint();
		launchPaint.setARGB(25, 255, 255, 255);
		
		initRects();
		launcher = loadLauncher();
		launcherPaint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		launcherPaint.setColorFilter(f);
		//ballMoved.put(1, false);
	}

	public void drawDebug(Canvas canvas)
	{
//		canvas.drawText("Height: " + ballHeightFromGround, 30, 50, textPaint);
//		canvas.drawText("Height(Pixels): " + ballPoint.y, 30, 90, textPaint);
//		canvas.drawText("ScreenHeight(Pixels): " + screenHeight, 30, 130, textPaint);
//		canvas.drawText("Initial Height: " + initialBallHeight , 30, 170, textPaint);
//		canvas.drawText("Change in Height: " + (initialBallHeight - finalBallHeight), 30, 210, textPaint);
//		canvas.drawText("Velocity: " + ballVelocityY, 30, 250, textPaint);
//		canvas.drawText("Time: " + decMat.format(totalTimeElapsed), 30, 290, textPaint);

//		if (points <= 2)
//		{
//			canvas.drawText("Coordinate Set 1: " + launchPoint[0].x + ", " + launchPoint[0].y, 30, 50, textPaint);
//			canvas.drawText("Coordinate Set 2: " + launchPoint[1].x + ", " + launchPoint[1].y, 30, 90, textPaint);
//			canvas.drawText("Coordinate Set 3: " + launchPoint[2].x + ", " + launchPoint[2].y, 30, 130, textPaint);

			//canvas.drawText("BallVelocityX = " + decMat.format(ball.velocityX.get(1)), 0, (screenWidth / 20), textPaint);
			//canvas.drawText("BallVelocityY = " + decMat.format(ball.velocityY.get(1)), 0, (screenWidth / 20 * 2), textPaint);

//			canvas.drawText("Ball position: " + decMat.format(ball.x) + ", " + 
//							decMat.format(ball.y), (screenWidth / 20 * (7 / 2)), screenHeight - (screenWidth / 20), textPaint);

//			canvas.drawText("Ball position: " + ball.x + ", ", (screenWidth / 20 * (7 / 2)), screenHeight - (screenWidth / 10), textPaint);
//			canvas.drawText(ball.y+"", (screenWidth / 20 * (17 / 2)), screenHeight - (screenWidth / 20), textPaint);
//		}
//		else
//		{
//			canvas.drawText("Coordinate Set 1: " + launchPoint[points - 3].x + ", " + launchPoint[points - 3].y, 30, 50, textPaint);
//			canvas.drawText("Coordinate Set 2: " + launchPoint[points - 2].x + ", " + launchPoint[points - 2].y, 30, 90, textPaint);
//			canvas.drawText("Coordinate Set 3: " + launchPoint[points - 1].x + ", " + launchPoint[points - 1].y, 30, 130, textPaint);

			//canvas.drawText("BallVelocityX = " + decMat.format(ball.velocityX.get(1)), 0, (screenWidth / 20), textPaint);
			//canvas.drawText("BallVelocityY = " + decMat.format(ball.velocityY.get(1)), 0, (screenWidth / 20 * 2), textPaint);

			//canvas.drawText("# of Points = " + points, screenWidth - (screenWidth / 20 * 9), (screenWidth / 20), textPaint);
			//canvas.drawText("M = " + decMat.format(physx.getM()), screenWidth - (screenWidth / 20 * 9), (screenWidth / 20 * 2), textPaint);

			//canvas.drawText("Ball position: " + decMat.format(ball.x) + ", " + 
			//			decMat.format(ball.y), (screenWidth / 20 * (7 / 2)), screenHeight - (screenWidth / 20), textPaint);



//			canvas.drawLine((float)0, (float)physx.findY(0), (float)screenWidth, (float)physx.findY(screenWidth), textPaint);
//			canvas.drawText("X, Y = " + x + ", " + y, 30, 400, textPaint);
//		}

		//canvas.drawText("Ball position: " + ball.x.get(1) + ", ", (screenWidth / 20 * (7 / 2)), screenHeight - (screenWidth / 10), textPaint);
		//canvas.drawText(ball.y.get(1) + "", (screenWidth / 20 * (17 / 2)), screenHeight - (screenWidth / 20), textPaint);

//		canvas.drawText("Canvas Size: " + screenHeight + "x" + screenWidth, (screenWidth / 20 * 4), (screenWidth / 20 * 4), textPaint);
//		canvas.drawText("Left, Right " + ballLeft + ", " + buttRight + " - "  + diff[0], (screenWidth / 20 * 4), (screenWidth / 20 * 12), textPaint);
//		canvas.drawText("Right, Left " + ballRight + ", " + buttLeft + " - "  + diff[1], (screenWidth / 20 * 4), (screenWidth / 20 * 14), textPaint);
//		canvas.drawText("Top, Bottom " + ballTop + ", " + buttBottom + " - "  + diff[2], (screenWidth / 20 * 4), (screenWidth / 20 * 16), textPaint);
//		canvas.drawText("Bottom, Top " + ballBottom + ", " + buttTop + " - "  + diff[3], (screenWidth / 20 * 4), (screenWidth / 20 * 18), textPaint);
	}

	public void newGame()
	{
		ballThread = new BallThread(getHolder());
		ballThread.setRunning(true);
		ballThread.start();
	}

	public void stopGame()
	{
		if (ballThread != null)
		{
			ballThread.setRunning(false);
		}
	}

	public void pauseGame()
	{
		threadPaused = true;
		ballThread.pauseResume();
	}

	public void resumeGame()
	{
		threadPaused = false;
		ballThread.pauseResume();
	}

	public void releaseRes()
	{
		//soundPool.release();
	}

	public boolean isThreadPaused()
	{
		return threadPaused;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) 
	{}

	public void surfaceCreated(SurfaceHolder holder) 
	{
		ballThread = new BallThread(holder);
		ballThread.setRunning(true);
		ballThread.start();
		threadPaused = false;
	}

	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		ballThread.setRunning(false);
	}

	public double decConvert(double a)
	{
		DecimalFormat decFormat = new DecimalFormat("#.#");

		return Double.parseDouble(decFormat.format(a));
	}

	public class BallThread extends Thread
	{
		private SurfaceHolder surfaceHolder;
		private boolean threadIsRunning = true;
		private boolean threadPaused;

		BallThread(SurfaceHolder holder)
		{
			surfaceHolder = holder;
			setName("BallThread");
			threadPaused = false;
		}

		public void setRunning(Boolean run)
		{
			threadIsRunning = run;
		}

		public void pauseResume()
		{
			threadPaused = !threadPaused;
		}

		@Override
		public void run() 
		{
			Canvas canvas = null;
			long previousFrameTime = System.currentTimeMillis();

			while (threadIsRunning)
			{
				try
				{
					canvas = surfaceHolder.lockCanvas(null);

					synchronized (surfaceHolder) 
					{
						long currentTime = System.currentTimeMillis();
						double elapsedTimeMS = currentTime - previousFrameTime;
						frameCount++;

						try
						{
							if (isGameStarted && !threadPaused)
								updatePositions(elapsedTimeMS);
							else if(threadPaused)
								Thread.sleep(100);
						}
						catch (InterruptedException e)
						{}
						drawGame(canvas);
						//previousFrameTime = currentTime;
					}
				}
				finally
				{
					if (canvas != null)
					{
						surfaceHolder.unlockCanvasAndPost(canvas);
					}

				}
			}
		}
	}
}
