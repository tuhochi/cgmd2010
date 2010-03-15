package at.ac.tuwien.cg.cgmd.bifth2010.level42;

public class Vector4
{
	public float x,y,z,w;
	
	public Vector4(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
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
		other.homogenize();
		x += other.x;
		y += other.y;
		z += other.z;
		return this;
	}
	
	public Vector4 add(Vector3 other)
	{
		homogenize();
		x += other.x;
		y += other.y;
		z += other.z;
		return this;
	}
	
	public Vector4 subtract(Vector4 other)
	{
		homogenize();
		other.homogenize();
		x -= other.x;
		y -= other.y;
		z -= other.z;
		return this;
	}
	
	public Vector4 subtract(Vector3 other)
	{
		homogenize();
		x -= other.x;
		y -= other.y;
		z -= other.z;
		return this;
	}
	
	public Vector4 multiply(Vector4 other)
	{
		homogenize();
		other.homogenize();
		x *= other.x;
		y *= other.y;
		z *= other.z;
		return this;
	}
	
	public Vector4 multiply(Vector3 other)
	{
		homogenize();
		x *= other.x;
		y *= other.y;
		z *= other.z;
		return this;
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
		other.homogenize();
		x /= other.x;
		y /= other.y;
		z /= other.z;
		return this;
	}
	
	public Vector4 divide(Vector3 other)
	{
		homogenize();
		x /= other.x;
		y /= other.y;
		z /= other.z;
		return this;
	}
	
	public Vector4 divide(float s)
	{
		homogenize();
		x /= s;
		y /= s;
		z /= s;
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
	
	public float length()
	{
		homogenize();
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
}
