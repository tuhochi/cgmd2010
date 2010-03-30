package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;

public class SatelliteTransformation {

	private float 	qx,qy,qz,
					qxStep,qyStep,qzStep,
					qxCurr,qyCurr,qzCurr;
	private final Matrix44 transform,basicTransform;
	
	public SatelliteTransformation(float qx, float qy, float qz, Matrix44 basicTransform) 
	{
		this.qx = (float)Math.toRadians(qx);
		this.qy = (float)Math.toRadians(qy);
		this.qz = (float)Math.toRadians(qz);

		this.transform = new Matrix44();
		this.basicTransform = basicTransform;
					
		this.qxStep = qx*Constants.TWOPI/100;
		this.qyStep = qy*Constants.TWOPI/100;
		this.qzStep = qz*Constants.TWOPI/100;
		
		this.qxCurr = 0;
		this.qyCurr = 0;
		this.qzCurr = 0;
	}
	
	public void update(float dt,float speed)
	{
		qxCurr += dt*qxStep*speed;
		qyCurr += dt*qyStep*speed;
		qzCurr += dt*qzStep*speed;
		
		if(qxCurr>=Constants.TWOPI)
			qxCurr-=Constants.TWOPI;
		if(qyCurr>=Constants.TWOPI)
			qyCurr-=Constants.TWOPI;
		if(qzCurr>=Constants.TWOPI)
			qzCurr-=Constants.TWOPI;
		
		transform.setIdentity();
		transform.addRotateX(qxCurr);
		transform.addRotateY(qyCurr);
		transform.addRotateZ(qzCurr);
		
		if(basicTransform!=null)
			transform.mult(basicTransform);
	}

	public Matrix44 getTransform() {
		return transform;
	}	
	
}
