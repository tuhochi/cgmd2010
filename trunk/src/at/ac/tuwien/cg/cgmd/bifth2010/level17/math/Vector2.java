package at.ac.tuwien.cg.cgmd.bifth2010.level17.math;

public class Vector2
{
	public float x, y;

	public Vector2 ()
	{
		x = 0;
		y = 0;
	}

	public Vector2 (float _x, float _y)
	{
		x = _x;
		y = _y;
	}

	public Vector2 (Vector2 v)
	{
		x = v.x;
		y = v.y;
	}



	public static float dotProduct (Vector2 v1, Vector2 v2)
	{
		return (v1.x*v2.x + v1.y*v2.y);
	}

	public static Vector2 add (Vector2 v1, Vector2 v2)
	{
		return (new Vector2(v1.x+v2.x, v1.y+v2.y));
	}
	
	public static Vector2 diff (Vector2 pointStart, Vector2 pointEnd)
	{
		return new Vector2(pointEnd.x-pointStart.x, pointEnd.y-pointStart.y);
	}
	
	public static Vector2 mult (Vector2 v, float s)
	{
		return new Vector2(v.x * s, v.y * s);
	}
	
	public float length()
	{
		return (float) Math.sqrt (x*x + y*y);
	}

	public void normalize ()
	{
		float thelen = (float) Math.sqrt (x*x + y*y);

		if (thelen != 0) 
		{
			x /= thelen;
			y /= thelen;
		}
	}
	
	public void invert()
	{
		x = -x;
		y = -y;
	}
	
	public float getAngle()
	{
		float angle = 0;
		if(x < 0)
			angle = (float)(Math.atan(y / x) + Math.PI);
		else if(y < 0)
			angle = (float)(Math.atan(y / x) + 2.0 * Math.PI);
		else
			angle = (float)Math.atan(y / x);
		
		return angle;
	}

	/**
	 * Debug output.
	 *
	 *
	 */

	public String toString ()
	{
		return ("(" + x + "," + y + ")");
	}
}
