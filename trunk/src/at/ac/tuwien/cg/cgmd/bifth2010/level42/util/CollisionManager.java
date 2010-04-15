package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import java.util.ArrayList;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.DirectionalMotion;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.MotionManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Moveable;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Orbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Model;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;

public class CollisionManager {

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
	private boolean objAIsMoveable,objBIsMoveable;
	
	public CollisionManager(Scene scene)
	{
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
	
	public boolean collisionDetected(Moveable a, Moveable b, float penetrationDepth, Vector3 centerDist)
	{
		if(centerDist == null)
			centerDist = new Vector3();
		
		//calc distance between center points
		centerDist.copy(b.getBoundingSphereWorld().center);
		centerDist.subtract(a.getBoundingSphereWorld().center);
		
		if(centerDist.length()+ penetrationDepth < 
					a.getBoundingSphereWorld().radius + b.getBoundingSphereWorld().radius)
			return true;
		
		
		return false;		
	}
	public void doCollisionDetection(MotionManager motionManager)
	{
		//for each entity
		for(int i=0; i<entityList.size(); i++) 
		{
			objA = entityList.get(i);	
			objAIsMoveable = (objA.getName().equals(Config.PLANET_NAME))?false:true;
			
			for(int j = i+1; j<entityList.size(); j++)
			{
				objB = entityList.get(j);
				objBIsMoveable = (objB.getName().equals(Config.PLANET_NAME))?false:true;
				
				//check for contact
				if(collisionDetected(objA,objB,Config.COLLISION_PENETRATION_DEPTH,centerDistance))
				{
					//collision detected
					
					//check for collision between satellite and planet
					if(!objAIsMoveable||!objBIsMoveable)
					{
						
						//distinguish entities
						SceneEntity planet = null;
						SceneEntity satellite = null;
						
						if(!objAIsMoveable){
							planet = (SceneEntity) objA;
							satellite = (SceneEntity) objB;
						}else{
							planet = (SceneEntity) objB;
							satellite = (SceneEntity) objA;
						}
						
						Vector3 planetCenterDistance = new Vector3();
						Model planetEntity = null;
						
						//find out which part of the planet got hit
						for(int u = 0; u < planet.models.size(); u++)
						{
							planetEntity = planet.models.get(u);
							
							//check for contact
							if(collisionDetected(planetEntity,satellite,Config.COLLISION_PENETRATION_DEPTH,planetCenterDistance))
							{
								Motion planetEntityMotion = planetEntity.getMotion();
		
								if(planetEntity.getMotion()==null)
								{
									
									//TODO: reuse obj - sat ebenfalls ablenken - sattrafo
									Vector3 pushVec = Vector3.subtract(planetEntity.getBoundingSphereWorld().center, planet.getBoundingSphereWorld().center);
									planetEntityMotion = new DirectionalMotion(planetEntity.getBoundingSphereWorld().center,
															pushVec,
															1f,
															planetEntity.getBasicOrientation());
									motionManager.addMotion(planetEntityMotion,planetEntity);
									satellite.getMotion().morph(pushVec);
								}
							}
						}						
					}else{
						
						objAMotion = objA.getMotion();
						objBMotion = objB.getMotion();
						
						if(objAMotion==null)
						{
							objAMotion = new Orbit(	objA.getBoundingSphereWorld().center,
													Config.UNIVERSE_CENTER,
													Constants.DUMMY_INIT_VEC,
													0.1f,
													null);
							motionManager.addMotion(objAMotion,objA);
						}
						
	
						if(objBMotion==null)
						{
							objBMotion = new Orbit(	objB.getBoundingSphereWorld().center,
													Config.UNIVERSE_CENTER,
													Constants.DUMMY_INIT_VEC,
													0.1f,
													null);
							motionManager.addMotion(objBMotion,objB);
						}
						
						
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
}
