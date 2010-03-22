package at.ac.tuwien.cg.cgmd.bifth2010.level33.math;

public class Vector2 {

	public float x, y;

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2() {
		set(0, 0);
	}

	public Vector2(float x, float y) {
		set(x, y);
	}

	public static float dotProduct(Vector2 v1, Vector2 v2) {
		return (v1.x * v2.x + v1.y * v2.y);
	}

	public void normalize() {
		float length = (float) Math.sqrt(x * x + y * y);

		if (length != 0) {
			this.x /= length;
			this.y /= length;
		}
	}

}
