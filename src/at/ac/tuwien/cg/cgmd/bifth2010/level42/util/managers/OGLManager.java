package at.ac.tuwien.cg.cgmd.bifth2010.level42.util.managers;

import static android.opengl.GLES10.*;

import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.opengl.GLES11;
import android.opengl.GLU;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.datastructures.Triple;

/**
 * The Class OGLManager.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class OGLManager
{
	/** The Constant instance. */
	public static final OGLManager instance = new OGLManager();

	/** whether the vertex client state is set */
	private boolean clientStateVertices;
	
	/** whether the normal client state is set */
	private boolean clientStateNormals;
	
	/** whether the texcoord client state is set */
	private boolean clientStateTexcoords;
	
	/** 
	 * Pointers to the Buffers, along with their offsets (Second part of the Triple)
	 * and their sizes in Bytes (Third part of the Triple)
	 */
	private ArrayList<Triple<Integer, Integer, Buffer>> vboBuffers;
	
	/** holds the current vbo offset, needed during assembling of the vbo */
	private int currentVBOoffset;
	
	/** The VBO's id */
	private int vbo;

	/** The modelview matrix */
	private Matrix44 modelview;
	
	/** The projection matrix */
	private Matrix44 projection;
	
	/** The viewport as returned by glViewport */
	private int[] viewport;

	//temp vars for unproject
	/** The viewport array4. */
	private int[] viewportArray4;
	
	/** The modelview array16. */
	private float[] modelviewArray16;
	
	/** The projection array16. */
	private float[] projectionArray16;
	
	/** The window. */
	private float[] window;
	
	/** The unprojected pos. */
	private float[] unprojectedPos;
	
	/** The unprojected pos vec. */
	private Vector3 unprojectedPosVec;

	/**
	 * Instantiates a new OGL manager.
	 */
	private OGLManager()
	{
		modelview = new Matrix44();
		projection = new Matrix44();
		viewport = new int[]{0,0,1,1};	// dummy viewport
		
		window = new float[3];
		unprojectedPos = new float[4];
		unprojectedPosVec = new Vector3();
		
		vboBuffers = new ArrayList<Triple<Integer,Integer,Buffer>>();
		
		reset();
	}

	/**
	 * Reset.
	 */
	public void reset()
	{
		clientStateVertices = false;
		clientStateNormals = false;
		clientStateTexcoords = false;
		currentVBOoffset = 0;
		vboBuffers.clear();
	}
	
	/**
	 * adds a buffer to the vbo, the buffer will be compiled
	 * into the vbo as soon as compileVBO is called.
	 */
	public int addBufferToVBO(Buffer buffer, int bytes)
	{
		int thisOffset = currentVBOoffset;
		vboBuffers.add(new Triple<Integer, Integer, Buffer>(thisOffset, bytes, buffer));
		currentVBOoffset += bytes;
		return thisOffset;
	}
	
	/**
	 * Compiles all added Buffers into a single VBO
	 */
	public void init()
	{
		int[] bufferIDs = new int[1];
		GLES11.glGenBuffers(1, bufferIDs, 0);
		vbo = bufferIDs[0];
		GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, vbo);
		GLES11.glBufferData(GLES11.GL_ARRAY_BUFFER, currentVBOoffset, null, GLES11.GL_STATIC_DRAW);
		
		ArrayList<Triple<Integer, Integer, Buffer>> buffers = vboBuffers;
		int size = buffers.size();
		
		for(int i=0; i<size; i++)
		{
			Triple<Integer, Integer, Buffer> triple = buffers.get(i);
			GLES11.glBufferSubData(GLES11.GL_ARRAY_BUFFER, 
					triple.getFirst(),		// offset
					triple.getSecond(),		// size
					triple.getThird());		// data
		}
	}
	
	public void deInit()
	{
		GLES11.glDeleteBuffers(1, new int[]{vbo}, 0);
		vboBuffers.clear();
	}
	
	/**
	 * sets Client states, minimizes state changes
	 *
	 * @param vertices whether the vertex client state should be enabled
	 * @param normals whether the normal client state should be enabled
	 * @param texcoords whether the texcoord client state should be enabled
	 */
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

	/**
	 * Gets the modelview.
	 *
	 * @return the modelview
	 */
	public synchronized Matrix44 getModelview()
	{
		return modelview;
	}

	/**
	 * Gets the projection.
	 *
	 * @return the projection
	 */
	public synchronized Matrix44 getProjection()
	{
		return projection;
	}

	/**
	 * Gets the viewport.
	 *
	 * @return the viewport
	 */
	public synchronized int[] getViewport()
	{
		return viewport;
	}

	/**
	 * Gl frustum infinite.
	 *
	 * @param left the left
	 * @param right the right
	 * @param bottom the bottom
	 * @param top the top
	 * @param zNear the z near
	 * @param zFar the z far
	 * @param result the result
	 */
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

	/**
	 * Glu look at.
	 *
	 * @param eyePos the eye pos
	 * @param inverseForwardNormalized the inverse forward normalized
	 * @param rightNormalized the right normalized
	 * @param upNormalized the up normalized
	 * @param result the result
	 */
	public void gluLookAt(Vector3 eyePos, Vector3 inverseForwardNormalized, Vector3 rightNormalized, Vector3 upNormalized, Matrix44 result)
	{
		float[] view = result.getArray16();
		view[ 0] = rightNormalized.v[0];
		view[ 1] = upNormalized.v[0];
		view[ 2] = inverseForwardNormalized.v[0];
		view[ 3] = 0.0f;
		view[ 4] = rightNormalized.v[1];
		view[ 5] = upNormalized.v[1];
		view[ 6] = inverseForwardNormalized.v[1];
		view[ 7] = 0.0f;
		view[ 8] = rightNormalized.v[2];
		view[ 9] = upNormalized.v[2];
		view[10] = inverseForwardNormalized.v[2];
		view[11] = 0.0f;
		view[12] = (rightNormalized.v[0]			* -eyePos.v[0]) + (rightNormalized.v[1]				* -eyePos.v[1]) + (rightNormalized.v[2]				* -eyePos.v[2]);
		view[13] = (upNormalized.v[0]				* -eyePos.v[0]) + (upNormalized.v[1]				* -eyePos.v[1]) + (upNormalized.v[2]				* -eyePos.v[2]);
		view[14] = (inverseForwardNormalized.v[0]	* -eyePos.v[0]) + (inverseForwardNormalized.v[1]	* -eyePos.v[1]) + (inverseForwardNormalized.v[2]	* -eyePos.v[2]);
		view[15] = 1.0f;
		
		result.set(view);
	}
	
	/**
	 * Un project.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the vector3
	 */
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
		
        unprojectedPosVec.v[0] = unprojectedPos[0];
        unprojectedPosVec.v[1] = unprojectedPos[1];
        unprojectedPosVec.v[2] = unprojectedPos[2];
        
        //normalize with 4th component
        unprojectedPosVec.divide(unprojectedPos[3]);
   
        return unprojectedPosVec;
	}
	
	public void saveScreenshot(OutputStream fos)
	{
		saveScreenshot(viewport[0], viewport[1], viewport[2], viewport[3], fos);
	}
	
	public static void saveScreenshot(int x, int y, int w, int h, OutputStream fos)
	{
		Bitmap bmp = getPixels(x,y,w,h);
		try
		{
			bmp.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
		}
		catch(Throwable t)
		{
			LogManager.eTag("ScreenShot","Saving PNG failed", t);
		}
	}

	private static Bitmap getPixels(int x, int y, int w, int h)
	{
		int bitmap_ogl[] = new int[w*h];
		for(int i=0; i<bitmap_ogl.length; i++)
			bitmap_ogl[i] = -1;
		int bitmap_android[] = new int[w*h];
		IntBuffer bitmap_ogl_wrapper = IntBuffer.wrap(bitmap_ogl);
		bitmap_ogl_wrapper.position(0);
		
		glReadPixels(x, y, w, h, GL_RGBA, GL_UNSIGNED_BYTE, bitmap_ogl_wrapper);
		
		/*
		 * Pixelformat correction from OpenGLs ABGR to Android ARGB
		 */
		for(int i=0; i<h; i++)
		{
			for(int j=0; j<w; j++)
			{
				int color_ABGR = bitmap_ogl[i*w+j];
				
				int alpha	= (color_ABGR >> 24) & 0xff;
				int blue	= (color_ABGR >> 16) & 0xff;
				int green	= (color_ABGR >>  8) & 0xff;
				int red		= (color_ABGR >>  0) & 0xff;
				
				int color_ARGB =	((alpha	& 0xff) << 24) |
									((red	& 0xff) << 16) |
									((green	& 0xff) <<  8) |
									((blue	& 0xff) <<  0);
				
				// Android starts in the upper left corner, OpenGL starts in the lower left corner..
				bitmap_android[(h-i-1)*w+j] = color_ARGB;
			}
		}
		
		Bitmap sb = Bitmap.createBitmap(bitmap_android, w, h, Config.ARGB_8888);
		
		return sb;
	}
}