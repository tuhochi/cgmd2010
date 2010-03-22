package at.ac.tuwien.cg.cgmd.bifth2010.level13;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class BoozyRenderer extends GLSurfaceView implements Renderer{

	private GameObject avatar;
	private GameObject beer;
	private Context context;
	Random rnd ;
	private List<GameObject> gameObjects;

	public BoozyRenderer(Context context){
		super(context);
		//this.context = context;
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		this.context = context;
		
		gameObjects = new LinkedList<GameObject>();
		rnd = new Random();
		avatar = new Player();
		
		
		beer = new Beer();
		beer.setPos(3, 4);
		gameObjects.add(beer);
		
		for (int i = 0; i < 10; i++){
			
			GameObject beerObj = new Beer();
			float posX = 1.5f+rnd.nextFloat()*2;
			float posY = 1.5f+rnd.nextFloat()*3;
			boolean xPositive = rnd.nextBoolean();
			boolean yPositive = rnd.nextBoolean();
			
			
			//beer.setPos(3, 4);
			if (xPositive)
				posX *= -1;
			if (yPositive)
				posY *= -1;
			beerObj.setPos(posX, posY);
			gameObjects.add(beerObj);
		}
		
		gameObjects.add(avatar);
	
		
	
		//gameObjects.add(beer);
		
		
		
		
		
	}
	
	public void generateRandomMoneyLossEvent(GameObject obj){
		
	//TODO: Random event generation
		
			GameObject gameObj = obj;
		
		float posX = 1.5f+rnd.nextFloat()*2;
		float posY = 1.5f+rnd.nextFloat()*3;
		boolean xPositive = rnd.nextBoolean();
		boolean yPositive = rnd.nextBoolean();
		
		
		//beer.setPos(3, 4);
		if (xPositive)
			posX *= -1;
		if (yPositive)
			posY *= -1;
		gameObj.setPos(posX, posY);
		gameObj.setActive(true);
		
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
			GameObject collidorA = gameObjects.get(i);

			
			for (int j = i+1; j < gameObjects.size(); j++){
				
				
				GameObject collidorB = gameObjects.get(j);

				if (collidorB.getActive()&& collidorA.getActive()){
				if (collidorA.getMinX() > collidorB.getMaxX() ||
					collidorA.getMaxX() < collidorB.getMinX() ||
					collidorA.getMinY() > collidorB.getMaxY() ||
					collidorA.getMaxY() < collidorB.getMinY()
					){
					//No collision
					
				}else{
					//Collision
					//Collision Handling <--- refactor to class
			
	
					//if (collidorB instanceof Beer && collidorA instanceof Beer){}
					
					if((collidorA instanceof Beer && collidorB instanceof Player) ||
							(collidorB instanceof Beer && collidorA instanceof Player) ){
						if (collidorA instanceof Beer)
							collidorA.setActive(false);
						else
							collidorB.setActive(false);
					}
					/*
					collidorA.setPos(collidorA.X()-collidorA.getSpeedX(),collidorA.Y()-collidorA.getSpeedY());
					collidorB.setPos(collidorB.X()-collidorB.getSpeedX(),collidorB.Y()-collidorB.getSpeedY());
					
					//Set speed to avoid getting stuck
					
					collidorA.setSpeedX(0);
					collidorA.setSpeedY(0);
					collidorB.setSpeedX(0);
					collidorB.setSpeedY(0);
				*/
		
				}
				
			}
				}
			}
			
		for (int i = 0; i < gameObjects.size(); i++){
			GameObject cObj = gameObjects.get(i);
			
			if (cObj.getActive()){
				//check if object is still active
			
				cObj.update();
				
				cObj.draw(gl);
			}else{
				generateRandomMoneyLossEvent(cObj);
				}
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
		//avatar.loadGLTexture(gl, this.context,R.drawable.l00_rabit_256);
		//beer.loadGLTexture(gl, this.context, R.drawable.l13_beer);
		for(int i = 0; i < gameObjects.size();i++){
			GameObject curObj = gameObjects.get(i);
			if (curObj instanceof Player)
				curObj.loadGLTexture(gl, this.context, R.drawable.l00_rabit_256);
			else if (curObj instanceof Beer)
				curObj.loadGLTexture(gl, this.context, R.drawable.l13_beer);
		}
		//gameObjects.get(1).loadGLTexture(gl, this.context, R.drawable.l00_levelicon_start);
		
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
				avatar.setSpeedY(0.1f);
				avatar.setSpeedX(0);
			}else if(currentY > downAreaY){
				avatar.setSpeedY(-0.1f);
				avatar.setSpeedX(0);
			}else if(currentX < leftAreaX){
				avatar.setSpeedX(-0.1f);
				avatar.setSpeedY(0);
			}else if(currentX > rightAreaX){
				avatar.setSpeedX(0.1f);
				avatar.setSpeedY(0);
			}else{
				avatar.setSpeedX(0);
				avatar.setSpeedY(0);
			}
			
			
			
			
		}
		
		
		
		
		
		return true;

	}
	
	
	
	
}
