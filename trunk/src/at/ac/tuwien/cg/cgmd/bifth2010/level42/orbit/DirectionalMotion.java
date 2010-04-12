package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;

public class DirectionalMotion extends Motion
{

	private final Vector3 directionVec,currentPos,tempDirectionVec;
	private float speed;
	private SatelliteTransformation satTrans;
	private Matrix44 transform;
	
	public DirectionalMotion(Vector3 startPos,Vector3 directionVec,float speed)
	{
		//init
		this();
		this.directionVec.copy(directionVec.normalize());
		this.currentPos.copy(startPos);
		this.speed = speed;
	}
	
	protected DirectionalMotion()
	{
		//init
		this.transform = new Matrix44();
		this.directionVec = new Vector3();
		this.currentPos = new Vector3();
		this.tempDirectionVec = new Vector3();
	}
	
	public void update(float dt)
	{
		tempDirectionVec.copy(directionVec);
		tempDirectionVec.multiply(speed);
		
		currentPos.add(tempDirectionVec);
	
		transform.setIdentity();
		
		if(satTrans!=null){
			satTrans.update(dt,speed);
			transform.mult(satTrans.getTransform());
		}
		
		transform.addTranslate(currentPos.x, currentPos.y, currentPos.z);
	}

	public Matrix44 getTransform() {
		return transform;
	}

	public void setSatTrans(SatelliteTransformation satTrans) {
		this.satTrans = satTrans;
	}

	@Override
	public void persist(DataOutputStream dos) throws IOException
	{
		this.directionVec.persist(dos);
		this.currentPos.persist(dos);
		dos.writeFloat(this.speed);
		
		if(satTrans != null){
			dos.writeBoolean(true);
			dos.writeUTF(satTrans.getClass().getName());
			this.satTrans.persist(dos);
		}else
			dos.writeBoolean(false);
	}

	@Override
	public void restore(DataInputStream dis) throws IOException
	{
		this.directionVec.restore(dis);
		this.currentPos.restore(dis);
		this.speed = dis.readFloat();
		
		if(dis.readBoolean()){
			String className = dis.readUTF();
			this.satTrans = SatelliteTransformation.restore(dis,className);
		}
	}
	

	@Override
	public SatelliteTransformation getSatTrans() {
		return satTrans;
	}

	@Override
	public Vector3 getCurrDirectionVec() {
		return directionVec;
	}

	@Override
	public void setTransform(Matrix44 transform) {
		this.transform = transform;
	}
	
	@Override
	public float getSpeed() {
		return this.speed;
	}

	@Override
	public void morph(Vector3 pushVec) {
		// TODO Auto-generated method stub
		
	}
}
