package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.opengl.GLSurfaceView;

import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.MoneyCarrier;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.Tower;

public class GLRenderer implements GLSurfaceView.Renderer{
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();	
		GameWorld.getSingleton().getGamefield().draw(gl);
		//GameWorld.getSingleton().calcCollisions();	
		if( System.currentTimeMillis() - GameMechanics.getSingleton().getLastCollDetDoneTime() > Definitions.COLLISION_DETECTION_TIMEOUT ){
			GameWorld.getSingleton().calcCollisions();
			GameMechanics.getSingleton().setCollDetTime();
			GameUI.updateText();
		}
		if( GameMechanics.getSingleton().getRoundNumber() >= 0) {
			if( System.currentTimeMillis() - GameMechanics.getSingleton().getRoundStartedTime() > Definitions.GAME_ROUND_WAIT_TIME ){
				GameWorld.getSingleton().initEnemies();
				GameMechanics.getSingleton().setRoundStartedTime();
				GameMechanics.getSingleton().nextRound();
			}
		}
		if( GameMechanics.getSingleton().getRoundNumber() >= Definitions.MAX_ROUND_NUMBER && GameWorld.getSingleton().getEnemiesSize() == 0 ){
			GameMechanics.getSingleton().finishGame(); //bit dirty hack
		} else {
			GameWorld.getSingleton().drawTowers(gl);
			GameWorld.getSingleton().drawEnemies(gl);
			
		}
		FPSCounter.getSingleton().addFrame();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		 gl.glMatrixMode(GL10.GL_PROJECTION);    
	     gl.glOrthof(0.0f, width, 0.0f, height, -1.0f, 10.0f);
	     gl.glViewport(0, 0, width, height);   
	     gl.glMatrixMode(GL10.GL_MODELVIEW);  
	     gl.glFrontFace(GL10.GL_CCW);
	     gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	     gl.glEnableClientState(GL10.GL_COLOR_ARRAY);   
	     gl.glShadeModel(GL10.GL_SMOOTH);
	     gl.glClearColor(0.0f, 0.49321f, 0.49321f, 1.0f); 
	     gl.glClearDepthf(1.0f);
	     gl.glEnable(GL10.GL_DEPTH_TEST);
	     gl.glEnable(GL10.GL_LINE_SMOOTH);
	     gl.glDepthFunc(GL10.GL_LEQUAL);
	     gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);	
	     gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);	
	     gl.glDisable(GL10.GL_DITHER);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		TextureManager.getSingletonObject().initializeGL(gl);
		TextureManager.getSingletonObject().loadTextures();
	}
	
}
