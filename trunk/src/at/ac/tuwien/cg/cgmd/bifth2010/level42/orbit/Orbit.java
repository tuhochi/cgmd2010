package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Ellipse;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;

public class Orbit implements Motion {

	private float 	speed,t,u,step,orbitAngle,
	
					//orbit transformation
					transformSpeed,
					transformAngle=0,transformStep=0,transformDiff=0,transformIteration=0,
					centerDiffFactor=0,centerDiffStep=0,centerDiff=0,centerDiffIteration=0,
					directionDiffFactor=0,directionDiffStep=0,directionDiff=0,directionDiffIteration=0;

	private Vector3 position,entityPos,centerPos,centerVec,toCenterVec,
					normalVec,directionVec,orbitAxis,
					
					//orbit transformation
					newEntityPos,newCenterPos,newCenterVec,newToCenterVec,newDirectionVec,newNormalVec,
					transformAxis;
	private Matrix44 transform,objectOrbitTransform,orbitTransform;

	private SatelliteTransformation satTrans;
	private Ellipse ellipse;
	
	
	public Orbit(	Vector3 entityPos,Vector3 centerPos,
					Vector3 directionVec,
					float speed	
				)
	{
		this.speed = speed;
		
		this.entityPos = entityPos;
		this.centerPos = centerPos;
		this.centerVec = Vector3.subtract(entityPos,centerPos);
		this.toCenterVec =  Vector3.subtract(centerPos,entityPos);
		this.normalVec = Vector3.crossProduct(toCenterVec, directionVec).normalize();
		this.directionVec = directionVec;
		
		this.newEntityPos = new Vector3(entityPos);
		this.newCenterPos = new Vector3(centerPos);
		this.newCenterVec = new Vector3(centerVec);
		this.newDirectionVec = new Vector3(directionVec);
		this.newNormalVec = new Vector3(normalVec);
		this.newToCenterVec = new Vector3(toCenterVec);

		//init
		transform = new Matrix44();
		position = new Vector3();
		transformAxis = new Vector3();
		orbitTransform = new Matrix44();
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
		
		//check for orbit transformation
		if(Math.abs(transformDiff)<Math.abs(transformAngle))
		{
			
			transformIteration = transformStep * dt * transformSpeed;
			
			if(Math.abs(transformDiff+transformIteration)>Math.abs(transformAngle))
				transformIteration = transformAngle-transformDiff;
			
			transformDiff+=transformIteration;
			orbitTransform.setRotate(transformAxis, transformIteration);
			orbitTransform.transformPoint(centerVec);
			orbitTransform.transformPoint(directionVec);
			
			//Log.i(LevelActivity.TAG,"transformDiff "+transformDiff +" transformAngle" +transformAngle +" transformIteration"+transformIteration );
		}
		
		if(Math.abs(centerDiff)<Math.abs(centerDiffFactor))
		{
			
			centerDiffIteration = centerDiffStep * dt * transformSpeed;
			
			if(Math.abs(centerDiff+centerDiffIteration)>Math.abs(centerDiffFactor))
				centerDiffIteration = centerDiffFactor-centerDiff;

			centerDiff += centerDiffIteration;
			centerVec.multiply( 1 + centerDiffIteration);
			
			//Log.i(LevelActivity.TAG,"centerDiff "+centerDiff +" centerDiffFactor" +centerDiffFactor+" centerDiffIteration "+centerDiffIteration );
		}
		
		if(Math.abs(directionDiff)<Math.abs(directionDiffFactor))
		{
			directionDiffIteration = directionDiffStep * dt * transformSpeed;
			
			if(Math.abs(directionDiff+directionDiffIteration)>Math.abs(directionDiffFactor))
				directionDiffIteration = directionDiffFactor-directionDiff;
			
			directionDiff += directionDiffIteration;
			directionVec.multiply( 1 + directionDiffIteration);
		}
		
		//calc position on ellipse - build transformation matrix
		evaluatePos();
		
		//update transformation in the entity
		//entity.setTransformation(transform);
	}
	
	private void evaluatePos()
	{
		//evaluate ellipse
		position = ellipse.getPoint(u);
				
		//reset transformation
		transform.setIdentity();
		
		//change orientation relative to the orbit
		//transform.mult(objectOrbitTransform);
		
		//object transf.
		if(satTrans!=null)
			transform.mult(satTrans.getTransform());
		
		//transformation of the ellipsoid
		transform.addTranslate(position.x,position.y,position.z);
	}

	public void transformOrbit(	Vector3 newEntityPos,Vector3 newCenterPos,
								Vector3 newDirectionVec, float transformSpeed)
	{
		this.transformSpeed = transformSpeed;
		
		//store new constellation
		this.newEntityPos.copy(newEntityPos);
		this.newCenterPos.copy(newCenterPos);
		
		this.newCenterVec.copy(newEntityPos);
		this.newCenterVec.subtract(newCenterPos);
		
		this.newToCenterVec.copy(newCenterPos);
		this.newToCenterVec.subtract(newEntityPos);
		
		this.newDirectionVec.copy(newDirectionVec);
		
		Vector3.crossProduct(newToCenterVec, newDirectionVec,newNormalVec);
		newNormalVec.normalize();
		

		//calc main rotation axis
		transformAxis = Vector3.crossProduct(normalVec,newNormalVec).normalize();
		transformAngle = (transformAxis.length()!=0)?Vector3.getAngle(normalVec,newNormalVec):0;
		transformStep = transformAngle/Constants.TWOPI;
		transformDiff = 0;
		
		centerDiffFactor = (newCenterVec.length()/centerVec.length()) - 1;
		centerDiffStep = centerDiffFactor/10;
		centerDiff = 0;
		
		directionDiffFactor = (newDirectionVec.length()/directionVec.length()) - 1;
		directionDiffStep = directionDiffFactor/10;
		directionDiff = 0;
		
	}
	public void decA(){
		transformOrbit(	Vector3.subtract(entityPos,new Vector3(1,-2,-5)),
						centerPos, directionVec,5);
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
