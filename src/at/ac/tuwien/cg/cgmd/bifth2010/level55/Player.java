package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.util.Log;

public class Player {
	
	Quad hase;
	Level level;
	Camera camera;
	Texture haseTex;
	
	public boolean doJump = false;
	public boolean doMoveRight = false;
	public boolean doMoveLeft = false;
		
	protected float[] playerPos = {2, 0};
	protected float[] a = {0, 0};
	protected float[] v = {0, 0};
	protected float frictionFactor = 0.012f;
	protected float breakForce = 10.0f;
	
	protected float moveForce = 350.0f;
	protected float jumpSpeed = 18.0f;
	protected float gForce = 300.0f;
	protected float bounceFactor = 0.5f;
	protected float bounceArea = 0.4f;
	
	protected final int COL_BOTTOM = 0;
	protected final int COL_TOP = 1;
	protected final int COL_LEFT = 2;
	protected final int COL_RIGHT = 3;
	//protected boolean[][] collision = new boolean[2][4];
	protected float[] collisionThreshold = new float[4];
	
	
	protected boolean canJump = false;
	
	protected float[] lastPos = new float[2];
	
	public void init(GL10 gl, Level _level, Camera _camera) {
		hase = new Quad();
		haseTex = new Texture();
		
		hase.init(gl, -0.5f, -0.5f, 1.0f, 1.0f);
		haseTex.create(R.drawable.l55_testtexture);
		level = _level;
		camera = _camera;
		
		for(int i=0;i<4;i++)
			collisionThreshold[i] = 10000.0f;
		
		collisionThreshold[COL_TOP] = 0.0f;
		
		playerPos[0] = 2.0f;
		playerPos[1] = 0.0f;
		
		lastPos[0] = playerPos[0];
		lastPos[1] = playerPos[1];
	}
	
	public void draw(GL10 gl) {
		haseTex.bind(gl);
		gl.glTranslatef(playerPos[0],playerPos[1],0.0f);
		hase.draw(gl);
	}
	
	public void update(float dt) 
	{		
		//Check for coinChange
		checkCoinChange(dt);
		
		if (doMoveLeft) {
			a[0] = -moveForce*dt;
		}
		if (doMoveRight) {
			a[0] = moveForce*dt;
		}
		
		if (doJump && canJump) {
			
			//level.changeCoinState(2, 4);
			
			//a[1] = 0.0f;
			v[1] = -jumpSpeed;
			doJump=false;
			canJump = false;
		}
		
		//Add gravity
		Log.d("player", "canJump = "+canJump);

		a[1] += gForce*dt;

				
		v[0] += a[0]*dt;
		v[1] += a[1]*dt;
		
		v[0] -= Math.signum(v[0])*frictionFactor*v[0]*v[0];
		v[1] -= Math.signum(v[1])*frictionFactor*v[1]*v[1];
		
		if(!(doMoveLeft || doMoveRight) && canJump) 
		{
			v[0] -= v[0]*Math.min(1.0f, breakForce*dt);
		}
			
		playerPos[0] += v[0]*dt;
		playerPos[1] += v[1]*dt;
		
		//Performs collision detection and flips signs of v if necessary
		doCollisionDetection(dt);
		
		camera.lookAt(playerPos[0], playerPos[1]);
		
		a[0] = 0.0f;
		a[1] = 0.0f;
	}
	
	protected void doCollisionDetection(float dt) 
	{
		int x;
		int y;
		int quadId;
		
		//*****************
		//Check bottom
		//*****************
		y = (int) Math.floor(playerPos[1]+0.5f);
		for(float i=-0.4f;i<=0.4f;i+=0.8f)
		{
			//Log.d("Player", "in loop");
			x = (int) Math.floor(playerPos[0]+i);
			quadId = level.getTypeAt(x, y);
			if(quadId!=-1 && quadId!=10 && quadId!=11)
			{
				collisionThreshold[COL_BOTTOM] = Math.min(collisionThreshold[COL_BOTTOM], (float) Math.floor(playerPos[1]) + 0.5f);
				if(v[1]>0) {
					playerPos[1] = collisionThreshold[COL_BOTTOM];
					v[1] *= -bounceFactor;
				}
			} else {
				collisionThreshold[COL_BOTTOM] = 10000.0f;
				canJump = false;
			}
		}
		if(playerPos[1]+bounceArea < collisionThreshold[COL_BOTTOM]) {
			collisionThreshold[COL_BOTTOM] = 10000.0f;
			canJump = false;
		} else {
			canJump = true;
		}
		
		//*****************
		//Check top
		//*****************
		y = (int) Math.floor(playerPos[1]-0.5f);
		for(float i=-0.4f;i<=0.4f;i+=0.8f)
		{
			//Log.d("Player", "in loop");
			x = (int) Math.floor(playerPos[0]+i);
			quadId = level.getTypeAt(x, y);
			if(quadId!=-1 && quadId!=10 && quadId!=11)
			{
				collisionThreshold[COL_TOP] = Math.max(collisionThreshold[COL_TOP], (float) Math.floor(playerPos[1]) + 0.5f);
				if(v[1]<0) {
					playerPos[1] = collisionThreshold[COL_TOP];
					v[1] *= -bounceFactor;
				}
			} else {
				collisionThreshold[COL_TOP] = 0.0f;
			}
		}
		
		//*****************
		//Check left
		//*****************
		x = (int) Math.floor(playerPos[0]-0.5f);
		for(float i=-0.3f;i<=0.3f;i+=0.6f)
		{
			//Log.d("Player", "in loop");
			y = (int) Math.floor(playerPos[1]+i);
			quadId = level.getTypeAt(x, y);
			if(quadId!=-1 && quadId!=10 && quadId!=11)
			{
				collisionThreshold[COL_LEFT] = Math.max(collisionThreshold[COL_LEFT], (float) Math.floor(playerPos[0]) + 0.5f);
				if(v[0]<0) {
					playerPos[0] = collisionThreshold[COL_LEFT];
					v[0] *= -bounceFactor;
				}
			} else {
				collisionThreshold[COL_LEFT] = 0.0f;
			}
		}
		
		//*****************
		//Check right
		//*****************
		x = (int) Math.floor(playerPos[0]+0.5f);
		for(float i=-0.3f;i<=0.3f;i+=0.6f)
		{
			//Log.d("Player", "in loop");
			y = (int) Math.floor(playerPos[1]+i);
			quadId = level.getTypeAt(x, y);
			if(quadId!=-1 && quadId!=10 && quadId!=11)
			{
				collisionThreshold[COL_RIGHT] = Math.min(collisionThreshold[COL_RIGHT], (float) Math.floor(playerPos[0]) + 0.5f);
				if(v[0]>0) {
					playerPos[0] = collisionThreshold[COL_RIGHT];
					v[0] *= -bounceFactor;
				}
			} else {
				collisionThreshold[COL_RIGHT] = 100000.0f;
			}
		}
	}
	
	protected void checkCoinChange(float dt)
	{
		if((int)lastPos[0]!=(int)playerPos[0] || (int)lastPos[1]!= (int)playerPos[1]) 
		{
			lastPos[0] = playerPos[0];
			lastPos[1] = playerPos[1];
			
			int x = (int) playerPos[0];
			int y = (int) playerPos[1];
			
			int quadId = level.getTypeAt(x, y);
			if(quadId == 10 || quadId == 11) {
				level.changeCoinState(x, y);
			}
		}
	}
	
	public void jump(boolean btDown) {
		doJump = btDown;
	}
	
	public void moveRight(boolean btDown) {
		doMoveRight = btDown;
	}
	
	public void moveLeft(boolean btDown) {
		doMoveLeft = btDown;
	}
}
