package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import javax.microedition.khronos.opengles.GL10;

/**
 * Represents a simple quad
 * @author Wolfgang Knecht
 *
 */
public class Quad extends Mesh {
	
	static float screenWidth;
	static float screenHeight;
	
	/**
	 * Initializes a screen quad
	 * @param gl The OpenGL context
	 */
	void init(GL10 gl) {
		init (gl, 0,0,screenWidth,screenHeight);
	}
	
	/**
	 * Initializes a quad
	 * @param gl The OpenGL context
	 * @param x The left x-coordinate of the quad
	 * @param y The top y-coordinate of the quad
	 * @param width The width of the quad
	 * @param height The height of the quad
	 */
	void init(GL10 gl, float x, float y, float width, float height) {
		float vertices[] = {
                x, y, -1.0f,
                x+width, y, -1.0f,
                x+width, y+height,  -1.0f,
                x, y+height,  -1.0f
        };
        
		float texCoords[] = {
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f,  1.0f,
                0.0f,  1.0f
        };

		
		short indices[] = {
                0, 1, 2,    0, 2, 3
        };
        
        super.init (gl, vertices, texCoords, indices);
	}
}
