package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class LevelRenderer implements Renderer {
	
	private int tileSizeX = 30;
	private int tileSizeY = 30;
	LevelCollision level;
	LevelObject bunny, coins[], scorePre, score1, score2, score3, arrowRight, arrowLeft, arrowUp, ctrl, touchPoint;
	Context context;
	GL10 gl;
	float positionX = 0;
	float positionY = 0;
	int width, height;
	int score;
	String coinState = "";
	int movementCounter = 0, counter = 0;
	final int fadeDuration = 30;
	int darkness = fadeDuration;
	public boolean jumping = false, moving = false;
	boolean touching = false;
	float oldX, oldY;
	
	public LevelRenderer(Context context) {
		this.context = context;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		
		//if collision with floor stop jumping
		if (bunny.update(gl)) {
			jumping = false;
		} else
			jumping = true;
		
		if (bunny.getLife()) {
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			GLU.gluOrtho2D(gl, bunny.getPositionX()-width/2+tileSizeX/2, bunny.getPositionX()+width/2-tileSizeX/2, bunny.getPositionY()+height/2-tileSizeY/2, bunny.getPositionY()-height/2+tileSizeY/2);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			
			scorePre.setPosition(bunny.getPositionX()-width/2+tileSizeX/2, bunny.getPositionY()-height/2+tileSizeY/2);
			score1.setPosition(bunny.getPositionX()-width/2+3*tileSizeX/2, bunny.getPositionY()-height/2+tileSizeY/2);
			score2.setPosition(bunny.getPositionX()-width/2+2*tileSizeX, bunny.getPositionY()-height/2+tileSizeY/2);
			score3.setPosition(bunny.getPositionX()-width/2+5*tileSizeX/2, bunny.getPositionY()-height/2+tileSizeY/2);
			ctrl.setPosition(bunny.getPositionX()+width/2-13*tileSizeX/2, bunny.getPositionY()+height/2-9*tileSizeY/2);
//			arrowRight.setPosition(bunny.getPositionX()+width/2-5*tileSizeX/2, bunny.getPositionY()+height/2-5*tileSizeY/2);
//			arrowLeft.setPosition(bunny.getPositionX()+width/2-9*tileSizeX/2, bunny.getPositionY()+height/2-5*tileSizeY/2);
//			arrowUp.setPosition(bunny.getPositionX()-width/2+tileSizeX/2, bunny.getPositionY()+height/2-5*tileSizeY/2);
		}  else {
			
			gl.glClearColor(0.0f, (float)0x99/255*darkness/fadeDuration, (float)0xCC/255*darkness/fadeDuration, 1.0f);
			bunny.setScore(-100);
			if(darkness != 0)
				darkness--;
			else {
				resetGame();
			}
		}
		
		int score = Math.max(100-bunny.getScore(),0);
		int a = (int)Math.floor((double)score/100.0);
		float texcoords[] = {
				a*0.1f,  1.0f,
				a*0.1f,  0.0f,
				(a+1)*0.1f,  0.0f,
				(a+1)*0.1f,  1.0f
			};
		score1.changeTexture(R.drawable.l50_numbers, texcoords);
		a = (int)Math.floor((double)(score%100)/10.0);
		texcoords[0] = a*0.1f;
		texcoords[2] = a*0.1f;
		texcoords[4] = (a+1)*0.1f;
		texcoords[6] = (a+1)*0.1f;
		score2.changeTexture(R.drawable.l50_numbers, texcoords);
		a = score%10;
		texcoords[0] = a*0.1f;
		texcoords[2] = a*0.1f;
		texcoords[4] = (a+1)*0.1f;
		texcoords[6] = (a+1)*0.1f;
		score3.changeTexture(R.drawable.l50_numbers, texcoords);
		
		
		
		// Clears the screen and depth buffer.
		if (jumping){
			bunny.changeTexture(R.drawable.l50_rabbit_jump, null);
			movementCounter = 0;
		} else if (moving) {
			if (movementCounter%12==0) {
				bunny.changeTexture(R.drawable.l50_rabbit_1, null);
			} else if (movementCounter%12==6) {
				bunny.changeTexture(R.drawable.l50_rabbit_2, null);
			}else if (movementCounter%6==3) {
				bunny.changeTexture(R.drawable.l50_rabbit_3, null);
			}
			movementCounter++;
		} else {
			bunny.changeTexture(R.drawable.l50_rabbit_3, null);
			movementCounter = 0;
		}
			
		
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		level.draw(gl, bunny.getPositionX(), bunny.getPositionY());
		bunny.draw(gl);	
		scorePre.draw(gl);
		score1.draw(gl);
		score2.draw(gl);
		score3.draw(gl);
		
		ctrl.draw(gl);
		if(touching)
			touchPoint.draw(gl);
		
//		arrowLeft.draw(gl);
//		arrowRight.draw(gl);
//		arrowUp.draw(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.width = width;
		this.height = height;
		
		float mult = 1.0f;
		if ((float)width/(float)height > 3.0f/2.0f)
			mult = (float)height/320.0f;
		else
			mult = (float)width/480.0f;
		
		float scaleX = mult;//(float)width/480.0f;//*mult;
		float scaleY = mult;//(float)height/320.0f;//*mult;

		tileSizeX*=scaleX;
		tileSizeY*=scaleY;
		level.scale(scaleX,scaleY);
		bunny.scale(scaleX,scaleY);	
		scorePre.scale(scaleX,scaleY);
		score1.scale(scaleX,scaleY);
		score2.scale(scaleX,scaleY);
		score3.scale(scaleX,scaleY);
		ctrl.scale(scaleX,scaleY);
//		arrowLeft.scale(scaleX,scaleY);
//		arrowRight.scale(scaleX,scaleY);
//		arrowUp.scale(scaleX,scaleY);
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, 0.0f, width, height, 0.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		this.gl = gl;
		gl.glClearColor(0.0f, (float)0x99/255, (float)0xCC/255, 1.0f);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		LevelObject.clearTextures(gl);
		level = new LevelCollision(gl, context, R.drawable.l50_level01_coll);
		level.setCoinState(coinState);
		//load the l00_icon first to get it into memory
		bunny = new LevelObject(gl, context, level, positionX, positionY, tileSizeX, tileSizeY, R.drawable.l00_coin, null);
		bunny.setScore(score);
		bunny.changeTexture(R.drawable.l50_tiles, null);
		bunny.changeTexture(R.drawable.l50_rabbit_2, null);
		bunny.changeTexture(R.drawable.l50_arrow_touched, null);
		bunny.changeTexture(R.drawable.l50_rabbit_1, null);
		bunny.enableGravity(true);
		bunny.enableCollision(true);
		
		scorePre = new LevelObject(gl, context, level, bunny.getPositionX()-width/2+tileSizeX/2, bunny.getPositionY()+height/2-tileSizeY/2, tileSizeX/2, tileSizeY/2, R.drawable.l00_coin, null);
		score1 = new LevelObject(gl, context, level, bunny.getPositionX()-width/2+3*tileSizeX/2, bunny.getPositionY()+height/2-tileSizeY/2, tileSizeX/2, tileSizeY/2, R.drawable.l50_numbers, null);
		score2 = new LevelObject(gl, context, level, bunny.getPositionX()-width/2+2*tileSizeX, bunny.getPositionY()+height/2-tileSizeY/2, tileSizeX/2, tileSizeY/2, R.drawable.l50_numbers, null);
		score3 = new LevelObject(gl, context, level, bunny.getPositionX()-width/2+5*tileSizeX/2, bunny.getPositionY()+height/2-tileSizeY/2, tileSizeX/2, tileSizeY/2, R.drawable.l50_numbers, null);
		
		ctrl = new LevelObject(gl, context, level, 0, 0, tileSizeX*6, tileSizeY*4, R.drawable.l50_ctrl, null);
		touchPoint = new LevelObject(gl, context, level, 0, 0, tileSizeX, tileSizeY, R.drawable.l50_arrow, null);
		
//		arrowRight = new LevelObject(gl, context, level, 0, 0, tileSizeX*2, tileSizeY*2, R.drawable.l50_arrow, null);
//		float texcoord2[] = { //mirror arrow
//				1.0f,  0.0f,
//				1.0f,  1.0f,
//				0.0f,  1.0f,
//				0.0f,  0.0f};
//		arrowLeft = new LevelObject(gl, context, level, 0, 0, tileSizeX*2, tileSizeY*2, R.drawable.l50_arrow, texcoord2);
//		float texcoord3[] = { //turn arrow upwards
//				1.0f,  1.0f,
//				0.0f,  1.0f,
//				0.0f,  0.0f,
//				1.0f,  0.0f};
//		arrowUp = new LevelObject(gl, context, level, 0, 0, tileSizeX*2, tileSizeY*2, R.drawable.l50_arrow, texcoord3);
	}
	
	public void touchScreen(MotionEvent event) {
		Log.d("actionmove", "Action: "+event.getAction()+" x: "+event.getX()+" y: "+event.getY()+" oldX: "+oldX+" oldY: "+oldY);
		
		if(!bunny.getLife()) {
			resetGame();
		}
		
		if (event.getAction()!=MotionEvent.ACTION_DOWN && event.getAction()!=MotionEvent.ACTION_MOVE) {
//			arrowUp.changeTexture(R.drawable.l50_arrow, null);
			bunny.move(2,0.0f);
//			arrowLeft.changeTexture(R.drawable.l50_arrow, null);
			bunny.move(3,0.0f);
//			arrowRight.changeTexture(R.drawable.l50_arrow, null);
			moving = false;
			touching = false;
			oldX = -1;
			oldY = -1;
			return;
		}
		
		int oldPos = -1;
		int newPos = -1;
		int killPos = -1;
		float amount = 0.0f;
		float y = event.getY();
		float x = event.getX();
		
		touching = true;
		touchPoint.setPosition(bunny.getPositionX()-width/2+x-tileSizeX, bunny.getPositionY()-height/2+y-tileSizeY);
		
		
		if (height-tileSizeY*4 <= y && y <= height && width-tileSizeX*6 <= x && x <= width) {
			if (y <= height-tileSizeY*3) {
				newPos = 0*3;
			} else if (height-tileSizeY <= y) {
				newPos = 2*3;
			} else {
				newPos = 1*3;
			}
			if (x <= width-tileSizeX*4) {
				newPos += 0;
				amount = (width-tileSizeX*4-x)/tileSizeX/2;
			} else if (width-tileSizeX*2 <= x) {
				newPos += 2;
				amount = (x-width+tileSizeX*2)/tileSizeX/2;
			} else {
				newPos += 1;
			}
		}
//		Log.d("actionmove", "x: "+x+" y: "+y+" oldX: "+oldX+" oldY: "+oldY+"border: "+(height-tileSizeY*2));
		
		if (event.getAction()==MotionEvent.ACTION_MOVE) {
			if (height-tileSizeY*4 <= oldY && oldY <= height && width-tileSizeX*6 <= oldX && oldX <= width) {
				if (oldY <= height-tileSizeY*3) {
					oldPos = 0*3;
				} else if (height-tileSizeY <= oldY) {
					oldPos = 2*3;
				} else {
					oldPos = 1*3;
				}
				if (oldX <= width-tileSizeX*4) {
					oldPos += 0;
				} else if (width-tileSizeX*2 <= oldX) {
					oldPos += 2;
				} else {
					oldPos += 1;
				}
			}
			if (newPos!=oldPos) {
				((Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
				killPos=oldPos;
			}
		}
		if (newPos!=oldPos) {
			killPos=oldPos;
			oldX = x;
			oldY = y;
		}
			
		
		if (!jumping && 0 <= newPos && newPos <= 2) {
			bunny.move(0,15.0f);
//				touchdown[0] = true; //unnecessary
//			arrowUp.changeTexture(R.drawable.l50_arrow_touched, null);
		}
		if (newPos%3==0) {
			bunny.move(2,5.0f*amount);
			moving = true;
//			arrowLeft.changeTexture(R.drawable.l50_arrow_touched, null);
		} else if (newPos%3==2) {
			bunny.move(3,5.0f*amount);
			moving = true;
//			arrowRight.changeTexture(R.drawable.l50_arrow_touched, null);
		} else {
			bunny.move(2,0.0f);
			bunny.move(3,0.0f);
			moving = false;
		}
		
		if (killPos==0) {
//			arrowUp.changeTexture(R.drawable.l50_arrow, null);
		} else if (killPos==2) {
			bunny.move(2,0.0f);
//			arrowLeft.changeTexture(R.drawable.l50_arrow, null);
		} else if (killPos==3) {
			bunny.move(3,0.0f);
//			arrowRight.changeTexture(R.drawable.l50_arrow, null);
		}
	}
	
	private void resetGame() {
		bunny.setPosition(0,0);
		bunny.revive();
		level.reset();
		gl.glClearColor(0.0f, (float)0x99/255, (float)0xCC/255, 1.0f);
		darkness = fadeDuration;
	}
	
	public int getScore() {return bunny.getScore();}
	public void setScore(int s) {score = s;}
	public void movePlayer(float x, float y) {bunny.move(x,y);}
	public void movePlayer(int direction, float amount) {bunny.move(direction,amount);}
	public void setPosition(float x, float y) {positionX = x; positionY = y;}
	public float getPositionX() {return bunny.getPositionX();}
	public float getPositionY() {return bunny.getPositionY();}
	public String getCoinState() {return level.getCoinState();}
	public void setCoinState(String state) {coinState=state;}

}