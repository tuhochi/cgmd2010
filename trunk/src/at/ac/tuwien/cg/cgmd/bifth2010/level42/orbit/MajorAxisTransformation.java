package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;

// TODO: Auto-generated Javadoc
/**
 * The Class MajorAxisTransformation represents a rotation over the major axis.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class MajorAxisTransformation extends SatelliteTransformation
{

	private float 	qx,qy,qz,
					qxStep,qyStep,qzStep,
					qxCurr,qyCurr,qzCurr,
					speed;
	

	/** The transformation matrix */
	private final Matrix44 transform;
	
	/** The basic orientation of the object */
	private final Matrix44 basicOrientation;

	/**
	 * Instantiates a new major axis transformation.
	 */
	public MajorAxisTransformation() 
	{
		this.transform = new Matrix44();
		this.basicOrientation = new Matrix44();
	}
	
	/**
	 * Instantiates a new major axis transformation.
	 *
	 * @param qx the rotation over the x axis
	 * @param qy the rotation over the y axis
	 * @param qz the rotation over the z axis
	 * @param speed the relative rotation speed
	 * @param basicOrientation the basic orientation of the object
	 */
	public MajorAxisTransformation(float qx, float qy, float qz, float speed, Matrix44 basicOrientation) 
	{
		this();
		
		this.qx = (float)Math.toRadians(qx);
		this.qy = (float)Math.toRadians(qy);
		this.qz = (float)Math.toRadians(qz);

		this.qxStep = this.qx*Constants.TWOPI/100;
		this.qyStep = this.qy*Constants.TWOPI/100;
		this.qzStep = this.qz*Constants.TWOPI/100;
		
		this.qxCurr = 0;
		this.qyCurr = 0;
		this.qzCurr = 0;
		
		this.speed = speed;
		
		if(basicOrientation!=null)
			this.basicOrientation.copy(basicOrientation);
	}
	
	public void update(float dt)
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
		transform.mult(basicOrientation);
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
		
		this.basicOrientation.persist(dos);
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
		
		this.basicOrientation.restore(dis);
	}
	
	
	@Override
	public Matrix44 getBasicOrientation() {
		return basicOrientation;
	}
	
	
	@Override
	public void setBasicOrientation(Matrix44 basicOrientation) {
		this.basicOrientation.copy(basicOrientation);
	}
}
