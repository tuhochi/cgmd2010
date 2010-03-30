package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

public class AxisAlignedBox3
{
	public final Vector3 min,max;
	
	public AxisAlignedBox3()
	{
		min = new Vector3();
		max = new Vector3();
	}

	public AxisAlignedBox3(AxisAlignedBox3 other)
	{
		min = new Vector3(other.min);
		max = new Vector3(other.max);
	}
	
	public AxisAlignedBox3(Vector3 min, Vector3 max)
	{
		this.min = min;
		this.max = max;
	}
	
	public float getVolume() 
	{
		return (max.x - min.x) * (max.y - min.y) * (max.z - min.z);
	}
	
	public void include(Vector3 v)
	{
		minimize(min, v);
		maximize(max, v);
	}
	
	public void include(AxisAlignedBox3 box)
	{
		minimize(min, box.min);
		maximize(max, box.max);
	}
	
	public boolean isInside(Vector3 v)
	{
		return !((v.x < min.x) ||
			     (v.x > max.x) ||
				 (v.y < min.y) ||
				 (v.y > max.y) ||
				 (v.z < min.z) ||
				 (v.z > max.z));
	}

	public boolean includes(AxisAlignedBox3 box)
	{
		return (box.min.x >= min.x &&
				box.min.y >= min.y &&
				box.min.z >= min.z &&
				box.max.x <= max.x &&
				box.max.y <= max.y &&
				box.max.z <= max.z);
	}
	
	void minimize(Vector3 min, Vector3 candidate)
	{
		if (candidate.x < min.x)
			min.x = candidate.x;
		if (candidate.y < min.y)
			min.y = candidate.y;
		if (candidate.z < min.z)
			min.z = candidate.z;
	}

	void maximize(Vector3 max, Vector3 candidate)
	{
		if (candidate.x > max.x)
			max.x = candidate.x;
		if (candidate.y > max.y)
			max.y = candidate.y;
		if (candidate.z > max.z)
			max.z = candidate.z;
	}
	
	Vector3 center()
	{ 
		return new Vector3(0.5f * (min.x+max.x), 0.5f * (min.y+max.y), 0.5f * (min.z+max.z)); 
	}
	
	@Override
	public String toString()
	{
		return "AxisAlignedBox3: min=" + min + ", max=" + max;
	}
}
