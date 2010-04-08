package at.ac.tuwien.cg.cgmd.bifth2010.level11;

public class Vector2 {

	public float x, y;
	
	public Vector2 () {
		
	}
	
	public Vector2 (float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float distance(Vector2 other){
		return (float)Math.sqrt(Math.pow(this.x-other.x,2) + Math.pow(this.y-other.y,2));
	}
	public Vector2 normalize(){
		float length = this.length();
		this.x /= length;
		this.y /= length;
		return this;
	}
	public float length(){
		return (float)Math.sqrt(Math.pow(this.x, 2)+ Math.pow(this.y,2));
	}
	public Vector2 sub(Vector2 other){
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}
	public Vector2 subThisFrom(Vector2 other){
		this.x = (other.x - this.x);
		this.y = (other.y - this.y);
		return this;
	}
	public Vector2 add(Vector2 other){
		this.x += other.x;
		this.y += other.y;
		return this;
	}
	public Vector2 mult(float skalar){
		this.x *= skalar;
		this.y *= skalar;
		return this;
	}
	public Vector2 clone(){
		Vector2 vec = new Vector2();
		vec.set(this.x, this.y);
		return vec;
	}
}
