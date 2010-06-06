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
	protected float[] deltaPos = {0, 0};
	protected float frictionFactor = 0.001f;
	protected float breakForce = 10.0f;
	
	protected float moveForce = 300.0f;
	protected float jumpSpeed = 11.0f;
	protected float gForce = 1000.0f;
	protected float bounceFactor = 0.5f;
	protected float bounceArea = 1.4f;
	
	protected final float timeStep = 0.016f;
	protected float restTime = 0.0f;
	
	protected final int COL_BOTTOM = 0;
	protected final int COL_TOP = 1;
	protected final int COL_LEFT = 2;
	protected final int COL_RIGHT = 3;
	protected float[] collisionThreshold = new float[4];
	
	protected final int JUMP_IDLE = 0;
	protected final int JUMP_FLYING = 1;
	protected int jumpMode = JUMP_IDLE;
	
	
	protected boolean canJump = false;
	
	protected float[] lastPos = new float[2];
	
	public void init(GL10 gl, Level _level, Camera _camera) {
		hase = new Quad();
		haseTex = new Texture();
		
		hase.init(gl, -0.5f, -0.5f, 1.0f, 1.0f);
		haseTex.create(R.drawable.l55_testtexture);
		level = _level;
		camera = _camera;
		
		a[0]=0;
		a[1]=0;
		v[0]=0;
		v[1]=0;
		
		for(int i=0;i<4;i++)
			collisionThreshold[i] = 10000.0f;
		
		collisionThreshold[COL_TOP] = 0.0f;
		
		playerPos[0] = 2.0f;
		playerPos[1] = 5.0f;
		
		lastPos[0] = playerPos[0];
		lastPos[1] = playerPos[1];
				
		jumpMode = JUMP_IDLE;
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
		
		dt += restTime;
		int nbSteps = (int) Math.floor(dt/timeStep);
		
		restTime = dt-nbSteps*timeStep;
		
		//Log.d("Player", "dt = "+dt+" nbSteps = "+nbSteps+" restTime = "+restTime);
		
		for(int i=0;i<nbSteps;i++) {	
			if (doMoveLeft) {
				a[0] = -moveForce*timeStep;
			}
			if (doMoveRight) {
				a[0] = moveForce*timeStep;
			}
			
			if (doJump && canJump && jumpMode == JUMP_IDLE) {
				v[1] = -jumpSpeed;
				doJump=false;
				canJump = false;
				jumpMode = JUMP_FLYING;
			}
			
			//Add gravity
			a[1] += gForce*timeStep;
	
					
			v[0] += a[0]*timeStep;
			v[1] += a[1]*timeStep;
			
			v[0] -= Math.signum(v[0])*frictionFactor*v[0]*v[0]*timeStep;
			v[1] -= Math.signum(v[1])*frictionFactor*v[1]*v[1]*timeStep;
			
			if(!((doMoveLeft && v[0]<0) || (doMoveRight && v[0]>0))) 
			{
				if(!(jumpMode==JUMP_FLYING && !doMoveLeft && !doMoveRight)) {
					v[0] -= v[0]*Math.min(1.0f, breakForce*timeStep);
				}
			}
			
			deltaPos[0] = v[0]*timeStep;
			deltaPos[1] = v[1]*timeStep;
			
			playerPos[0] += deltaPos[0];
			playerPos[1] += deltaPos[1];
			
			//Performs collision detection and flips signs of v if necessary
			doCollisionDetection(timeStep);
					
			a[0] = 0.0f;
			a[1] = 0.0f;
		}
		camera.lookAt(playerPos[0], playerPos[1]);
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
			if(quadId!=-1 && quadId!=10 && quadId!=11 && quadId!=12 && quadId!=13 && quadId!=14 && quadId!=15)
			{
				collisionThreshold[COL_BOTTOM] = Math.min(collisionThreshold[COL_BOTTOM], (float) Math.floor(playerPos[1]) + 0.5f);
				if(v[1]>0) {
					playerPos[1] = collisionThreshold[COL_BOTTOM];
					v[1] = 0;
					jumpMode = JUMP_IDLE;
					break;
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
			if(quadId!=-1 && quadId!=10 && quadId!=11 && quadId!=12 && quadId!=13 && quadId!=14 && quadId!=15)
			{
				collisionThreshold[COL_TOP] = Math.max(collisionThreshold[COL_TOP], (float) Math.floor(playerPos[1]) + 0.5f);
				if(v[1]<0) {
					playerPos[1] = collisionThreshold[COL_TOP];
					v[1] *= -bounceFactor;
					break;
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
			if(quadId!=-1 && quadId!=10 && quadId!=11 && quadId!=12 && quadId!=13 && quadId!=14 && quadId!=15)
			{
				collisionThreshold[COL_LEFT] = Math.max(collisionThreshold[COL_LEFT], (float) Math.floor(playerPos[0]) + 0.5f);
				if(v[0]<0) {
					playerPos[0] = collisionThreshold[COL_LEFT];
					v[0] *= -bounceFactor;
					break;
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
			if(quadId!=-1 && quadId!=10 && quadId!=11 && quadId!=12 && quadId!=13 && quadId!=14 && quadId!=15)
			{
				collisionThreshold[COL_RIGHT] = Math.min(collisionThreshold[COL_RIGHT], (float) Math.floor(playerPos[0]) + 0.5f);
				if(v[0]>0) {
					playerPos[0] = collisionThreshold[COL_RIGHT];
					v[0] *= -bounceFactor;
					break;
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
			if(quadId == 10 || quadId == 11 || quadId == 12 || quadId == 13) {
				level.changeCoinState(x, y);
			}
			
			if (quadId == 14 || quadId == 15) {
				level.finish();
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
