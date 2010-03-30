package at.ac.tuwien.cg.cgmd.bifth2010.level55;


import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Level {
	public void init(GL10 gl) {
		testQuad = new Quad();
		testQuad.init(10,20,256,256);
		
		textureTest = new Texture();
		textureTest.create(R.drawable.l55_testtexture);
		
		TilesManager.init();
	}
	
	public void render(GL10 gl) {   
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureTest.texture);

        testQuad.draw(gl);
        
        TilesManager.draw(gl);
	}
	
	void update(float dT) {
		
	}
	
	Quad testQuad;
	Texture textureTest;

}
