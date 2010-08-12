package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * Represents the user interface
 * @author Wolfgang Knecht
 *
 */
public class Interface {
	Texture jumpFieldTex;
	Texture leftFieldTex;
	Texture rightFieldTex;
	Quad leftQuad;
	Quad rightQuad;
	Quad jumpQuad;
	
	float screenWidth;
	float screenHeight;
	
	float fieldSize=2.0f;
	float gap=0.5f;
	
	/**
	 * Initializes the interface
	 * @param gl The OpenGL context
	 */
	public void init(GL10 gl) {
		jumpFieldTex=new Texture();
		jumpFieldTex.create(R.drawable.l55_jumpfield);
		
		leftFieldTex=new Texture();
		leftFieldTex.create(R.drawable.l55_leftfield);
		
		rightFieldTex=new Texture();
		rightFieldTex.create(R.drawable.l55_rightfield);
		
		jumpQuad=new Quad();
		jumpQuad.init(gl, 0.0f,0.0f,fieldSize*2.25f,fieldSize*0.5f);
		
		leftQuad=new Quad();
		leftQuad.init(gl, 0.0f,0.0f,fieldSize,fieldSize);
		
		rightQuad=new Quad();
		rightQuad.init(gl, 0.0f,0.0f,fieldSize,fieldSize);
	}
	
	/**
	 * Renders the interface
	 * @param gl The OpenGL context
	 */
	public void draw(GL10 gl) {
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		
		gl.glLoadIdentity();
		gl.glTranslatef(screenWidth-gap-fieldSize, screenHeight-gap-fieldSize, 0.0f);
		rightFieldTex.bind(gl);
		rightQuad.draw(gl);
		
		gl.glTranslatef(-gap-fieldSize, 0.0f, 0.0f);
		leftFieldTex.bind(gl);
		leftQuad.draw(gl);
		
		gl.glTranslatef(0.0f, -(fieldSize+gap)*0.5f, 0.0f);
		jumpFieldTex.bind(gl);
		jumpQuad.draw(gl);
	}
}
