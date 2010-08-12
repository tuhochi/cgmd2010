package at.ac.tuwien.cg.cgmd.bifth2010.level33.model;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Color;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector3f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.SceneGraph;

/**
 * the Class Geometry
 * @author roman hochstoger & christoph fuchs
 */
public class Geometry {
	
	// primitive opengl Type
	public enum Type {
		Points, Lines, Triangles, LineStrip, TriangleStrip, TriangleFan
	}
	
	// opengl gl10
	private GL10 gl;

	// vertices objects
	private float vertices[];
	private int vertexHandle = -1;
	private FloatBuffer vertexBuffer;

	// color objects
	private float colors[];
	private int colorHandle = -1;
	private FloatBuffer colorBuffer;

	// text coordinate objects
	private float texCoords[];
	private int texHandle = -1;
	private FloatBuffer texCoordBuffer;

	// normals objects
	private float normals[];
	private int normalHandle = -1;
	private FloatBuffer normalBuffer;

	// indices oft the vertex, color, normal and text coordinates arrays
	private int indexVertex = 0;
	private int indexColor = 0;
	private int indexNormal = 0;
	private int indexTexCoords = 0;
	
	// the number of the vertices
	private int numVertices = 0;

	// to knew if this geometry is initialized
	private boolean init = false;

	// a pointer to the last rendered geometry, this is needed to improve the performance, if the last geometry was this geometry many gl switches must not be done
	private static Geometry lastGeometry;

	// this boolean decides if the vertex buffer object is used (fast buffer in the graphic card)
	public static boolean useVBO = true;


	// if several Geometry Objects are in one Geometry then the geometryOffset Array is used
	// the advantage is that it decrees gl switches 
	// eg.: Obj1 & Obj2 =>  geometryOffset={16,32}  --> 0-16 & 16-32
	// if only one Object eg: Obj1 then it is null;
	public int geometryOffset[] = null;
	
	// if several Geometry Objects, then they can be from various primitive types (not testet yet) 
	private Type type[];

	// this is the texturId on the graphic card
	private int textureId= -1;
	// this is the texture resource-ID (R file) 
	int textur = -1;


	/**
	 * this is the Geometry constructor
	 * @param gl						the opengl gl object
	 * @param type						primitive type of the geometry
	 * @param numVertices				number of vertices (eq.: 1 triangle == 1 numVertices == 3 vertices == 4 colors == 2 texCoords == 3 normals )
	 * @param hasColors					true if the geometry has colors
	 * @param hasTextureCoordinates		true if the geometry has texture coordinates
	 * @param hasNormals				true if the geometry has normals
	 * @param textur					if this geometry has a texture this is the resource-Id of the file
	 * @param geometryOffset			if multiple geometry«s in on geometry (sub groups) then this is the offset
	 */
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
	 * this constructor accept an ObjModel
	 * @param gl		the opengl gl object
	 * @param objModel 	Object Geometry
	 * @param textur 	if this geometry has a texture this is the resource-Id of the file
	 */
	public Geometry(GL10 gl, ObjModel objModel, int textur) {
		
		this.gl = gl;
		this.type = objModel.type;
		this.geometryOffset=objModel.geometryOffset;
		this.vertices = objModel.vertices;
		this.colors = objModel.colors;
		this.texCoords = objModel.texCoords;
		this.normals = objModel.normals;
		this.numVertices = objModel.numVertices;
		Log.d("numVertices","= "+objModel.numVertices);		
		this.textur=textur;

	}
	
	/**
	 * this getter generate an ObjModel from this geometry and return it
	 * @return		the ObjModel from this geometry
	 */
	public ObjModel GetObjModel(){
		Log.d("numVertices","= "+numVertices);
		return new ObjModel(type,geometryOffset,vertices,colors,texCoords,normals,numVertices);
	}

	/**
	 * it will allocate a buffer with the given size
	 * @param size 	of the buffer
	 * @return	the allocated buffer
	 */
	private FloatBuffer allocateBuffer(int size) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(size * 4);
		buffer.order(ByteOrder.nativeOrder());
		return buffer.asFloatBuffer();
	}

	/**
	 * this will initialize this geometry, allocate the needed buffers, initialize the vbo if used, bind texture
	 */
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
					this.textureId = textureIds[0];
					gl.glBindTexture(GL10.GL_TEXTURE_2D, this.textureId);
					
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
				
					GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

					
					gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
										
					/*
					 * This is a change to the original tutorial, as buildMipMap does not exist anymore
					 * in the Android SDK.
					 * 
					 * We check if the GL context is version 1.1 and generate MipMaps by flag.
					 * Otherwise we call our own buildMipMap implementation
					 */
					if(gl instanceof GL11) {
						gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
						GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
						
					//
					} else {
						buildMipmap(gl, bitmap);
					}		
					
					//Clean up
					bitmap.recycle();
					
						} catch (Exception ex) {
					Log.d("Texture", "Couldn't load bitmap");
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

	/**
	 * return the adequate GL10 primitive opengl-Type of this geometry
	 * @param type primitive type
	 * @return GL10 type
	 */
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

	/**
	 * this will render the whole geometry with this geometry-primitive type
	 */
	public void render() {
		if(this.type==null)
			render(Type.Triangles, 0, numVertices);
		else
			render(this.type[0], 0, numVertices);
	}
	
	/**
	 * this will render the whole geometry with the given type
	 * @param type		opengl-primitive type
	 */
	public void render(Type type) {
		render(type, 0, numVertices);
	}

	
	/**
	 * This method render a specific Geometry, with a specific Type
	 * if there are multiple Objects in this Geometry
	 * 
	 * @param id 	the id of the sub-geometry (group)
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


	/**
	 * this will render a specific sub geometry with given offset and numberOfVertices with this geometry primitive type
	 * @param offset		beginning offset from the buffer/array
	 * @param numVertices	length of the buffer/array
	 */
	public void render(int offset, int numVertices) {
		
		render(this.type[0],offset,numVertices);
	}

	/**
	 * this will render a specific sub geometry with given offset and numberOfVertices with given geometry primitive type
	 * @param type			opengl primitive type
	 * @param offset		beginning offset from the buffer/array
	 * @param numVertices	length of the buffer/array
	 */
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

	/**
	 * this will add an vertex from a vector
	 * @param v		the given vector
	 */
	public void vertex(Vector3f v) {
		vertex(v.x, v.y, v.z);
	}
	
	/**
	 * this will add an vertex from the given x,y,z values
	 * @param x		x-coordinate
	 * @param y		y-coordinate
	 * @param z		z-coordinate
	 */
	public void vertex(float x, float y, float z) {
		init = false;
		int offset = indexVertex * 3;
		vertices[offset] = x;
		vertices[offset + 1] = y;
		vertices[offset + 2] = z;
		indexVertex++;
		numVertices=indexVertex;
	}

	/**
	 * this will add an color to the given vertex from an Color Object
	 * @param c	Color Object
	 */
	public void color(Color c) {
		this.color(c.r, c.g, c.b, c.a);
	}
	
	/**
	 * this will add an color to the given vertex from an Color Object
	 * @param c	Color Object
	 */
	public void color(float r, float g, float b) {
		this.color(r, g, b, 1);
	}
	
	/**
	 * this will add an color to the given r g b a values
	 * @param r		red value
	 * @param g		green value	
	 * @param b		blue value
	 * @param a		alpha value (transparency)
	 */
	public void color(float r, float g, float b, float a) {
		init = false;
		int offset = indexColor * 4;
		colors[offset] = r;
		colors[offset + 1] = g;
		colors[offset + 2] = b;
		colors[offset + 3] = a;
		indexColor++;
	}

	/**
	 * this will add an normal from the given x y z values
	 * @param x		x coordinate
	 * @param y		y coordinate
	 * @param z		z coordinate
	 */
	public void normal(float x, float y, float z) {
		init = false;
		int offset = indexNormal * 3;
		normals[offset] = x;
		normals[offset + 1] = y;
		normals[offset + 2] = z;
		indexNormal++;
	}

	/**
	 * this will add an texture coordinate (UV)
	 * @param s		U coordinate
	 * @param t		V coordinate
	 */
	public void texCoord(float s, float t) {
		init = false;
		int offset = indexTexCoords * 2;
		texCoords[offset] = s;
		texCoords[offset + 1] = t;
		indexTexCoords++;
	}

	/**
	 * will return the maximum of vertices
	 * @return maximum of vertices
	 */
	public int getMaximumVertices() {
		return vertices.length / 3;
	}

	/**
	 * will reset this geometry
	 */
	public void reset() {
		indexVertex = 0;
		indexColor = 0;
		indexNormal = 0;
		indexTexCoords = 0;
		init = false;
	}

	/**
	 * will dispose / delete used buffer on graphic card and set arrays to null for Garbage Collector
	 */
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
	}
	
	/**
	 * this method add another Geometry to this one, (only same primitive type and with same textureId)
	 * @param other 		geometry with will be added
	 * @param rotation		orientation of the added geometry
	 * @param scale			scale of the added geometry
	 * @param translation	translation of the added geometry
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
	
	/**
	 * Our own MipMap generation implementation.
	 * Scale the original bitmap down, always by factor two,
	 * and set it as new mipmap level.
	 * 
	 * Thanks to Mike Miller (with minor changes)!
	 * 
	 * @param gl - The GL Context
	 * @param bitmap - The bitmap to mipmap
	 */
	private void buildMipmap(GL10 gl, Bitmap bitmap) {
		//
		int level = 0;
		//
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();

		//
		while(height >= 1 || width >= 1) {
			//First of all, generate the texture from our bitmap and set it to the according level
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
			
			//
			if(height == 1 || width == 1) {
				break;
			}

			//Increase the mipmap level
			level++;

			//
			height /= 2;
			width /= 2;
			Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
			
			//Clean up
			bitmap.recycle();
			bitmap = bitmap2;
		}
	}


	
}
