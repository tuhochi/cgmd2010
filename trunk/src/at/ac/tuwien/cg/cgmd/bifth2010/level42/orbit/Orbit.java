package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Ellipse;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;

public class Orbit implements Movement {

	private float 	speed,t,u,step,orbitAngle;

	private Vector3 position,entityPos,centerPos,centerVec,toCenterVec,
					normalVec,directionVec,orbitAxis;
	private Matrix44 transform,objectOrbitTransform;

	private SceneEntity entity;
	private SatelliteTransformation satTrans;
	private Ellipse ellipse;
	
	
	public Orbit(	SceneEntity entity,
					Vector3 entityPos,Vector3 centerPos,
					Vector3 directionVec,
					float speed	
				)
	{
		this.entity = entity;
		this.speed = speed;
		
		this.entityPos = entityPos;
		this.centerPos = centerPos;
		this.centerVec = Vector3.subtract(entityPos,centerPos);
		this.toCenterVec =  Vector3.subtract(centerPos,entityPos);
		this.normalVec = Vector3.crossProduct(toCenterVec, directionVec).normalize();
		this.directionVec = directionVec;
		
		//init
		transform = new Matrix44();
		position = new Vector3();
		t = 0;
		u = 0;
		
		//generate ellipse from vec
		ellipse = new Ellipse(centerPos,centerVec,directionVec);
		
		//calc init parameter and stepsize
		preCalc();
		
	}
		
	private void preCalc()
	{
		//calc start parameter
		this.u = (float)(t*Constants.TWOPI);
		//todo: relative to size?!
		this.step = Constants.TWOPI/ellipse.perimeter;
		
		//change the orientation between orbit and object
		orbitAxis = Vector3.crossProduct(normalVec,Constants.Y_AXIS).normalize();
		orbitAngle = Vector3.getAngle(normalVec, Constants.Y_AXIS);
		objectOrbitTransform = Matrix44.getRotate(orbitAxis, -orbitAngle);
	}
		
	public void update(float dt)
	{
		//inc parameter
		u+=(speed*step*dt);
		if(u>=Constants.TWOPI)
			u-=Constants.TWOPI;

		//update sat transformation
		if(satTrans!=null)
			satTrans.update(dt,speed);
		
		//calc position on ellipse - build transformation matrix
		evaluatePos();
		
		//update transformation in the entity
		entity.setTransformation(transform);
	}
	
	private void evaluatePos()
	{
		//evaluate ellipse
		position = ellipse.getPoint(u);
				
		//reset transformation
		transform.setIdentity();
		
		//change orientation relative to the orbit
		transform.mult(objectOrbitTransform);
		
		//object transf.
		if(satTrans!=null)
			transform.mult(satTrans.getTransform());
		
		//transformation of the ellipsoid
		transform.addTranslate(position.x,position.y,position.z);
	}

	public void decA(){
		ellipse.a.subtract(new Vector3(1,0,0));
	}
	public void setSatTrans(SatelliteTransformation satTrans) {
		this.satTrans = satTrans;
	}

	public Matrix44 getTransform() {
		return transform;
	}

	public void setTransform(Matrix44 transform) {
		this.transform = transform;
	}

	
}
