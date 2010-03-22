package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;

public class IntervalTransformation {

	private float intStart,intEnd,speed,qx,qy,qz,step,t;
	private Matrix44 transform;
	
	public IntervalTransformation(float intStart, float intEnd, float speed,
			float qy, float qz, float qx, Matrix44 basicTransform) {
	
		this.intStart = intStart;
		this.intEnd = intEnd;
		this.speed = speed;
		this.qy = qy;
		this.qz = qz;
		this.qx = qx;

		this.transform = new Matrix44();
		this.transform.mult(basicTransform);
		
		this.t = intStart;
		this.step = (intStart-intEnd)/100;
		
		
	}
	
	
	
	
	
	
}
