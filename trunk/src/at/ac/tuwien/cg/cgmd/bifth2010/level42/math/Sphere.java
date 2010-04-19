package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

import java.util.ArrayList;

/**
 * The Class Sphere.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Sphere
{
	public final Vector3 center;
	public float radius;
	
	private final Vector3 temp;
	
	/**
	 * Instantiates a new sphere with zero radius
	 */
	public Sphere()
	{
		this(new Vector3(0,0,0), 0);
	}
	
	/**
	 * Copy constructor
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
	}
	
	/**
	 * Includes a Sphere into this
	 *
	 * @param boundingSphere the bounding sphere
	 */
	public void include(Sphere boundingSphere)
	{
		// temp = vector between centers
		temp.x = boundingSphere.center.x - center.x;
		temp.y = boundingSphere.center.y - center.y;
		temp.z = boundingSphere.center.z - center.z;
		
		float distanceBetweenCenters = temp.length();
		

		if(distanceBetweenCenters != 0)	// spheres have different centers
		{
			// radius of the resulting sphere
			float newRadius = (distanceBetweenCenters + boundingSphere.radius + radius)/2.0f;
			
			// temp = radius of this sphere in direction of the two centers
			temp.normalize();
			temp.multiply(radius);
			// center = point on hull of this sphere on the line containing both centers
			center.subtract(temp);
			
			// temp = radius of the resulting sphere
			temp.normalize();
			temp.multiply(newRadius);
			
			// center = center of the resulting sphere
			center.add(temp);
			radius = newRadius;
		}
		else	// both spheres share the same center
		{
			radius = Math.max(radius, boundingSphere.radius);
		}
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
	 * Calculates this sphere from a set of Vector3s
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
	 * Calculates a sphere with it's center at the bounding boxes center
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
	 * Calculates a sphere with it's center at the vertices centroid
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
