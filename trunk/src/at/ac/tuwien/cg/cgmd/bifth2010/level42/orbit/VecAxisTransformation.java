package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;

public class VecAxisTransformation extends SatelliteTransformation{

	private float qvStep,qvCurr;
	public float qv;
	private final Matrix44 transform,basicOrientation;
	public final Vector3 axis;

	public VecAxisTransformation() 
	{
		this.transform = new Matrix44();
		this.basicOrientation = new Matrix44();
		this.axis = new Vector3();
	}
	
	public VecAxisTransformation(Vector3 axis, float qv, Matrix44 basicOrientation) 
	{
		this();
		
		this.qv = (float)Math.toRadians(qv);
		this.qvStep = qv*Constants.TWOPI/100;
		this.qvCurr = 0;
		
		this.axis.copy(axis);

		if(basicOrientation!=null)
			this.basicOrientation.copy(basicOrientation);
	}
	
	public void update(float dt,float speed)
	{
		qvCurr += dt*qvStep*speed;
	
		if(qvCurr>=Constants.TWOPI)
			qvCurr-=Constants.TWOPI;
		
		transform.setIdentity();
		transform.mult(basicOrientation);
		transform.addRotate(axis,qvCurr);

		
	}

	public Matrix44 getTransform() {
		return transform;
	}	
	
	
	public void persist(DataOutputStream dos) throws IOException
	{
		dos.writeFloat(qv);
		dos.writeFloat(qvStep);
		dos.writeFloat(qvCurr);
		axis.persist(dos);
		basicOrientation.persist(dos);
	}
	
	public void restore(DataInputStream dis) throws IOException
	{
		this.qv = dis.readFloat();
		this.qvStep = dis.readFloat();
		this.qvCurr = dis.readFloat();
		axis.restore(dis);
		basicOrientation.restore(dis);
	}

	@Override
	public Matrix44 getBasicOrientaion() {
		return basicOrientation;
	}
	@Override
	public void setBasicOrientaion(Matrix44 basicOrientation) {
		this.qvCurr = 0;
		this.basicOrientation.copy(basicOrientation);
	}
}