package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class BoozyRenderer extends GLSurfaceView implements Renderer{

	private Quad avatar;
	private Context context;
	
	private List<Quad> gameObjects;
	

	float charMoveY = 0.0f;
	float charMoveX = 0.0f;
	
	
	
	
	public BoozyRenderer(Context context){
		super(context);
		//this.context = context;
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		this.context = context;
		
		gameObjects = new LinkedList<Quad>();
		
		avatar = new Quad();
		gameObjects.add(avatar);
		avatar.setObjectType("player");
		gameObjects.add(new Quad());
		gameObjects.get(1).setPos(3, 4);
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		
		//Limits the avatar to the screen boundaries (very diry ugly and hacky)

		if(!(avatar.X()+avatar.getSpeedX() < 3.5 && avatar.X()+avatar.getSpeedX() > -3.5)){
			avatar.setSpeedX(0);
		}
		
		if(!(avatar.Y()+avatar.getSpeedY() < 5.2 && avatar.Y()+avatar.getSpeedY() > -5.2)){
			avatar.setSpeedY(0);
		}
	
		
		//Basic aabb collision detection
		
		for (int i = 0; i < gameObjects.size(); i++){
		
			for (int j = i+1; j < gameObjects.size(); j++){
				Quad collidorA = gameObjects.get(i);
				Quad collidorB = gameObjects.get(j);
	
				if (collidorA.getMinX() > collidorB.getMaxX() ||
					collidorA.getMaxX() < collidorB.getMinX() ||
					collidorA.getMinY() > collidorB.getMaxY() ||
					collidorA.getMaxY() < collidorB.getMinY()
					){
					//No collision
					
				}else{
					//Collision
					collidorA.setPos(collidorA.X()-collidorA.getSpeedX(),collidorA.Y()-collidorA.getSpeedY());
					collidorB.setPos(collidorB.X()-collidorB.getSpeedX(),collidorB.Y()-collidorB.getSpeedY());
					
					//Set speed to avoid getting stuck
					//Behaviour definiton goes here lat0r
					collidorA.setSpeedX(0);
					collidorA.setSpeedY(0);
					collidorB.setSpeedX(0);
					collidorB.setSpeedY(0);
					}
				}
			}
		
		
		for (int i = 0; i < gameObjects.size(); i++){
			gameObjects.get(i).update();
			gameObjects.get(i).draw(gl);
			
		}
		
		
		
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
		// Default setup taken from nehe android port tutorial
		avatar.loadGLTexture(gl, this.context,R.drawable.l00_rabit_256);
		gameObjects.get(1).loadGLTexture(gl, this.context, R.drawable.l00_levelicon_start);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(1.0f, 1.0f, 0.0f, 0.5f);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	
	
	}

	public boolean onTouchEvent(MotionEvent event){
		float currentX = event.getX();
		float currentY = event.getY();
		
		if(event.getAction() == MotionEvent.ACTION_UP){
			int upAreaY = this.getHeight()/10;
			int downAreaY = this.getHeight()-upAreaY;
			int leftAreaX = this.getWidth()/10;
			int rightAreaX = this.getWidth()-leftAreaX;
			
			
			if (currentY < upAreaY){
				avatar.setSpeedY(0.01f);
				avatar.setSpeedX(0);
			}else if(currentY > downAreaY){
				avatar.setSpeedY(-0.01f);
				avatar.setSpeedX(0);
			}else if(currentX < leftAreaX){
				avatar.setSpeedX(-0.01f);
				avatar.setSpeedY(0);
			}else if(currentX > rightAreaX){
				avatar.setSpeedX(0.01f);
				avatar.setSpeedY(0);
			}else{
				avatar.setSpeedX(0);
				avatar.setSpeedY(0);
			}
			
			
			
			
		}
		
		
		
		
		
		return true;

	}
	
	
	
	
}
