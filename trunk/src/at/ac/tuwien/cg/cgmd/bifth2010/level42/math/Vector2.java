package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

public class Vector2
{
	public float x,y;

	public Vector2(float xy)
	{
		copy(xy,xy);
	}
	
	public Vector2(float x, float y)
	{
		copy(x,y);
	}
	
	private void copy(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 other)
	{
		this(other.x, other.y);
	}

	public Vector2()
	{
		this(0,0);
	}
	
	public Vector2 add(Vector2 other)
	{
		x += other.x;
		y += other.y;
		return this;
	}
	
	public static Vector2 add(Vector2 a, Vector2 b)
	{
		return new Vector2(a).add(b);
	}
	
	public Vector2 subtract(Vector2 other)
	{
		x -= other.x;
		y -= other.y;
		return this;
	}
	
	public static Vector2 subtract(Vector2 a, Vector2 b)
	{
		return new Vector2(a).subtract(b);
	}
	
	public Vector2 multiply(Vector2 other)
	{
		x *= other.x;
		y *= other.y;
		return this;
	}
	
	public static Vector2 multiply(Vector2 a, float b)
	{
		return new Vector2(a).multiply(b);
	}
	
	
	public static Vector2 multiply(Vector2 a, Vector2 b)
	{
		return new Vector2(a).multiply(b);
	}
	
	public static float dotProduct(Vector2 a, Vector2 b)
	{
		return (a.x*b.x + a.y*b.y);
	}
	
	public Vector2 multiply(float s)
	{
		x *= s;
		y *= s;
		return this;
	}
	
	public Vector2 divide(Vector2 other)
	{
		x /= other.x;
		y /= other.y;
		return this;
	}
	
	public static Vector2 divide(Vector2 a, Vector2 b)
	{
		return new Vector2(a).divide(b);
	}
	
	public Vector2 divide(float s)
	{
		x /= s;
		y /= s;
		return this;
	}
	
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
	
	public static Vector2 normalize(Vector2 a)
	{
		return new Vector2(a).normalize();
	}
	
	public float length()
	{
		return (float)Math.sqrt(x*x + y*y);
	}
	
	@Override
	public String toString()
	{
		return "(" + x + "," + y + ")";
	}
}
