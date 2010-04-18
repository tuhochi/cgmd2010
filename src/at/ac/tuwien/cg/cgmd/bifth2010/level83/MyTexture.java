package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.graphics.Bitmap;
import android.opengl.GLUtils;


public class MyTexture {

	int handle;
	int resourceId;
	String file;
	
	public MyTexture(int id){
		this.resourceId = id;
	}
	
	public MyTexture(String file){
		this.file = file;
	}
	
	public void loadTexture(Bitmap file, GL10 gl){
		
		int[] num = new int[1];
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glGenTextures(1, num, 0);
		
		this.handle = num[0];
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, this.handle);
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);
        
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, file, 0);
        int[] mCropWorkspace = new int[4];
        
        mCropWorkspace[0] = 0;
        mCropWorkspace[1] = file.getHeight();
        mCropWorkspace[2] = file.getWidth();
        mCropWorkspace[3] = -file.getHeight();
        
        file.recycle();
        
        ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, 
                GL11Ext.GL_TEXTURE_CROP_RECT_OES, mCropWorkspace, 0);  
	}
	

	
	public void Dispose(GL10 gl) {
		int[] num = {handle};
		
		gl.glDeleteTextures(1, num, 0);
	}
		//Nur textur binden
	public void Bind(GL10 gl) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, handle);
	}

}
