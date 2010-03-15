package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

public class Vector4
{
	public float x,y,z,w;
	
	public Vector4(float x, float y, float z, float w)
	{
		copy(x,y,z,w);
	}
	
	private void copy(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	private void copy(Vector4 other)
	{
		copy(other.x, other.y, other.z, other.w);
	}
	
	public Vector4(float x, float y, float z)
	{
		this(x,y,z,1);
	}
	
	public Vector4(Vector4 other)
	{
		this(other.x, other.y, other.z, other.w);
	}
	
	public Vector4(Vector3 other)
	{
		this(other.x, other.y, other.z, 1);
	}

	public Vector4()
	{
		this(0,0,0,0);
	}
	
	public Vector4 add(Vector4 other)
	{
		homogenize();
		other = homogenize(other);
		x += other.x;
		y += other.y;
		z += other.z;
		return this;
	}
	
	public static Vector4 add(Vector4 a, Vector4 b)
	{
		return new Vector4(a).add(b);
	}
	
	public Vector4 add(Vector3 other)
	{
		homogenize();
		x += other.x;
		y += other.y;
		z += other.z;
		return this;
	}
	
	public static Vector4 add(Vector4 a, Vector3 b)
	{
		return new Vector4(a).add(b);
	}
	
	public Vector4 subtract(Vector4 other)
	{
		homogenize();
		other = homogenize(other);
		x -= other.x;
		y -= other.y;
		z -= other.z;
		return this;
	}
	
	public static Vector4 subtract(Vector4 a, Vector4 b)
	{
		return new Vector4(a).subtract(b);
	}
	
	public Vector4 subtract(Vector3 other)
	{
		homogenize();
		x -= other.x;
		y -= other.y;
		z -= other.z;
		return this;
	}
	
	public static Vector4 subtract(Vector4 a, Vector3 b)
	{
		return new Vector4(a).subtract(b);
	}
	
	public Vector4 multiply(Vector4 other)
	{
		homogenize();
		other = homogenize(other);
		x *= other.x;
		y *= other.y;
		z *= other.z;
		return this;
	}
	
	public static Vector4 multiply(Vector4 a, Vector4 b)
	{
		return new Vector4(a).multiply(b);
	}
	
	public Vector4 multiply(Vector3 other)
	{
		homogenize();
		x *= other.x;
		y *= other.y;
		z *= other.z;
		return this;
	}
	
	public static Vector4 multiply(Vector4 a, Vector3 b)
	{
		return new Vector4(a).multiply(b);
	}
	
	public Vector4 multiply(float s)
	{
		homogenize();
		x *= s;
		y *= s;
		z *= s;
		return this;
	}
	
	public Vector4 divide(Vector4 other)
	{
		homogenize();
		other = homogenize(other);
		x /= other.x;
		y /= other.y;
		z /= other.z;
		return this;
	}
	
	public static Vector4 divide(Vector4 a, Vector4 b)
	{
		return new Vector4(a).divide(b);
	}
	
	public Vector4 divide(Vector3 other)
	{
		homogenize();
		x /= other.x;
		y /= other.y;
		z /= other.z;
		return this;
	}
	
	public static Vector4 divide(Vector4 a, Vector3 b)
	{
		return new Vector4(a).divide(b);
	}
	
	public Vector4 divide(float s)
	{
		homogenize();
		x /= s;
		y /= s;
		z /= s;
		return this;
	}
	
	public Vector4 transform(Matrix44 m)
	{
		Vector4 newV = m.transformPoint(this);
		copy(newV);
		return this;
	}
	
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
	
	public static Vector4 normalize(Vector4 a)
	{
		return new Vector4(a).normalize();
	}
	
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
	
	public static Vector4 homogenize(Vector4 a)
	{
		return new Vector4(a).homogenize();
	}
	
	public float length()
	{
		homogenize();
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
}
