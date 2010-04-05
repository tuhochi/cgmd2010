package at.ac.tuwien.cg.cgmd.bifth2010.level55;


import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Level {
	
	Quad interfaceQuad;
	Texture textureTest;
	
	TileLayer frontLayer;
	TileLayer secondLayer;
	TileLayer thirdLayer;
	
	public void init(GL10 gl) {
		textureTest = new Texture();
		textureTest.create(R.drawable.l55_testtexture);
		
		frontLayer=new TileLayer();
		frontLayer.init(1.0f);
		
		secondLayer=new TileLayer();
		secondLayer.init(0.5f);
		
		thirdLayer=new TileLayer();
		thirdLayer.init(0.25f);
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
