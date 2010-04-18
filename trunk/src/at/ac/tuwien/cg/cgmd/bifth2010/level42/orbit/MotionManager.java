package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.util.ArrayList;
import java.util.Random;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;

public class MotionManager {
	
	private final ArrayList<Moveable> list;
	public static final MotionManager instance = new MotionManager();
	
	//temp vars
	private final Vector3 satTransformAxis,tempDirectionVec,tempPushVec;
	private final Matrix44 tempBasicOrientation;
	private Moveable tempEntity;
	
	private MotionManager()
	{
		list =  new ArrayList<Moveable>();
		satTransformAxis = new Vector3();
		tempDirectionVec = new Vector3();
		tempPushVec = new Vector3();
		tempBasicOrientation = new Matrix44();
	}
	
	public void addMotion(Motion motion,Moveable entity)
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
	
	public Moveable getMotion(int index)
	{
		return list.get(index);
	}
	
	public Moveable getMotion(Moveable entity)
	{
		return list.get(list.indexOf(entity));
	}
	
	
	public void updateMotion(float dt)
	{
		for(int i=0; i<list.size();i++)
		{
			tempEntity = list.get(i);
			tempEntity.getMotion().update(dt);
		}
	}
	
	public void reset()
	{
		list.clear();
	}
	
	public void changeSatelliteTransformation(Moveable entity,Vector3 directionVec,Vector3 pushVec, float speedRotationRatio)
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
			vecSatTransform.setBasicOrientaion(tempBasicOrientation);
			//set new rotation axis
			vecSatTransform.axis.copy(satTransformAxis);
	
			tempDirectionVec.copy(directionVec).normalize();
			tempPushVec.copy(pushVec).normalize();
		
			//set the new rotation angle
			vecSatTransform.setAngle(Vector3.getAngle(tempDirectionVec, tempPushVec),speedRotationRatio);
			
			//Log.d(LevelActivity.TAG,"satAngle="+vecSatTransform.qv);
		}
	}
	
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
