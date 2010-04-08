package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.util.ArrayList;
import java.util.Random;

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
			motion.setSatTrans(new SatelliteTransformation(0,0,0));
			list.add(entity);
		}else{
			//transform motion
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
			tempEntity.setTransformation(tempEntity.getMotion().getTransform());
		}
	}
	
	public void generateRandomOrbit(	Scene scene,
										float minSpeed,float maxSpeed,
										float minQx,float maxQx,
										float minQz,float maxQz,
										int minDistance,int maxDistance	)
	{
		Matrix44 rotation = new Matrix44();
		Matrix44 orientation = new Matrix44();
		SceneEntity entity = null;
		Random rand = new Random();
		Vector3 a = new Vector3();
		Vector3 b = new Vector3();
		Vector3 center = new Vector3(0,0,0);
		Vector3 translate = new Vector3();
		
		for(int i=0;i<scene.sceneEntities.size();i++){
			entity = scene.sceneEntities.get(i);
			if(entity.getName().indexOf("Satellite_")>=0){
				
				//generate random setup
				
				a.x = (float)rand.nextInt(2*maxDistance) - 2*minDistance;
				b.z = (float)rand.nextInt(2*maxDistance) - 2*minDistance;
				rotation.setIdentity();
				rotation.addRotateY((float)rand.nextDouble()*Constants.TWOPI);
				rotation.addRotateX((float)rand.nextDouble()*maxQx + minQx);
				rotation.addRotateZ((float)rand.nextDouble()*maxQz + minQz);
				rotation.transformPoint(a);
				rotation.transformPoint(b);
				
				orientation.copy(entity.getTransformation());
				translate.copy(entity.getBoundingSphereWorld().center);
				orientation.addTranslate(-translate.x,-translate.y, -translate.z);
				//TODO: basicorientation
				addMotion(	new Orbit(	a,center,b,
										(float)rand.nextDouble()*maxSpeed + minSpeed,
										null),
							entity);
			}
		}
	}
}
