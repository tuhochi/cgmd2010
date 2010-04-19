package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import java.util.ArrayList;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.DirectionalMotion;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.MotionManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Movable;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Orbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Model;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;

/**
 * This Manager handles the selection and collisions between objects
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class CollisionManager {

	private ArrayList<SceneEntity> entityList;
	private SceneEntity entity,nearestEntity;
	
	/** The point on the line */
	private final Vector3 p;
	
	/** The direction vector of the line */
	private final Vector3 a;
	
	/** The point which distance should be evaluated */
	private final Vector3 q;
	
	private final Vector3 pq;
	private final Vector3 normalDistance;
	
	private final Vector3 centerDistance, planetCenterDistance,
						  objACurrDir,objBCurrDir,
						  objAPushVec,objBPushVec,planetPushVec,
					      toCenterVecA,toCenterVecB;
	private Motion objAMotion,objBMotion;
	
	private Movable objA,objB;
	
	private float minDistance;
	private boolean objAIsMoveable,objBIsMoveable;
	
	
	/**
	 * Instantiates a new collision manager.
	 * @param scene the scene
	 */
	public CollisionManager(Scene scene)
	{
		this.entityList = scene.sceneEntities;
		
		this.p = new Vector3();
		this.q = new Vector3();
		this.a = new Vector3();
		this.pq = new Vector3();
		this.normalDistance = new Vector3();
		
		this.centerDistance = new Vector3();
		this.planetCenterDistance = new Vector3();
		
		this.objACurrDir = new Vector3();
		this.objBCurrDir = new Vector3();
		
		this.objAPushVec = new Vector3();
		this.objBPushVec = new Vector3();
		this.planetPushVec = new Vector3();
		
		this.toCenterVecA = new Vector3();
		this.toCenterVecB = new Vector3();
		
		this.minDistance = 0;
	}
	
	/**
	 * Shoot a ray through the scene and detect nearest intersection with the 
	 * objects
	 *
	 * @param origin the origin of the ray
	 * @param direction the ray direction
	 * @return the nearest SceneEntity intersected by the ray
	 */
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
	
	/**
	 * Detect the collision between <code>objA</code> and <code>objB</code> 
	 *
	 * @param objA the object a
	 * @param objB the object b
	 * @param penetrationDepth the depth that the objects has to penetrate 
	 * each other
	 * @param centerDistance the vector between the center of 
	 * the bounding spheres of each object
	 * @return true, if there is a collision otherwise false
	 */
	private static boolean collisionDetected(Movable objA, Movable objB, float penetrationDepth, Vector3 centerDistance)
	{
		if(centerDistance == null)
			centerDistance = new Vector3();
		
		//calc distance between center points
		centerDistance.copy(objB.getBoundingSphereWorld().center);
		centerDistance.subtract(objA.getBoundingSphereWorld().center);
		
		if(centerDistance.length()+ penetrationDepth < 
					objA.getBoundingSphereWorld().radius + objB.getBoundingSphereWorld().radius)
			return true;
		
		
		return false;		
	}
	
	
	
	/**
	 * Do collision detection between all objects in the scene
	 */
	public void doCollisionDetection()
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
									
									//TODO: sat ebenfalls ablenken? - sattrafo
									planetPushVec.copy(planetEntity.getBoundingSphereWorld().center);
									planetPushVec.subtract(planet.getBoundingSphereWorld().center);
									
									planetEntityMotion = new DirectionalMotion(	planetEntity.getBoundingSphereWorld().center,
																				planetPushVec,
																				1f,
																				planetEntity.getBasicOrientation());
									MotionManager.instance.addMotion(planetEntityMotion,planetEntity);
									satellite.getMotion().morph(planetPushVec);
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
							MotionManager.instance.addMotion(objAMotion,objA);
						}
						
	
						if(objBMotion==null)
						{
							objBMotion = new Orbit(	objB.getBoundingSphereWorld().center,
													Config.UNIVERSE_CENTER,
													Constants.DUMMY_INIT_VEC,
													0.1f,
													null);
							MotionManager.instance.addMotion(objBMotion,objB);
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
						
						objAPushVec.copy(objBCurrDir);
						objAPushVec.add(toCenterVecA);

						objBPushVec.copy(objACurrDir);
						objBPushVec.add(toCenterVecB);

						objA.getMotion().morph(objAPushVec);
						objB.getMotion().morph(objBPushVec);
						
						MotionManager.instance.changeSatelliteTransformation(objA, objACurrDir, objAPushVec,Config.INTERSATELLITE_SPEEDROTA_RATIO);
						MotionManager.instance.changeSatelliteTransformation(objB, objBCurrDir, objBPushVec,Config.INTERSATELLITE_SPEEDROTA_RATIO);					
					}
				}
				
			}
		}
	}
}
