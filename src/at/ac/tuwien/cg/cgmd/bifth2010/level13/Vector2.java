package at.ac.tuwien.cg.cgmd.bifth2010.level13;

/**
 * 
 * @author group13
 *
 * garbage collector efficient 2d vector class
 * memory will only be allocated in the clone method
 *
 */

public class Vector2
{
	/** x/y value */
	public float x, y;


	/**
	 * constructor initializes members with default values
	 * 
	 * @return Vector2
	 */
	public Vector2 ()
	{
		x = 0;
		y = 0;
	}

	/**
	 * constructor initializes members
	 * 
	 * @param _x value x should be set to
	 * @param _y value y should be set to
	 * 
	 * @return Vector2
	 */
	public Vector2 (float _x, float _y)
	{
		x = _x;
		y = _y;
	}

	/**
	 * constructor for copying a vector
	 * 
	 * @param v vector to be copied
	 * 
	 * @return Vector2
	 */
	public Vector2 (Vector2 v)
	{
		x = v.x;
		y = v.y;
	}


	/**
	 * setter for x/y-coordinate
	 * 
	 * @param x value of x-coordinate
	 * @param y value of y-coordinate
	 */
	public void setXY(float x, float y){
		this.x = x;
		this.y = y;
	}

	/**
	 * calculates dot-product
	 * 
	 * @param v1 argument of dot-product
	 * 
	 * @return result of dot-product
	 */
	public float dotProduct (Vector2 v1)
	{
		return x*v1.x + y*v1.y;
	}


	/**
	 * adds a vector to this vector
	 * 
	 * @param v1 vector to be added
	 */
	public void add (Vector2 v1)
	{
		this.x+=v1.x;
		this.y+=v1.y;
	}


	/**
	 * subtracts a vector from this vector
	 * 
	 * @param other vector which should be subtracted
	 */
	public void sub (Vector2 other)
	{
		this.x -= other.x;
		this.y -= other.y;
	}


	/**
	 * multiplies a vector with this vector
	 * 
	 * @param s operand of multiply
	 */
	public void mult (float s)
	{
		this.x *=s;
		this.y *=s;
	}


	/**
	 * calculates the length of this vector
	 * 
	 * @return length of vector
	 */
	public float length()
	{
		return (float) Math.sqrt (x*x + y*y);
	}


	/**
	 * normalizes this vector
	 */
	public void normalize ()
	{
		float thelen = (float) Math.sqrt (x*x + y*y);
		if (thelen != 0) 
		{
			x /= thelen;
			y /= thelen;
		}
	}


	/**
	 * inverts this vector
	 */
	public void invert()
	{
		x = -x;
		y = -y;
	}


	/**
	 * calculates vector angle
	 * 
	 * @return angle of vector
	 */
	public float getAngle()
	{
		float angle = 0;
		if(x < 0) {
			angle = (float)(Math.atan(y / x) + Math.PI);
		}
		else if(y < 0) {
			angle = (float)(Math.atan(y / x) + 2.0 * Math.PI);
		}
		else {
			angle = (float)Math.atan(y / x);
		}

		return angle;
	}


	/**
	 * clones this vector
	 * 
	 * @return cloned vector
	 */
	public Vector2 clone(){
		Vector2 evilTwin = new Vector2();
		evilTwin.x = x;
		evilTwin.y = y;
		return evilTwin;

	}

	/**
	 * get sign of x-coordinate
	 * 
	 * @return 1 if x >= 0, else -1
	 */
	public int signX(){
		if(this.x >= 0) {
			return 1;
		}
		else {
			return -1;
		}
	}


	/**
	 * get sign of y-coordinate
	 * 
	 * @return 1 if y >= 0, else -1
	 */
	public int signY(){
		if(this.y >= 0) {
			return 1;
		}
		else {
			return -1;
		}
	}



	/**
	 * creates string representation of vector
	 * 
	 * @return string representation of this vector
	 */

	@Override
	public String toString ()
	{
		return ("(" + x + "," + y + ")");
	}
}
