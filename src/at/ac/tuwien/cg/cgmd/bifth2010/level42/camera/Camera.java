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
		
		//calc the current altitude
		currentAltitude = 90.0f-radToDeg((float)Math.acos(Vector3.dotProduct(new Vector3(0.0f,1.0f,0.0f),Vector3.normalize(inverseViewVec))));
		
		//iterate 
		float azimuthDiff = azimuth - currentAzimuth;
		float altitudeDiff = altitude - currentAltitude;
		float distanceDiff = distance - currentDistance;

		// calc the iteration step
		azimuthDiff *= motionFactor * 30.0f;
		altitudeDiff *= motionFactor * 30.0f;
		distanceDiff *= motionFactor * 30.0f;

		Matrix44 qx = Matrix44.getRotate(new Vector3(0.0f, 1.0f, 0.0f), Camera.degToRad(azimuthDiff));
		inverseViewVec.transform(qx);

		// create the axis for the vertical rotation (altitude)
		Vector3 viewVecXZProjection = inverseViewVec;
		viewVecXZProjection.y = 0.0f;
		viewVecXZProjection.normalize();

		// normal of the XZ projection and the y axis = rotation axis
		Vector3 qyAxis = Vector3.crossProduct(new Vector3(0.0f, 1.0f, 0.0f),viewVecXZProjection);
		qyAxis.normalize();

		// rotatate view vec (grad)
		Matrix44 qy = Matrix44.getRotate(qyAxis, Camera.degToRad(altitudeDiff));

		inverseViewVec.transform(qy);
		inverseViewVec.normalize();

		// update current angle data
		currentAzimuth += azimuthDiff;
		currentDistance += distanceDiff;
		eyePosition = Vector3.add(viewPosition,Vector3.multiply(inverseViewVec,currentDistance));
	}

	
	public void setLastPosition(int x, int y) {
		this.lastPosition[0] = x;
		this.lastPosition[1] = y;
	}

	private static float radToDeg(float rad)
	{
		return (rad*180.0f)/((float)Math.PI);
	}
	
	private static float degToRad(float deg)
	{
		return (deg*((float)Math.PI))/180.0f;
	}
}
