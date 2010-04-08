package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.util.ArrayList;

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
}
