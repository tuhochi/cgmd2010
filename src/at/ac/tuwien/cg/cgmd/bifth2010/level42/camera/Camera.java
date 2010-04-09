package at.ac.tuwien.cg.cgmd.bifth2010.level42.camera;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.OGLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable;

//static imports
import static android.opengl.GLES10.*;

public class Camera implements Persistable
{
	private final OGLManager oglManager = OGLManager.instance;
	
	public final Vector3 viewPosition, eyePosition, upVector, 
					inverseViewVector, rightVector;
	
	private float 	minAltitude,maxAltitude, azimuth, altitude, 
					currentAzimuth, currentAltitude, motionFactor,
					minDistance,maxDistance;

	private float distance,currentDistance;
	private final float[] lastPosition;
	
	private final Matrix44 qx,qy;
	
	//temp vectors
	private Vector3 qyAxis,tempInverseViewVec,viewVecXZProjection;
	
	public Camera(	float distance,float minAltitude,float maxAltitude,
					float initAzimuth, float initAltitude, float motionFactor,
					float minDistance, float maxDistance)
	{

		this.distance = distance;
		this.currentDistance = distance;
		this.azimuth=initAzimuth;
		this.altitude=initAltitude;
		this.currentAzimuth=0;
		this.currentAltitude=0;
		this.minAltitude = minAltitude;
		this.maxAltitude = maxAltitude;
		this.motionFactor = motionFactor;
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
		
		this.qx = new Matrix44();
		this.qy = new Matrix44();
		this.qyAxis = new Vector3();
		this.viewVecXZProjection = new Vector3();
		this.tempInverseViewVec = new Vector3();
		this.rightVector = new Vector3();
		
		//init position
		eyePosition = new Vector3(0.0f,0.0f,distance);
		lastPosition = new float[2];
		lastPosition[0] = eyePosition.x;
		lastPosition[1] = eyePosition.y;
		viewPosition =  new Vector3(0.0f,0.0f,0.0f);
		inverseViewVector = Vector3.subtract(eyePosition,viewPosition);
		inverseViewVector.normalize();

		//right = forward x up = up x -forward
		Vector3.crossProduct(Constants.Y_AXIS, inverseViewVector, rightVector);
		rightVector.normalize();
		
		upVector = Vector3.crossProduct(inverseViewVector, rightVector);
		upVector.normalize();
	}
	
	public void look()
	{
		Matrix44 modelview = oglManager.getModelview();
		
		oglManager.gluLookAt(eyePosition, inverseViewVector, rightVector, upVector, modelview);
		
		glLoadMatrixf(modelview.getArray16(), 0);
	}
	
	public void setMousePosition(float x, float y)
	{
		//calc difference to last position
		float diffx = lastPosition[0]-x;
		float diffy = lastPosition[1]-y;
		//store last position
		lastPosition[0] = x;
		lastPosition[1] = y;
		//set new desired angle 
		azimuth += motionFactor * 30 * diffx;
		altitude += motionFactor * 30 * diffy;
		
		if(altitude>=maxAltitude)
			altitude = maxAltitude;
		if(altitude<=minAltitude)
			altitude = minAltitude;
		
	}
	
	public float getDistance()
	{
		return distance;
	}

	public void setDistance(float newDistance)
	{
		if(newDistance > maxDistance)
		{
			this.distance = maxDistance;
			return;
		}
		if(newDistance < minDistance)
		{
			this.distance = minDistance;
			return;
		}

		this.distance = newDistance;
	}
	
	public void updatePosition(float posX, float posY, float posZ, float dt)
	{
		//set view position
		viewPosition.x = posX;
		viewPosition.y = posY;
		viewPosition.z = posZ;
		
		//iterate 
		float azimuthDiff = azimuth - currentAzimuth;
		float altitudeDiff = altitude - currentAltitude;
		float distanceDiff = distance - currentDistance;

		if(azimuthDiff!=0||altitudeDiff!=0||distanceDiff!=0)
		{
			
			// calc the iteration step
			azimuthDiff *= motionFactor * 30.0f;
			altitudeDiff *= motionFactor * 30.0f;
			distanceDiff *= motionFactor * 30.0f;
	
			//reset
			qx.setIdentity();
			//rotate around the y axis
			qx.setRotate(Constants.Y_AXIS,(float)Math.toRadians(azimuthDiff));
			qx.transformPoint(inverseViewVector);
	
			// create the axis for the vertical rotation (altitude)
			viewVecXZProjection.copy(inverseViewVector);
			viewVecXZProjection.y = 0.0f;
			viewVecXZProjection.normalize();
	
			// normal of the XZ projection and the y axis = rotation axis
			Vector3.crossProduct(Constants.Y_AXIS,viewVecXZProjection,qyAxis);
			qyAxis.normalize();
	
			// rotatate view vec (grad)
			qy.setIdentity();
			qy.setRotate(qyAxis,(float)Math.toRadians(altitudeDiff));
	
			qy.transformPoint(inverseViewVector);
			inverseViewVector.normalize();
			
			//right = forward x up = up x -forward
			Vector3.crossProduct(Constants.Y_AXIS, inverseViewVector, rightVector);
			rightVector.normalize();
			
			Vector3.crossProduct(inverseViewVector, rightVector, upVector);
			upVector.normalize();
	
			// update current angle data
			currentAzimuth += azimuthDiff;
			currentAltitude += altitudeDiff;
			currentDistance += distanceDiff;
			
			
			//eyePosition = viewPosition + inverseViewVec * currentDistance
			eyePosition.copy(viewPosition);
			tempInverseViewVec.copy(inverseViewVector);
			eyePosition.add(tempInverseViewVec.multiply(currentDistance));
		}
	}
	
	public void setLastPosition(int x, int y)
	{
		this.lastPosition[0] = x;
		this.lastPosition[1] = y;
	}
	
	public void persist(DataOutputStream dos) throws IOException
	{
		dos.writeFloat(azimuth);
		dos.writeFloat(altitude);
		dos.writeFloat(distance);
		
		dos.writeFloat(currentAzimuth);
		dos.writeFloat(currentAltitude);
		dos.writeFloat(currentDistance);
		
		dos.writeFloat(minAltitude);
		dos.writeFloat(maxAltitude);
		dos.writeFloat(minDistance);
		dos.writeFloat(maxDistance);
		
		dos.writeFloat(motionFactor);
		
		eyePosition.persist(dos);
		inverseViewVector.persist(dos);
		rightVector.persist(dos);
		upVector.persist(dos);
		viewPosition.persist(dos);
	}
	
	public void restore(DataInputStream dis) throws IOException
	{
		azimuth = dis.readFloat();
		altitude = dis.readFloat();
		distance = dis.readFloat();
		
		currentAzimuth = dis.readFloat();
		currentAltitude = dis.readFloat();
		currentDistance = dis.readFloat();
		
		minAltitude = dis.readFloat();
		maxAltitude = dis.readFloat();
		minDistance = dis.readFloat();
		maxDistance = dis.readFloat();
		
		motionFactor = dis.readFloat();
		
		eyePosition.restore(dis);
		inverseViewVector.restore(dis);
		rightVector.restore(dis);
		upVector.restore(dis);
		viewPosition.restore(dis);
	}
}
