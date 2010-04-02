package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;

public class DirectionalMotion implements Motion {

	private final Vector3 startPos,directionVec,currentPos,tempDirectionVec;
	private float speed;
	private SatelliteTransformation satTrans;
	private final SceneEntity entity;
	private final Matrix44 transform;
	
	public DirectionalMotion(	SceneEntity entity, Vector3 startPos, 
								Vector3 directionVec, float speed
								)
	{
		this.entity = entity;
		this.startPos = startPos;
		this.directionVec = directionVec.normalize();
		this.speed = speed;
		
		currentPos = new Vector3(startPos);
		tempDirectionVec = new Vector3();
		transform = new Matrix44();
	}
	
	public void update(float dt)
	{
		tempDirectionVec.copy(directionVec);
		tempDirectionVec.multiply(speed);
		
		currentPos.add(tempDirectionVec);
	
		transform.setIdentity();
		
		if(satTrans!=null){
			satTrans.update(dt,speed);
			transform.mult(satTrans.getTransform());
		}
		
		transform.addTranslate(currentPos.x, currentPos.y, currentPos.z);
		
		entity.setTransformation(transform);
	}

	public Matrix44 getTransform() {
		return transform;
	}

	public void setSatTrans(SatelliteTransformation satTrans) {
		this.satTrans = satTrans;
	}
	
}
