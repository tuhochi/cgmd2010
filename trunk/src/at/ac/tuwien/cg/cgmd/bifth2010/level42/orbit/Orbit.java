package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;

public class Orbit {

	private float 	speed,t,
					a,b,
					u;
	private int direction;
	private Vector3 pos,entityPos,centerPos,centerVec,toCenterVec,directionVec;
	private Matrix44 transform,tempTransform;
	private ObjectOrientation orientation;
	private SceneEntity entity;
	private SatelliteTransformation satTrans;
	private boolean up,inverted;
	
	public static final int DIRECTION_POSITIVE = 0;
	public static final int DIRECTION_NEGATIVE = 1;
	
	public static final int STARTPOINT_A = 0;
	public static final int STARTPOINT_C = 1;
	
	//temp vars
	private float sinu,cosu,sinv,cosv,step;
	private Vector3 projection,normalVec;
	
	public Orbit(	SceneEntity entity, float speed, float t,int direction,
					float a, float b, 
					ObjectOrientation orientation
				) 
	{
		init();	
		
		this.entity = entity;
		this.speed = speed;
		this.t = t;
		this.direction = direction;
		this.orientation = orientation;
		
		this.a = a;
		this.b = b;

		precalc();
	}
	
	
	
	public Orbit(	SceneEntity entity,
					Vector3 entityPos,Vector3 centerPos,Vector3 directionVec,
					float b, float speed	
				)
	{
		//init vars and temp calcs
		init();
		
		//init
		this.entity = entity;
		this.speed = speed;
		this.orientation = new ObjectOrientation();
		
		this.entityPos = entityPos;
		this.centerPos = centerPos;
		this.centerVec = Vector3.subtract(entityPos,centerPos);
		this.toCenterVec = Vector3.subtract(centerPos,entityPos);
		this.directionVec = directionVec;
		
		this.normalVec = Vector3.crossProduct(directionVec,toCenterVec);
		this.normalVec.normalize();
		
		this.inverted = false;
		this.up = true;
		if( Vector3.getAngle(normalVec,Constants.Y_AXIS)>((float)Math.PI/2)){
			this.up = false;
		}
				
		//main axis
		this.a = centerVec.length();
		this.b = b;

		//get orientation
		extractOrientation();

		if(up)
			this.direction = (!inverted)?Orbit.DIRECTION_POSITIVE:Orbit.DIRECTION_NEGATIVE;
		else
			this.direction = (!inverted)?Orbit.DIRECTION_NEGATIVE:Orbit.DIRECTION_POSITIVE;
				
		
		precalc();
	}
	
	private void init()
	{
		//temp var init
		tempTransform = new Matrix44();
		projection = new Vector3();
		
		//set transform matrix for ellipsoid
		transform = new Matrix44();
		pos = new Vector3();
	}
	
	private void precalc()
	{
		//calc start
		u = (float)(t*Constants.TWOPI);
		step = Constants.TWOPI/100;
	}
	
	private void extractOrientation()
	{

		Vector3 crossEbenen = Vector3.crossProduct(normalVec, Constants.Y_AXIS);
		crossEbenen.normalize();
	
		//get translation
		this.orientation.reset();

		projection.copy(centerVec);
		projection.y = 0;
		projection.normalize();
		
		float basicOrientation = (projection.length()!=0)?Vector3.getAngle(Constants.X_AXIS, projection):0;
		orientation.transform.addRotateY(basicOrientation);
		
		if(crossEbenen.length()!=0){
			float normalVecRot = -Vector3.getAngle(normalVec,Constants.Y_AXIS);
			if(normalVecRot>((float)Math.PI/2)){
				inverted = true;
				//orientation.transform.addRotateY((float)Math.PI);
			}
			orientation.transform.addRotate(crossEbenen,normalVecRot);
		}
	
		
		orientation.transform.addTranslate(centerPos.x, centerPos.y, centerPos.z);
	
	}
	
	public void updatePos(float dt)
	{
	
		if(direction == DIRECTION_POSITIVE)
		{
			u+=(speed*step*dt);
			if(u>=Constants.TWOPI)
				u-=Constants.TWOPI;
		}else if(direction == DIRECTION_NEGATIVE)
		{
			u-=(speed*step*dt);
			if(u<=-Constants.TWOPI)
				u+=Constants.TWOPI;
		}

		if(satTrans!=null)
			satTrans.update(dt,speed);
		
		calcPos();
		//update transformation in the entity
		entity.setTransformation(transform);
	}
	
	private void calcPos()
	{
		//evaluate ellipsoid
		cosu = (float)Math.cos(u);
		sinu = (float)Math.sin(u);
		
		pos.x = a * cosu;
		pos.y = 0;
		pos.z = b * sinu;

		//translate position on ellipsoid
		transform.setTranslate(pos.x,pos.y, pos.z);
		
		if(satTrans!=null)
			transform.mult(satTrans.getTransform());
		
		//transformation of the ellipsoid
		tempTransform.copy(orientation.getTransform());
		tempTransform.mult(transform);
		
		transform.copy(tempTransform);
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
