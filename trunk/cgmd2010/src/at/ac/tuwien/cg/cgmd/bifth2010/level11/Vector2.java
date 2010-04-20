package at.ac.tuwien.cg.cgmd.bifth2010.level11;

public class Vector2 {

	public float x, y;
	
	public Vector2 () {
		
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float distance(Vector2 other){
		return (float)Math.sqrt(Math.pow(this.x-other.x,2) + Math.pow(this.y-other.y,2));
	}
	public Vector2 clone(){
		Vector2 vec = new Vector2();
		vec.set(this.x, this.y);
		return vec;
	}
}
