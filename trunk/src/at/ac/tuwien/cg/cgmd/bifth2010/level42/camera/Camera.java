package at.ac.tuwien.cg.cgmd.bifth2010.level42.camera;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;

//static imports
import static android.opengl.GLU.*;

public class Camera
{

	
	private Vector3 viewPosition, eyePosition, upVector, orientationVec, 
					inverseViewVec;
	
	private float 	minAltitude,maxAltitude, azimuth, altitude, 
					currentAzimuth, currentAltitude, motionFactor,
					minDistance,maxDistance,focalLength;

	private float distance,currentDistance;
	private float[] lastPosition;
	
	private Matrix44 qx,qy;
	
	//temp vectors
	private Vector3 yAxis,qyAxis,tempInverseViewVec,viewVecXZProjection;
	
	public Camera(	float distance,float minAltitude,float maxAltitude,
					float initAzimuth, float initAltitude, float motionFactor,
					float minDistance, float maxDistance)
	{
		
		orientationVec = new Vector3(0.0f,0.0f,-1.0f);
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
		
		this.yAxis = new Vector3(0.0f,1.0f,0.0f);
		this.qx = new Matrix44();
		this.qy = new Matrix44();
		this.qyAxis = new Vector3();
		this.viewVecXZProjection = new Vector3();
		this.tempInverseViewVec = new Vector3();
		
		//init position
		eyePosition = new Vector3(0.0f,0.0f,distance);
		lastPosition = new float[2];
		lastPosition[0] = eyePosition.x;
		lastPosition[1] = eyePosition.y;
		viewPosition =  new Vector3(0.0f,0.0f,0.0f);
		inverseViewVec = Vector3.subtract(eyePosition,viewPosition);
		inverseViewVec.normalize();
		upVector = new Vector3(0.0f,1.0f,0.0f);
	}
	
	public void look(GL10 gl)
	{
		gluLookAt(gl,eyePosition.x, eyePosition.y, eyePosition.z,
			viewPosition.x, viewPosition.y, viewPosition.z, upVector.x, upVector.y, upVector.z);
	}
	
	public void setMousePosition(int x,int y)
	{
		//calc difference to last position
		float diffx = lastPosition[0]-(float)x;
		float diffy = lastPosition[1]-(float)y;
		//store last position
		lastPosition[0] = (float)x;
		lastPosition[1] = (float)y;
		//set new desired angle 
		azimuth += motionFactor * 30 * diffx;
		altitude += motionFactor * 30 * diffy;
		
		if(altitude>=maxAltitude)
			altitude = maxAltitude;
		if(altitude<=minAltitude)
			altitude = minAltitude;
		
	}
	
	public float getDistance() {
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
	
	public void updatePosition(Vector3 targetPosition, float dt)
	{
		//set view position
		viewPosition = targetPosition;
		
		//iterate 
		float azimuthDiff = azimuth - currentAzimuth;
		float altitudeDiff = altitude - currentAltitude;
		float distanceDiff = distance - currentDistance;

		if(azimuthDiff!=0.0f||altitudeDiff!=0.0){
			
			// calc the iteration step
			azimuthDiff *= motionFactor * 30.0f;
			altitudeDiff *= motionFactor * 30.0f;
			distanceDiff *= motionFactor * 30.0f;
	
			//reset
			qx.setIdentity();
			//rotate around the y axis
			qx.setRotate(yAxis,(float)Math.toRadians(azimuthDiff));
			qx.transformPoint(inverseViewVec);
	
			// create the axis for the vertical rotation (altitude)
			viewVecXZProjection.copy(inverseViewVec);
			viewVecXZProjection.y = 0.0f;
			viewVecXZProjection.normalize();
	
			// normal of the XZ projection and the y axis = rotation axis
			Vector3.crossProduct(yAxis,viewVecXZProjection,qyAxis);
			qyAxis.normalize();
	
			// rotatate view vec (grad)
			qy.setIdentity();
			qy.setRotate(qyAxis,(float)Math.toRadians(altitudeDiff));
	
			qy.transformPoint(inverseViewVec);
			inverseViewVec.normalize();
	
			// update current angle data
			currentAzimuth += azimuthDiff;
			currentAltitude += altitudeDiff;
			currentDistance += distanceDiff;
			
			
			//eyePosition = viewPosition + inverseViewVec * currentDistance
			eyePosition.copy(viewPosition);
			tempInverseViewVec.copy(inverseViewVec);
			eyePosition.add(tempInverseViewVec.multiply(currentDistance));
		}
	}

	
	public void setLastPosition(int x, int y) {
		this.lastPosition[0] = x;
		this.lastPosition[1] = y;
	}
}
