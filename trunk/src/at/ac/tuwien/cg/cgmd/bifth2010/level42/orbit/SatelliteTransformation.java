package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;

public class SatelliteTransformation {

	private float 	qx,qy,qz,
					qxStep,qyStep,qzStep,
					qxCurr,qyCurr,qzCurr;
	private final Matrix44 transform;

	public SatelliteTransformation() 
	{
		this.transform = new Matrix44();
	}
	
	public SatelliteTransformation(float qx, float qy, float qz) 
	{
		this();
		
		this.qx = (float)Math.toRadians(qx);
		this.qy = (float)Math.toRadians(qy);
		this.qz = (float)Math.toRadians(qz);

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
		
	}

	public Matrix44 getTransform() {
		return transform;
	}	
	
	
	public void persist(DataOutputStream dos) throws IOException
	{
		dos.writeFloat(qx);
		dos.writeFloat(qy);
		dos.writeFloat(qz);
		
		dos.writeFloat(qxStep);
		dos.writeFloat(qyStep);
		dos.writeFloat(qzStep);

		dos.writeFloat(qxCurr);
		dos.writeFloat(qyCurr);
		dos.writeFloat(qzCurr);
	}
	
	public void restore(DataInputStream dis) throws IOException
	{
		this.qx = dis.readFloat();
		this.qy = dis.readFloat();
		this.qz = dis.readFloat();
		
		this.qxStep = dis.readFloat();
		this.qyStep = dis.readFloat();
		this.qzStep = dis.readFloat();

		this.qxCurr = dis.readFloat();
		this.qyCurr = dis.readFloat();
		this.qzCurr = dis.readFloat();
	}
}
