package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLU.gluLookAt;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector3f;
/**
 * The Class Camera
 * @author roman hochstoger & christoph fuchs
 */
public class Camera {
	
	/** The Camera's standard distance*/
	public final static float standardZoom= 10.f;// standart Game zoom
	
	/** The Camera's distance to see the whole level*/
	public final static float outZoom = 22.f;// overview Zoom
	
	/** The actually camera-distance */
	public static float zoom = standardZoom;
	
	private boolean somethingChanged = true;
	
	Vector3f eye = new Vector3f(0,zoom, 0);
	Vector3f view = new Vector3f(0, 0, -0.00000001f);
	Vector3f up = new Vector3f(0, 1, 0);
	
	/**
	 * to init the Camera
	 * @param gl
	 * @param width of the Screen
	 * @param height of the Screen
	 */
    public void init(GL10 gl, int width, int height){
    	
		 float aspectRatio = (float)width / height;
	        GLU.gluPerspective( gl, 45, aspectRatio, 0.001f, 100 );
	        reset();
    	
    }
    /**
     * to reset the zoom/view
     */
    public void reset(){
    	
    	this.zoom=standardZoom;
	        somethingChanged=true;
    }
    
    /**
     * look at specific point
     * @param gl
     */
	public void lookAt(GL10 gl) {
		// if nothing has changed -> do nothing
		if(!somethingChanged)
			return;
		
		 Log.d("lookAt","1");
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

        Log.d("lookAt","2");

        eye.set(0,zoom, 0);
        
        	
      //  DebugView
      //  	eye.set(((GameView.lastTouch.x*2)-1)*10,((GameView.lastTouch.y*2)-1)*10,((GameView.lastTouch.x*2)-1)*10);
        //eye.set(0,zoom, 0.0000000001f);
      	gluLookAt(gl,eye.x, eye.y, eye.z, view.x, view.y, view.z , up.x, up.y, up.z  );// momentan nur zum probieren, danach von oben

      	// set to standard -> hasNotChanged == true

      	somethingChanged=false;
	}

	/**
	 * switch between play zoom and overview zoom
	 */
	public void switchZoom() {
		somethingChanged=true;
	
		if(zoom==standardZoom)
		{
			zoom=outZoom;
			SceneGraph.cameraTranslation.set(0,0);// set camera Translation to 0,0 that when a map is pushed -> the character is in the center
			SceneGraph.moveCamera(0, 0);// and moveCamera is called to cast the view, eq the character is on the edge
		}
		else
			zoom=standardZoom;
	}



}
