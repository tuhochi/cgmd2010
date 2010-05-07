package at.ac.tuwien.cg.cgmd.bifth2010.level33;

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.GL_PROJECTION;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glViewport;
import static android.opengl.GLU.gluPerspective;
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

	public GameRenderer() {
		// TODO Auto-generated constructor stub
		Log.d("GameRenderer","constructor");
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		LevelActivity.sceneGraph.render(gl);
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
		Log.d("onSurfaceCreated","jo");
		SceneGraph.init(gl); // now init the Geometry VBO´s
		Log.d("onSurfaceCreated","fin");
		
	}

}