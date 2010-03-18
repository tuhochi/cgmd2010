package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import static android.opengl.GLES10.*;

import java.nio.FloatBuffer;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.MaterialManager.Material;

public class Geometry
{
	public static final int VERTEX_LENGTH = 3;
	MaterialManager materialManager;
	Material material;
	FloatBuffer vertices, normals, texcoords;
	int numVertices;
	int vboID;
	
	public Geometry()
	{
		numVertices = 0;
		vboID = -1;
		materialManager = MaterialManager.getInstance();
	}
	
	public Geometry(Material material, FloatBuffer vertices, FloatBuffer normals, FloatBuffer texcoords, int numVertices)
	{
		this();
		this.material = material;
		this.vertices = vertices;
		this.normals = normals;
		this.texcoords = texcoords;
		this.numVertices = numVertices;
		
		/*
		 * TODO:Generate VBOs
		 */
		vboID = -1;
	}
	
	public void render(int rendermode)
	{
		materialManager.bindMaterial(material);
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
				glVertexPointer(VERTEX_LENGTH, GL_FLOAT, 0, vertices);
			if(normals != null)
				glNormalPointer(GL_FLOAT, 0, normals);
			if(texcoords != null)
				glTexCoordPointer(2, GL_FLOAT, 0, texcoords);
			glDrawArrays(GL_TRIANGLES, 0, numVertices);
			break;
		}
	}
}
