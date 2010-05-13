package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Sphere.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Sphere
{
	
	/** The center. */
	public final Vector3 center;
	
	/** The radius. */
	public float radius;
	
	/** The temp. */
	private final Vector3 temp;
	
	private final Vector3 temp2;
	
	/**
	 * Instantiates a new sphere with zero radius.
	 */
	public Sphere()
	{
		this(new Vector3(0,0,0), 0);
	}
	
	/**
	 * Copy constructor.
	 *
	 * @param other the other
	 */
	public Sphere(Sphere other)
	{
		this(new Vector3(other.center), other.radius);
	}
	
	/**
	 * Instantiates a new sphere.
	 *
	 * @param center the center
	 * @param radius the radius as a Vector3
	 */
	public Sphere(Vector3 center, Vector3 radius)
	{
		this(center, radius.length());
	}
	
	/**
	 * Instantiates a new sphere.
	 *
	 * @param center the center
	 * @param radius the radius
	 */
	public Sphere(Vector3 center, float radius)
	{
		this.center = center;
		this.radius = radius;
		temp = new Vector3();
		temp2 = new Vector3();
	}
	
	/**
	 * copy method.
	 *
	 * @param other the other
	 */
	public void set(Sphere other)
	{
		this.center.set(other.center);
		this.radius = other.radius;
	}
	
	/**
	 * Checks if is point inside.
	 *
	 * @param point the point
	 * @return true, if is point inside
	 */
	public boolean isPointInside(Vector3 point)
	{
		temp.set(point);
		return temp.subtract(center).length() <= radius;
	}
	
	/**
	 * Calculates this sphere from a set of Vector3s.
	 *
	 * @param vertices the point set
	 */
	public void setPointSet(ArrayList<Vector3> vertices)
	{
		Vector3 centerBB = new Vector3();
		float radiusBB = calcSphereFromBBox(vertices, centerBB);
		
		Vector3 centerC = new Vector3();
		float radiusC = calcSphereFromCentroid(vertices, centerC);
		
		if(radiusBB < radiusC)
		{
			center.set(centerBB);
			radius = radiusBB;
		}
		else
		{
			center.set(centerC);
			radius = radiusC;
		}
	}
	
	/**
	 * Sets the sphere set.
	 *
	 * @param sphereSet the new sphere set
	 */
	public void setSphereSet(ArrayList<Sphere> sphereSet)
	{
		int size = sphereSet.size();
		
		if(size == 1)
		{
			center.set(sphereSet.get(0).center);
			radius = sphereSet.get(0).radius;
			return;
		}
		
		temp.set(0,0,0);
		
		for(int i=0; i<size; i++)
			temp.add(sphereSet.get(i).center);
		
		temp.divide(size);
		
		/*
		 * temp holds the centroid now
		 */
		
		float radius = 0;
		
		for(int i=0; i<size; i++)
		{
			Sphere s = sphereSet.get(i);
			float r = temp2.set(s.center).subtract(temp).length() + s.radius;
			if(r > radius)
				radius = r;
		}

		this.radius = radius;
		this.center.set(temp);
	}
	
	/**
	 * Calculates a sphere with it's center at the bounding boxes center.
	 *
	 * @param vertices the vertices
	 * @param center the center
	 * @return the radius
	 */
	private float calcSphereFromBBox(ArrayList<Vector3> vertices, Vector3 center)
	{
		AxisAlignedBox3 bbox = new AxisAlignedBox3();
		for(Vector3 v : vertices)
			bbox.include(v);
		
		center.set(bbox.center());
		
		float r = 0;
		for(Vector3 v : vertices)
		{
			float temp_r = Vector3.subtract(v, center).length();
			if(temp_r > r)
				r = temp_r;
		}
		return r;
	}
	
	/**
	 * Calculates a sphere with it's center at the vertices centroid.
	 *
	 * @param vertices the vertices
	 * @param center the center
	 * @return the radius
	 */
	private float calcSphereFromCentroid(ArrayList<Vector3> vertices, Vector3 center)
	{
		Vector3 centroid = new Vector3();
		for(Vector3 v : vertices)
			centroid.add(v);
		centroid.divide(vertices.size());
		
		center.set(centroid);
		
		float r = 0;
		for(Vector3 v : vertices)
		{
			float temp_r = Vector3.subtract(v, center).length();
			if(temp_r > r)
				r = temp_r;
		}
		return r;
	}
}
