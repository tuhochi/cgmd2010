package at.ac.tuwien.cg.cgmd.bifth2010.level17.math;

import javax.microedition.khronos.opengles.GL10;
import android.util.Log;

/*** 
 * A Picker class that is used to transform screen space coordinates 
 * into world coordinates.
 * 
 * Makes use of the MatrixGrabber class from the android sdk samples
 * to track view and projection matrices.
 * 
 * @author Johannes Scharl
 */
public class Picker 
{
	private static final String LOG_TAG = "Picker";
	
	// used to grab the current view and projection matrices
	private MatrixGrabber mMatrixGrabber = new MatrixGrabber();
	/*private int mScreenWidth;
	private int mScreenHeight;*/
	private Matrix4x4 mViewProjection = new Matrix4x4();
	private Matrix4x4 mInvViewProjection = new Matrix4x4();
	private float mLastDepth;


	/**
	 * Updates the View and Projection Matrices
	 * and stores width and height
	 * These values are needed to project device screen coordinates to
	 * world space coordinates
	 * 
	 * @param gl - The OpenGL context
	 * @param width - The width of the screen
	 * @param height - The height of the screen
	 */
	public void CameraChanged(GL10 gl)
	{
		// calculate matrices
		mMatrixGrabber.getCurrentState(gl);
		Matrix4x4 view = new Matrix4x4();
		view.fromFloatArray(mMatrixGrabber.mModelView);
		Matrix4x4 projection = new Matrix4x4();
		projection.fromFloatArray(mMatrixGrabber.mProjection);

		mViewProjection = Matrix4x4.mult(projection, view);
		mInvViewProjection = mViewProjection.InvertedCopy();
    	Log.d(LOG_TAG, "View: " + view.toString());
    	Log.d(LOG_TAG, "Projection: " + projection.toString());
    	Log.d(LOG_TAG, "View Projection: " + mViewProjection.toString());
    	Log.d(LOG_TAG, "Inverse View Projection: " + mInvViewProjection.toString());
	}

	/**
	 * Sets a world position that is stored as reference.
	 * World positions returned in the future will have the same
	 * screen space depth as this position
	 * 
	 * @param referencePosition - The position to store
	 */
	public void SetReferencePosition(Vector3 referencePosition)
	{
		// project the referencePosition into screen space
		Vector4 pos4dim = new Vector4(referencePosition);
		Vector4 screenSpacePos = mViewProjection.transformPoint(pos4dim);
		screenSpacePos.homogenize();
    	Log.d(LOG_TAG, "Calculated Screen Space Position: " + screenSpacePos.toString());
		
		mLastDepth = screenSpacePos.z; // for some reason, the y axis contains the depth, not the z axis as one would expect...
    	Log.d(LOG_TAG, "Calculated Depth: " + mLastDepth);
	}
	
	/**
	 * Unprojects a device screen position to a world space coordinates
	 * As depth, the depth of the last projected referencePosition
	 * is taken.
	 * 
	 * @param pickerCoordinates - The position on the screen (device coordinates)
	 */
	public Vector3 GetWorldPosition(Vector2 pickerCoordinates)
	{
    	Log.d(LOG_TAG, "Device Coordinates: " + pickerCoordinates.toString());
		// calculate picking position in screen space
		Vector3 screenSpace = new Vector3();
		screenSpace.y = Device2ScreenSpace(pickerCoordinates.x);
		screenSpace.x = Device2ScreenSpace(pickerCoordinates.y);
    	Log.d(LOG_TAG, "Screen Coordinates: " + screenSpace.toString());
		// set the same depth as in screenSpacePos.y
		screenSpace.z = mLastDepth;
		// unproject the picking position to world coordinates
		Vector4 screenSpace4dim = new Vector4(screenSpace);
		Vector4 worldSpace = mInvViewProjection.transformPoint(screenSpace4dim);
    	Log.d(LOG_TAG, "Transformed World Coordinates: " + worldSpace.toString());
		// return these coordinates homogenized
		return new Vector3(worldSpace);
	}
	
	private float Device2ScreenSpace(float device)
	{
		float screen = device *= 2f;
		screen -= 1f;
		return screen;
	}

	/**
	 * Unprojects a device screen position to a world space coordinates
	 * Takes a referencePosition that is set before
	 * 
	 * @param pickerCoordinates - The position on the screen (device coordinates)
	 * @param referencePosition - The position to store
	 */
	public Vector3 GetWorldPosition(Vector2 pickerCoordinates, Vector3 referencePosition)
	{
		SetReferencePosition(referencePosition);
		return GetWorldPosition(pickerCoordinates);
	}
}
