package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LevelCollision {
	private int collisionArray[];
	private int width, height;
	
	public LevelCollision(Context context, int id) {
		
	}
	
	private void LoadTexture(int num, int id, GL10 gl, Context context)
	{			
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);
		width = bmp.getWidth();
		height = bmp.getHeight();
		collisionArray=new int[width*height];
		bmp.getPixels(collisionArray, 0, width, 0, 0, width, height);					
	}
	
	public int TestCollision(int x,int y) {
		return collisionArray[x*width+y];
	}
	
}
