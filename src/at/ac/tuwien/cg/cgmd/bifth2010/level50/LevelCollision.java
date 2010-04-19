package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LevelCollision {
	private int collisionArray[];
	private int width, height;
	
	public LevelCollision(Context context, int id) {
		LoadTexture(id, context);
	}
	
	private void LoadTexture(int id, Context context)
	{			
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);
		width = bmp.getWidth();
		height = bmp.getHeight();
		int pixels[] = new int[width*height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);	
		collisionArray=new int[width*height];
		for(int i=0; i<height; i++)
        {
             for(int j=0; j<width; j++)
             {
                  //correction of R and B
                  int pix=pixels[i*width+j];
                  int pb=(pix>>16)&0xff;
                  int pr=(pix<<16)&0x00ff0000;
                  int px1=(pix&0xff00ff00) | pr | pb;
                  //correction of rows
                  collisionArray[i*width+j]=px1;
             }
        }  
		
	}
	
	public int TestCollision(int x,int y) {
		if (0<=x && x<width && 0<=y && y<height)
			return collisionArray[y*width+x];
		else return 0;
	}
	
}
