package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.nio.FloatBuffer;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;

//static imports
import static android.opengl.GLES10.*;

public class Model
{
	Matrix44 transformation;
	FloatBuffer vertices, normals, texcoords;
	int numVertices;
	int vertexLength;
	Integer vboID;
	
	public Model()
	{
		transformation = new Matrix44();
		vertices = null;
		normals = null;
		texcoords = null;
		numVertices = 0;
		vertexLength = 3;
		vboID = null;
	}
	
	public void render(int rendermode)
	{
		switch(rendermode)
		{
		case Scene.RENDERMODE_VBO:
			/*
			 * implement vbo rendering
			 */
			break;
		case Scene.RENDERMODE_VERTEXARRAY:
			if(vertices != null)
				glVertexPointer(vertexLength, GL_FLOAT, 0, vertices);
			if(normals != null)
				glNormalPointer(GL_FLOAT, 0, normals);
			if(texcoords != null)
				glTexCoordPointer(2, GL_FLOAT, 0, texcoords);
			glDrawArrays(GL_TRIANGLES, 0, numVertices);
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
