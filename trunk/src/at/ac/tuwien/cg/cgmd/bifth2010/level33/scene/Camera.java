package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLU.gluLookAt;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.GameView;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector3f;

public class Camera {
	
	public final static float standardZoom= 10.f;// standart Game zoom
	public final static float outZoom = 50.f;// overview Zoom
	public static float zoom = standardZoom;
	private boolean somethingChanged = true;
	
	Vector3f eye = new Vector3f(0f,zoom, 0.0000000001f);
	Vector3f view = new Vector3f(0, 0, 0);
	Vector3f up = new Vector3f(0, 1, 0);
	
	
    

    public void init(GL10 gl, int width, int height){
    	
		 float aspectRatio = (float)width / height;
	        GLU.gluPerspective( gl, 45, aspectRatio, 0.1f, 100 );
    	
    }
    
	public void lookAt(GL10 gl) {
		
		// if nothing has changed -> do nothing
		if(!somethingChanged)
			return;
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

        System.out.println("lookAt");

        eye.y=zoom;
        
//        if(zoom==standardZoom)
//        	view.set(0, 0, 0);
//        else
//        	view.set(0,0,0);
//        	eye.set(((GameView.lastTouch.x*2)-1)*10,((GameView.lastTouch.y*2)-1)*10,0);
        
        
      	gluLookAt(gl,eye.x, eye.y, eye.z, view.x, view.y, view.z , up.x, up.y, up.z  );// momentan nur zum probieren, danach von oben

      	// set to standard -> hasNotChanged == true
      	somethingChanged=false;
	}

	public void switchZoom() {
		somethingChanged=true;
	
		if(zoom==standardZoom)
			zoom=outZoom;
		else
			zoom=standardZoom;
		
	}


}
