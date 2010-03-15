package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

public class Vector3
{
public float x,y,z;
	
	public Vector3(float x, float y, float z)
	{
		copy(x,y,z);
	}
	
	private void copy(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	private void copy(Vector3 other)
	{
		copy (other.x, other.y, other.z);
	}
	
	public Vector3(Vector3 other)
	{
		this(other.x, other.y, other.z);
	}

	public Vector3()
	{
		this(0,0,0);
	}
	
	public Vector3 add(Vector3 other)
	{
		x += other.x;
		y += other.y;
		z += other.z;
		return this;
	}
	
	public static Vector3 add(Vector3 a, Vector3 b)
	{
		return new Vector3(a).add(b);
	}
	
	public Vector3 subtract(Vector3 other)
	{
		x -= other.x;
		y -= other.y;
		z -= other.z;
		return this;
	}
	
	public static Vector3 subtract(Vector3 a, Vector3 b)
	{
		return new Vector3(a).subtract(b);
	}
	
	public Vector3 multiply(Vector3 other)
	{
		x *= other.x;
		y *= other.y;
		z *= other.z;
		return this;
	}
	
	public static Vector3 multiply(Vector3 a, Vector3 b)
	{
		return new Vector3(a).multiply(b);
	}
	
	public Vector3 multiply(float s)
	{
		x *= s;
		y *= s;
		z *= s;
		return this;
	}
	
	public Vector3 divide(Vector3 other)
	{
		x /= other.x;
		y /= other.y;
		z /= other.z;
		return this;
	}
	
	public static Vector3 divide(Vector3 a, Vector3 b)
	{
		return new Vector3(a).divide(b);
	}
	
	public Vector3 divide(float s)
	{
		x /= s;
		y /= s;
		z /= s;
		return this;
	}
	
	public Vector3 transform(Matrix44 m)
	{
		Vector3 newV = m.transformPoint(this);
		copy(newV);
		return this;
	}
	
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
	
	public static Vector3 normalize(Vector3 a)
	{
		return new Vector3(a).normalize();
	}
	
	public float length()
	{
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
}
