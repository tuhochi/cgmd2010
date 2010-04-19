package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

/**
 * The Class AxisAlignedBox3.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class AxisAlignedBox3
{
	public final Vector3 min,max;
	
	/**
	 * Instantiates a new axis aligned box3.
	 */
	public AxisAlignedBox3()
	{
		min = new Vector3();
		max = new Vector3();
	}

	/**
	 * Copy Constructor
	 *
	 * @param other the other
	 */
	public AxisAlignedBox3(AxisAlignedBox3 other)
	{
		min = new Vector3(other.min);
		max = new Vector3(other.max);
	}
	
	/**
	 * Instantiates a new axis aligned box3.
	 *
	 * @param min the min
	 * @param max the max
	 */
	public AxisAlignedBox3(Vector3 min, Vector3 max)
	{
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Gets the volume.
	 *
	 * @return the volume
	 */
	public float getVolume() 
	{
		return (max.x - min.x) * (max.y - min.y) * (max.z - min.z);
	}
	
	/**
	 * Resizes the box to include a Vector3
	 *
	 * @param v the Vector3
	 */
	public void include(Vector3 v)
	{
		minimize(min, v);
		maximize(max, v);
	}
	
	/**
	 * Resizes the box to include box
	 *
	 * @param box the box
	 */
	public void include(AxisAlignedBox3 box)
	{
		minimize(min, box.min);
		maximize(max, box.max);
	}
	
	/**
	 * Checks if a Point is inside.
	 *
	 * @param v the Point
	 * @return true, if Point is inside
	 */
	public boolean isInside(Vector3 v)
	{
		return !((v.x < min.x) ||
			     (v.x > max.x) ||
				 (v.y < min.y) ||
				 (v.y > max.y) ||
				 (v.z < min.z) ||
				 (v.z > max.z));
	}

	/**
	 * Checks if a Box is inside
	 *
	 * @param box the box
	 * @return true, if box is inside
	 */
	public boolean includes(AxisAlignedBox3 box)
	{
		return (box.min.x >= min.x &&
				box.min.y >= min.y &&
				box.min.z >= min.z &&
				box.max.x <= max.x &&
				box.max.y <= max.y &&
				box.max.z <= max.z);
	}
	
	/**
	 * Sets min to the minimum of min and candidate
	 *
	 * @param min will be set to the minimum
	 * @param candidate the candidate
	 */
	void minimize(Vector3 min, Vector3 candidate)
	{
		if (candidate.x < min.x)
			min.x = candidate.x;
		if (candidate.y < min.y)
			min.y = candidate.y;
		if (candidate.z < min.z)
			min.z = candidate.z;
	}

	/**
	 * Sets max to the maximum of max and candidate
	 *
	 * @param max will be set to the maximum
	 * @param candidate the candidate
	 */
	void maximize(Vector3 max, Vector3 candidate)
	{
		if (candidate.x > max.x)
			max.x = candidate.x;
		if (candidate.y > max.y)
			max.y = candidate.y;
		if (candidate.z > max.z)
			max.z = candidate.z;
	}
	
	/**
	 * @return the center of this box
	 */
	Vector3 center()
	{ 
		return new Vector3(0.5f * (min.x+max.x), 0.5f * (min.y+max.y), 0.5f * (min.z+max.z)); 
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "AxisAlignedBox3: min=" + min + ", max=" + max;
	}
}
