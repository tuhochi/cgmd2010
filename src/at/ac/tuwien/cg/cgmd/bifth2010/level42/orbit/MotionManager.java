package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.util.ArrayList;
import java.util.Random;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;

/**
 * The MotionManager handles the linking between objects 
 * (entities) and their motion
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class MotionManager {
	
	private final ArrayList<Movable> list;
	
	/** Manager Singleton */
	public static final MotionManager instance = new MotionManager();
	
	//temp vars
	private final Vector3 satTransformAxis,tempDirectionVec,tempPushVec;
	private final Matrix44 tempBasicOrientation;
	private Movable tempEntity;
	
	/**
	 * Instantiates a new motion manager.
	 */
	private MotionManager()
	{
		list =  new ArrayList<Movable>();
		satTransformAxis = new Vector3();
		tempDirectionVec = new Vector3();
		tempPushVec = new Vector3();
		tempBasicOrientation = new Matrix44();
	}
	
	/**
	 * Register and link a movable object/entity to a motion
	 * @param motion the motion of the object
	 * @param entity the object
	 */
	public void addMotion(Motion motion,Movable entity)
	{
		int index = list.indexOf(entity);
		if(index==-1){
			entity.setMotion(motion);	
			//TODO: reuse obj
			if(motion.getSatTrans()==null)
				motion.setSatTrans(new VecAxisTransformation(Constants.Y_AXIS,(float)Math.PI,5,null));
			motion.setTransform(entity.getTransformation());
			list.add(entity);
		}
	}
	
	
	public void setMotion(Motion motion,Movable entity)
	{
		int index = list.indexOf(entity);
		if(index==-1){
			addMotion(motion,entity);
		}else{
			Motion oldMotion = entity.getMotion();
			entity.setMotion(motion);	
			motion.setSatTrans(oldMotion.getSatTrans());
			motion.setTransform(entity.getTransformation());
		}
	}
	
	/**
	 * Gets the motion of an entity
	 *
	 * @param index the index in the list
	 * @return the linked motion
	 */
	public Movable getMotion(int index)
	{
		return list.get(index);
	}
	
	/**
	 * Gets the motion of an entity
	 *
	 * @param entity the entity reference
	 * @return the linked motion
	 */
	public Movable getMotion(Movable entity)
	{
		return list.get(list.indexOf(entity));
	}
	
	
	/**
	 * Update all registered motions (next iteration step)
	 * @param dt the delta time between frames for a frame-independent motion
	 */
	public void updateMotion(float dt)
	{
		for(int i=0; i<list.size();i++)
		{
			tempEntity = list.get(i);
			tempEntity.getMotion().update(dt);
		}
	}
	
	/**
	 * Clear the list of movable objects and their motions
	 */
	public void reset()
	{
		list.clear();
	}
	
	/**
	 * Change the satellite transformation
	 * @param entity object holding the satellite transformation
	 * @param directionVec the direction vector at the current position
	 * @param pushVec the representing change of the transformation
	 * @param speedRotationRatio the speed/rotation ratio for speed limiting
	 */
	public void changeSatelliteTransformation(	Movable entity,Vector3 directionVec,
												Vector3 pushVec, float speedRotationRatio)
	{
		//generate new rotation axis
		Vector3.crossProduct(directionVec, pushVec, satTransformAxis);
		satTransformAxis.normalize();
		
		
		SatelliteTransformation currSatTransform = (VecAxisTransformation)entity.getMotion().getSatTrans();
		VecAxisTransformation vecSatTransform;
		
		if(currSatTransform!=null && currSatTransform instanceof VecAxisTransformation)
		{
			vecSatTransform = (VecAxisTransformation)currSatTransform;
			tempBasicOrientation.copy(vecSatTransform.getTransform());
			
			//set current transformation (rotation) as basic orientation
			vecSatTransform.setBasicOrientation(tempBasicOrientation);
			//set new rotation axis
			vecSatTransform.axis.set(satTransformAxis);
	
			tempDirectionVec.set(directionVec).normalize();
			tempPushVec.set(pushVec).normalize();
		
			//set the new rotation angle
			vecSatTransform.setAngle(Vector3.getAngle(tempDirectionVec, tempPushVec),speedRotationRatio);
			
			//Log.d(LevelActivity.TAG,"satAngle="+vecSatTransform.qv);
		}
	}
	
	
	/**
	 * Generate random orbits for each satellite in the scene
	 *
	 * @param scene the hole scene
	 * @param minSpeed the minimal speed of the satellites
	 * @param maxSpeed the maximal speed of the satellites
	 * @param minQx the minimal x - axis rotation
	 * @param maxQx the maximal x - axis rotation
	 * @param minQz the minimal z - axis rotation
	 * @param maxQz the maximal z - axis rotation
	 * @param minDistance the minimal distance of the satellites
	 * @param maxDistance the maximal distance of the satellites
	 * @param minDistanceRatio the minimal distance ratio
	 * @param maxDistanceRatio the maximal distance ratio
	 */
	public void generateRandomOrbit(	Scene scene,
										float minSpeed,float maxSpeed,
										float minQx,float maxQx,
										float minQz,float maxQz,
										int minDistance,int maxDistance,
										float minDistanceRatio, float maxDistanceRatio)
	{
		Matrix44 rotation = new Matrix44();
		SceneEntity entity = null;
		Random rand = new Random();
		Vector3 a = new Vector3();
		Vector3 b = new Vector3();
		Vector3 center = new Vector3(0,0,0);
		Vector3 rotationAxis = new Vector3();
		Orbit generatedOrbit = null;
		
		VecAxisTransformation satTransform = null;
		
		for(int i=0;i<scene.sceneEntities.size();i++){
			entity = scene.sceneEntities.get(i);
			if(entity.getName().startsWith("Satellite_")){
				
				//generate the orthonormal axis for the ellipse
				a.x = ((float)rand.nextDouble()*(maxDistance-minDistance) + minDistance);
				a.x = (rand.nextBoolean())? a.x*-1 : a.x;
				
				/** generate a similar value for b over the <code>distanceRatio</code> */
				b.z = a.x *((float)rand.nextDouble()*(maxDistanceRatio-minDistanceRatio) + minDistanceRatio);
				b.z = (rand.nextBoolean())? b.z*-1 : b.z;
				
//				Log.d(LevelActivity.TAG," a.x="+a.x+" b.z="+b.z + " rand="+((float)rand.nextDouble()*(maxDistanceRatio-minDistanceRatio) + minDistanceRatio));
				
				rotation.setIdentity();
				rotation.addRotateY((float)rand.nextDouble()*Constants.TWOPI);
				rotation.addRotateX((float)rand.nextDouble()*(maxQx - minQx) + minQx);
				rotation.addRotateZ((float)rand.nextDouble()*(maxQz - minQz) + minQz);
				rotation.transformPoint(a);
				rotation.transformPoint(b);

//				Log.d(LevelActivity.TAG," 	qx="+(float)rand.nextDouble()*(maxQx - minQx) + minQx
//										+ " qz="+(float)rand.nextDouble()*(maxQz - minQz) + minQz);
							
				generatedOrbit = new Orbit(	a,center,b,
										(float)rand.nextDouble()*maxSpeed + minSpeed,
										entity.getBasicOrientation());
				
				//generate random satellite transformation
				rotationAxis.x = (float)rand.nextDouble();
				rotationAxis.y = (float)rand.nextDouble();
				rotationAxis.z = (float)rand.nextDouble();
				rotationAxis.normalize();
				
				satTransform = new VecAxisTransformation(	rotationAxis,
															1,
															(float)rand.nextDouble() * 5 + 1, //empiric values :) 
															null );
				
				satTransform.setAngle((float)rand.nextDouble()+0.1f, Config.INTERSATELLITE_SPEEDROTA_RATIO);
				
				generatedOrbit.setSatTrans(satTransform);
				addMotion(generatedOrbit,entity);
			}
		}
	}
}
