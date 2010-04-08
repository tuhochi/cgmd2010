package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Ellipse;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;

public class Orbit extends Motion
{

	private float 	speed,t,u,step,orbitAngle,
	
					//orbit transformation
					transformSpeed,
					transformAngle=0,transformStep=0,transformDiff=0,transformIteration=0,
					centerDiffFactor=0,centerDiffStep=0,centerDiff=0,centerDiffIteration=0,
					directionDiffFactor=0,directionDiffStep=0,directionDiff=0,directionDiffIteration=0,
					
					//morph
					newSpeed,oldPerimeter,oldStepSize,
					speedMorphStep,speedMorphIteration,dynamicMorphSpeed;

	public Vector3 position,
						 entityPos,centerPos,normalVec,
						 newEntityPos,newCenterPos;
					
					//orbit transformation
	private Vector3 	centerVec,toCenterVec,directionVec,orbitAxis,
							newCenterVec,newToCenterVec,newDirectionVec,newNormalVec,
							transformAxis,
							currtDirApproximation,tempDirectionVec;
	
	private Matrix44 objectOrbitTransform;
	private Matrix44 transform,basicOrientation,orbitTransform;
	private SatelliteTransformation satTrans;
	private Ellipse ellipse;
	
	protected Orbit()
	{
		
	}
	
	public Orbit(	Vector3 entityPos,Vector3 centerPos,
					Vector3 directionVec,
					float speed, 
					Matrix44 basicOrientation
				)
	{
		this.speed = speed;
		this.newSpeed = speed;
		
		this.entityPos = new Vector3(entityPos);
		this.centerPos = new Vector3(centerPos);
		this.directionVec = new Vector3(directionVec);
		this.centerVec = Vector3.subtract(this.entityPos,this.centerPos);
		this.toCenterVec =  Vector3.subtract(this.centerPos,this.entityPos);
		this.normalVec = Vector3.crossProduct(this.toCenterVec, this.directionVec).normalize();
		
		
		if(basicOrientation!=null)
			this.basicOrientation = new Matrix44(basicOrientation);
		else
			this.basicOrientation = new Matrix44();
		
		this.newEntityPos = new Vector3(entityPos);
		this.newCenterPos = new Vector3(centerPos);
		this.newCenterVec = new Vector3(centerVec);
		this.newDirectionVec = new Vector3(directionVec);
		this.newNormalVec = new Vector3(normalVec);
		this.newToCenterVec = new Vector3(toCenterVec);

		//init other temp vars 
		this.transform = new Matrix44();
		this.position = new Vector3();
		this.orbitAxis = new Vector3();
		this.currtDirApproximation = new Vector3();
		this.tempDirectionVec = new Vector3();
		this.transformAxis = new Vector3();
		this.orbitTransform = new Matrix44();
		this.t = 0;
		this.u = 0;
		
		//generate ellipse from vec
		this.ellipse = new Ellipse(this.centerPos,this.centerVec,this.directionVec);
		
		//calc init parameter and stepsize
		preCalc();
		
	}
		
	private void preCalc()
	{
		//calc start parameter
		this.u = (float)(t*Constants.TWOPI);
		//stepsize relative so perimeter
		this.step = Constants.TWOPI/ellipse.perimeter;
		
		//change the orientation between orbit and object - TODO: wird ansich nicht benötigt
		Vector3.crossProduct(normalVec,Constants.Y_AXIS,orbitAxis);
		orbitAxis.normalize();
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
		
		//increase the new directionvec
		if(speed!=newSpeed)
		{
			speedMorphIteration = speedMorphStep * dt * dynamicMorphSpeed;
			
			if(speedMorphStep>=0)
			{
				if(speed + speedMorphIteration > newSpeed)
				{
					speedMorphIteration = newSpeed - speed;
				}
			}else{
				if(speed + speedMorphIteration < newSpeed)
				{
					speedMorphIteration = newSpeed - speed;
				}
			}
				
			speed+=speedMorphIteration;
			Log.d(LevelActivity.TAG,"Morph speed curr="+speed+" iteration="+speedMorphIteration+" newspeed="+newSpeed + " dynSpeed="+dynamicMorphSpeed);
		}
		
		//check for orbit transformation
		if(Math.abs(transformDiff)<Math.abs(transformAngle))
		{
//			transformIteration = transformStep * dt * transformSpeed;
//			
//			if(Math.abs(transformDiff+transformIteration)>Math.abs(transformAngle))
//				transformIteration = transformAngle-transformDiff;
//			
//			transformDiff+=transformIteration;
			orbitTransform.setRotate(transformAxis, transformAngle);
			
			orbitTransform.transformPoint(centerVec);
			orbitTransform.transformPoint(directionVec);
			orbitTransform.transformPoint(normalVec);
			orbitTransform.transformPoint(entityPos);
			
			float centerangle = Vector3.getAngle(	new Vector3(centerVec).normalize(),
													new Vector3(newCenterVec).normalize());
			
			orbitTransform.setRotate(newNormalVec, centerangle);
			
			orbitTransform.transformPoint(centerVec);

			float directionrangle = Vector3.getAngle(	new Vector3(directionVec).normalize(),
					new Vector3(newDirectionVec).normalize());

			orbitTransform.setRotate(newNormalVec,-directionrangle);
			orbitTransform.transformPoint(directionVec);

			
			//Log.i(LevelActivity.TAG,"transformDiff "+transformDiff +" transformAngle" +transformAngle +" transformIteration"+transformIteration );
			Log.i(LevelActivity.TAG,"centervec = "+ new Vector3(centerVec).normalize() 
												  + " newcentervec="+new Vector3(newCenterVec).normalize());
			Log.i(LevelActivity.TAG,"entitypos = "  + entityPos
					  								+ " newpos="+newCenterVec);
		
			//testonly
			transformDiff = transformAngle;
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
	}
	
	private void evaluatePos()
	{
		//evaluate ellipse
		position.copy(ellipse.getPoint(u));
				
		//reset transformation
		transform.setIdentity();
		
		//set basic orientation
		transform.mult(basicOrientation);
		
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
		
		Vector3.crossProduct(this.newToCenterVec, this.newDirectionVec, this.newNormalVec);
		this.newNormalVec.normalize();
		
		//calc main rotation axis
		Vector3.crossProduct(this.normalVec,this.newNormalVec,this.transformAxis);
		transformAxis.normalize();
		
		transformAngle = (transformAxis.length()!=0)?Vector3.getAngle(normalVec,newNormalVec):0;
		transformStep = transformAngle/Constants.TWOPI;
		transformDiff = 0;
		
		Log.i(LevelActivity.TAG,"angle "+transformAngle);
		
		centerDiffFactor = (this.newCenterVec.length()/centerVec.length()) - 1;
		centerDiffStep = centerDiffFactor/10;
		centerDiff = 0;
		
		directionDiffFactor = (this.newDirectionVec.length()/directionVec.length()) - 1;
		directionDiffStep = directionDiffFactor/10;
		directionDiff = 0;
		
		u = 0;
	
	}
	
	public void setSatTrans(SatelliteTransformation satTrans) {
		this.satTrans = satTrans;
	}

	public Matrix44 getTransform() {
		return transform;
	}

	
	public void morphOrbit(Vector3 pushVec)
	{
		//approx current direction vec
		currtDirApproximation.copy(ellipse.getPoint(u+step));
		currtDirApproximation.subtract(position);
		currtDirApproximation.normalize();
		
		//new speed evaluation
		tempDirectionVec.copy(currtDirApproximation);
		tempDirectionVec.multiply(speed);
		tempDirectionVec.add(pushVec);
		newSpeed = tempDirectionVec.length();
		
		//new direction
		currtDirApproximation.multiply(this.directionVec.length());
		currtDirApproximation.add(pushVec);

		//store old perimeter
		oldPerimeter = ellipse.perimeter;
		oldStepSize = step;
		
		//update ellipse
		this.entityPos.copy(this.position);
		this.directionVec.copy(currtDirApproximation);
		this.centerVec.copy(this.entityPos);
		this.centerVec.subtract(this.centerPos);
		
		this.toCenterVec.copy(this.centerPos);
		this.toCenterVec.subtract(this.entityPos);
		
		this.ellipse.calcPerimeter();
		
		//stepsize should be the same relative to the new perimeter
		this.step = (oldStepSize*oldPerimeter)/ellipse.perimeter;
		this.u = 0;
		
		//speedmorphing parameters
		this.speedMorphStep = (this.newSpeed-this.speed)/100;
		
		if(newSpeed>speed)
			this.dynamicMorphSpeed = (newSpeed/speed)*200;
		else
			this.dynamicMorphSpeed = (speed/newSpeed)*200;
		

	}

	@Override
	public void persist(DataOutputStream dos)
	{
		
	}

	@Override
	protected void restore(DataInputStream dis)
	{
		
	}
}
