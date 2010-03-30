package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;

public class Sphere
{
	public final Vector3 center;
	public final Vector3 radius;
	
	private Vector3 temp;
	
	public Sphere()
	{
		this(new Vector3(0,0,0), new Vector3(0,0,0));
	}
	
	public Sphere(Sphere other)
	{
		center = new Vector3(other.center);
		radius = new Vector3(other.radius);
	}
	
	public Sphere(Vector3 center, float radius)
	{
		this(center,new Vector3(1,0,0).multiply(radius));
	}
	
	public Sphere(Vector3 center, Vector3 radius)
	{
		this.center = center;
		this.radius = radius;
		temp = new Vector3();
	}
	
	public float getRadius()
	{
		return radius.length();
	}

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
			float newRadius = (distanceBetweenCenters + boundingSphere.getRadius() + getRadius())/2.0f;
			
			// temp = radius of this sphere in direction of the two centers
			temp.normalize();
			temp.multiply(getRadius());
			// center = point on hull of this sphere on the line containing both centers
			center.subtract(temp);
			
			// temp = radius of the resulting sphere
			temp.normalize();
			temp.multiply(newRadius);
			radius.copy(temp);
			
			// center = center of the resulting sphere
			center.add(radius);
		}
		else	// both spheres share the same center
		{
			radius.normalize().multiply(Math.max(getRadius(), boundingSphere.getRadius()));
		}
	}
}
