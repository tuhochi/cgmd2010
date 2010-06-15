package at.ac.tuwien.cg.cgmd.bifth2010.level11;

/**
 * 2 dimensional float vector. all operations are performed on the calling vector.
 * when you do not want to modify the calling vector, you have to clone it first, 
 * @author g11
 *
 */
public class Vector2 {
	/**
	 * first and second component of the vector
	 */
	public float x, y;
	
	/**
	 * Default vector, x and y is set to 0
	 */
	public Vector2 () {
		x = 0.0f;
		y = 0.0f;
	}
	/**
	 * Vector constructor. Sets x and y component to the given parameters
	 * @param x x
	 * @param y y
	 */
	public Vector2 (float x, float y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Sets x and y component to the given parameters
	 * @param x x
	 * @param y y
	 */
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Return the calculated distance between the two points specified by this vector and another
	 * @param other other vector interpreted as point
	 * @return distance
	 */
	public float distance(Vector2 other){
		return (float)Math.sqrt(Math.pow(this.x-other.x,2) + Math.pow(this.y-other.y,2));
	}
	/**
	 * Returns the normalized vector
	 * @return this
	 */
	public Vector2 normalize(){
		float length = this.length();
		this.x /= length;
		this.y /= length;
		return this;
	}
	/**
	 * Returns the length of the vector
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
	 * Subtracts this from other and stores result in this
	 * @param other vector from which this is subtracted
	 * @return this
	 */
	public Vector2 subThisFrom(Vector2 other){
		this.x = (other.x - this.x);
		this.y = (other.y - this.y);
		return this;
	}
	/**
	 * Adds this to other vector and stores result in this
	 * @param other other vector that is added
	 * @return this
	 */
	public Vector2 add(Vector2 other){
		this.x += other.x;
		this.y += other.y;
		return this;
	}
	/**
	 * Multiplicates this with skalar and stores result in this
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
	/**
	 * vector values to string
	 */
	public String toString(){
		return "x: "+x+"; y: "+y+"; ";
	}
}
