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
	public Vector2f(Vector2f v) {
		set(v.x,v.y);
	}

	public Vector2f add(Vector2f other)
	{
		this.x += other.x;
		this.y += other.y;
		return this;
		
	}
	
	@Override
	public String toString() {
		return "x="+x+" y="+y;
	}
	public Vector2f subtract(Vector2f other)
	{
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}
	
	public Vector2f divide(float divide)
	{
		this.x /= divide;
		this.y /= divide;
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
	
	public boolean equals(Vector2f other){
		if(this.x==other.x&&this.y==other.y)
			return true;
		return false;
		
	}
	
	public boolean equals(float x,float y){
		if(this.x==x&&this.y==y)
			return true;
		return false;
		
	}
	
	public float area(){
		return x*y;
	}
	
	public float length()
	{
		return (float)Math.sqrt(x*x + y*y);
	}

	public void set(Vector2f other) {
		set(other.x,other.y);
	}

}
