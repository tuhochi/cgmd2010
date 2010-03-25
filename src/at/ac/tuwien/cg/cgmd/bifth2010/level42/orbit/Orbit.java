package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;

public class Orbit {

	private float 	speed,t,
					a,b,c,
					v,u;
	private int direction;
	private Vector3 pos;
	private Matrix44 transform,tempTransform;
	private ObjectOrientation orientation;
	private SceneEntity entity;
	private SatelliteTransformation satTrans;
	
	public static final int DIRECTION_POSITIVE = 0;
	public static final int DIRECTION_NEGATIVE = 1;
	
	public static final int STARTPOINT_A = 0;
	public static final int STARTPOINT_B = 1;
	
	//temp vars
	private float sinu,cosu,sinv,cosv,step;
	private Vector3 projection;
	
	public Orbit(	SceneEntity entity, float speed, float t,int direction,
					float a, float b, float c, 
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
		this.c = c;
		this.v = 0;

		precalc();
	}
	
	
	
	public Orbit(	SceneEntity entity,
					Vector3 vecToCenter,Vector3 directionVec,
					int startpoint, float otherAxis, float c,
					float speed	)
	{
		//init vars and temp calcs
		init();
		
		//init
		this.entity = entity;
		this.speed = speed;
		this.orientation = new ObjectOrientation();
		this.v = 0;
	
		switch(startpoint){
			case STARTPOINT_A:
					//main axis
					this.a = vecToCenter.length();
					this.b = otherAxis;
					this.c = c;
					//get orientation
					extractOrientation(vecToCenter);
					
					if(Vector3.getAngle(vecToCenter,directionVec)>0){
						this.direction = Orbit.DIRECTION_POSITIVE;
						this.t = 0.25f;
					}else{
						this.direction = Orbit.DIRECTION_NEGATIVE;
						this.t = 0.25f;
					}
					
				break;
			case STARTPOINT_B:
					//main axis
					this.a = otherAxis;
					this.b = vecToCenter.length();
					this.c = c;
					//get orientation
					extractOrientation(vecToCenter);
					
					if(Vector3.getAngle(vecToCenter,directionVec)>0){
						this.direction = Orbit.DIRECTION_POSITIVE;
						this.t = 0.0f;
					}else{
						this.direction = Orbit.DIRECTION_NEGATIVE;
						this.t = 0.0f;
					}
				break;
		}
		
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
		//precalc sin/cos
		sinv = (float)Math.sin(v);
		cosv = (float)Math.cos(v);

		//calc start
		u = (float)((t*Constants.TWOPI)-Math.PI);
		step = Constants.TWOPI/100;
	}
	
	private void extractOrientation(Vector3 vecToCenter)
	{
		//get rotations
		projection.copy(vecToCenter);
		projection.y = 0;
		projection.normalize();
		this.orientation.qx = (projection.length()!=0)?Vector3.getAngle(Constants.X_AXIS, projection):0;
		
		projection.copy(vecToCenter);
		projection.z = 0;
		projection.normalize();
		this.orientation.qy = (projection.length()!=0)?Vector3.getAngle(Constants.Y_AXIS, projection):0;
		
		projection.copy(vecToCenter);
		projection.x = 0;
		projection.normalize();
		this.orientation.qz = (projection.length()!=0)?Vector3.getAngle(Constants.Z_AXIS, projection):0;
		
		//get translation
		this.orientation.tx = vecToCenter.x;
		this.orientation.ty = vecToCenter.y;
		this.orientation.tz = vecToCenter.z;

		//build matrix
		orientation.update();
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
		
		pos.x = a * sinu * cosv;
		pos.y = b * sinu * sinv;
		pos.z = c * cosu;

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
