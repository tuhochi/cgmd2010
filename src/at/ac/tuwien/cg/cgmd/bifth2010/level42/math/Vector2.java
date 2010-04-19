package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

/**
 * The Class Vector2.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Vector2
{
	public float x,y;
	
	/**
	 * Instantiates a new vector2.
	 *
	 * @param xy Both fields are set to xy
	 */
	public Vector2(float xy)
	{
		set(xy,xy);
	}
	
	/**
	 * Instantiates a new vector2.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Vector2(float x, float y)
	{
		set(x,y);
	}
	
	/**
	 * Sets the new values
	 *
	 * @param x the x
	 * @param y the y
	 */
	private void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Copy Constructor
	 *
	 * @param other the other Vector2
	 */
	public Vector2(Vector2 other)
	{
		this(other.x, other.y);
	}

	/**
	 * Instantiates a new vector2(0,0)
	 */
	public Vector2()
	{
		this(0,0);
	}
	
	/**
	 * Instantiates a new vector2.
	 *
	 * @param arr must be a float[2]
	 */
	public Vector2(float[] arr)
	{
		this(arr[0],arr[1]);
	}
	
	/**
	 * @return this, as a float[2]
	 */
	public float[] getArray2()
	{
		return new float[] {x,y};
	}
	
	/**
	 * Adds the another Vector2 to this
	 *
	 * @param other the other Vector2
	 * @return this
	 */
	public Vector2 add(Vector2 other)
	{
		x += other.x;
		y += other.y;
		return this;
	}
	
	/**
	 * Adds two Vector2
	 *
	 * @param a the first Vector2
	 * @param b the second Vector2
	 * @return a new Vector2, set to a+b
	 */
	public static Vector2 add(Vector2 a, Vector2 b)
	{
		return new Vector2(a).add(b);
	}
	
	/**
	 * Subtracts another Vector2 from this
	 *
	 * @param other the other Vector2
	 * @return this
	 */
	public Vector2 subtract(Vector2 other)
	{
		x -= other.x;
		y -= other.y;
		return this;
	}
	
	/**
	 * Subtracts two Vector2
	 *
	 * @param a the first Vector2
	 * @param b the second Vector2
	 * @return a new Vector2, set to a-b
	 */
	public static Vector2 subtract(Vector2 a, Vector2 b)
	{
		return new Vector2(a).subtract(b);
	}
	
	/**
	 * Multiply this by another Vector2
	 *
	 * @param other the other Vector2
	 * @return this
	 */
	public Vector2 multiply(Vector2 other)
	{
		x *= other.x;
		y *= other.y;
		return this;
	}
	
	/**
	 * Multiply a Vector2 by a factor
	 *
	 * @param a the Vector2
	 * @param b the factor
	 * @return a new Vector2, set to a*b
	 */
	public static Vector2 multiply(Vector2 a, float b)
	{
		return new Vector2(a).multiply(b);
	}
	
	
	/**
	 * Multiplies two Vector2
	 *
	 * @param a the first Vector2
	 * @param b the second Vector2
	 * @return a new Vector2, set to a*b
	 */
	public static Vector2 multiply(Vector2 a, Vector2 b)
	{
		return new Vector2(a).multiply(b);
	}
	
	/**
	 * Calculates the dot product of two Vector2
	 *
	 * @param a the first Vector2
	 * @param b the second Vector2
	 * @return the dot product of a and b
	 */
	public static float dotProduct(Vector2 a, Vector2 b)
	{
		return (a.x*b.x + a.y*b.y);
	}
	
	/**
	 * Multiplies this by a factor
	 *
	 * @param s the factor
	 * @return this
	 */
	public Vector2 multiply(float s)
	{
		x *= s;
		y *= s;
		return this;
	}
	
	/**
	 * Divides this by another Vector2
	 *
	 * @param other the other Vector2
	 * @return this
	 */
	public Vector2 divide(Vector2 other)
	{
		x /= other.x;
		y /= other.y;
		return this;
	}
	
	/**
	 * Divides two Vector2
	 *
	 * @param a the first Vector2
	 * @param b the second Vector2
	 * @return a new Vector2, set to a/b
	 */
	public static Vector2 divide(Vector2 a, Vector2 b)
	{
		return new Vector2(a).divide(b);
	}
	
	/**
	 * Divides this by a divisor
	 *
	 * @param s the divisor
	 * @return this
	 */
	public Vector2 divide(float s)
	{
		x /= s;
		y /= s;
		return this;
	}
	
	/**
	 * Normalizes this
	 *
	 * @return this
	 */
	public Vector2 normalize()
	{
		float length = length();
		if(length != 0 && length != 1)
		{
			x /= length;
			y /= length;
		}
		return this;
	}
	
	/**
	 * Normalizes a Vector2
	 *
	 * @param a the Vector2
	 * @return a new Vector2, set to a, normalized
	 */
	public static Vector2 normalize(Vector2 a)
	{
		return new Vector2(a).normalize();
	}
	
	/**
	 * @return the length of this
	 */
	public float length()
	{
		return (float)Math.sqrt(x*x + y*y);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "(" + x + "," + y + ")";
	}
}
