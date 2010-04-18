package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11Ext;

import android.util.Log;

public class MySprite implements Drawable{

	float x,y,z,width,height;
	private int textureNum;
	
	public MySprite(int resourceId,float x,float y,float width,float height,GL10 gl) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		textureNum = MyTextureManager.singleton.addTexturefromResources(resourceId, gl);
		Log.d("Spirte","texturenum ="+textureNum);	 
	}
	
	public MySprite(String filename,float x,float y,float width,float height,GL10 gl) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		textureNum = MyTextureManager.singleton.addTexturefromAssets(filename, gl);
		Log.d("Spirte","texturenum ="+textureNum);	 
	}
	
	public MySprite(MySprite sprite){
		this.x = sprite.x;
		this.y = sprite.y;
		this.width = sprite.width;
		this.height = sprite.height;
		
		this.textureNum = sprite.textureNum;
	}

	public MySprite(MySprite sprite,float x, float y){
		this(sprite);
		
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void Dispose(GL10 gl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Draw(GL10 gl) {
		MyTextureManager.singleton.textures[textureNum].Bind(gl);
		((GL11Ext) gl).glDrawTexfOES(x,y,0,width, height);
	}

	@Override
	public void Init(GL10 gl) {
		
		
	}

}
