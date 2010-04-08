package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import static android.opengl.GLES10.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.FloatBuffer;

import android.opengl.GLES11;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.AxisAlignedBox3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Sphere;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.MaterialManager.Material;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.OGLManager;

public class Geometry
{
	public static final int VERTEX_LENGTH = 3;
	public static final int TEXCOORD_LENGTH = 2;
	
	private final MaterialManager materialManager = MaterialManager.instance;
	private final OGLManager oglManager = OGLManager.instance;
	private final Material material;
	private final FloatBuffer vertices, normals, texcoords;
	private final AxisAlignedBox3 boundingBox;
	private final Sphere boundingSphere;
	private final int numVertices;
	private final int vboID;
	private final int vertexOffset;
	private final int normalOffset;
	private final int texcoordOffset;
	
	public Geometry(Material material, FloatBuffer vertices, FloatBuffer normals, FloatBuffer texcoords, AxisAlignedBox3 boundingBox, Sphere boundingSphere, int numVertices)
	{
		this.material = material;
		this.vertices = vertices;
		this.normals = normals;
		this.texcoords = texcoords;
		this.boundingBox = boundingBox;
		this.boundingSphere = boundingSphere;
		this.numVertices = numVertices;
		
		if(Config.GLES11)
		{
			int[] buffers = new int[1];
			GLES11.glGenBuffers(1, buffers, 0);
			vboID = buffers[0];
			oglManager.bindVBO(vboID);
			int datasize = ((vertices != null ? numVertices*VERTEX_LENGTH : 0) + (normals != null ? numVertices*VERTEX_LENGTH : 0) + (texcoords != null ? numVertices*TEXCOORD_LENGTH : 0))*4;
			GLES11.glBufferData(GLES11.GL_ARRAY_BUFFER, datasize, null, GLES11.GL_STATIC_DRAW);
			int currentoffset = 0;
			if(vertices != null)
			{
				int size = numVertices*VERTEX_LENGTH*4;
				GLES11.glBufferSubData(GLES11.GL_ARRAY_BUFFER, currentoffset, size, vertices);
				vertexOffset = currentoffset;
				currentoffset += size;
			}
			else
				vertexOffset = 0;
			if(normals != null)
			{
				int size = numVertices*VERTEX_LENGTH*4;
				GLES11.glBufferSubData(GLES11.GL_ARRAY_BUFFER, currentoffset, size, normals);
				normalOffset = currentoffset;
				currentoffset += size;
			}
			else
				normalOffset = 0;
			if(texcoords != null)
			{
				int size = numVertices*TEXCOORD_LENGTH*4;
				GLES11.glBufferSubData(GLES11.GL_ARRAY_BUFFER, currentoffset, size, texcoords);
				texcoordOffset = currentoffset;
				currentoffset += size;
			}
			else
				texcoordOffset = 0;
		}
		else
		{
			vboID = 0;
			vertexOffset = 0;
			normalOffset = 0;
			texcoordOffset = 0;
		}
	}
	
	public void persist(DataOutputStream dos)
	{
		material.persist(dos);
	}
	
	public void restore(DataInputStream dis)
	{
		material.restore(dis);
	}
	
	public void render(int rendermode)
	{
		materialManager.bindMaterial(material);
		oglManager.clientState(vertices != null, normals != null, texcoords != null);
		
		switch(rendermode)
		{
		case Scene.RENDERMODE_VBO:
			if(vboID > 0)
			{
				oglManager.bindVBO(vboID);
				if(vertices != null)
					GLES11.glVertexPointer(VERTEX_LENGTH, GL_FLOAT, 0, vertexOffset);
				if(normals != null)
					GLES11.glNormalPointer(GL_FLOAT, 0, normalOffset);
				if(texcoords != null)
					GLES11.glTexCoordPointer(TEXCOORD_LENGTH, GL_FLOAT, 0, texcoordOffset);
				
				glDrawArrays(GL_TRIANGLES, 0, numVertices);
				
				break; // breaking inside the if, so it defaults to vertex array rendering if vbo's are not available
			}
		case Scene.RENDERMODE_VERTEXARRAY:
			if(vertices != null)
				glVertexPointer(VERTEX_LENGTH, GL_FLOAT, 0, vertices);
			if(normals != null)
				glNormalPointer(GL_FLOAT, 0, normals);
			if(texcoords != null)
				glTexCoordPointer(TEXCOORD_LENGTH, GL_FLOAT, 0, texcoords);
			
			glDrawArrays(GL_TRIANGLES, 0, numVertices);
			break;
		}
	}
	
	public void update()
	{
	}
	
	public AxisAlignedBox3 getBoundingBox()
	{
		return boundingBox;
	}
	
	public Sphere getBoundingSphere()
	{
		return boundingSphere;
	}
}
