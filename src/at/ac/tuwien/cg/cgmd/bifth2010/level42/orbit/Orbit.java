package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;

public class Orbit {

	private float 	speed,
					a,b,c,
					v,u,
					qx,qy,qz,
					tx,ty,tz;
	private Vector3 pos;
	private Matrix44 orbitTransform,transform;
	
	//temp vars
	private float sinu,cosu,sinv,cosv,step,twoPI;
	
	public Orbit(float speed, float v, float a, float b, float c,  float qx,
			float qy,float qz,float tx,float ty,float tz) 
	{
		this.speed = speed;
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
		this.u = (float) -Math.PI;
		this.step = twoPI/100;

		this.pos = new Vector3();
		
		//set transform matrix for ellipsoid
		transform = new Matrix44();
		orbitTransform = new Matrix44();
		
		orbitTransform.addRotateX((float)Math.toRadians(this.qx));
		orbitTransform.addRotateY((float)Math.toRadians(this.qy));
		orbitTransform.addRotateZ((float)Math.toRadians(this.qz));
		orbitTransform.addTranslate(this.tx, this.ty, this.tz);
		
	}
	
	
	public void updatePos(float dt)
	{
		u+=(speed*step*dt);
		if(u==twoPI)
			u=0;

		calcPos();
	}
	
	private void calcPos()
	{
		cosu = (float)Math.cos(u);
		sinu = (float)Math.sin(u);
		
		pos.x = a * cosu * cosv;
		pos.y = b * cosu * sinv;
		pos.z = c * sinu;

		transform.setIdentity();
		transform.addTranslate(pos.x,pos.y, pos.z);
		transform.mult(orbitTransform);
		
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
