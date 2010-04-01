package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 * @author arthur (group 13)
 *
 */
public abstract class GameObject {
	//smallest size of an object (= player size, = tile size)
	protected static final int BLOCKSIZE = 34;
	
	//offset of background and beer due to movement
	protected static final Vector2 offset = new Vector2(0, 0);
	
	//position of object ((0,0) is bottom left)
	protected Vector2 position;
	
	//vertex coordinates
	protected FloatBuffer vertexBuffer;
	protected float[] vertices;
	protected ShortBuffer indexBuffer;
	
	//texture of object (=singleton)
	protected Texture texture;
	
	/**
	 * constructor sets up object (used by subtypes)
	 * @param objectWidth
	 * @param objectHeight
	 */
	public GameObject(float objectWidth, float objectHeight) {
		//set position
		this.position = new Vector2(0, 0);
		
		//define vertices coordinates
		vertices = new float[12];
		//bottem left
		vertices[0] = 0f;
		vertices[1] = 0f;
		vertices[2] = 0f;
		//bottom right
		vertices[3] = objectWidth;
		vertices[4] = 0f;
		vertices[5] = 0f;
		//top right
		vertices[6] = objectWidth;
		vertices[7] = objectHeight;
		vertices[8] = 0f;
		//top left
		vertices[9] = 0f;
		vertices[10] = objectHeight;
		vertices[11] = 0f;
		
		//set up vertex-buffer
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		//set up index buffer
		short[] indices = { 0, 1, 2, 0, 2, 3 };
		ByteBuffer indexBBuffer = ByteBuffer.allocateDirect(indices.length * 2);
		indexBBuffer.order(ByteOrder.nativeOrder());
		indexBuffer = indexBBuffer.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		//set texture
		this.texture = TextureSingletons.getTexture(this.getClass().getSimpleName());
	}
	
	/**
	 * draws the object onto the screen
	 * @param gl
	 */
	public abstract void draw(GL10 gl);

	/**
	 * updates offset with movement
	 * @param movement
	 */
	public static void updateOffset(Vector2 movement) {
		offset.add(movement);
	}	
}
