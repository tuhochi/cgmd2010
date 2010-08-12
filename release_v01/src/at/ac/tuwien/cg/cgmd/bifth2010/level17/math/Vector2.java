package at.ac.tuwien.cg.cgmd.bifth2010.level17.math;
/*** 
 * Represents a 2D Vector
 * 
 * @author Matthias Maschek
 */
public class Vector2
{
	public float x, y;

	/**
	 * Empty Constructor
	 */
	public Vector2 ()
	{
		x = 0;
		y = 0;
	}

	/**
	 * Constructor
	 */
	public Vector2 (float _x, float _y)
	{
		x = _x;
		y = _y;
	}

	/**
	 * Copy Constructor
	 */
	public Vector2 (Vector2 v)
	{
		x = v.x;
		y = v.y;
	}

	/**
	 * Calculates the dot product of two vectors
	 */
	public static float dotProduct (Vector2 v1, Vector2 v2)
	{
		return (v1.x*v2.x + v1.y*v2.y);
	}


	/**
	 * Return a new vector that is the sum of two vectors.
	 */
	public static Vector2 add (Vector2 v1, Vector2 v2)
	{
		return (new Vector2(v1.x+v2.x, v1.y+v2.y));
	}
	
	/**
	 * Return a new vector that points from one point to another.
	 */
	public static Vector2 diff (Vector2 pointStart, Vector2 pointEnd)
	{
		return new Vector2(pointEnd.x-pointStart.x, pointEnd.y-pointStart.y);
	}

	/**
	 * multiplies a vector with a scalar
	 */
	public static Vector2 mult (Vector2 v, float s)
	{
		return new Vector2(v.x * s, v.y * s);
	}

	/**
	 * Returns the length of the vector
	 */
	public float length()
	{
		return (float) Math.sqrt (x*x + y*y);
	}


	/**
	 * Normalize this vector.
	 */
	public void normalize ()
	{
		float thelen = (float) Math.sqrt (x*x + y*y);

		if (thelen != 0) 
		{
			x /= thelen;
			y /= thelen;
		}
	}

	/**
	 * Returns the length of the vector
	 */
	public void invert()
	{
		x = -x;
		y = -y;
	}
	
	/**
	 * returns the angle between this vector and the x axis ???
	 */
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
	 */

	public String toString ()
	{
		return ("(" + x + "," + y + ")");
	}
}
