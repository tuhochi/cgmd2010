package at.ac.tuwien.cg.cgmd.bifth2010.level88.util;


/**
 * Class for vectors consisting of two elements
 * @author Asperger, Radax
 */
public class Vector2 {
	public float x;
	public float y;

	
	/**
	 * Constructor
	 */
	public Vector2() {
		x = 0;
		y = 0;
	}
	
	
	/**
	 * Constructor
	 * @param _x x-component of the vector
	 * @param _y y-component of the vector
	 */
	public Vector2(float _x, float _y) {
		x = _x;
		y = _y;
	}
	
	
	/**
	 * Constructor
	 * @param v Vector2 to use for creating a new object
	 */
	public Vector2(Vector2 v) {
		x = v.x;
		y = v.y;
	}

	
	/**
	 * Calculate the dotproduct of two vectors
	 * @param v1 first vector
	 * @param v2 second vector
	 * @return dotproduct of the two vectors
	 */
	public static float dotProduct (Vector2 v1, Vector2 v2)
	{
		return (v1.x*v2.x + v1.y*v2.y);
	}

	
	/**
	 * Add the two vectors together
	 * @param v1 first vector
	 * @param v2 second vector
	 * @return vector consisting of the addition of the two vectors
	 */
	public static Vector2 add (Vector2 v1, Vector2 v2)
	{
		return (new Vector2(v1.x+v2.x, v1.y+v2.y));
	}
	
	
	/**
	 * Calculate the length of a starting and an end vector
	 * @param pointStart starting vector
	 * @param pointEnd end vector
	 * @return length of the calculated vector
	 */
	public static Vector2 diff (Vector2 pointStart, Vector2 pointEnd)
	{
		return new Vector2(pointEnd.x-pointStart.x, pointEnd.y-pointStart.y);
	}
	
	
	/**
	 * Multiplicate a vector with a constant
	 * @param v vector
	 * @param s constant
	 * @return vector multiplicated with a constant
	 */
	public static Vector2 mult (Vector2 v, float s)
	{
		return new Vector2(v.x * s, v.y * s);
	}
	
	
	/**
	 * Dotproduct with a vector
	 * @param v Vector
	 * @return dotproduct of the two vectors
	 */
	public float dotProduct (Vector2 v)
	{
		return (x*v.x + y*v.y);
	}

	
	/**
	 * Add a vector to the current vector
	 * @param v Vector to add
	 */
	public void add (Vector2 v)
	{
		x += v.x;
		y += v.y;
	}

	
	/**
	 * Multilpicate the vector with a constant
	 * @param s constant for multiplication
	 */
	public void mult (float s)
	{
		x *= s;
		y *= s;
	}
	
	
	/**
	 * Get the length of the vector
	 * @return the length of the vector
	 */
	public float length()
	{
		return (float) Math.sqrt (x*x + y*y);
	}

	
	/**
	 * Normalize the vector
	 */
	public void normalize ()
	{
		float len = length();

		if (len != 0) 
		{
			x /= len;
			y /= len;
		}
	}
	
	
	/**
	 * Invert the vector
	 */
	public void invert()
	{
		x = -x;
		y = -y;
	}
	
	
	/**
	 * Get the angle of the vector
	 * @return the angle of the vector
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
		
		angle /= Math.PI;
		angle *= 180;
		
		return angle;
	}
}
