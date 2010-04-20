package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;

public class Model
{
	Matrix44 transformation;
	FloatBuffer vertices, normals, texcoords;
	int numVertices;
	int vertexLength;
//	int vboID;
	
	public Model()
	{
		transformation = new Matrix44();
		vertices = null;
		normals = null;
		texcoords = null;
		numVertices = 0;
		vertexLength = 3;
	}
	
	public void render(GL10 gl, int rendermode)
	{
		switch(rendermode)
		{
		case Scene.RENDERMODE_VBO: break;
		case Scene.RENDERMODE_VERTEXARRAY:
			if(vertices != null)
				gl.glVertexPointer(vertexLength, GL10.GL_FLOAT, 0, vertices);
			if(normals != null)
				gl.glNormalPointer(GL10.GL_FLOAT, 0, normals);
			if(texcoords != null)
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texcoords);
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, numVertices);
			break;
		}
	}
	
	public Matrix44 getTransformation()
	{
		return transformation;
	}
	public void setTransformation(Matrix44 transformation)
	{
		this.transformation = transformation;
	}
	
	
}
