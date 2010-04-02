package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import java.util.ArrayList;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;

public class CollisionManager {

	private Scene scene;
	private ArrayList<SceneEntity> entityList;
	private SceneEntity entity,nearestEntity;
	
	private Vector3 p; // punkt auf der gerade
	private Vector3 a; // richtungsvektor der gerade
	private Vector3 q; // punkt der auf abstand getestet werden soll
	
	private Vector3 pq;
	private Vector3 normalDistance;
	
	private float minDistance;
	
	public CollisionManager(Scene scene)
	{
		this.scene = scene;
		this.entityList = scene.sceneEntities;
		
		this.p = new Vector3();
		this.q = new Vector3();
		this.a = new Vector3();
		this.pq = new Vector3();
		this.normalDistance = new Vector3();
		
		this.minDistance = 0;
	}
	
	public SceneEntity intersectRay(Vector3 origin,Vector3 direction)
	{
		a.copy(direction);
		a.normalize();
		p.copy(origin);
		
		//reset values
		minDistance = Float.MAX_VALUE;
		nearestEntity = null;

		//iterate over all scene entities and calc the hesse normal form
		for(int i=0;i<entityList.size();i++)
		{
			entity = entityList.get(i);
			//get entity bounding sphere position
			q.copy(entity.getBoundingSphereWorld().center);
			
			//calc vector from q -> p
			pq.copy(p);
			pq.subtract(q);
			
			Vector3.crossProduct(pq,a,normalDistance);
			if(normalDistance.length()<entity.getBoundingSphereWorld().radius)
			{
				Log.d(LevelActivity.TAG,"found hit with " + entity.getName() + " distance="+normalDistance.length()+" sphereradius="+entity.getBoundingSphereWorld().radius);
				
				//only store the one with the min distance
				if(nearestEntity==null)
					nearestEntity = entity;
				
				if(pq.length()<minDistance)
				{
					nearestEntity = entity;
					minDistance = pq.length();
				}
			}
		}
		if(nearestEntity!=null)
			Log.d(LevelActivity.TAG,"ray intersected with "+nearestEntity.getName());
		else
			Log.d(LevelActivity.TAG,"no ray intersection found");
		return nearestEntity;
		
	}
}
