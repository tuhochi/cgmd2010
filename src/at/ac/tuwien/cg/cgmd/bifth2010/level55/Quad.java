package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import javax.microedition.khronos.opengles.GL10;

public class Quad extends Mesh {
	
	static float screenWidth;
	static float screenHeight;

	void init(GL10 gl) {
		init (gl, 0,0,screenWidth,screenHeight);
	}
	
	void init(GL10 gl, float x, float y, float width, float height) {
		float vertices[] = {
                x, y, 1,
                x+width, y, 1,
                x+width, y+height,  1,
                x, y+height,  1
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
