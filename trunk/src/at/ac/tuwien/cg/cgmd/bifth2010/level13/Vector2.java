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
	public int x, y;


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
	public Vector2 (int _x, int _y)
	{
		x = _x;
		y = _y;
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
