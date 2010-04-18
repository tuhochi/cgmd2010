package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import java.io.IOException;
import java.io.InputStream;

import android.R.string;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Images 
{
	private static native int nativePushBitmap(int[] pixels, String name, int w, int h);
	
	private Context mContext;
	
	
	public Images(Context _context)
	{
		mContext = _context;
	}
	
	public void loadImages()
	{
		loadBitmap(R.drawable.l77_star_64, "l77_star_64.png");
		loadBitmap(R.drawable.l77_heart_64, "l77_heart_64.png");
		loadBitmap(R.drawable.l77_karo_64, "l77_karo_64.png");
		loadBitmap(R.drawable.l77_triangle_64, "l77_triangle_64.png");
		loadBitmap(R.drawable.l77_circle_64, "l77_circle_64.png");
	}
	
	private void loadBitmap(int res_id, String name)
	{
        InputStream is = mContext.getResources().openRawResource(res_id); 

        Bitmap bitmap;
		try {
		    bitmap = BitmapFactory.decodeStream(is);
		} finally {
		    try {
		        is.close();
		    } catch(IOException e) {
		    }
		}
		
		//...or Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imgname);
		//1st method: GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		//2nd method
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		
		int[] pixels = new int[w * h]; 
		bitmap.getPixels(pixels, 0, w, 0, 0, w, h); 
		
//		for (int i = 0; i < w; i++)
//		{
//			for (int j = 0; j < h; j++)
//			{
//				if (i <= 0 || j <= 0)
//				{
//					pixels[j*w+i] = 0xff00ff00;
//				}
//				else
//				{
//					pixels[j*w+i] = 0x00770077;
//				}
//			}
//		}
		
		nativePushBitmap(pixels, name, w, h);
		
	}
}
