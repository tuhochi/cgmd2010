package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Ellipse;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;

public class Orbit extends Motion
{

	private float 	speed,u,step,

					//scale morphing
					scalingMorphSpeed,
					centerDiffFactor,centerDiffStep,centerDiff,centerDiffIteration,
					directionDiffFactor,directionDiffStep,directionDiff,directionDiffIteration,
					centerVecCap,directionVecCap,
					
					//speed morphing
					newSpeed,oldPerimeter,oldStepSize,
					speedMorphStep,speedMorphIteration,dynamicMorphSpeed;

	private boolean doCenterVecScaling,doDirectionVecScaling;
	public final Vector3 position,
						 entityPos,centerPos;
					
							//orbit morphing
	private final Vector3 	centerVec,directionVec,
							currtDirApproximation,tempDirectionVec,
							refDirectionVec,refCenterVec;

	private Matrix44 transform;
	private final Matrix44  basicOrientation;
	private SatelliteTransformation satTrans;
	private Ellipse ellipse;

	
	protected Orbit()
	{	
		//init
		position = new Vector3();
		entityPos = new Vector3();
		centerPos = new Vector3();
		
		centerVec = new Vector3();
		directionVec = new Vector3();
		currtDirApproximation = new Vector3();
		tempDirectionVec = new Vector3();
		refDirectionVec = new Vector3();
		refCenterVec = new Vector3();
		
		transform = new Matrix44();
		basicOrientation = new Matrix44();

		u = 0;
	}
	
	public Orbit(	Vector3 entityPos,Vector3 centerPos,
					Vector3 directionVec,
					float speed, 
					Matrix44 basicOrientation
				)
	{
		
		//init fields
		this();
		
		this.speed = speed;
		this.newSpeed = speed;
		
		this.entityPos.copy(entityPos);
		this.centerPos.copy(centerPos);
		this.directionVec.copy(directionVec);
		
		this.centerVec.copy(this.entityPos);
		this.centerVec.subtract(this.centerPos);

		if(basicOrientation!=null)
			this.basicOrientation.copy(basicOrientation);
		
		//generate ellipse from vec
		this.ellipse = new Ellipse(this.centerPos,this.centerVec,this.directionVec);
		
		//stepsize relative so perimeter
		this.step = Constants.TWOPI/ellipse.perimeter;
	}
		
	public void update(float dt)
	{
		//check the size and speed of the orbit
		limitUniverse();
		
		//inc parameter
		u+=(speed*step*dt);
		if(u>=Constants.TWOPI)
			u-=Constants.TWOPI;

		//update sat transformation
		if(satTrans!=null)
			satTrans.update(dt);
		
		//update the morphing for the direction vec length
		updateSpeedMorphing(dt);
		
		//update the morphing for the axis scaling
		updateAxisScaling(dt);
			
		//calc position on ellipse - build transformation matrix
		evaluatePos();
		
		//check the limitations
//		if(this.centerVec.length()>10 || this.directionVec.length()>10)
//			Log.d(LevelActivity.TAG,"OUT centerVec="+centerVec.length()+" directionVec="+directionVec.length()+" speed="+speed);
	}
	
	private void updateSpeedMorphing(float dt)
	{
		if(speed!=newSpeed)
		{
			speedMorphIteration = speedMorphStep * dt * dynamicMorphSpeed;
			
			if(speedMorphStep<0)
			{
				if(speed + speedMorphIteration < newSpeed){
					//speedMorphIteration = newSpeed - speed;
					speed=newSpeed;
				}else{
					speed+=speedMorphIteration;
				}
			}else{
				if(speed + speedMorphIteration > newSpeed){
					//speedMorphIteration = newSpeed - speed;
					speed=newSpeed;
				}else{
					speed+=speedMorphIteration;
				}
			}
				
		
			//Log.d(LevelActivity.TAG,"Morph speed curr="+speed+" iteration="+speedMorphIteration+" newspeed="+newSpeed + " dynSpeed="+dynamicMorphSpeed);
		}
	}
	
	private void updateAxisScaling(float dt)
	{
		//store old perimeter for the updateStepSize method
		oldPerimeter = ellipse.perimeter;
		oldStepSize = step;
		doCenterVecScaling = false;
		doDirectionVecScaling = false;
		
		if(Math.signum(centerDiffStep)<0){
			if(centerDiffFactor<centerDiff){
				doCenterVecScaling = true;
				centerDiffIteration = centerDiffStep * dt * scalingMorphSpeed;
				
				if(centerDiff+centerDiffIteration<centerDiffFactor)
					centerDiffIteration = centerDiffFactor-centerDiff;
				//Log.d(LevelActivity.TAG,"centerDiff "+centerDiff +" centerDiffFactor" +centerDiffFactor+" centerDiffIteration "+centerDiffIteration );
			}
		}else{
			if(centerDiffFactor>centerDiff){
				doCenterVecScaling = true;
				centerDiffIteration = centerDiffStep * dt * scalingMorphSpeed;
			
				if(centerDiff+centerDiffIteration>centerDiffFactor)
					centerDiffIteration = centerDiffFactor-centerDiff;
	
				//Log.d(LevelActivity.TAG,"centerDiff "+centerDiff +" centerDiffFactor" +centerDiffFactor+" centerDiffIteration "+centerDiffIteration );
			}
		}

		if(Math.signum(directionDiffStep)<0){
			if(directionDiffFactor<directionDiff){
				doDirectionVecScaling = true;
				directionDiffIteration = directionDiffStep * dt * scalingMorphSpeed;
				
				if(directionDiff+directionDiffIteration<directionDiffFactor)
					directionDiffIteration = directionDiffFactor-directionDiff;
				
				//Log.d(LevelActivity.TAG,"directionDiff "+directionDiff +" directionDiffFactor" +directionDiffFactor+" directionDiffIteration "+directionDiffIteration );
			}
		}else{
			if(directionDiffFactor>directionDiff){
				doDirectionVecScaling = true;
				directionDiffIteration = directionDiffStep * dt * scalingMorphSpeed;
				
				if(directionDiff+directionDiffIteration>directionDiffFactor)
					directionDiffIteration = directionDiffFactor-directionDiff;

				//Log.d(LevelActivity.TAG,"directionDiff "+directionDiff +" directionDiffFactor" +directionDiffFactor+" directionDiffIteration "+directionDiffIteration );
			}
		}
		
		if(doCenterVecScaling){
			centerDiff += centerDiffIteration;
			
			centerVec.copy(refCenterVec);
			centerVec.multiply(centerDiff);
		}
		
		if(doDirectionVecScaling){
			directionDiff += directionDiffIteration;
			
			directionVec.copy(refDirectionVec);
			directionVec.multiply(directionDiff);
		}
		
		if(doCenterVecScaling||doDirectionVecScaling){
			//change the stepsize relative to the new orbitsize
			updateStepSize();
		}

	}
	
	private void evaluatePos()
	{
		//evaluate ellipse
		position.copy(ellipse.getPoint(u));
				
		//reset transformation
		transform.setIdentity();
		
		//set basic orientation
		transform.mult(basicOrientation);
				
		//object transf.
		if(satTrans!=null)
			transform.mult(satTrans.getTransform());
		
		//transformation of the ellipsoid
		transform.addTranslate(position.x,position.y,position.z);
	}

	
	
	public void setSatTrans(SatelliteTransformation satTrans) {
		this.satTrans = satTrans;
	}

	public Matrix44 getTransform() {
		return transform;
	}

	public void morphAxisScale(float aAxisFactor,float bAxisFactor,float morphSpeed)
	{
		scalingMorphSpeed = morphSpeed;
		refCenterVec.copy(centerVec);
		refDirectionVec.copy(directionVec);
		
		centerDiffFactor = aAxisFactor;
		centerDiffStep = (centerDiffFactor-1)/100;
		centerDiff = 1;
		
		directionDiffFactor = bAxisFactor;
		directionDiffStep = (directionDiffFactor-1)/100;
		directionDiff = 1;
	
	}
	
	private void limitUniverse()
	{
		this.centerVecCap = 1;
		this.directionVecCap = 1;
		
		if(this.centerVec.length()>Config.UNIVERSE_CENTERLENGTH_LIMIT)
			this.centerVecCap = Config.UNIVERSE_CENTERLENGTH_LIMIT/this.centerVec.length();
		if(this.directionVec.length()>Config.UNIVERSE_DIRLENGTH_LIMIT)
			this.directionVecCap = Config.UNIVERSE_DIRLENGTH_LIMIT/this.directionVec.length();
		
		//Log.d(LevelActivity.TAG,"centerVecCap ="+centerVecCap+" directionVecCap="+directionVecCap);
		
		if(this.centerVecCap!=1 || this.directionVecCap != 1)
			morphAxisScale(centerVecCap, directionVecCap, 20);
		
		if(this.newSpeed > Config.UNIVERSE_SPEED_LIMIT)
			this.newSpeed = Config.UNIVERSE_SPEED_LIMIT;
	}
	
	public void updateStepSize()
	{
		//!!!oldPerimeter and oldStepSize has to be already set
		
		this.ellipse.calcPerimeter();
		//stepsize should be the same relative to the new perimeter
		this.step = (oldStepSize*oldPerimeter)/ellipse.perimeter;
	}
	
	public void morph(Vector3 pushVec)
	{
		//stop scale morphing
		directionDiffFactor = directionDiff;
		centerDiffFactor = centerDiff;
		
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
		
		//cap size and speed of orbit
		//-> get the new speed value
		limitUniverse();
		
		//change the stepsize relative to the new orbitsize
		updateStepSize();
		
		//restart ellipse
		this.u = 0;

		//add dynamic speed boost :)
		if(newSpeed<speed){
			this.dynamicMorphSpeed = (newSpeed/speed)*120;
			this.speed -= (pushVec.length()*0.25f);
		}else{
			this.dynamicMorphSpeed = (speed/newSpeed)*120;
			this.speed += (pushVec.length()*0.25f);
		}
		//speedmorphing parameters
		this.speedMorphStep = (this.newSpeed-this.speed)/100;
		
	}
	
	@Override
	public void persist(DataOutputStream dos) throws IOException
	{
		dos.writeFloat(speed);
		dos.writeFloat(u);
		dos.writeFloat(step);

		//scale morphing
		dos.writeFloat(scalingMorphSpeed);
		dos.writeFloat(centerDiffFactor);
		dos.writeFloat(centerDiffStep);
		dos.writeFloat(centerDiff);
		dos.writeFloat(directionDiffFactor);
		dos.writeFloat(directionDiffStep);
		dos.writeFloat(directionDiff);
		refCenterVec.persist(dos);
		refDirectionVec.persist(dos);
		
		//speed morphing
		dos.writeFloat(newSpeed); 
		dos.writeFloat(oldPerimeter);
		dos.writeFloat(oldStepSize);
		dos.writeFloat(speedMorphStep);
		dos.writeFloat(speedMorphIteration); 
		dos.writeFloat(dynamicMorphSpeed);
	
		centerPos.persist(dos);
		centerVec.persist(dos);
		directionVec.persist(dos);
		basicOrientation.persist(dos);
		
		if(satTrans != null){
			dos.writeBoolean(true);
			dos.writeUTF(satTrans.getClass().getName());
			this.satTrans.persist(dos);
		}else
			dos.writeBoolean(false);
	}

	@Override
	public void restore(DataInputStream dis) throws IOException
	{
		speed = dis.readFloat();
		u = dis.readFloat();
		step = dis.readFloat();

		//scale morphing
		scalingMorphSpeed = dis.readFloat();
		centerDiffFactor = dis.readFloat();
		centerDiffStep = dis.readFloat();
		centerDiff = dis.readFloat();
		directionDiffFactor = dis.readFloat();
		directionDiffStep = dis.readFloat();
		directionDiff = dis.readFloat();
		refCenterVec.restore(dis);
		refDirectionVec.restore(dis);		
	
		//speed morphing
		newSpeed  = dis.readFloat(); 
		oldPerimeter = dis.readFloat();
		oldStepSize = dis.readFloat();
		speedMorphStep = dis.readFloat();
		speedMorphIteration = dis.readFloat(); 
		dynamicMorphSpeed = dis.readFloat();
		
		centerPos.restore(dis);
		centerVec.restore(dis);
		directionVec.restore(dis);
		basicOrientation.restore(dis);
		
		if(dis.readBoolean()){
			String className = dis.readUTF();
			satTrans = SatelliteTransformation.restore(dis,className);
		}
		
		ellipse = new Ellipse(centerPos,centerVec,directionVec);
		
		
	}

	@Override
	public SatelliteTransformation getSatTrans() {
		return satTrans;
	}

	@Override
	public Vector3 getCurrDirectionVec() {
		//approx current direction vec
		currtDirApproximation.copy(ellipse.getPoint(u+step));
		currtDirApproximation.subtract(position);
		return currtDirApproximation;
	}

	@Override
	public void setTransform(Matrix44 transform) {
		this.transform = transform;		
	}

	@Override
	public float getSpeed() {
		return this.speed;
	}
}
