package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import static android.opengl.GLES10.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.FloatBuffer;

import android.opengl.GLES11;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.AxisAlignedBox3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Sphere;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.OGLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable;

/**
 * The Class Geometry.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Geometry implements Persistable
{
	/** The Constant VERTEX_LENGTH. */
	public static final int VERTEX_LENGTH = 3;
	
	/** The Constant TEXCOORD_LENGTH. */
	public static final int TEXCOORD_LENGTH = 2;
	
	/** The ogl manager. */
	private final OGLManager oglManager = OGLManager.instance;
	
	/** The Buffers. */
	private final FloatBuffer vertices, normals, texcoords;
	
	/** The bounding box. */
	private final AxisAlignedBox3 boundingBox;
	
	/** The bounding sphere. */
	private final Sphere boundingSphere;
	
	/** The number of vertices. */
	private final int numVertices;
	
	/** The nr of vertex bytes. */
	private final int nrOfVertexBytes;
	
	/** The nr of normal bytes. */
	private final int nrOfNormalBytes;
	
	/** The nr of texcoord bytes. */
	private final int nrOfTexcoordBytes;
	
	/** The vbo id. */
	private int vboID;
	
	/** The vertex offset. */
	private final int vertexOffset;
	
	/** The normal offset. */
	private final int normalOffset;
	
	/** The texcoord offset. */
	private final int texcoordOffset;

	/** if this is initialized. */
	private boolean initialized;
	
	/**
	 * Instantiates a new geometry.
	 *
	 * @param vertices the vertices
	 * @param normals the normals
	 * @param texcoords the texcoords
	 * @param boundingBox the bounding box
	 * @param boundingSphere the bounding sphere
	 * @param numVertices the num vertices
	 */
	public Geometry(FloatBuffer vertices, FloatBuffer normals, FloatBuffer texcoords, AxisAlignedBox3 boundingBox, Sphere boundingSphere, int numVertices)
	{
		this.vertices = vertices;
		this.normals = normals;
		this.texcoords = texcoords;
		this.boundingBox = boundingBox;
		this.boundingSphere = boundingSphere;
		this.numVertices = numVertices;
		initialized = false;
		
		nrOfVertexBytes = numVertices*VERTEX_LENGTH*4;
		nrOfNormalBytes = numVertices*VERTEX_LENGTH*4;
		nrOfTexcoordBytes = numVertices*TEXCOORD_LENGTH*4;
		
		// calculate vbo offsets
		int currentoffset = 0;
		if(vertices != null)
		{
			vertexOffset = currentoffset;
			currentoffset += nrOfVertexBytes;
		}
		else
			vertexOffset = 0;
		
		if(normals != null)
		{
			normalOffset = currentoffset;
			currentoffset += nrOfNormalBytes;
		}
		else
			normalOffset = 0;
		
		if(texcoords != null)
		{
			texcoordOffset = currentoffset;
			currentoffset += nrOfTexcoordBytes;
		}
		else
			texcoordOffset = 0;
	}
	
	/**
	 * Inits the Geometry
	 */
	void init()
	{
		if(!initialized)
		{
			if(Config.GLES11)
			{
				int[] buffers = new int[1];
				GLES11.glGenBuffers(1, buffers, 0);
				vboID = buffers[0];
				oglManager.bindVBO(vboID);
				int datasize = ((vertices != null ? numVertices*VERTEX_LENGTH : 0) + (normals != null ? numVertices*VERTEX_LENGTH : 0) + (texcoords != null ? numVertices*TEXCOORD_LENGTH : 0))*4;
				GLES11.glBufferData(GLES11.GL_ARRAY_BUFFER, datasize, null, GLES11.GL_STATIC_DRAW);
				
				if(vertices != null)
					GLES11.glBufferSubData(GLES11.GL_ARRAY_BUFFER, vertexOffset, nrOfVertexBytes, vertices);
				
				if(normals != null)
					GLES11.glBufferSubData(GLES11.GL_ARRAY_BUFFER, normalOffset, nrOfNormalBytes, normals);
				
				if(texcoords != null)
					GLES11.glBufferSubData(GLES11.GL_ARRAY_BUFFER, texcoordOffset, nrOfTexcoordBytes, texcoords);
			}
			
			initialized = true;
		}
	}
	
	/**
	 * De-inits the Geometry
	 */
	void deInit()
	{
		initialized = false;
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#persist(java.io.DataOutputStream)
	 */
	public void persist(DataOutputStream dos)
	{
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#restore(java.io.DataInputStream)
	 */
	public void restore(DataInputStream dis)
	{
	}
	
	/**
	 * Render.
	 *
	 * @param rendermode the rendermode
	 */
	public void render(int rendermode)
	{
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
	
	/**
	 * Update.
	 */
	public void update()
	{
	}
	
	/**
	 * Gets the bounding box.
	 *
	 * @return the bounding box
	 */
	public AxisAlignedBox3 getBoundingBox()
	{
		return boundingBox;
	}
	
	/**
	 * Gets the bounding sphere.
	 *
	 * @return the bounding sphere
	 */
	public Sphere getBoundingSphere()
	{
		return boundingSphere;
	}
}
