package at.ac.tuwien.cg.cgmd.bifth2010.level20;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


/**
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class RenderEntity extends GameEntity implements Renderable, Clickable {

	/** The OpenGl texture handle */
	protected int texture;
	/** If it is visible */
	protected boolean visible;	
	/**Flag whether the entity is clickable or not. */	
	protected boolean clickable;	
	/** Flag whether the entity is currently animated or not. (If an animator is attached to it) */
	protected boolean animated;
	/** Holds the vertices */
	protected FloatBuffer vertexBuffer;
	/** Holds the texture coordinates */
	protected FloatBuffer textureBuffer;
	/** The half width of the bounding box */
	protected float bb_hWidth;
	/** The half height of the bounding box */
	protected float bb_hHeight;
	
	
	/**
	 * @param x The X coordinate. 
	 * @param y The Y coordinate. 
	 * @param z The Z coordinate (the depth). 
	 * @param width The width of the entity.
	 * @param height The height of the entity.
	 */
	public RenderEntity(float x, float y, float z, float width, float height) {	
		super();		
		init();
		createQuad(x, y, z, width, height, 0.0f, 0.0f, 1.0f, 1.0f);		
	}
	

	/**
	 * Constructor for setting custom texture coordinates.
	 * 
	 * @see public RenderEntity(float x, float y, float z, float width, float height)
	 * @param texCoordOffsetX	The x-offset to use for the texture coordinates.
	 * @param texCoordOffsetY	The y-offset to use for the texture coordinates.
	 * @param texCoordW			The width of the texture rectangle.
	 * @param texCoordT			The height of the texture rectangle.
	 */
	public RenderEntity(float x, float y, float z, float width, float height, 
			float texCoordOffsetX, float texCoordOffsetY, float texCoordW, float texCoordH) {
		super();		
		init();
		createQuad(x, y, z, width, height, texCoordOffsetX, texCoordOffsetY, texCoordW, texCoordH);		
	}
	
	
	private void createQuad(float x, float y, float z, float width, float height, 
			float texCoordOffsetX, float texCoordOffsetY, float texCoordW, float texCoordH) {
		setPos(x, y);
		this.z = z;
		setDim(width, height);

		// If not changed, the bb is the same size as this dimension
		setBBDim(width, height);
		
		// Since these values are correct !now!, just use them
		float vertices[] = {-bb_hWidth, -bb_hHeight, 0, 		
							 bb_hWidth, -bb_hHeight, 0, 		
							-bb_hWidth,  bb_hHeight, 0,
							 bb_hWidth,  bb_hHeight, 0};

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		float texCoords[] = {
				texCoordOffsetX,  texCoordOffsetY+texCoordH,
				texCoordOffsetX+texCoordW, texCoordOffsetY+texCoordH,
				texCoordOffsetX,  texCoordOffsetY,
				texCoordOffsetX+texCoordW, texCoordOffsetY
		};
		
//		texCoords = {0.0f,  1.0f,
//				 1.0f, 1.0f,
//				 0.0f,  0.0f,
//				 1.0f, 0.0f};
		
		byteBuf = ByteBuffer.allocateDirect(texCoords.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texCoords);
		textureBuffer.position(0);
	}
	
	
	/** Common value initialization of RenderEntity. */
	public void init() {
		// INFO: Changed this initial value to 0 to get a white quad on screen everytime
		texture = 0;
		visible = true;		
		clickable = true;
		animated = false;
	}
	
	@Override
	public void render(GL10 gl) {
		
		if (!visible)
			return;		

		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
				
		//Point to our buffers
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glPushMatrix();
		
			// Move around
			gl.glTranslatef(x, y, z);
			// Rotate around the Z axis
			gl.glRotatef(angle, 0, 0, 1);
					
			//Draw the vertices as triangle strip
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		gl.glPopMatrix();
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level20.Clickable#hitTest(float, float)
	 */
	@Override
	public boolean hitTest(float hitX, float hitY) {
		if (!visible || !clickable) return false;
		
		return (hitX >= x - bb_hWidth && hitX < x + bb_hWidth && hitY >= y - bb_hHeight && hitY < y + bb_hHeight);
	}
	
	/** 
	 * Collision test with another quad.
	 * */
	public boolean collisionTest(float minX, float minY, float maxX, float maxY) {
		if (!visible) return false;
		
		return ( ((minX >= x - bb_hWidth && minX <= x + bb_hWidth ) ||	 		// minX 
				 (maxX >= x - bb_hWidth && maxX <= x + bb_hWidth )) &&   		// maxX
				 ((minY >= y - bb_hHeight && minY <= y + bb_hHeight ) || 		// minY
				 (maxY >= y - bb_hHeight && maxY <= y + bb_hHeight )) ); 		// maxY
	}
	
	/**
	 * Sets the bounding box dimension in a comfortable way
	 * @param width The width of the bounding box.
	 * @param height The height of the bounding box
	 */
	public void setBBDim(float width, float height) {
		bb_hWidth = width * 0.5f;
		bb_hHeight = height * 0.5f;
	}
	
}
