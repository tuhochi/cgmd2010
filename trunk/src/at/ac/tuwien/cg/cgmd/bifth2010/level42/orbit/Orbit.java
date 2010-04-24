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

// TODO: Auto-generated Javadoc
/**
 * The Class Orbit represents a elliptic motion.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Orbit extends Motion
{
	
	/** The iteration speed. */
	private float 	speed;
	
	/** The dynamic morph speed. */
	private float  	u,step,

					//directionVec rotation
					dirVecRotationDiff,dirVecRotationDiffStep,dirVecRotationDiffIteration,
					
					//scale morphing
					aScalingMorphSpeed,bScalingMorphSpeed,
					centerDiffFactor,centerDiffStep,centerDiff,centerDiffIteration,
					directionDiffFactor,directionDiffStep,directionDiff,directionDiffIteration,
					centerVecCap,directionVecCap,
					
					//speed morphing
					newSpeed,oldPerimeter,oldStepSize,
					speedMorphStep,speedMorphIteration,dynamicMorphSpeed;

	/** The do direction vec scaling. */
	private boolean doCenterVecScaling,doDirectionVecScaling;
	
	/** The position on the ellipse. */
	public final Vector3 position;
	
	/** The entity position is equivalent to the initial position (u=0). */
	public final Vector3 entityPos;
	
	/** The center position of the orbit. */
	public final Vector3 centerPos;
					
	/** Represents the vector from the initial position (u=0) to the center of the orbit (a - axis). */
	private final Vector3 centerVec;

	/** The direction vector encodes the iteration direction (cw,ccw) and the b axis. */
	private final Vector3 directionVec;

	private final Vector3 normalVec;
	
	/** The ref center vec. */
	private final Vector3 currtDirApproximation,tempDirectionVec,
						  refDirectionVec,refCenterVec;

	/** The generated transformation matrix. */
	private Matrix44 transform;
	

	private Matrix44 dirRotationMatrix;
	
	/** The basic orientation of the object. */
	private final Matrix44  basicOrientation;
	
	/** The satellite transformation of the object. */
	private SatelliteTransformation satTrans;
	
	/** The mathematical basis of the orbit. */
	private Ellipse ellipse;

	/**
	 * Instantiates a new orbit.
	 */
	protected Orbit()
	{	
		//init
		position = new Vector3();
		entityPos = new Vector3();
		centerPos = new Vector3();
		normalVec = new Vector3();
		
		centerVec = new Vector3();
		directionVec = new Vector3();
		currtDirApproximation = new Vector3();
		tempDirectionVec = new Vector3();
		refDirectionVec = new Vector3();
		refCenterVec = new Vector3();
		
		transform = new Matrix44();
		basicOrientation = new Matrix44();
		dirRotationMatrix = new Matrix44();

		u = 0;
		
		dirVecRotationDiff = 0;
		dirVecRotationDiffStep = 0;
	}

	/**
	 * Instantiates a new orbit.
	 * 
	 * @param entityPos
	 *            the entity position is equivalent to the initial position (u=0)
	 * @param centerPos
	 *            the center position of the orbit
	 * @param directionVec
	 *            the direction vector encodes the iteration direction (cw,ccw) and the b axis
	 * @param speed
	 *            the iteration speed
	 * @param basicOrientation
	 *            the basic orientation of the object
	 */
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
		
		this.entityPos.set(entityPos);
		this.centerPos.set(centerPos);
		this.directionVec.set(directionVec);
		
		this.centerVec.set(this.entityPos);
		this.centerVec.subtract(this.centerPos);

		if(basicOrientation!=null)
			this.basicOrientation.copy(basicOrientation);

		Vector3.crossProduct(this.centerVec, this.directionVec, this.normalVec);
		this.normalVec.normalize();
		
		/**
		 * INFO: the given vectors are used per reference!
		 */
		this.ellipse = new Ellipse(this.centerPos,this.centerVec,this.directionVec);
		
		//stepsize relative so perimeter
		this.step = Constants.TWOPI/ellipse.perimeter;
		
		//check the size and speed of the orbit
		limitUniverse();
		
	}
	
	/**
	 * Iterate over the orbit.
	 *
	 * @param dt delta time between frames for a frame-independent motion
	 */
	public void update(float dt)
	{	
		
		//inc parameter
		u+=(speed*step*dt);
		if(u>=Constants.TWOPI)
			u-=Constants.TWOPI;

		//update sat transformation
		if(satTrans!=null)
			satTrans.update(dt);
		
		updateDirVecRotation(dt);
	
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
	
	private void updateDirVecRotation(float dt)
	{
		if(dirVecRotationDiff > 0)
		{
			dirVecRotationDiffIteration = dirVecRotationDiffStep*dt;
			
			if(dirVecRotationDiff-Math.abs(dirVecRotationDiffIteration)<0)
				dirVecRotationDiffIteration = Math.signum(dirVecRotationDiffStep)*dirVecRotationDiff;
			
			dirRotationMatrix.setIdentity();
			dirRotationMatrix.addRotate(normalVec, dirVecRotationDiffIteration);
			dirRotationMatrix.transformPoint(directionVec);
			//for axis scaling..
			dirRotationMatrix.transformPoint(refDirectionVec);
			dirVecRotationDiff -= (float)Math.abs(dirVecRotationDiffIteration);
		}
	}
	
	/**
	 * Update the linear transition of the speed.
	 *
	 * @param dt delta time between frames for a frame-independent transition
	 */
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
	
	/**
	 * Update the linear axis scaling.
	 *
	 * @param dt delta time between frames for a frame-independent transition
	 */
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
				centerDiffIteration = centerDiffStep * dt * aScalingMorphSpeed;
				
				if(centerDiff+centerDiffIteration<centerDiffFactor)
					centerDiffIteration = centerDiffFactor-centerDiff;
				//Log.d(LevelActivity.TAG,"centerDiff "+centerDiff +" centerDiffFactor" +centerDiffFactor+" centerDiffIteration "+centerDiffIteration );
			}
		}else{
			if(centerDiffFactor>centerDiff){
				doCenterVecScaling = true;
				centerDiffIteration = centerDiffStep * dt * aScalingMorphSpeed;
			
				if(centerDiff+centerDiffIteration>centerDiffFactor)
					centerDiffIteration = centerDiffFactor-centerDiff;
	
				//Log.d(LevelActivity.TAG,"centerDiff "+centerDiff +" centerDiffFactor" +centerDiffFactor+" centerDiffIteration "+centerDiffIteration );
			}
		}

		if(Math.signum(directionDiffStep)<0){
			if(directionDiffFactor<directionDiff){
				doDirectionVecScaling = true;
				directionDiffIteration = directionDiffStep * dt * bScalingMorphSpeed;
				
				if(directionDiff+directionDiffIteration<directionDiffFactor)
					directionDiffIteration = directionDiffFactor-directionDiff;
				
				//Log.d(LevelActivity.TAG,"directionDiff "+directionDiff +" directionDiffFactor" +directionDiffFactor+" directionDiffIteration "+directionDiffIteration );
			}
		}else{
			if(directionDiffFactor>directionDiff){
				doDirectionVecScaling = true;
				directionDiffIteration = directionDiffStep * dt * bScalingMorphSpeed;
				
				if(directionDiff+directionDiffIteration>directionDiffFactor)
					directionDiffIteration = directionDiffFactor-directionDiff;

				//Log.d(LevelActivity.TAG,"directionDiff "+directionDiff +" directionDiffFactor" +directionDiffFactor+" directionDiffIteration "+directionDiffIteration );
			}
		}
		
		if(doCenterVecScaling){
			centerDiff += centerDiffIteration;
			
			centerVec.set(refCenterVec);
			centerVec.multiply(centerDiff);
		}
		
		if(doDirectionVecScaling){
			directionDiff += directionDiffIteration;
			
			directionVec.set(refDirectionVec);
			directionVec.multiply(directionDiff);
		}
		
		if(doCenterVecScaling||doDirectionVecScaling){
			//change the stepsize relative to the new orbitsize
			updateStepSize();
		}

	}
	
	public void rotateDirectionVec(float angle,float stepSize){
		
		this.dirVecRotationDiff = (float)Math.toRadians(angle);
		this.dirVecRotationDiffStep = dirVecRotationDiff/stepSize;
		if(angle<0)
			this.dirVecRotationDiffStep*=-1;
		
		this.dirRotationMatrix.setIdentity();
		this.dirRotationMatrix.addRotate(normalVec, dirVecRotationDiffStep);
		Log.d(LevelActivity.TAG," NORMALVEC="+normalVec);
	}
	
	/**
	 * Evaluate the current position along the ellipse.
	 */
	private void evaluatePos()
	{
		//evaluate ellipse
		position.set(ellipse.getPoint(u));
				
		//reset transformation
		transform.setIdentity();
		
		//set basic orientation
		transform.mult(basicOrientation);
				
		//object transformation
		if(satTrans!=null)
			transform.mult(satTrans.getTransform());
		
		//transformation of the ellipsoid
		transform.addTranslate(position.x,position.y,position.z);
	}



	/**
	 * Provides a linear scaling of the orbit`s axis.
	 *
	 * @param aAxisFactor the scaling factor for the a axis - center vector (1 = no change)
	 * @param bAxisFactor the scaling factor for the b axis - direction vector (1 = no change)
	 * @param aAxisMorphSpeed the transition speed
	 */
	public void morphAxisScale(float aAxisFactor,float bAxisFactor,float aAxisMorphSpeed,float bAxisMorphSpeed)
	{
		aScalingMorphSpeed = aAxisMorphSpeed;
		bScalingMorphSpeed = bAxisMorphSpeed;
		refCenterVec.set(centerVec);
		refDirectionVec.set(directionVec);
		centerDiffFactor = aAxisFactor;
		centerDiffStep = (centerDiffFactor-1)/100;
		centerDiff = 1;
		
		directionDiffFactor = bAxisFactor;
		directionDiffStep = (directionDiffFactor-1)/100;
		directionDiff = 1;
	}
	
	/**
	 * Limit universe according to the limits set in the @see Config.
	 */
	private void limitUniverse()
	{
		this.centerVecCap = 1;
		this.directionVecCap = 1;
		
		if(this.centerVec.length()>Config.UNIVERSE_CENTERLENGTH_LIMIT)
			this.centerVecCap = Config.UNIVERSE_CENTERLENGTH_LIMIT/this.centerVec.length();
		
		if(this.directionVec.length()>Config.UNIVERSE_DIRLENGTH_LIMIT)
			this.directionVecCap = Config.UNIVERSE_DIRLENGTH_LIMIT/this.directionVec.length();
		
		Log.d(LevelActivity.TAG,"centerVecCap ="+centerVecCap+" directionVecCap="+directionVecCap);
		
		if((this.centerVecCap!=1 || this.directionVecCap != 1)){
			morphAxisScale(centerVecCap, directionVecCap, 20,20);
		}
		
		if(this.newSpeed > Config.UNIVERSE_SPEED_LIMIT){
			this.newSpeed = Config.UNIVERSE_SPEED_LIMIT;
		}
	}
	
	/**
	 * Change the step size relative to the new perimeter
	 * <code>oldPerimeter</code> and <code>oldStepSize</code>
	 * has to be set before calling this method.
	 */
	private void updateStepSize()
	{
		//!!!oldPerimeter and oldStepSize has to be already set
		
		this.ellipse.calcPerimeter();
		//stepsize should be the same relative to the new perimeter
		this.step = (oldStepSize*oldPerimeter)/ellipse.perimeter;
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion#morph(at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3)
	 */
	public void morph(Vector3 pushVec)
	{
		//stop scale morphing
		directionDiffFactor = directionDiff;
		centerDiffFactor = centerDiff;
		
		//approx current direction vec
		currtDirApproximation.set(ellipse.getPoint(u+step));
		currtDirApproximation.subtract(position);
		currtDirApproximation.normalize();
		
		//new speed evaluation
		tempDirectionVec.set(currtDirApproximation);
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
		this.entityPos.set(this.position);
		this.directionVec.set(currtDirApproximation);
		this.centerVec.set(this.entityPos);
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
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion#persist(java.io.DataOutputStream)
	 */
	@Override
	public void persist(DataOutputStream dos) throws IOException
	{
		dos.writeFloat(speed);
		dos.writeFloat(u);
		dos.writeFloat(step);

		//scale morphing
		dos.writeFloat(aScalingMorphSpeed);
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

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion#restore(java.io.DataInputStream)
	 */
	@Override
	public void restore(DataInputStream dis) throws IOException
	{
		speed = dis.readFloat();
		u = dis.readFloat();
		step = dis.readFloat();

		//scale morphing
		aScalingMorphSpeed = dis.readFloat();
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

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion#getSatTrans()
	 */
	@Override
	public SatelliteTransformation getSatTrans() {
		return satTrans;
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion#getCurrDirectionVec()
	 */
	@Override
	public Vector3 getCurrDirectionVec() {
		//approx current direction vec
		currtDirApproximation.set(ellipse.getPoint(u+step));
		currtDirApproximation.subtract(position);
		return currtDirApproximation;
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion#setTransform(at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44)
	 */
	@Override
	public void setTransform(Matrix44 transform) {
		this.transform = transform;		
	}


	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion#getSpeed()
	 */
	@Override
	public float getSpeed() {
		return this.speed;
	}
	

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion#setSatTrans(at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.SatelliteTransformation)
	 */
	public void setSatTrans(SatelliteTransformation satTrans) {
		this.satTrans = satTrans;
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion#getTransform()
	 */
	public Matrix44 getTransform() {
		return transform;
	}
}
