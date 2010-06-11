package at.ac.tuwien.cg.cgmd.bifth2010.level33;

import static android.opengl.GLES10.GL_MODELVIEW;

import static android.opengl.GLES10.GL_PROJECTION;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glViewport;
import static android.opengl.GLU.gluPerspective;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2i;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.Camera;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.SceneGraph;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;



public class GameRenderer implements GLSurfaceView.Renderer {
	
	LevelActivity levelActivity = null;
	SceneGraph sg = null;
	boolean isLoadingScreenActive=false;
	public static boolean SWITCH_VIEW = false;

	public GameRenderer(LevelActivity levelActivity, SceneGraph sg) {
		// TODO Auto-generated constructor stub
		Log.d("GameRenderer","constructor");
		this.levelActivity = levelActivity;
		this.sg = sg;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		
		if(LevelActivity.GAME_STATE.equals(LevelActivity.AllGameStates.PLAYING))
		{
			if(SWITCH_VIEW)
			{
				sg.switchView();
				SWITCH_VIEW=false;
			}
			
			LevelActivity.sceneGraph.render(gl);
		}
		
		if(LevelActivity.GAME_STATE.equals(LevelActivity.AllGameStates.LOADING))
		{
			if(!isLoadingScreenActive)
				isLoadingScreenActive=true;
			else
			{
				//Init all things
				SceneGraph.init(gl); // now init the Geometry VBO´s
				
				LevelActivity.GAME_STATE=LevelActivity.AllGameStates.READY;
				sg.loadingComplete();
			}
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.d("onSurfaceChanged","jo");
		if (height == 0) {
			height = 1;
		}

		gl.glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
		LevelActivity.resolution = new Vector2f(width, height);
		
		// set Game Frustum
		double x =Camera.standardZoom/Math.sqrt(2)*(LevelActivity.resolution.x/LevelActivity.resolution.y);
		double y = x/(LevelActivity.resolution.x/LevelActivity.resolution.y);
		
		SceneGraph.touchDim.set((float)x,(float)y);
		
		Log.d("Frustum: x/y",String.valueOf(x)+" "+String.valueOf(y) );
		
		Vector2i f= new Vector2i((int)Math.round(x/2+1.5),(int)Math.round(y/2+1.5));
		SceneGraph.frustumDim.set(f.x,f.y);
		
		Log.d("old Frustum: ",String.valueOf(SceneGraph.frustumDim.x)+" "+String.valueOf(SceneGraph.frustumDim.y) );
		Log.d("new Frustum: ",String.valueOf(f.x)+" "+String.valueOf(f.y) );
		
		

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		//levelActivity.viewSwitcher.getNextView();
		//levelActivity.setContentView(R.layout.l33_loading_screen);
		
		Log.d("onSurfaceCreated","jo");
		//SceneGraph.init(gl); // now init the Geometry VBO´s
		Log.d("onSurfaceCreated","fin");
		
		//levelActivity.setContentView(R.layout.l33_level);
		
		
		
	}

}