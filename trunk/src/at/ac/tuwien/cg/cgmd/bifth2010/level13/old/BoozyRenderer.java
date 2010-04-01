package at.ac.tuwien.cg.cgmd.bifth2010.level13.old;





import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;


public class BoozyRenderer extends GLSurfaceView implements Renderer{


	private Context context;
	private BoozyControl bControl;
	boolean colorWheel = false;


	
	public BoozyRenderer(Context context){
		super(context);

		this.bControl = new BoozyControl();
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		this.context = context;
		bControl.initialSetup();
	}
	
	
	
	@Override
	public void onDrawFrame(GL10 gl) {
		/*
		if (bControl.isDrunk()){
			if(colorWheel == true){
				colorWheel = false;
				gl.glClearColor(1.0f, 1.0f, 0.0f, 0.5f);
			}else{
				colorWheel = true;
				gl.glClearColor(1.0f, 0.0f, 0.0f, 0.5f);
			}

		}
		*/
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		bControl.run(gl);
	}
		

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0)
			height = 1;
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity(); //Reset projection matrix
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity(); //Reset world matrix
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		
		bControl.loadTexturesByObjectTypes(gl, context);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(1.0f, 1.0f, 0.0f, 0.5f);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		
	}


	
	public boolean onTouchEvent(MotionEvent event){
		
		bControl.handleAvatarControls(event, this);
		
		return true;

	}
	
	
	
	
}
