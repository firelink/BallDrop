
package com.ball.drop;

import android.app.*;
import android.graphics.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.view.GestureDetector.*;
import java.io.*;
import android.content.res.*;

public class BallDrop extends Activity
{
	private GestureDetector gesture;
	private BallView ball;
	public static Activity activity;
	public static Bitmap foo;
	//public AssetManager Assets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		activity = this;
		
		// = readAssetsBitmap("foo.png");
		
		setContentView(R.layout.main);
		
//		Debug.startMethodTracing("ballDrop");
		
		ball = (BallView)findViewById(R.id.ballView);
		
		gesture = new GestureDetector(this, gestureListener);
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		//Ignore orientation/keyboard
		super.onConfigurationChanged(newConfig);
	}
	
	protected void onPause() 
	{
		super.onPause();
		ball.stopGame();
	}
	
	protected void onDestroy() 
	{
		super.onDestroy();
		ball.stopGame();
		ball.disposeAssets();
		//ball.releaseRes();
//		Debug.stopMethodTracing();
	}
	
	public boolean onTouchEvent(MotionEvent e) 
	{
		int action = e.getAction();
		
		if(action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN)
		{			
			//ball.touchReceptor(e);
			
			ball.buttonPressed(e);
		}
		else if(action == MotionEvent.ACTION_UP)
		{
			//ball.drop();
			ball.buttonPressed(e);
		}
		
		return gesture.onTouchEvent(e);
	}
	
	SimpleOnGestureListener gestureListener = new SimpleOnGestureListener()
	{	
		public boolean onDoubleTap(MotionEvent e) 
		{
			//ball.addBall();
			return true;
		}
	};

}
