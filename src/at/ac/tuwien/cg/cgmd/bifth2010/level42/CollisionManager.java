package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import java.util.ArrayList;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Moveable;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;

public class CollisionManager {

	private Scene scene;
	private ArrayList<SceneEntity> entityList;
	private SceneEntity entity,nearestEntity;
	
	private final Vector3 p; // punkt auf der gerade
	private final Vector3 a; // richtungsvektor der gerade
	private final Vector3 q; // punkt der auf abstand getestet werden soll
	
	private final Vector3 pq;
	private final Vector3 normalDistance;
	
	private final Vector3 centerDistance,objACurrDir,objBCurrDir,toCenterVecA,toCenterVecB;
	private Motion objAMotion,objBMotion;
	
	private Moveable objA,objB;
	
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
		
		this.centerDistance = new Vector3();
		this.objACurrDir = new Vector3();
		this.objBCurrDir = new Vector3();
		this.toCenterVecA = new Vector3();
		this.toCenterVecB = new Vector3();
		
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
	
	public void doCollisionDetection()
	{
		//for each entity
		for(int i=0; i<entityList.size(); i++) 
		{
			objA = entityList.get(i);	
			for(int j = i+1; j<entityList.size(); j++)
			{
				//detect collision
				objB = entityList.get(j);
				
				//calc distance between center points
				centerDistance.copy(objB.getBoundingSphereWorld().center);
				centerDistance.subtract(objA.getBoundingSphereWorld().center);
				
				if(centerDistance.length()+0.2 < 
						objA.getBoundingSphereWorld().radius + objB.getBoundingSphereWorld().radius)
				{
					objAMotion = objA.getMotion();
					objBMotion = objB.getMotion();
					
					//TODO: generate motion
					if(objAMotion == null || objBMotion == null)
						continue;
					
					objACurrDir.copy(objA.getMotion().getCurrDirectionVec()).normalize();
					objBCurrDir.copy(objB.getMotion().getCurrDirectionVec()).normalize();
										
					//weight with current speed
					objACurrDir.multiply(objA.getMotion().getSpeed()*0.2f);
					objBCurrDir.multiply(objB.getMotion().getSpeed()*0.2f);
					
					toCenterVecA.copy(centerDistance);
					toCenterVecA.normalize().multiply(-objA.getBoundingSphereWorld().radius);
					
					toCenterVecB.copy(centerDistance);
					toCenterVecB.normalize().multiply(objB.getBoundingSphereWorld().radius);
					
					objACurrDir.add(toCenterVecB);
					objBCurrDir.add(toCenterVecA);
					
					objA.getMotion().morph(objBCurrDir);
					objB.getMotion().morph(objACurrDir);
				}
				
			}
		}
	}
}
