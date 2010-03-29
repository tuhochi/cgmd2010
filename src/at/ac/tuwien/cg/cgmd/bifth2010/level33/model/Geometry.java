package at.ac.tuwien.cg.cgmd.bifth2010.level33.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Color;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector3;

public class Geometry {
	public enum Type {
		Points, Lines, Triangles, TriangleStrip
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

	public static boolean useVBO = true;

	public static int geometryCount = 0;

	public Geometry() {

	}

	public Geometry(GL10 gl, int numVertices, boolean hasColors,
			boolean hasTextureCoordinates, boolean hasNormals) {
		this.gl = gl;
		vertices = new float[numVertices * 3];
		int[] buffer = new int[1];

		if (!useVBO)
			vertexBuffer = allocateBuffer(numVertices * 3);
		else {
			((GL11) gl).glGenBuffers(1, buffer, 0);
			vertexHandle = buffer[0];
			vertexBuffer = FloatBuffer.wrap(vertices);
		}

		if (hasColors) {
			colors = new float[numVertices * 4];
			if (!useVBO)
				colorBuffer = allocateBuffer(numVertices * 4);
			else {
				((GL11) gl).glGenBuffers(1, buffer, 0);
				colorHandle = buffer[0];
				colorBuffer = FloatBuffer.wrap(colors);
			}
		}

		if (hasTextureCoordinates) {
			texCoords = new float[numVertices * 2];
			if (!useVBO)
				texCoordBuffer = allocateBuffer(numVertices * 2);
			else {
				((GL11) gl).glGenBuffers(1, buffer, 0);
				texHandle = buffer[0];
				texCoordBuffer = FloatBuffer.wrap(texCoords);
			}
		}

		if (hasNormals) {
			normals = new float[numVertices * 3];
			if (!useVBO)
				normalBuffer = allocateBuffer(numVertices * 3);
			else {
				((GL11) gl).glGenBuffers(1, buffer, 0);
				normalHandle = buffer[0];
				normalBuffer = FloatBuffer.wrap(normals);
			}
		}
	}

	private FloatBuffer allocateBuffer(int size) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(size * 4);
		buffer.order(ByteOrder.nativeOrder());
		return buffer.asFloatBuffer();
	}

	private void update() {
		if (!useVBO) {
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

	private int getPrimitiveType(Type type) {
		if (type == Type.Lines)
			return GL10.GL_LINES;
		else if (type == Type.Triangles)
			return GL10.GL_TRIANGLES;
		else if (type == Type.TriangleStrip)
			return GL10.GL_TRIANGLE_STRIP;
		else
			return GL10.GL_POINTS;

	}

	public void render(Type type, int offset, int numVertices) {
		boolean wasInit = init;
		if (!init)
			update();

		if (this == lastGeometry && wasInit) {
			gl.glDrawArrays(getPrimitiveType(type), offset, numVertices);
			return;
		} else {
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
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
		lastGeometry = this;
	}

	public void render() {
		render(Type.Triangles, 0, numVertices);
	}

	public void render(Type type) {
		render(type, 0, numVertices);
	}

	public void vertex(Vector3 v) {
		vertex(v.x, v.y, v.z);
	}

	public void vertex(float x, float y, float z) {
		init = false;
		int offset = indexVertex * 3;
		vertices[offset] = x;
		vertices[offset + 1] = y;
		vertices[offset + 2] = z;
		indexVertex++;
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
}
