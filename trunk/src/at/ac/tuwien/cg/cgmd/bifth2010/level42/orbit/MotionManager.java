package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.CollisionManager;
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
	private final Vector3 	satTransformAxis,
							tempDirectionVec, tempCenterVec,
							tempPushVec,
							tempForceDirectionVec,deflactionDirVec;
	private final Matrix44 tempBasicOrientation;
	private Movable tempEntity;
	
	/**
	 * Instantiates a new motion manager.
	 */
	private MotionManager()
	{
		this.list =  new ArrayList<Movable>();
		this.satTransformAxis = new Vector3();
		this.tempDirectionVec = new Vector3();
		this.tempCenterVec = new Vector3();
		this.tempForceDirectionVec = new Vector3();
		this.tempPushVec = new Vector3();
		this.tempBasicOrientation = new Matrix44();
		this.deflactionDirVec = new Vector3();
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
			motion.setTransform(entity.getTransformation());
			motion.setSatTrans(oldMotion.getSatTrans());
		}
	}
	
	
	public void applySelectionForce(Movable entity, Vector3 pushVec)
	{
		Motion motion = entity.getMotion();
		
		//check for a change to directional motion
		if(pushVec.length()>= Config.MIN_STRENGTH_FOR_DIRECTIONAL){
	
			if(motion instanceof DirectionalMotion){
				//TODO change direction
			}else{
				//determine aiming center
				Movable aimEntity = CollisionManager.instance.getNearestToCenterEntity();
				Vector3 aimCenter = null;
				
				if(aimEntity!=null)
					aimCenter = aimEntity.getBoundingSphereWorld().center;
				else
					aimCenter = Config.UNIVERSE_CENTER;
				
				tempForceDirectionVec.set(aimCenter);
				tempForceDirectionVec.subtract(entity.getCurrentPosition());		
				
				Log.d(LevelActivity.TAG,"SELECTION push force="+pushVec.length());
				
				DirectionalMotion dirMotion =  
					new DirectionalMotion(	entity.getCurrentPosition(),
											tempForceDirectionVec,
											pushVec.length()*Config.SELECTION_FORCE_FACTOR,
											motion.getBasicOrientation());
				//exchange motion
				setMotion(dirMotion, entity);
			}
		}else{
			motion.morph(pushVec);
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
			
			//check the outer limits
			checkUniverseLimits(tempEntity);
		}
	}
	
	
	private void checkUniverseLimits(Movable entity)
	{
		Motion motion = entity.getMotion();
		
		if(motion instanceof DirectionalMotion){
			if(entity.getCurrentPosition().length()>Config.UNIVERSE_CENTERLENGTH_LIMIT){
				//change to orbit motion
				//Log.d(LevelActivity.TAG,"UNIVERSE LIMIT="+entity.getCurrentPosition().length());
				transformDirMotionInOrbit(entity);
			}
		}
	}
	
	public void transformDirMotionInOrbit(Movable obj){
		
		if(obj.getMotion() instanceof DirectionalMotion){
			
			Log.d(LevelActivity.TAG,"TRANSFORM in ORBIT");
			
			DirectionalMotion oldDirMotion = (DirectionalMotion)obj.getMotion();
			
			//set to the actual direction and normalize
			deflactionDirVec.set(obj.getMotion().getCurrDirectionVec()).normalize();
			// * centerVec * 4
			deflactionDirVec.multiply(obj.getCurrentPosition().length()*Config.DIRORBITTRANSFORM_DIRVEC_FACTOR);
			// + small deflaction for cross prod
			deflactionDirVec.add(Constants.DUMMY_VARIATION_VEC);
									
			Orbit newOrbit = new Orbit(	obj.getCurrentPosition(),
										Config.UNIVERSE_CENTER,
										deflactionDirVec,
										oldDirMotion.getSpeed(),
										oldDirMotion.getBasicOrientation());

			tempCenterVec.set(newOrbit.centerVec).normalize();
			tempDirectionVec.set(newOrbit.directionVec).normalize();
			float angle = Vector3.getAngle(tempCenterVec, tempDirectionVec);

			Log.d(LevelActivity.TAG,"DIR TO ORBIT TRANSFORM - angle="+((float)Math.toDegrees(angle)));
		
			//dir = center 
			if(Float.isNaN(angle))
				angle = 0;
			
			newOrbit.rotateDirectionVec(Constants.PIHALF-angle);
			setMotion(newOrbit,obj);
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
	public void generateRandomOrbits(	Scene scene,
										float minSpeed,float maxSpeed,
										float minQx,float maxQx,
										float minQz,float maxQz,
										float minDistance,float maxDistance,
										float minDistanceRatio, float maxDistanceRatio)
	{
		Matrix44 rotation = new Matrix44();
		SceneEntity entity = null;
		Random rand = new Random();
		Vector3 a = new Vector3();
		Vector3 b = new Vector3();
		Vector3 center = Config.UNIVERSE_CENTER;
		Vector3 rotationAxis = new Vector3();
		Orbit generatedOrbit = null;
		
		int orbitCounter = 0;
		
		VecAxisTransformation satTransform = null;
		
		for(int i=0;i<scene.sceneEntities.size();i++){
			entity = scene.sceneEntities.get(i);
			if(entity.getName().startsWith(Config.SATELLITE_PREFIX)){
				
				//generate the orthonormal axis for the ellipse
				a.x = ((float)rand.nextDouble()*(maxDistance-minDistance) + minDistance);
				a.x = (rand.nextBoolean())? -a.x : a.x;
				
				/** generate a similar value for b over the <code>distanceRatio</code> */
				b.z = a.x *((float)rand.nextDouble()*(maxDistanceRatio-minDistanceRatio) + minDistanceRatio);
				b.z = (rand.nextBoolean())? -b.z: b.z;
				
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
				
				orbitCounter++;
			}
		}
		Log.d(LevelActivity.TAG,orbitCounter+" RANDOM SAT ORBITS GENERATED");
	}
}
