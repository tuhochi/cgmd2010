package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import static android.opengl.GLES10.*;
import android.opengl.GLES11;
import android.opengl.GLU;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;

public class OGLManager
{
	public static final OGLManager instance = new OGLManager();

	private boolean clientStateVertices;
	private boolean clientStateNormals;
	private boolean clientStateTexcoords;

	private int currentlyBoundVBO;

	private Matrix44 modelview;
	private Matrix44 projection;
	private int[] viewport;

	//vars for unproject
	private int[] viewportArray4;
	private float[] modelviewArray16;
	private float[] projectionArray16;
	
	private float[] window;
	private float[] unprojectedPos;
	private Vector3 unprojectedPosVec;

	
	private OGLManager()
	{
		clientStateVertices = false;
		clientStateNormals = false;
		clientStateTexcoords = false;
		
		currentlyBoundVBO = 0;

		modelview = new Matrix44();
		projection = new Matrix44();
		viewport = new int[4];
		
		window = new float[3];
		unprojectedPos = new float[4];
		unprojectedPosVec = new Vector3();
	}

	public void reset()
	{
		clientStateVertices = false;
		clientStateNormals = false;
		clientStateTexcoords = false;
		currentlyBoundVBO = 0;
	}
	
	public void clientState(boolean vertices, boolean normals, boolean texcoords)
	{
		if(vertices && !clientStateVertices)
			glEnableClientState(GL_VERTEX_ARRAY);
		if(!vertices && clientStateVertices)
			glDisableClientState(GL_VERTEX_ARRAY);

		if(normals && !clientStateNormals)
			glEnableClientState(GL_NORMAL_ARRAY);
		if(!normals && clientStateNormals)
			glDisableClientState(GL_NORMAL_ARRAY);

		if(texcoords && !clientStateTexcoords)
			glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		if(!texcoords && clientStateTexcoords)
			glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	}

	public void bindVBO(int id)
	{
		if(id != currentlyBoundVBO)
		{
			GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, id);
			currentlyBoundVBO = id;
		}
	}

	public synchronized Matrix44 getModelview()
	{
		return modelview;
	}

	public synchronized Matrix44 getProjection()
	{
		return projection;
	}

	public synchronized int[] getViewport()
	{
		return viewport;
	}

	public void glFrustumInfinite(float left, float right, float bottom, float top, float zNear, float zFar, Matrix44 result)
	{
		float x, y, a, b, c, d;
		x = (2.0f * zNear) / (right - left);
		y = (2.0f * zNear) / (top - bottom);
		a = (right + left) / (right - left);
		b = (top + bottom) / (top - bottom);

		if (zFar == Float.MAX_VALUE)
		{
			// no depth clamp extension here - workaround see
			// http://www.gamasutra.com/view/feature/2942/the_mechanics_of_robust_stencil_.php?page=2
			c = -0.999f;
			d = -1.999f * zNear;
		}
		else
		{
			c = -(zFar + zNear) / (zFar - zNear);
			d = -(2.0f * zFar * zNear) / (zFar - zNear);
		}

		result.set(
				x, 0, 0, 0,
				0, y, 0, 0,
				a, b, c, -1,
				0, 0, d, 0);
	}

	public void gluLookAt(Vector3 eyePos, Vector3 inverseForwardNormalized, Vector3 rightNormalized, Vector3 upNormalized, Matrix44 result)
	{
		float[] view = result.getArray16();
		view[ 0] = rightNormalized.x;
		view[ 1] = upNormalized.x;
		view[ 2] = inverseForwardNormalized.x;
		view[ 3] = 0.0f;
		view[ 4] = rightNormalized.y;
		view[ 5] = upNormalized.y;
		view[ 6] = inverseForwardNormalized.y;
		view[ 7] = 0.0f;
		view[ 8] = rightNormalized.z;
		view[ 9] = upNormalized.z;
		view[10] = inverseForwardNormalized.z;
		view[11] = 0.0f;
		view[12] = (rightNormalized.x			* -eyePos.x) + (rightNormalized.y			* -eyePos.y) + (rightNormalized.z			* -eyePos.z);
		view[13] = (upNormalized.x				* -eyePos.x) + (upNormalized.y				* -eyePos.y) + (upNormalized.z				* -eyePos.z);
		view[14] = (inverseForwardNormalized.x	* -eyePos.x) + (inverseForwardNormalized.y	* -eyePos.y) + (inverseForwardNormalized.z	* -eyePos.z);
		view[15] = 1.0f;
		
		result.set(view);
	}
	
	public Vector3 unProject(int x, int y)
	{
		viewportArray4 = getViewport();
		modelviewArray16 = getModelview().getArray16();
		projectionArray16 = getProjection().getArray16();
		
		window[0] = (float)x;
		window[1] = (float)(viewport[3] - y);
		window[2] = 1;
			
        GLU.gluUnProject( 	window[0], window[1], window[2], modelviewArray16, 0, 
        					projectionArray16, 0, viewportArray4, 0, unprojectedPos, 0);
		
        unprojectedPosVec.x = unprojectedPos[0];
        unprojectedPosVec.y = unprojectedPos[1];
        unprojectedPosVec.z = unprojectedPos[2];
        
        //normalize with 4th component
        unprojectedPosVec.divide(unprojectedPos[3]);
   
        return unprojectedPosVec;
	}
}