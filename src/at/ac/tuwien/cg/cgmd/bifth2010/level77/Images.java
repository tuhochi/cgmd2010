package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import java.io.IOException;
import java.io.InputStream;

import android.R.string;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * @author mike_vasiljevs
 *	Bitmap class - loads Java resource bitmaps for native code
 */
public class Images
{
	private static native int nativePushBitmap(int[] pixels, String name, int w, int h);
	
	public static int[] curr_bitmap_data;
	public static int curr_w, curr_h;
	
	private Context mContext;
	
	
	public Images(Context _context)
	{
		mContext = _context;
	}
	
	/**
	 * specifies which images to load, forwards to loadBitmap
	 */
	public void loadImages()
	{
		loadBitmap(R.drawable.l77_multi_blocks, "l77_multi_blocks.png");
		loadBitmap(R.drawable.l77_paused, "l77_paused.png");
		loadBitmap(R.drawable.l77_grey_box, "l77_grey_box.png");
		
	}
	
	/**
	 * loadBitmap
	 * loads a bitmap from resource id and forwards it to native code
	 * @param res_id
	 * @param name
	 */
	public void loadBitmap(int res_id, String name)
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
		
//		for (int j = 0; j < h; j++)
//			for (int i = 0; i < w; i++)
//			{
//				if (i <= 2 || j <= 2)
//					pixels[j*w+i] = 0xff0000ff;
//				else
//					pixels[j*w+i] = 0x22ee0099;
//					
//			}
		
		nativePushBitmap(pixels, name, w, h);
		
	}
	
	//TODO to be called by jni when threads/javaVM get fixed (at the moment crashes)
	//	sets the static field with the current bitmap data that should be read by jni
	public void getBitmapData(String name)
	{
		int res_id = 0;
		
		if (name == "multi-blocks.png")
			res_id = R.drawable.l77_multi_blocks;
		else
			res_id = R.drawable.l00_map;
		
		//load bitmap
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

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		
		curr_bitmap_data = new int[w * h]; 
		bitmap.getPixels(curr_bitmap_data, 0, w, 0, 0, w, h);
		
		curr_w = w;
		curr_h = h;
		
		//return curr_bitmap_data;
	}
	
	//debug stuff
	public void printStr(String s)
	{
		Log.d("l77native", "--l77:native callback-- " + s);
	}
	
	public void voidTest()
	{
		Log.d("l77native", "--l77:native callback, voidTest()--");
	}
}
