package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.nio.FloatBuffer;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;

//static imports
import static android.opengl.GLES10.*;

public class Model
{
	MaterialManager materialManager;
	Matrix44 transformation;
	FloatBuffer vertices, normals, texcoords;
	String materialName;
	int numVertices;
	int vertexLength;
	int vboID;
	
	public Model()
	{
		transformation = new Matrix44();
		vertices = null;
		normals = null;
		texcoords = null;
		numVertices = 0;
		vertexLength = 3;
		vboID = -1;
		materialManager = MaterialManager.getInstance();
	}
	
	public void render(int rendermode)
	{
		materialManager.bindMaterial(materialName);
		switch(rendermode)
		{
		case Scene.RENDERMODE_VBO:
			if(vboID > 0)
			{
				/*
				 * implement vbo rendering
				 */
				break; // breaking inside the if, so it defaults to vertex array rendering if vbo's are not available
			}
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
