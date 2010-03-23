package at.ac.tuwien.cg.cgmd.bifth2010.level17.math;

import javax.microedition.khronos.opengles.GL10;

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
	// used to grab the current view and projection matrices
	private MatrixGrabber mMatrixGrabber = new MatrixGrabber();
	private int mScreenWidth;
	private int mScreenHeight;
	private Matrix4x4 mViewProjection = new Matrix4x4();
	private Matrix4x4 mInvViewProjection = new Matrix4x4();
	private float lastDepth;


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
	public void CameraChanged(GL10 gl, int width, int height)
	{
		// store width and height
		mScreenWidth = width;
		mScreenHeight = height;
		
		// calculate matrices
		mMatrixGrabber.getCurrentState(gl);
		Matrix4x4 view = new Matrix4x4();
		view.fromFloatArray(mMatrixGrabber.mModelView);
		Matrix4x4 projection = new Matrix4x4();
		projection.fromFloatArray(mMatrixGrabber.mProjection);

		mViewProjection = Matrix4x4.mult(view, projection);
		mInvViewProjection = mViewProjection.InvertedCopy();
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
		
		lastDepth = screenSpacePos.y; // for some reason, the y axis contains the depth, not the z axis as one would expect...
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
		// TODO: calculate picking position in screen space
		// TODO: set the same depth as in screenSpacePos.y
		// TODO: unproject the picking position to world coordinates
		// TODO: return these coordinates homogenized
		return new Vector3();
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
