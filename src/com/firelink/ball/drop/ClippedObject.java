package com.firelink.ball.drop;

import android.graphics.RectF;
import android.util.SparseArray;

public class ClippedObject
{
	private int numOfObjects;
	private SparseArray<Object> objectPoints;
	private SparseArray<String> objectType;
	
	ClippedObject()
	{
		numOfObjects = 1;
		objectPoints = new SparseArray<Object>();
		objectType = new SparseArray<String>();
		
		initObjects();
	}
	
	private void initObjects()
	{
		for(int i = 0; i < objectPoints.size(); i++)
		{
			
		}
	}
	
	public float[][] getCoords()
	{
		return new float[1][1];
	}
	
	public int addObject(RectF rect)
	{
		objectPoints.put(numOfObjects, rect);
		objectType.put(numOfObjects, "RectF");
		numOfObjects++;
		
		return numOfObjects;
	}
	public int addObject(float left, float top, float right, float bottom)
	{
		objectPoints.put(numOfObjects, new RectF(left, top, right, bottom));
		objectType.put(numOfObjects, "RectF");
		numOfObjects++;
		
		return numOfObjects;
	}
	public int addObject(Ball b)
	{
		objectPoints.put(numOfObjects, b);
		objectType.put(numOfObjects, "Ball");
		numOfObjects++;
		
		return numOfObjects;
	}
	
	public int addObject(Block b)
	{
		objectPoints.put(numOfObjects, b);
		objectType.put(numOfObjects, "Block");
		numOfObjects++;
		
		return numOfObjects;
	}
	
	public void updateObject(int num, Object o, String t)
	{
		objectPoints.put(num, o);
		objectType.put(num, t);
	}
	
	public Object[] retrieveObjects()
	{
		Object obj[] = new Object[numOfObjects];
		for(int i = 1; i < numOfObjects; i++)
		{
			try
			{
				obj[i] = objectPoints.get(i);
			}catch(Exception e){continue;}
		}
		
		return obj;
	}
	
	public String[] retrieveObjectTypes()
	{
		String str[] = new String[numOfObjects];
		for(int i = 1; i < numOfObjects; i++)
		{
			try
			{
				str[i] = objectType.get(i);
			}catch(Exception e){continue;}
		}
		
		return str;
	}
	
	public int size()
	{
		return numOfObjects;
	}
	
	public void delete(int num)
	{
		objectPoints.delete(num);
	}
}
