package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * This class represents the Shelf in the background of the game area. It is a fullscreen quad with changing texture coordinates. 
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class Shelf extends RenderEntity {

		
	
	/**
	 * @param width The screen width.
	 * @param height The screen height.
	 */
	public Shelf(float width, float height) {
		
		super();
		
		texture = 0;
		visible = true;
		setPos(x, y);
		z = 0;
		setDim(width, height);
		
		float vertices[] = {0, 		0, 		0, 		
							width, 	0, 		0, 		
							0,  	height, 0,
							width,  height, 0};

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		// (The texture is built that way)
		float oneSixth = 1.0f / 6.0f;		
		float texCoords[] = {0, 1 - oneSixth,
							 1, 1 - oneSixth,
							 0, oneSixth,
							 1, oneSixth};
		
		byteBuf = ByteBuffer.allocateDirect(texCoords.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texCoords);
		textureBuffer.position(0);
		
	}

	
	/**
	 * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - The GL Context
	 */
	public void render(GL10 gl) {
		
		
		// Apply Texture Matrix Transformation
		gl.glMatrixMode(GL10.GL_TEXTURE);		
		gl.glPushMatrix();
		// Translate the texture in texture coordinate system (, so divide it by the screen width).
		gl.glTranslatef(LevelActivity.gameManager.productManager.pixelsMoved / width, 0, 0);
		
		// Switch to ModelView and render as usual
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		super.render(gl);
			
		// Now pop Texture Matrix back, so that further objects are drawn with texture at origin. 
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glPopMatrix();
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);		
	}
}
	
