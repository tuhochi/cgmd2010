package at.ac.tuwien.cg.cgmd.bifth2010.level33.math;

/**
 * The class Vector3f
 * @author roman hochstoger & christoph fuchs
 */
public class Vector3f {

	public float x, y, z; // float value of the vector

	public Vector3f(float x, float y, float z) {
		set(x, y, z);
	}

	/**
	 * this constructor initialize the vector with the given floats
	 * @param x		x-value
	 * @param y		y-value
	 * @param z		z-value
	 */
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;

	}
	
	/**
	 * overridden toString method for easy debugging
	 */
	public String toString(){
		
		return String.valueOf(x+" "+y+" "+z);
	}

	/**
	 * this translation method add an other vector, it will change the values of this vector
	 * @param translation	vector that will be added to this
	 */
	public void translate(Vector3f translation) {
		this.x+=translation.x;
		this.y+=translation.y;
		this.z+=translation.z;
	}

}
