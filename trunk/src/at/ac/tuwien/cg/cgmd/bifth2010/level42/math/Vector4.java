package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

/**
 * The Class Vector4.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Vector4
{
	public float x,y,z,w;
	
	/**
	 * Instantiates a new vector4.
	 *
	 * @param xyzw All four fields are set to xyzw
	 */
	public Vector4(float xyzw)
	{
		set(xyzw,xyzw,xyzw,xyzw);
	}
	
	/**
	 * Instantiates a new vector4.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @param w the w
	 */
	public Vector4(float x, float y, float z, float w)
	{
		set(x,y,z,w);
	}
	
	/**
	 * Sets the new values
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @param w the w
	 */
	private void set(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * Instantiates a new vector4, w will be set to 1.0f
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public Vector4(float x, float y, float z)
	{
		this(x,y,z,1);
	}
	
	/**
	 * copy constructor
	 *
	 * @param other the other Vector4
	 */
	public Vector4(Vector4 other)
	{
		this(other.x, other.y, other.z, other.w);
	}
	
	/**
	 * copy constructor, w will be set to 1.0f
	 *
	 * @param other a Vector3 to get x,y,z from
	 */
	public Vector4(Vector3 other)
	{
		this(other.x, other.y, other.z, 1);
	}

	/**
	 * Instantiates a new vector4(0,0,0,1)
	 */
	public Vector4()
	{
		this(0,0,0,1);
	}
	
	/**
	 * Instantiates a new vector4.
	 *
	 * @param arr must be a float[4]
	 */
	public Vector4(float[] arr)
	{
		this(arr[0],arr[1],arr[2],arr[3]);
	}
	
	/**
	 * @return this vector as a float[4]
	 */
	public float[] getArray4()
	{
		return new float[] {x,y,z,w};
	}
	
	/**
	 * Adds another Vector4 to this
	 *
	 * @param other the other Vector4
	 * @return this
	 */
	public Vector4 add(Vector4 other)
	{
		homogenize();
		other = homogenize(other);
		x += other.x;
		y += other.y;
		z += other.z;
		return this;
	}
	
	/**
	 * Adds two Vector4
	 *
	 * @param a the first Vector4
	 * @param b the second Vector4
	 * @return a new Vector4, set to a+b
	 */
	public static Vector4 add(Vector4 a, Vector4 b)
	{
		return new Vector4(a).add(b);
	}
	
	/**
	 * Adds a Vector3 to this
	 *
	 * @param other a Vector3
	 * @return this
	 */
	public Vector4 add(Vector3 other)
	{
		homogenize();
		x += other.x;
		y += other.y;
		z += other.z;
		return this;
	}
	
	/**
	 * Adds a Vector4 and a Vector3
	 *
	 * @param a the Vector4
	 * @param b the Vector3
	 * @return a new Vector4, set to a+b
	 */
	public static Vector4 add(Vector4 a, Vector3 b)
	{
		return new Vector4(a).add(b);
	}
	
	/**
	 * Subtracts a Vector4 from this
	 *
	 * @param other the other Vector4
	 * @return this
	 */
	public Vector4 subtract(Vector4 other)
	{
		homogenize();
		other = homogenize(other);
		x -= other.x;
		y -= other.y;
		z -= other.z;
		return this;
	}
	
	/**
	 * Subtracts to Vector4
	 *
	 * @param a the first Vector4
	 * @param b the second Vector4
	 * @return a new Vector4, set to a-b
	 */
	public static Vector4 subtract(Vector4 a, Vector4 b)
	{
		return new Vector4(a).subtract(b);
	}
	
	/**
	 * Subtracts a Vector3 from this
	 *
	 * @param other the Vector3
	 * @return this
	 */
	public Vector4 subtract(Vector3 other)
	{
		homogenize();
		x -= other.x;
		y -= other.y;
		z -= other.z;
		return this;
	}
	
	/**
	 * Subtract a Vector3 from a Vector4
	 *
	 * @param a the Vector4
	 * @param b the Vector3
	 * @return a new Vector4, set to a-b
	 */
	public static Vector4 subtract(Vector4 a, Vector3 b)
	{
		return new Vector4(a).subtract(b);
	}
	
	/**
	 * Multiply this with a Vector4
	 *
	 * @param other the other Vector4
	 * @return this
	 */
	public Vector4 multiply(Vector4 other)
	{
		homogenize();
		other = homogenize(other);
		x *= other.x;
		y *= other.y;
		z *= other.z;
		return this;
	}
	
	/**
	 * Multiply two Vector4
	 *
	 * @param a the first Vector4
	 * @param b the second Vector4
	 * @return a new Vector4, set to a*b
	 */
	public static Vector4 multiply(Vector4 a, Vector4 b)
	{
		return new Vector4(a).multiply(b);
	}
	
	/**
	 * Multiply this with a Vector3
	 *
	 * @param other the Vector3
	 * @return this
	 */
	public Vector4 multiply(Vector3 other)
	{
		homogenize();
		x *= other.x;
		y *= other.y;
		z *= other.z;
		return this;
	}
	
	/**
	 * Multiply a Vector4 and a Vector3
	 *
	 * @param a the Vector4
	 * @param b the Vector3
	 * @return a new Vector4, set to a*b
	 */
	public static Vector4 multiply(Vector4 a, Vector3 b)
	{
		return new Vector4(a).multiply(b);
	}
	
	/**
	 * Multiply this with a factor
	 *
	 * @param s the factor
	 * @return this
	 */
	public Vector4 multiply(float s)
	{
		homogenize();
		x *= s;
		y *= s;
		z *= s;
		return this;
	}
	
	/**
	 * Divide this by another Vector4
	 *
	 * @param other the other Vector4
	 * @return this
	 */
	public Vector4 divide(Vector4 other)
	{
		homogenize();
		other = homogenize(other);
		x /= other.x;
		y /= other.y;
		z /= other.z;
		return this;
	}
	
	/**
	 * Divide two Vector4
	 *
	 * @param a the first Vector4
	 * @param b the second Vector4
	 * @return a new Vector4, set to a/b
	 */
	public static Vector4 divide(Vector4 a, Vector4 b)
	{
		return new Vector4(a).divide(b);
	}
	
	/**
	 * Divide this by a Vector3
	 *
	 * @param other the Vector3
	 * @return this
	 */
	public Vector4 divide(Vector3 other)
	{
		homogenize();
		x /= other.x;
		y /= other.y;
		z /= other.z;
		return this;
	}
	
	/**
	 * Divide a Vector4 by a Vector3
	 *
	 * @param a the Vector4
	 * @param b the Vector3
	 * @return a new Vector4, set to a/b
	 */
	public static Vector4 divide(Vector4 a, Vector3 b)
	{
		return new Vector4(a).divide(b);
	}
	
	/**
	 * Divide this by a factor
	 *
	 * @param s the the factor
	 * @return this
	 */
	public Vector4 divide(float s)
	{
		homogenize();
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
	public Vector4 normalize()
	{
		homogenize();
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
	 * Normalizes a Vector4
	 *
	 * @param a the Vector4
	 * @return a new Vector4, set to a, normalized.
	 */
	public static Vector4 normalize(Vector4 a)
	{
		return new Vector4(a).normalize();
	}
	
	/**
	 * Homogenizes this
	 *
	 * @return this
	 */
	public Vector4 homogenize()
	{
		if(w != 1.0f)
		{
			x /= w;
			y /= w;
			z /= w;
			w = 1;
		}
		return this;
	}
	
	/**
	 * Homogenizes a Vector4
	 *
	 * @param a the Vector4
	 * @return a new Vector4, set to a, homogenized.
	 */
	public static Vector4 homogenize(Vector4 a)
	{
		return new Vector4(a).homogenize();
	}
	
	/**
	 * @return the length of this Vector4
	 */
	public float length()
	{
		homogenize();
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "(" + x + "," + y + "," + z + "," + w + ")";
	}
}
