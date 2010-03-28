package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;

public class ObjectOrientation {
	
	public float 	qx,qy,qz,
					tx,ty,tz,
					sx,sy,sz;
	
	public Matrix44 transform;

	public ObjectOrientation(float qx, float qy, float qz, float tx, float ty,
			float tz, float sx, float sy, float sz) {
		
		initValues();
		
		this.qx = (float) Math.toRadians(qx);
		this.qy = (float) Math.toRadians(qy);
		this.qz = (float) Math.toRadians(qz);
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
		
		transform = new Matrix44();
		update();
	}
	
	public ObjectOrientation(float qx, float qy, float qz) {
		
		initValues();
		
		this.qx = (float) Math.toRadians(qx);
		this.qy = (float) Math.toRadians(qy);
		this.qz = (float) Math.toRadians(qz);

		transform = new Matrix44();
		update();
	}
	
	public ObjectOrientation(float qx, float qy, float qz, float tx, float ty, float tz) {
		
		initValues();
		this.qx = (float) Math.toRadians(qx);
		this.qy = (float) Math.toRadians(qy);
		this.qz = (float) Math.toRadians(qz);
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
		transform = new Matrix44();
		update();
	}
	
	private void initValues()
	{
		this.qx = 0;
		this.qy = 0;
		this.qz = 0;
		this.tx = 0;
		this.ty = 0;
		this.tz = 0;
		this.sx = 1;
		this.sy = 1;
		this.sz = 1;
		
	}
	
	public ObjectOrientation()
	{
		initValues();
		transform = new Matrix44();
		update();
	}
	
	public void reset()
	{
		initValues();
		update();
	}
	
	public void update()
	{
		transform.setIdentity();
		
		transform.addRotateX(qx);
		transform.addRotateY(qy);
		transform.addRotateZ(qz);
		
		transform.addScale(sx, sy, sz);
		
		transform.addTranslate(tx, ty, tz);
	}

	public Matrix44 getTransform() 
	{
		return transform;
	}
}
