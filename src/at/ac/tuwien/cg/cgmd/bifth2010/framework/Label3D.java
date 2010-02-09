package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.CommonFunctions;

public class Label3D {

	private static final String CLASS_TAG = Label3D.class.getSimpleName();
	Canvas mCanvas = null;
	Rectangle mRetangle = null;
	private float mWidth = 0;
	
	public Label3D(float width){
		mWidth  = width;
	}
	
	
	
	public void setText(GL10 gl, Paint paint, String[] text, int textSpacing) {
		int measuredTextWidth = 0;
		for(int i=0; i<text.length; i++) {
			if(text[i]!=null){
				int width = (int) Math.ceil(paint.measureText(text[i]));
				if(width>measuredTextWidth){
					measuredTextWidth=width;
				}
			}
		}
		
        int ascent = (int) Math.ceil(-paint.ascent());
        int descent = (int) Math.ceil(paint.descent());
        int textHeight = ascent + descent;
        int labelHeight = textHeight * text.length + textSpacing * (text.length-1);
			
        Bitmap mTextBitmap = null;
        
        try{
        	mTextBitmap = Bitmap.createBitmap(measuredTextWidth, labelHeight, Bitmap.Config.ARGB_4444);
        }catch(OutOfMemoryError e){
        	Log.e(CLASS_TAG, "Couldn't create bitmap: "+e.getMessage());
        	return;
        }
        
        Canvas mCanvas = new Canvas(mTextBitmap);

		mTextBitmap.eraseColor(Color.argb(255, 0, 0, 0));
		
		float fX = 0;
		if(paint.getTextAlign()==Align.CENTER){
			fX = (float)measuredTextWidth * 0.5f;
		} else if (paint.getTextAlign()==Align.RIGHT){
			fX = (float)measuredTextWidth;
		}
		int iPos = textHeight;
		for(int i=0; i<text.length; i++) {
			if(text[i]!=null){
				mCanvas.drawText(text[i], fX, iPos, paint);
			}
			iPos += textHeight + textSpacing;
		}
		
		float fAspect = (float)labelHeight / (float)measuredTextWidth;
		mRetangle=new Rectangle(mWidth, mWidth*fAspect);
		mRetangle.setTexture(gl, mTextBitmap);
		mTextBitmap.recycle();		
	}
	
	public void draw(GL10 gl) {
		if(mRetangle==null){
			return;
		}
		mRetangle.draw(gl);
	}

	public float getHeight() {
		if(mRetangle==null){
			return 0;
		}
		return mRetangle.getHeight();
	}
}
