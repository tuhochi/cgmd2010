package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;

public class SatelliteTransformation {

	private float 	qx,qy,qz,
					qxStep,qyStep,qzStep,
					qxCurr,qyCurr,qzCurr;
	private Matrix44 transform,basicTransform;
	
	//temp vars
	private float twoPI;
	
	public SatelliteTransformation(float qx, float qy, float qz, Matrix44 basicTransform) 
	{

		this.qy = qy;
		this.qz = qz;
		this.qx = qx;

		this.transform = new Matrix44();
		this.basicTransform = basicTransform;
					
		this.twoPI = (float)(Math.PI*2);
		this.qxStep = qx*360/100;
		this.qyStep = qy*360/100;
		this.qzStep = qz*360/100;
		
		this.qxCurr = 0;
		this.qyCurr = 0;
		this.qzCurr = 0;
	}
	
	public void update(float dt,float speed)
	{
		
		qxCurr += dt*qx*qxStep*speed;
		qyCurr += dt*qy*qyStep*speed;
		qzCurr += dt*qz*qzStep*speed;
		
		if(qxCurr>=360)
			qxCurr-=360;
		if(qyCurr>=360)
			qyCurr-=360;
		if(qzCurr>=360)
			qzCurr-=360;
		
		transform.setIdentity();
		transform.addRotateX((float)Math.toRadians(qxCurr));
		transform.addRotateY((float)Math.toRadians(qyCurr));
		transform.addRotateZ((float)Math.toRadians(qzCurr));
		
		if(basicTransform!=null)
			transform.mult(basicTransform);
	}

	public Matrix44 getTransform() {
		return transform;
	}	
	
}
