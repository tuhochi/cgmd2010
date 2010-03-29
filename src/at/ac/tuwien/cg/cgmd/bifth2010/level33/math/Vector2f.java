package at.ac.tuwien.cg.cgmd.bifth2010.level33.math;

public class Vector2f {

	public float x, y;

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f() {
		set(0, 0);
	}

	public Vector2f(float x, float y) {
		set(x, y);
	}
	public Vector2f add(Vector2f other)
	{
		this.x += other.x;
		this.y += other.y;
		return this;
		
	}
	public Vector2f subtract(Vector2f other)
	{
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}

	public static float dotProduct(Vector2f v1, Vector2f v2) {
		return (v1.x * v2.x + v1.y * v2.y);
	}

	public void normalize() {
		float length = (float) Math.sqrt(x * x + y * y);

		if (length != 0) {
			this.x /= length;
			this.y /= length;
		}
	}
	
	public float area(){
		return x*y;
	}
	
	public float length()
	{
		return (float)Math.sqrt(x*x + y*y);
	}

}
