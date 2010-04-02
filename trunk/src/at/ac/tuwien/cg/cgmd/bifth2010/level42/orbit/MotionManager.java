package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.util.ArrayList;
import java.util.HashMap;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Sphere;
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
		entity.setMotion(motion);
		SceneEntity se = (SceneEntity)entity;
		Sphere sphere = se.getBoundingSphereWorld();
		motion.setSatTrans(new SatelliteTransformation(0,0,0,entity.getTransformation().addTranslate(-sphere.center.x,-sphere.center.y,-sphere.center.z)));
		list.add(entity);
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
}
