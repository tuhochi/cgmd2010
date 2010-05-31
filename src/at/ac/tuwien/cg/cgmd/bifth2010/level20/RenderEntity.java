package at.ac.tuwien.cg.cgmd.bifth2010.level20;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


/**
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class RenderEntity extends GameEntity implements Renderable {

	protected int texture;
	protected boolean visible;	
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer textureBuffer;
	
	
	/**
	 * @param x The X coordinate. 
	 * @param y The Y coordinate. 
	 * @param z The Z coordinate (the depth). 
	 * @param width The width of the entity.
	 * @param height The height of the entity.
	 */
	public RenderEntity(float x, float y, float z, float width, float height) {
		
		super();
		// INFO: Changed this initial value to 0 to get a white quad on screen everytime
		texture = 0;
		visible = true;
		setPos(x, y);
		this.z = z;
		setDim(width, height);
		
		float hWidth = width * 0.5f;
		float hHeight = height * 0.5f;
		
		float vertices[] = {-hWidth, -hHeight, 0, 		
							 hWidth, -hHeight, 0, 		
							-hWidth,  hHeight, 0,
							 hWidth,  hHeight, 0};

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		// TODO: Does not handle independent Quad and Texture sizes
//		float ratio = width / height;		
		float texCoords[] = {0.0f,  1.0f,
							 1.0f, 1.0f,
							 0.0f,  0.0f,
							 1.0f, 0.0f};
		
		byteBuf = ByteBuffer.allocateDirect(texCoords.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texCoords);
		textureBuffer.position(0);
	}
	
	/**
	 * This Constructor is just for Shelf
	 */
	protected RenderEntity() {
		super();
	}
	
	@Override
	public void render(GL10 gl) {
		
		if (!visible)
			return;
		

		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		
		//Point to our buffers
//		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);		
		
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
		
		//Disable the client state before leaving
//		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}	
	
	// FERDI: Das brauch ma doch nicht :P Und verwenden tuts im Moment auch keine Komponente
//	public int texture() {
//		return texture;
//	}
//
//	public void setTexture(int texture) {
//		this.texture = texture;
//	}
//
//	public boolean isVisible() {
//		return visible;
//	}
//
//	public void setVisible(boolean visible) {
//		this.visible = visible;
//	}
}
