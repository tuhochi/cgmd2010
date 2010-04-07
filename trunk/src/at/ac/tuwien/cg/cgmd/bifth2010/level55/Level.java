package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Level {
	
	Quad interfaceQuad;
	Texture textureTest;
	
	TileLayer frontLayer;
	TileLayer secondLayer;
	TileLayer thirdLayer;
	
	public void init(GL10 gl, Context context) {
		textureTest = new Texture();
		textureTest.create(R.drawable.l55_testtexture);
		
		frontLayer=new TileLayer();
		frontLayer.init(gl, 1.0f, 1.0f, R.raw.l55_level, R.drawable.l55_front_layer_tex, 2, 4, context);
		
		secondLayer=new TileLayer();
		secondLayer.init(gl, 0.5f, 2.0f, R.raw.l55_level, R.drawable.l55_testtexture, 2, 4, context);
		
		thirdLayer=new TileLayer();
		thirdLayer.init(gl, 0.25f, 3.0f, R.raw.l55_level, R.drawable.l55_testtexture, 2, 4, context);
	}
	
	public void draw(GL10 gl) {   
        textureTest.bind(gl);
        
        thirdLayer.draw(gl);
        secondLayer.draw(gl);
        frontLayer.draw(gl);
	}
	
	public void setPosition(float x, float y) {
		frontLayer.posX=x;
		frontLayer.posY=y;
		
		secondLayer.posX=x;
		secondLayer.posY=y;
		
		thirdLayer.posX=x;
		thirdLayer.posY=y;
	}
	
	void update(float dT) {
		frontLayer.update(dT);
	}
	
	

}
