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

public class MotionManager {
	
	private final ArrayList<Moveable> list;
	public static final MotionManager instance = new MotionManager();
	
	private Moveable tempEntity;
	
	private MotionManager()
	{
		list =  new ArrayList<Moveable>();
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
	
	public void changeSatelliteTransformation(Moveable entity,Vector3 directionVec,Vector3 pushVec)
	{
		//TODO: reuse obj
		Vector3 axis = Vector3.crossProduct(directionVec, pushVec);
		axis.normalize();
		SatelliteTransformation satTrafo = entity.getMotion().getSatTrans();
		VecAxisTransformation vecSatTrafo;
		
		if(satTrafo!=null && satTrafo instanceof VecAxisTransformation){
			vecSatTrafo = (VecAxisTransformation)satTrafo;
			Matrix44 temp = new Matrix44(vecSatTrafo.getTransform());
			vecSatTrafo.setBasicOrientaion(temp);
			vecSatTrafo.axis.copy(axis);
			
			Vector3 tempNormDirection = Vector3.normalize(directionVec);
			Vector3 tempNormPush = Vector3.normalize(pushVec);
		
			vecSatTrafo.setAngle(Vector3.getAngle(tempNormDirection, tempNormPush));
			//Log.d(LevelActivity.TAG,"satAngle="+vecSatTrafo.qv);
		}
	}
	
	public void generateRandomOrbit(	Scene scene,
										float minSpeed,float maxSpeed,
										float minQx,float maxQx,
										float minQz,float maxQz,
										int minDistance,int maxDistance	)
	{
		Matrix44 rotation = new Matrix44();
		SceneEntity entity = null;
		Random rand = new Random();
		Vector3 a = new Vector3();
		Vector3 b = new Vector3();
		Vector3 center = new Vector3(0,0,0);
		Orbit tempOrbit = null;
		
		for(int i=0;i<scene.sceneEntities.size();i++){
			entity = scene.sceneEntities.get(i);
			if(entity.getName().startsWith("Satellite_")){
				
				//generate random setup
				
				a.x = (float)rand.nextInt(2*maxDistance) - 2*minDistance;
				b.z = (float)rand.nextInt(2*maxDistance) - 2*minDistance;
				rotation.setIdentity();
				rotation.addRotateY((float)rand.nextDouble()*Constants.TWOPI);
				rotation.addRotateX((float)rand.nextDouble()*maxQx + minQx);
				rotation.addRotateZ((float)rand.nextDouble()*maxQz + minQz);
				rotation.transformPoint(a);
				rotation.transformPoint(b);

				tempOrbit = new Orbit(	a,center,b,
										(float)rand.nextDouble()*maxSpeed + minSpeed,
										entity.getBasicOrientation());
				addMotion(tempOrbit,entity);
			}
		}
	}
}
