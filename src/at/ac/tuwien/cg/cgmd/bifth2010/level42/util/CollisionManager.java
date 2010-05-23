package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.DirectionalMotion;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.DirectionalPlanetMotion;
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
public class CollisionManager implements Persistable{

	private Scene scene;
	
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
						  objAPushVec,objBPushVec,planetPushVec,morphPlanetPushVec,
					      toCenterVecA,toCenterVecB;
	private Motion objAMotion,objBMotion;
	
	private Movable objA,objB,goldPlanet,skysphere;
	
	private float minDistance;
	private boolean objAIsMoveable,objBIsMoveable;
	
	public Vector<Model> remainingPlanetParts;
	
	public static CollisionManager instance = new CollisionManager();
	public static NearestEntityComperator comperator;
	
	public GameManager gameManager;
	private MotionManager motionManager;
	private TimeManager timeManager;
	private SoundManager soundManager;
	
	private int collOffset;
	private int collOffsetLimit;

	public CollisionManager()
	{
		this.scene = null;
		this.entityList = null;
		
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
		this.morphPlanetPushVec = new Vector3();
		
		this.toCenterVecA = new Vector3();
		this.toCenterVecB = new Vector3();
			
		this.minDistance = 0;

		this.remainingPlanetParts = new Vector<Model>();
		comperator = new NearestEntityComperator();
				
		this.motionManager = MotionManager.instance;
		this.timeManager = TimeManager.instance;
		this.soundManager = SoundManager.instance;
	}
	
	public void init(Scene scene)
	{
		this.scene = scene;
		this.entityList = scene.sceneEntities;
		
		remainingPlanetParts.clear();
		
		for(int i=entityList.size()-1;i>=0;i--)
		{
			if(entityList.get(i).getName().equals(Config.PLANET_NAME)){
				goldPlanet = entityList.get(i);
				remainingPlanetParts.addAll(entityList.get(i).models);
			}
			if(entityList.get(i).getName().equals(Config.SKYSPHERE_NAME)){
				skysphere = entityList.get(i);
			}
		}
		Collections.sort(remainingPlanetParts, comperator);
		//printRemaingingPlanetParts();
		Log.d(LevelActivity.TAG,"PLANET BSPHERE RADIUS:"+goldPlanet.getBoundingSphereWorld().radius);
		
		this.collOffset = (int)(entityList.size()/2);
		this.collOffsetLimit = entityList.size();
	}
	
	public void initAndSetGameManager(GameManager gameManager)
	{
		gameManager.init(remainingPlanetParts.size());
		this.gameManager = gameManager;
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
		a.set(direction);
		a.normalize();
		p.set(origin);
		
		//reset values
		minDistance = Float.MAX_VALUE;
		nearestEntity = null;

		//iterate over all scene entities and calc the hesse normal form
		for(int i=0;i<entityList.size();i++)
		{
			entity = entityList.get(i);
			
			//exclude inactive entities
			if(entity.isDisabled()||entity.equals(skysphere))
				continue;
			
			//get entity bounding sphere position
			q.set(entity.getBoundingSphereWorld().center);
			
			//calc vector from q -> p
			pq.set(p);
			pq.subtract(q);
			
			Vector3.crossProduct(pq,a,normalDistance);
			if(normalDistance.length() < (entity.getBoundingSphereWorld().radius+Config.SELECTION_BSPHERE_INCREMENT))
			{
				Log.d(LevelActivity.TAG,"found hit with " + entity.getName() + " distance="+normalDistance.length()+" sphereradius="+entity.getBoundingSphereWorld().radius);
				
				//only store the one with the min distance
				if(nearestEntity==null && entity.getName().startsWith(Config.SATELLITE_PREFIX))
					nearestEntity = entity;
				
				if(pq.length()<minDistance && entity.getName().startsWith(Config.SATELLITE_PREFIX));
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
		centerDistance.set(objB.getBoundingSphereWorld().center);
		centerDistance.subtract(objA.getBoundingSphereWorld().center);
		
		if(centerDistance.length()+ penetrationDepth < 
					objA.getBoundingSphereWorld().radius + objB.getBoundingSphereWorld().radius)
			return true;
		
		
		return false;		
	}
	
	/**
	 * Do collision detection between all scene entities in the scene
	 */
	public void doCollisionDetection()
	{		
		int entityListSize = entityList.size();
		int listSize = remainingPlanetParts.size();
		
		if(collOffset==0){
			collOffset = (int)(entityListSize/2);
			collOffsetLimit = entityListSize;
		}else{
			collOffset = 0;
			collOffsetLimit = (int)(entityListSize/2);
		}
		
		//for each entity
		for(int i=collOffset; i<collOffsetLimit; i++) 
		{
			objA = entityList.get(i);	
			
			//only collide with active entities
			if(objA.isDisabled()||objA.equals(skysphere))
				continue;
			
			objAIsMoveable = (objA.equals(goldPlanet))?false:true;
			
			
			for(int j = i+1; j<entityListSize; j++)
			{
				objB = entityList.get(j);
				
				//only collide with active entities
				if(objB.isDisabled()||objB.equals(skysphere))
					continue;
				
				objBIsMoveable = (objB.equals(goldPlanet))?false:true;

				//check for contact
				if(collisionDetected(objA,objB,Config.COLLISION_PENETRATION_DEPTH,centerDistance))
				{
					//collision detected
					//Log.d(LevelActivity.TAG,"COLLISION DETECTED");
					
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
						
						/**
						 * SPECIAL CASE
						 * planet part moves out of the planet -> avoid collision with planet
						 */
						if(satellite.getMotion().getFilterPlanetColl())
							continue;
					
						//set planet entrance flag
						satellite.getMotion().setInsidePlanet(true);
												
						Model planetPart = null;
						
						//find out which part of the planet got hit
						//only search in remaining planet parts
						listSize = remainingPlanetParts.size();
						for(int u = listSize-1; u >=0; u--)
						{
							planetPart = remainingPlanetParts.get(u);
									
							//check for contact
							if(collisionDetected(planetPart,satellite,Config.COLLISION_PENETRATION_DEPTH,planetCenterDistance))
							{
								if(!satellite.getMotion().getPlayedCollSound()){
									playCollSound(satellite);
									satellite.getMotion().setPlayedCollSound(true);
								}
								
								Motion planetPartMotion = planetPart.getMotion();
								
								if(planetPart.getMotion()==null)
								{
									planetPushVec.set(planetPart.getBoundingSphereWorld().center);
									planetPushVec.subtract(Config.UNIVERSE_CENTER);
									
									if(planetPushVec.length()==0)
										planetPushVec.set(Constants.DUMMY_INIT_VEC);
									
									planetPartMotion = new DirectionalPlanetMotion(	planetPart.getBoundingSphereWorld().center,
																					planetPushVec,
																					satellite.getMotion().getSpeed()*Config.PLANETCOLL_SPEED_FROM_SAT_FACTOR,
																					planetPart.getBasicOrientation());
									
									
									if(satellite.getMotion().getSpeed()<Config.MIN_SPEED_FOR_UNDAMPED_DIRECTIONAL){
										morphPlanetPushVec.set(planetPushVec);
										morphPlanetPushVec.normalize();
										morphPlanetPushVec.multiply(satellite.getMotion().getSpeed()*Config.PLANETPART_BOUNCE_FAC);
										satellite.getMotion().morph(morphPlanetPushVec);
									}
									
									motionManager.addMotion(planetPartMotion,planetPart);
									planetPartMotion.setInsidePlanet(true);
									
									//Log.d(LevelActivity.TAG,"PLANET COLL - SAT speed="+satellite.getMotion().getSpeed()+" PLANET speed="+planetPart.getMotion().getSpeed());
									
									//report game manager
									gameManager.incScore();
								}
								
								//delete from aiming list
								remainingPlanetParts.remove(planetPart);
								
								//avoid inter-planet-part-coll
								if(remainingPlanetParts.size()>1){
									planetPartMotion.setFilterPlanetColl(true);
								}									
								
								// schedule for untie
								scene.unTie(planet, planetPart);
							}
						}
						
					}
					else
					{
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
						
						
						objACurrDir.set(objA.getMotion().getCurrDirectionVec()).normalize();
						objBCurrDir.set(objB.getMotion().getCurrDirectionVec()).normalize();
											
						//weight with current speed
						objACurrDir.multiply(objA.getMotion().getSpeed()*0.2f);
						objBCurrDir.multiply(objB.getMotion().getSpeed()*0.2f);
						
						toCenterVecA.set(centerDistance);
						toCenterVecA.normalize().multiply(-objA.getBoundingSphereWorld().radius);
						
						toCenterVecB.set(centerDistance);
						toCenterVecB.normalize().multiply(objB.getBoundingSphereWorld().radius);
						
						objAPushVec.set(objBCurrDir);
						objAPushVec.add(toCenterVecA);

						objBPushVec.set(objACurrDir);
						objBPushVec.add(toCenterVecB);

						if(objAPushVec.length()!=0){
							objA.getMotion().morph(objAPushVec);
							motionManager.changeSatelliteTransformation(objA, objACurrDir, objAPushVec,Config.INTERSATELLITE_SPEEDROTA_RATIO);
						}
						
						if(objBPushVec.length()!=0){
							objB.getMotion().morph(objBPushVec);
							motionManager.changeSatelliteTransformation(objB, objBCurrDir, objBPushVec,Config.INTERSATELLITE_SPEEDROTA_RATIO);					
						}
				}

				//END of contact detection
				}else{
					//special case: sat is not longer "inside the planet"
					Motion motion = objA.getMotion();
					float distance = objA.getCurrentPosition().length();
					
					if(motion !=null && motion.isInsidePlanet()){
				
						if(!motionManager.isPlanetPart(objA)){
							if(distance > Config.TRANSFORMATION_DISTANCE){
								if(motion instanceof DirectionalMotion){
									motionManager.transformDirMotionInOrbit(objA);
									motion.setPlayedCollSound(false);
								}else{
									//orbit collision
									objA.getMotion().setInsidePlanet(false);
									motion.setPlayedCollSound(false);
								}
							}
						}else{
							if(distance > goldPlanet.getBoundingSphereWorld().radius + Config.PLANETPART_REUSE_MINDISTANCE){
								motion.setInsidePlanet(false);
								motion.setFilterPlanetColl(false);
								motion.setPlayedCollSound(false);
							}
						}
						
					}
				}
				
			}
		}
	}
	private void playCollSound(Movable satellite)
	{
		//play sound per sat - planet hit only once
		if(satellite.getMotion().getSpeed()>Config.MIN_SPEED_FOR_UNDAMPED_DIRECTIONAL)
			soundManager.playSound(Config.SOUND_HEAVYIMPACT);
		else
			soundManager.playSound(Config.SOUND_IMPACT);
	}
	
	public Movable getAutoAimEntity()
	{
		//printRemaingingPlanetParts();
		if(remainingPlanetParts.size()>0){	
			//pseudo random selection of next element
			int index = (int)timeManager.getRemainingGameTime()%remainingPlanetParts.size();
			Log.d(LevelActivity.TAG,"RANDOM SELECTION INDEX:"+index);
			return remainingPlanetParts.get(index);
		}else 
			return null;
	}
	
	
	protected class NearestEntityComperator implements Comparator<Movable>{

		@Override
		public int compare(Movable objA, Movable objB) {
			if(objA.getBoundingSphereWorld().center.length() <
					objB.getBoundingSphereWorld().center.length())
				return -1;
			
			if(objA.getBoundingSphereWorld().center.length() ==
					objB.getBoundingSphereWorld().center.length())
				return 0;
			
			return 1;
		}
		
	}


	@Override
	public void persist(DataOutputStream dos) throws IOException {
	}


	private void printRemaingingPlanetParts()
	{
		Log.d(LevelActivity.TAG,"AUTOAIM ----");
		for(int i=0;i<remainingPlanetParts.size();i++)
			Log.d(LevelActivity.TAG,"AUTOAIM: i=" + i + " length=" + remainingPlanetParts.get(i).getBoundingSphereWorld().center.length());
	}

	@Override
	public void restore(DataInputStream dis) throws IOException {
		
		int size = remainingPlanetParts.size();
		for(int u = size-1; u >= 0; u--)
		{
			if(remainingPlanetParts.get(u).getMotion()!=null)
				remainingPlanetParts.remove(u);
		}
		Collections.sort(remainingPlanetParts, comperator);
		printRemaingingPlanetParts();
	}

}
