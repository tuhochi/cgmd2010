package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable;

/**
 * The Class Vector3.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Vector3 implements Persistable
{
	public float x,y,z;
	
	/**
	 * Instantiates a new vector3.
	 *
	 * @param xyz All three fields are set to xyz
	 */
	public Vector3(float xyz)
	{
		set(xyz,xyz,xyz);
	}
	
	/**
	 * Instantiates a new vector3.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public Vector3(float x, float y, float z)
	{
		set(x,y,z);
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#persist(java.io.DataOutputStream)
	 */
	public void persist(DataOutputStream dos) throws IOException
	{
		dos.writeFloat(x);
		dos.writeFloat(y);
		dos.writeFloat(z);
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#restore(java.io.DataInputStream)
	 */
	public void restore(DataInputStream dis) throws IOException
	{
		x = dis.readFloat();
		y = dis.readFloat();
		z = dis.readFloat();
	}
	
	/**
	 * Sets the new values
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public Vector3 set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	/**
	 * Sets the new values
	 *
	 * @param other contains the values to set
	 */
	public Vector3 set(Vector3 other)
	{
		return set(other.x, other.y, other.z);
	}
	
	/**
	 * copy constructor
	 *
	 * @param other the other Vector3
	 */
	public Vector3(Vector3 other)
	{
		this(other.x, other.y, other.z);
	}

	/**
	 * Instantiates a new vector3(0,0,0)
	 */
	public Vector3()
	{
		this(0,0,0);
	}
	
	/**
	 * Instantiates a new vector3.
	 *
	 * @param arr must be an array[3]
	 */
	public Vector3(float[] arr)
	{
		this(arr[0],arr[1],arr[2]);
	}
	
	/**
	 * Gets the array3.
	 *
	 * @return this as a float[3]
	 */
	public float[] getArray3()
	{
		return new float[] {x,y,z};
	}
	
	/**
	 * Inverts this
	 *
	 * @return this
	 */
	public Vector3 invert()
	{
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	/**
	 * Adds another Vector3 to this
	 *
	 * @param other the Vector3 to add
	 * @return this
	 */
	public Vector3 add(Vector3 other)
	{
		x += other.x;
		y += other.y;
		z += other.z;
		return this;
	}
	
	/**
	 * Adds two Vector3
	 *
	 * @param a the first Vector3
	 * @param b the second Vector3
	 * @return a new Vector3, set to a+b
	 */
	public static Vector3 add(Vector3 a, Vector3 b)
	{
		return new Vector3(a).add(b);
	}
	
	/**
	 * Subtracts a Vector3 from this
	 *
	 * @param other the Vector3 to subtract
	 * @return this
	 */
	public Vector3 subtract(Vector3 other)
	{
		x -= other.x;
		y -= other.y;
		z -= other.z;
		return this;
	}
	
	/**
	 * Subtract a Vector3 from another
	 *
	 * @param a the first Vector3
	 * @param b the second Vector3
	 * @return a new Vector3, set to a-b
	 */
	public static Vector3 subtract(Vector3 a, Vector3 b)
	{
		return new Vector3(a).subtract(b);
	}
	
	/**
	 * Multiply this with another Vector3
	 *
	 * @param other the Vector3 to multiply
	 * @return this
	 */
	public Vector3 multiply(Vector3 other)
	{
		x *= other.x;
		y *= other.y;
		z *= other.z;
		return this;
	}
	
	/**
	 * Multiplies a Vector3 with a factor
	 *
	 * @param a the Vector3
	 * @param b the factor
	 * @return a new Vector3, set to a*b
	 */
	public static Vector3 multiply(Vector3 a, float b)
	{
		return new Vector3(a).multiply(b);
	}
	
	
	/**
	 * Multiplies two Vector3
	 *
	 * @param a the first Vector3
	 * @param b the second Vector3
	 * @return a new Vector3, set to a*b
	 */
	public static Vector3 multiply(Vector3 a, Vector3 b)
	{
		return new Vector3(a).multiply(b);
	}
	
	/**
	 * Calculates the dot product of two Vector3
	 *
	 * @param a the first Vector3
	 * @param b the second Vector3
	 * @return the dot product of a and b
	 */
	public static float dotProduct (Vector3 a, Vector3 b)
	{
		return (a.x*b.x + a.y*b.y + a.z*b.z);
	}
	
	/**
	 * Calculates the cross product of two Vector3
	 *
	 * @param a the first Vector3
	 * @param b the second Vector3
	 * @return a new Vector3, set to a x b
	 */
	public static Vector3 crossProduct (Vector3 a, Vector3 b)
	{
		Vector3 result = new Vector3();
		crossProduct(a, b, result);
		return result;
	}
	
	/**
	 * Calculates the cross product of two Vector3 and writes it back into a parameter
	 *
	 * @param a the first Vector3
	 * @param b the second Vector3
	 * @param result the result will be written into this parameter
	 */
	public static void crossProduct(Vector3 a, Vector3 b, Vector3 result)
	{
		result.x = a.y*b.z - a.z*b.y;
		result.y = a.z*b.x - a.x*b.z;
		result.z = a.x*b.y - a.y*b.x;
	}
	
	/**
	 * Multiplies this with a factor
	 *
	 * @param s the factor
	 * @return this
	 */
	public Vector3 multiply(float s)
	{
		x *= s;
		y *= s;
		z *= s;
		return this;
	}
	
	/**
	 * Divides this by another Vector3
	 *
	 * @param other the Vector3
	 * @return this
	 */
	public Vector3 divide(Vector3 other)
	{
		x /= other.x;
		y /= other.y;
		z /= other.z;
		return this;
	}
	
	/**
	 * Divides two Vector3
	 *
	 * @param a the first Vector3
	 * @param b the second Vector3
	 * @return a new Vector3, set to a/b
	 */
	public static Vector3 divide(Vector3 a, Vector3 b)
	{
		return new Vector3(a).divide(b);
	}
	
	/**
	 * Divides this by a divisor
	 *
	 * @param s the divisor
	 * @return this
	 */
	public Vector3 divide(float s)
	{
		x /= s;
		y /= s;
		z /= s;
		return this;
	}
	
	/**
	 * Normalizes this
	 *
	 * @return this
	 */
	public Vector3 normalize()
	{
		float length = length();
		if(length != 0 && length != 1)
		{
			x /= length;
			y /= length;
			z /= length;
		}
		return this;
	}
	
	/**
	 * Normalizes a Vector3
	 *
	 * @param a the Vector3
	 * @return a new Vector3, set to a, normalized
	 */
	public static Vector3 normalize(Vector3 a)
	{
		return new Vector3(a).normalize();
	}
	
	/**
	 * Gets the angle between two Vector3
	 *
	 * @param a the first Vector3
	 * @param b the second Vector3
	 * @return the angle between a and b
	 */
	public static float getAngle(Vector3 a, Vector3 b)
	{
		return (float)Math.acos((Vector3.dotProduct(a, b)/(a.length()*b.length())));
	}
	
	/**
	 * @return the length of this
	 */
	public float length()
	{
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "(" + x + "," + y + "," + z + ")";
	}
}
