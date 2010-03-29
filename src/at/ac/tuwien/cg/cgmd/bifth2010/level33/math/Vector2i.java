package at.ac.tuwien.cg.cgmd.bifth2010.level33.math;

public class Vector2i {

	public int x, y;

	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2i() {
		set(0, 0);
	}

	public Vector2i(int x, int y) {
		set(x, y);
	}
	public Vector2i add(Vector2i other)
	{
		this.x += other.x;
		this.y += other.y;
		return this;
		
	}
	public Vector2i subtract(Vector2i other)
	{
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}

	public int area(){
		return x*y;
	}
	

}
