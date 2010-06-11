package at.ac.tuwien.cg.cgmd.bifth2010.level30.math;

//i shamelessly copied this from group 17. thank you! 
//i asked them, they said its ok.

public class Vector3
{
	public float x, y, z;

	public Vector3 ()
	{
		x = 0;
		y = 0;
		z = 0;
	}

	public Vector3 (float _x, float _y, float _z)
	{
		x = _x;
		y = _y;
		z = _z;
	}

	public Vector3 (Vector3 v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	public Vector3 (Vector4 v)
	{
		x = v.x/v.w;
		y = v.y/v.w;
		z = v.z/v.w;
	}

	/**
	 * Return a new vector representing the cross product of two vectors.
	 */

	public static Vector3 crossProduct (Vector3 v1, Vector3 v2)
	{
		return (new Vector3(v1.y*v2.z - v1.z*v2.y, v1.z*v2.x - v1.x*v2.z, v1.x*v2.y - v1.y*v2.x));
	}

	/**
	 * Return the dot product of two vectors.
	 */

	public static float dotProduct (Vector3 v1, Vector3 v2)
	{
		return (v1.x*v2.x + v1.y*v2.y + v1.z*v2.z);
	}

	/**
	 * Return a new vector that is the sum of two vectors.
	 */

	public static Vector3 add (Vector3 v1, Vector3 v2)
	{
		return (new Vector3(v1.x+v2.x, v1.y+v2.y, v1.z+v2.z));
	}

	/**
	 * Return a new vector that points from one point to another.
	 */

	public static Vector3 diff (Vector3 pointEnd, Vector3 pointStart)
	{
		return new Vector3(pointEnd.x-pointStart.x, pointEnd.y-pointStart.y, pointEnd.z-pointStart.z);
	}
	
	public static Vector3 mult (Vector3 v, float s)
	{
		return new Vector3(v.x * s, v.y * s, v.z * s);
	}

	/**
	 * Normalize this vector.
	 */

	public void normalize ()
	{
		float thelen = (float) Math.sqrt (x*x + y*y + z*z);

		if (thelen != 0) 
		{
			x /= thelen;
			y /= thelen;
			z /= thelen;
		}
	}
	
	/**
	 * Linear Interpolation
	 */
	public static Vector3 interpolate (Vector3 p1, Vector3 p2, float t)
	{
		float oneMinusT = 1.0f-t;
		Vector3 res = new Vector3();
		res.x = p1.x*t + p2.x*(oneMinusT);
		res.y = p1.y*t + p2.y*(oneMinusT);
		res.z = p1.z*t + p2.z*(oneMinusT);		
		return res;
		
	}
	
	public float length ()
	{
		return (float) Math.sqrt (x*x + y*y + z*z);
	}
	
	public void invert()
	{
		x = -x;
		y = -y;
		z = -z;
	}

	/**
	 * Debug output.
	 */

	public String toString ()
	{
		return ("(" + x + "," + y + "," + z + ")");
	}
}
