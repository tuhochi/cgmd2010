package at.ac.tuwien.cg.cgmd.bifth2010.level33.model;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Color;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector3f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.SceneGraph;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;
import at.ac.tuwien.cg.cgmd.bifth2010.level66.OBJModel;

public class Geometry {
	public enum Type {
		Points, Lines, Triangles, LineStrip, TriangleStrip, TriangleFan
	}

	private GL10 gl;

	private float vertices[];
	private int vertexHandle = -1;
	private FloatBuffer vertexBuffer;

	private float colors[];
	private int colorHandle = -1;
	private FloatBuffer colorBuffer;

	private float texCoords[];
	private int texHandle = -1;
	private FloatBuffer texCoordBuffer;

	private float normals[];
	private int normalHandle = -1;
	private FloatBuffer normalBuffer;

	private int indexVertex = 0;
	private int indexColor = 0;
	private int indexNormal = 0;
	private int indexTexCoords = 0;
	
	private int numVertices = 0;

	private boolean init = false;

	private static Geometry lastGeometry;

	public static boolean useVBO = Config.GLES11;

	public static int geometryCount = 0;
	
	
	// if multible Geometry Objects are in one Geometry then the Offset Array is used
	// eg.: Obj1 & Obj2 =>  geometryOffset={16,32}  --> 0-16 & 16-32
	// if only one Object eg: Obj1 then it is null;
	public int geometryOffset[] = null;
	
	private Type type[];

	private int textureId= -1;
	int textur = -1;


	public Geometry(GL10 gl, Type[] type, int numVertices, boolean hasColors,
			boolean hasTextureCoordinates, boolean hasNormals,int textur,int[] geometryOffset) {
		
		this.gl = gl;
		this.type = type;
		this.geometryOffset=geometryOffset;
		
		vertices = new float[numVertices * 3];
		if(hasColors)
			colors = new float[numVertices * 4];
		if(hasTextureCoordinates)
			texCoords = new float[numVertices * 2];
		if(hasNormals)
			normals = new float[numVertices * 3];
		
		this.textur=textur;

	}
	
	/**
	 * this constructor accept the ObjModel
	 * @param gl
	 * @param objModel Object Geometry
	 * @param textur Texture ID
	 */
	public Geometry(GL10 gl, ObjModel objModel, int textur) {
		
		this.gl = gl;
		this.type = objModel.type;
		this.geometryOffset=objModel.geometryOffset;
		
		vertices = objModel.vertices;
		colors = objModel.colors;
		texCoords = objModel.texCoords;
		normals = objModel.normals;
		
		this.textur=textur;

	}
	
	public ObjModel GetObjModel(){
		return new ObjModel(type,geometryOffset,vertices,colors,texCoords,normals);
	}

	private FloatBuffer allocateBuffer(int size) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(size * 4);
		buffer.order(ByteOrder.nativeOrder());
		return buffer.asFloatBuffer();
	}

	private void init() {
	
		int[] buffer = new int[1];
		
		if (!useVBO)
			vertexBuffer = allocateBuffer(numVertices * 3);
		else {
			((GL11) gl).glGenBuffers(1, buffer, 0);
			vertexHandle = buffer[0];
			vertexBuffer = FloatBuffer.wrap(vertices);
		}

		if (colors!=null) {
			
			if (!useVBO)
				colorBuffer = allocateBuffer(numVertices * 4);
			else {
				((GL11) gl).glGenBuffers(1, buffer, 0);
				colorHandle = buffer[0];
				colorBuffer = FloatBuffer.wrap(colors);
			}
		}

		if (texCoords!=null) {
			
			if (!useVBO)
				texCoordBuffer = allocateBuffer(numVertices * 2);
			else {
				((GL11) gl).glGenBuffers(1, buffer, 0);
				texHandle = buffer[0];
				texCoordBuffer = FloatBuffer.wrap(texCoords);
			}
		}

		if (this.normals!=null) {
			
			if (!useVBO)
				normalBuffer = allocateBuffer(numVertices * 3);
			else {
				((GL11) gl).glGenBuffers(1, buffer, 0);
				normalHandle = buffer[0];
				normalBuffer = FloatBuffer.wrap(normals);
			}
		}
		// init Texture
		if(textur!=-1){
			
			InputStream isImage = SceneGraph.activity.getResources().openRawResource(textur);
			
			if(isImage!=null){
				try {
					Bitmap bitmap = BitmapFactory.decodeStream(isImage);
					
					int[] textureIds = new int[1];
					gl.glGenTextures(1, textureIds, 0);
					textureId = textureIds[0];
					gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
					GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
							GL10.GL_LINEAR);
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
							GL10.GL_LINEAR);
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
							GL10.GL_CLAMP_TO_EDGE);
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
							GL10.GL_CLAMP_TO_EDGE);
					bitmap.recycle();
					
						} catch (Exception ex) {
					Log.d("Texture Sample", "Couldn't load bitmap");
				}
			}
		}
		
		

		
		
		Log.d("init","Geometry");
		if (!useVBO) {
			Log.d("init","Geometry2");
			vertexBuffer.put(vertices);
			vertexBuffer.position(0);

			if (colors != null) {
				colorBuffer.put(colors);
				colorBuffer.position(0);
			}

			if (texCoords != null) {
				texCoordBuffer.put(texCoords);
				texCoordBuffer.position(0);
			}

			if (normals != null) {
				normalBuffer.put(normals);
				normalBuffer.position(0);
			}
		} else {
			GL11 gl = (GL11) this.gl;

			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertexHandle);
			gl.glBufferData(GL11.GL_ARRAY_BUFFER, vertices.length * 4,
					vertexBuffer, GL11.GL_DYNAMIC_DRAW);

			if (colors != null) {
				gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, colorHandle);
				gl.glBufferData(GL11.GL_ARRAY_BUFFER, colors.length * 4,
						colorBuffer, GL11.GL_DYNAMIC_DRAW);
			}

			if (normals != null) {
				gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, normalHandle);
				gl.glBufferData(GL11.GL_ARRAY_BUFFER, normals.length * 4,
						normalBuffer, GL11.GL_DYNAMIC_DRAW);
			}

			if (texCoords != null) {
				gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, texHandle);
				gl.glBufferData(GL11.GL_ARRAY_BUFFER, texCoords.length * 4,
						texCoordBuffer, GL11.GL_DYNAMIC_DRAW);
			}

			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		}

		numVertices = indexVertex;
		reset();
		init = true;
	}

	private int getPrimitiveType( Type type )
    {
		if (type == Type.Lines)
			return GL10.GL_LINES;
		else if (type == Type.Triangles)
			return GL10.GL_TRIANGLES;
		else if (type == Type.LineStrip)
			return GL10.GL_LINE_STRIP;
		else if (type == Type.TriangleStrip)
			return GL10.GL_TRIANGLE_STRIP;
		else if (type == Type.Points)
			return GL10.GL_POINTS;
		else
			return GL10.GL_TRIANGLE_FAN;
            
    }

	public void render() {
		if(this.type==null)
			render(Type.Triangles, 0, numVertices);
		else
			render(this.type[0], 0, numVertices);
	}

	/*
	 * This method render a specific Geometry, with a specific Type
	 * if there are multiple Objects in this Geometry
	 */
	public void render(int id){
		if(geometryOffset!=null)
		{
			if(id==0)
				render(0,geometryOffset[id]);
			else
				render(geometryOffset[id-1],geometryOffset[id]-geometryOffset[id-1]);
		}
		else
			render(this.type[0]);
	}

	public void render(Type type) {
		render(type, 0, numVertices);
	}
	
	public void render(int offset, int numVertices) {
		
		render(this.type[0],offset,numVertices);
	}

	public void render(Type type, int offset, int numVertices) {
			
		boolean wasInit = init;
		if (!init)
			init();

		// minimize Gl Stat changing
		if (this == Geometry.lastGeometry && wasInit) {
			gl.glDrawArrays(getPrimitiveType(type), offset, numVertices);
			return;
		} else {
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_2D);
		}

		if(textureId!=-1){
			gl.glEnable( GL10.GL_TEXTURE_2D );
            gl.glBindTexture( GL10.GL_TEXTURE_2D, textureId );
		}
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		if (useVBO) {
			((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, vertexHandle);
			((GL11) gl).glVertexPointer(3, GL10.GL_FLOAT, 0, 0);
		} else
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		if (colors != null) {
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			if (useVBO) {
				((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, colorHandle);
				((GL11) gl).glColorPointer(4, GL10.GL_FLOAT, 0, 0);
			} else
				gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		}

		if (texCoords != null) {
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			if (useVBO) {
				((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, texHandle);
				((GL11) gl).glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
			} else
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
		}

		if (normals != null) {
			gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
			if (useVBO) {
				((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, normalHandle);
				((GL11) gl).glNormalPointer(GL10.GL_FLOAT, 0, 0);
			} else
				gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
		}

		gl.glDrawArrays(getPrimitiveType(type), offset, numVertices);
		
		Geometry.lastGeometry = this;
	}

	public void vertex(Vector3f v) {
		vertex(v.x, v.y, v.z);
	}

	public void vertex(float x, float y, float z) {
		init = false;
		int offset = indexVertex * 3;
		vertices[offset] = x;
		vertices[offset + 1] = y;
		vertices[offset + 2] = z;
		indexVertex++;
		numVertices=indexVertex;
	}

	public void color(Color c) {
		this.color(c.r, c.g, c.b, c.a);
	}
	public void color(float r, float g, float b) {
		this.color(r, g, b, 1);
	}
	
	public void color(float r, float g, float b, float a) {
		init = false;
		int offset = indexColor * 4;
		colors[offset] = r;
		colors[offset + 1] = g;
		colors[offset + 2] = b;
		colors[offset + 3] = a;
		indexColor++;
	}

	public void normal(float x, float y, float z) {
		init = false;
		int offset = indexNormal * 3;
		normals[offset] = x;
		normals[offset + 1] = y;
		normals[offset + 2] = z;
		indexNormal++;
	}

	public void texCoord(float s, float t) {
		init = false;
		int offset = indexTexCoords * 2;
		texCoords[offset] = s;
		texCoords[offset + 1] = t;
		indexTexCoords++;
	}

	public int getMaximumVertices() {
		return vertices.length / 3;
	}

	public void reset() {
		indexVertex = 0;
		indexColor = 0;
		indexNormal = 0;
		indexTexCoords = 0;
		init = false;
	}

	public void dispose() {
		if (useVBO) {
			GL11 gl = (GL11) this.gl;
			if (vertexHandle != -1)
				gl.glDeleteBuffers(1, new int[] { vertexHandle }, 0);
			if (colorHandle != -1)
				gl.glDeleteBuffers(1, new int[] { colorHandle }, 0);
			if (normalHandle != -1)
				gl.glDeleteBuffers(1, new int[] { normalHandle }, 0);
			if (texHandle != -1)
				gl.glDeleteBuffers(1, new int[] { texHandle }, 0);
		}

		vertices = null;
		vertexBuffer = null;
		colors = null;
		colorBuffer = null;
		normals = null;
		normalBuffer = null;
		texCoords = null;
		texCoordBuffer = null;
		geometryCount--;
	}
	
	/**
	 * this method add another Geometry to this one, (only same type and with same textureId)
	 * @param other 
	 * @param rotation
	 * @param scale
	 * @param translation
	 */
	public void addGeometryAt(Geometry other, Vector3f rotation, Vector3f scale,Vector3f translation) {
		
		if(other==this)
			return;
		
		// VERTICES
		if(other.vertices!=null){
			float[] oldVertices = this.vertices.clone();

			// init new vertices array
			this.vertices = new float[other.vertices.length+this.vertices.length];
			this.indexVertex=0;
			// add old vertices
			for (int index = 0; index < oldVertices.length/3; index++)
				this.vertex(oldVertices[3*index],oldVertices[3*index+1],oldVertices[3*index+2]);
			// add new vertices
			for (int index = 0; index < other.vertices.length/3; index++) {
				Vector3f vertex = new Vector3f(other.vertices[3*index],
						other.vertices[3*index + 1],
						other.vertices[3*index + 2]);
				//		vertex.rotate(rotation); // TODO
				//		vertex.scale(scale);
				vertex.translate(translation);
				this.vertex(vertex);
			}
			
			Log.d("d", "d");
		}
		
		// COLORS
		if(other.colors!=null){
			float[] oldColors = this.colors.clone();
			
			// init new color array
			this.colors = new float[other.colors.length+this.colors.length];
			this.indexColor=0;
			// add old color
			for (int index = 0; index < oldColors.length/4; index++)
				this.color(oldColors[4*index],oldColors[4*index+1],oldColors[4*index+2],oldColors[4*index+3]);
			// add new color
			for (int index = 0; index < other.colors.length/4; index++)
				this.color(other.colors[4*index],other.colors[4*index+1],other.colors[4*index+2],other.colors[4*index+3]);
		}
		
		// NORMALS
		if(other.normals!=null){
			float[] oldNormals = this.normals.clone();

			// init new normal array
			this.normals = new float[other.normals.length+this.normals.length];
			this.indexNormal=0; 
			// add old normal
			for (int index = 0; index < oldNormals.length/3; index++)
				this.normal(oldNormals[3*index],oldNormals[3*index+1],oldNormals[3*index+2]);
			// add new normal
			for (int index = 0; index < other.vertices.length/3; index++)
				this.normal(other.normals[3*index],other.normals[3*index+1],other.normals[3*index+2]);
		}
		
		// TEXCOORDS
		if(other.texCoords!=null){
			float[] oldTexCoords = this.texCoords.clone();
			// init new textcoords array
			this.texCoords = new float[other.texCoords.length+this.texCoords.length];
			this.indexTexCoords=0;
			// add old textcoords
			for (int index = 0; index < oldTexCoords.length/2; index++)
				this.texCoord(oldTexCoords[2*index],oldTexCoords[2*index+1]);
			// add new textcoords
			for (int index = 0; index < other.texCoords.length/2; index++)
				this.texCoord(other.texCoords[2*index],other.texCoords[2*index+1]);
		}	
		
		System.out.println("end?");
		
	}


	
}
