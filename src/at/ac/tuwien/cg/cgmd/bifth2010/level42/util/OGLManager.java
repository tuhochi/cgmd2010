package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import static android.opengl.GLES10.*;
import android.opengl.GLES11;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;

public class OGLManager
{
	public static final OGLManager instance = new OGLManager();
	
	private boolean clientStateVertices = false;
	private boolean clientStateNormals = false;
	private boolean clientStateTexcoords = false;
	
	private int currentlyBoundVBO = 0;
	
	private Matrix44 modelview = new Matrix44();
	private Matrix44 projection = new Matrix44();
	private int[] viewport = new int[4];
	
	private OGLManager()
	{
		
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
}
