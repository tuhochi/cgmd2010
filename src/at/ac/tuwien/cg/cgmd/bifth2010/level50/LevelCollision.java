package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import java.util.HashMap;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class LevelCollision {
	private int collisionArray[];
	private int width, height;
	private HashMap<Integer, LevelObject> coins = new HashMap<Integer, LevelObject>();
	
	public LevelCollision(GL10 gl, Context context, int id) {
		LoadTexture(gl, id, context);
	}
	
	private void LoadTexture(GL10 gl, int id, Context context)
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
                  if ((px1&0x00ffffff) == 0x00ff0000) {
                	  LevelObject coin = new LevelObject(gl, context, this, j*20, i*20, 20, 20, R.drawable.l00_coin);
                	  coins.put(i*width+j,coin);
                  }
             }
        }  
	}
	
	public int TestCollision(int x,int y) {
		if (0<=x && x<width && 0<=y && y<height) {
			if (coins.containsKey(y*width+x))
				coins.get(y*width+x).changeTexture(R.drawable.l00_icon);
			return collisionArray[y*width+x];
		} else return 0;
	}
	
	public void draw(GL10 gl) {
		for (LevelObject coin : coins.values()) {
			coin.draw(gl);
		}
	}	
}
