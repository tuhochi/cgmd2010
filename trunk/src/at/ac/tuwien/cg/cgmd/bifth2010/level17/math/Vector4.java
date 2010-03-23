package at.ac.tuwien.cg.cgmd.bifth2010.level17.math;

public class Vector4
{
	public float x, y, z, w;

	public static final Vector4 origin = new Vector4 (0, 0, 0);

	public Vector4 (float _x, float _y, float _z, float _w)
	{
		x = _x;
		y = _y;
		z = _z;
		w = _w;
	}

	public Vector4 (float _x, float _y, float _z)
	{
		x = _x;
		y = _y;
		z = _z;
		w = 1.0f;
	}

	public Vector4 (Vector4 v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
		w = v.w;
	}

	public Vector4 (Vector3 v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
		w = 1.0f;
	}

	public Vector4 ()
	{
		x = 0;
		y = 0;
		z = 0;
		w = 1.0f;
	}

	/**
	 * Return a new vector representing the cross product of two vectors.
	 */

	public static Vector4 crossProduct (Vector4 v1, Vector4 v2)
	{
		return (new Vector4(v1.y*v2.z - v1.z*v2.y, v1.z*v2.x - v1.x*v2.z, v1.x*v2.y - v1.y*v2.x));
	}

	/**
	 * Return the dot product of two vectors.
	 */

	public static float dotProduct (Vector4 v1, Vector4 v2)
	{
		return (v1.x*v2.x + v1.y*v2.y + v1.z*v2.z + v1.w*v2.w);
	}

	/**
	 * Return a new vector that is the sum of two vectors.
	 */

	public static Vector4 add (Vector4 v1, Vector4 v2)
	{
		return (new Vector4(v1.x+v2.x, v1.y+v2.y, v1.z+v2.z, v1.w+v2.w));
	}

	/**
	 * Return a new vector that points from one point to another.
	 */

	public static Vector4 diff (Vector4 pointStart, Vector4 pointEnd)
	{
		return new Vector4(pointEnd.x-pointStart.x, pointEnd.y-pointStart.y, pointEnd.z-pointStart.z, pointEnd.w-pointStart.w);
	}
	
	public static Vector4 mult (Vector4 v, float s)
	{
		return new Vector4(v.x * s, v.y * s, v.z * s, v.w * s);
	}

	/**
	 * Normalize this vector.
	 */

	public void normalize ()
	{
		float thelen = (float) Math.sqrt (x*x + y*y + z*z + w*w);

		if (thelen != 0) 
		{
			x /= thelen;
			y /= thelen;
			z /= thelen;
			w /= thelen;
		}
	}
	
	public void invert()
	{
		x = -x;
		y = -y;
		z = -z;
		w = -w;
	}

	public String toString ()
	{
		return ("(" + x + "," + y + "," + z + "," + w + ")");
	}

	/**
	 * Debug output.
	 */

	public void print ()
	{
		System.out.println (this.toString());
	}
	
	public void homogenize()
	{
		x = x/w;
		y = y/w;
		z = z/w;
		w = 1.0f;
	}
}
