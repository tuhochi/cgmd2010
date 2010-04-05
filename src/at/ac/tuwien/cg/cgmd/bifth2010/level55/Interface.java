package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import javax.microedition.khronos.opengles.GL10;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Interface {
	Texture inputFieldTex;
	Quad leftQuad;
	Quad rightQuad;
	Quad jumpQuad;
	
	float screenWidth;
	float screenHeight;
	
	public void init() {
		inputFieldTex=new Texture();
		inputFieldTex.create(R.drawable.l55_inputfield);
		
		jumpQuad=new Quad();
		jumpQuad.init(0.0f,0.0f,1.0f,1.0f);
	}
	
	public void draw(GL10 gl) {
		gl.glLoadIdentity();
		gl.glTranslatef(0.5f, screenHeight-1.5f, 0.0f);
		inputFieldTex.bind(gl);
		jumpQuad.draw(gl);
	}
}
