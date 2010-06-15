package at.ac.tuwien.cg.cgmd.bifth2010.level12;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.opengl.GLSurfaceView;

/** class responsible for drawing the opengl elements */
public class GLRenderer implements GLSurfaceView.Renderer{
	private boolean mCDTSoundPlayed = false;
	
	/**
	 * method called to draw on each frame, checks if its time to do the collision detection, calls the UI update method, 
	 * checks if its time to finish the game( last round number, no enemies on the gamefield anymore), or if its time to start a new round.
	 * Then all active globjects are drawn.
	 */
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
		if( GameMechanics.getSingleton().getRoundNumber() > Definitions.MAX_ROUND_NUMBER && GameWorld.getSingleton().getEnemiesSize() == 0 ){
			GameMechanics.getSingleton().finishGame(); //bit dirty hack
			return;
		}
		if( GameMechanics.getSingleton().getRoundNumber() == 0) {
			if( System.currentTimeMillis() - GameMechanics.getSingleton().getRoundStartedTime() > Definitions.GAME_START_TIME+2000 && !mCDTSoundPlayed){
				GameWorld.getSingleton().playCTDSound();
				mCDTSoundPlayed = true;
			}	
			if( System.currentTimeMillis() - GameMechanics.getSingleton().getRoundStartedTime() > Definitions.GAME_START_TIME+4000 ){
				GameWorld.getSingleton().initEnemies();
				GameMechanics.getSingleton().setRoundStartedTime();
				GameMechanics.getSingleton().nextRound();
			}
		}
		else if( GameMechanics.getSingleton().getRoundNumber() <= Definitions.MAX_ROUND_NUMBER ) {
			
			if( System.currentTimeMillis() - GameMechanics.getSingleton().getRoundStartedTime() > Definitions.GAME_ROUND_WAIT_TIME ){
				GameWorld.getSingleton().initEnemies();
				GameMechanics.getSingleton().setRoundStartedTime();
				GameMechanics.getSingleton().nextRound();
			}
		}
		
		GameWorld.getSingleton().drawTowers(gl);
		GameWorld.getSingleton().drawEnemies(gl);
		//FPSCounter.getSingleton().addFrame();
	}

	/** called from the app activity on resume, initializes the viewing perspective and opengl functions */
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

	/**  called before onSurfaceChanges, forwards the opengl context to the texture manager and (re)loades the texture samples */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		TextureManager.getSingletonObject().initializeGL(gl);
		TextureManager.getSingletonObject().loadTextures();
	}
	
}
