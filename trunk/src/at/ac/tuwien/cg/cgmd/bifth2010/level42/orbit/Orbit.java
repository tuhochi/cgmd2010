package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;

public class Orbit {

	private float 	speed,t,direction,
					a,b,c,
					v,u,
					qx,qy,qz,
					tx,ty,tz;
	private Vector3 pos;
	private Matrix44 orbitTransform,transform,tempTransform;
	private SceneEntity entity;
	
	public static final int DIRECTION_POSITIVE = 0;
	public static final int DIRECTION_NEGATIVE = 1;
	
	//temp vars
	private float sinu,cosu,sinv,cosv,step,twoPI;
	
	public Orbit(	SceneEntity entity, float speed, float v, float t,int direction,
					float a, float b, float c,  float qx,
					float qy,float qz,float tx,float ty,float tz
				) 
	{
		this.entity = entity;
		this.speed = speed;
		this.t = t;
		this.direction = direction;
		
		this.a = a;
		this.b = b;
		this.c = c;
		this.v = v;
		this.qx = qx;
		this.qy = qy;
		this.qz = qz;
		
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
		
		this.sinv = (float)Math.sin(this.v);
		this.cosv = (float)Math.cos(this.v);
				
		this.twoPI = (float)(Math.PI*2);
		
		//calc start
		this.u = (float)((t*twoPI)-Math.PI);
		this.step = twoPI/100;

		this.pos = new Vector3();
		
		//set transform matrix for ellipsoid
		transform = new Matrix44();
		orbitTransform = new Matrix44();
		tempTransform = new Matrix44();
		 
		orbitTransform.addRotateX((float)Math.toRadians(this.qx));
		orbitTransform.addRotateY((float)Math.toRadians(this.qy));
		orbitTransform.addRotateZ((float)Math.toRadians(this.qz));
		orbitTransform.addTranslate(this.tx, this.ty, this.tz);
		
	}
	
	
	public void updatePos(float dt)
	{
	
		if(direction == DIRECTION_POSITIVE)
		{
			u+=(speed*step*dt);
			if(u>=twoPI)
				u-=twoPI;
		}else if(direction == DIRECTION_NEGATIVE)
		{
			u-=(speed*step*dt);
			if(u<=-twoPI)
				u+=twoPI;
		}

		calcPos();
		//update transformation in the entity
		entity.setTransformation(transform);
	}
	
	private void calcPos()
	{
		//evaluate ellipsoid
		cosu = (float)Math.cos(u);
		sinu = (float)Math.sin(u);
		
		pos.x = a * cosu * cosv;
		pos.y = b * cosu * sinv;
		pos.z = c * sinu;

		//translate position on ellipsoid
		transform.setTranslate(pos.x,pos.y, pos.z);
		
		//transformation of the ellipsoid
		tempTransform.copy(orbitTransform);
		tempTransform.mult(transform);
		
		transform.copy(tempTransform);
	}


	public Matrix44 getTransform() {
		return transform;
	}


	public void setTransform(Matrix44 transform) {
		this.transform = transform;
	}


	public Matrix44 getOrbitTransform() {
		return orbitTransform;
	}


	public void setOrbitTransform(Matrix44 orbitTransform) {
		this.orbitTransform = orbitTransform;
	}
	
}
