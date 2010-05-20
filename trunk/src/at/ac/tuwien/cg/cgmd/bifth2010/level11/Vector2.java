package at.ac.tuwien.cg.cgmd.bifth2010.level11;

public class Vector2 {

	public float x, y;
	/**
	 * x and y is set to 0
	 */
	public Vector2 () {
		x = 0;
		y = 0;
	}
	/**
	 * vector contructor. sets x and y component to the given parameters
	 * @param x x
	 * @param y y
	 */
	public Vector2 (float x, float y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * sets x and y component to the given parameters
	 * @param x x
	 * @param y y
	 */
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * calculates the distance between the two points specified by this vector and other
	 * @param other other vector interpreted as point
	 * @return distance
	 */
	public float distance(Vector2 other){
		return (float)Math.sqrt(Math.pow(this.x-other.x,2) + Math.pow(this.y-other.y,2));
	}
	/**
	 * normalizes this vector
	 * @return this
	 */
	public Vector2 normalize(){
		float length = this.length();
		this.x /= length;
		this.y /= length;
		return this;
	}
	/**
	 * returns the length of the vector
	 * @return length
	 */
	public float length(){
		return (float)Math.sqrt(Math.pow(this.x, 2)+ Math.pow(this.y,2));
	}
	public Vector2 sub(Vector2 other){
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}
	/**
	 * subtracts this from other and stores result in this
	 * @param other vector from which this is subtracted
	 * @return this
	 */
	public Vector2 subThisFrom(Vector2 other){
		this.x = (other.x - this.x);
		this.y = (other.y - this.y);
		return this;
	}
	/**
	 * adds this to other vector and stores result in this
	 * @param other other vector that is added
	 * @return this
	 */
	public Vector2 add(Vector2 other){
		this.x += other.x;
		this.y += other.y;
		return this;
	}
	/**
	 * multiplicates this with skalar and stores result in this
	 * @param skalar 
	 * @return
	 */
	public Vector2 mult(float skalar){
		this.x *= skalar;
		this.y *= skalar;
		return this;
	}
	/**
	 * copies the value of this and returns a clone
	 */
	public Vector2 clone(){
		Vector2 vec = new Vector2();
		vec.set(this.x, this.y);
		return vec;
	}
	public String toString(){
		return "x: "+x+"; y: "+y+"; ";
	}
}
