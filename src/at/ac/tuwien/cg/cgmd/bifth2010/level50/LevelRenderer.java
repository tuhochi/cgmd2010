package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
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
	LevelObject bunny, coins[], scorePre, score1, score2, score3, arrowRight, arrowLeft, arrowUp, touchPoint, 
						urLine, ulLine, ruLine, rbLine, brLine, blLine, luLine, lbLine;
	Context context;
	GL10 gl;
	float positionX = 0;
	float positionY = 0;
	int width, height;
	int score;
	String coinState = "";
	int movementCounter = 0, frameCounter = 0;
	final int fadeDuration = 30;
	int darkness = fadeDuration;
	public boolean jumping = false, moving = false;
	boolean touching = false;
	float oldX, oldY;
	long oldFrameTime = 0;
	long timeCounter = 0;
	float fps = 30.0f;
	boolean scaled = false;
	MediaPlayer mp;
	
	public LevelRenderer(Context context) {
		this.context = context;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		
		if (frameCounter%10==9) {
			if (oldFrameTime!=0)
			fps = 10.0f/(float)(System.currentTimeMillis()-oldFrameTime)*1000.0f;
			oldFrameTime = System.currentTimeMillis();
		}
		
		if (bunny.getScore()>=100) {
			if (darkness == fadeDuration) {
				if (mp != null)
					mp.release();
				mp = MediaPlayer.create(context, R.raw.l50_finish);
				if (mp != null)
					mp.start();
			}
			gl.glClearColor((float)(darkness-fadeDuration)/fadeDuration,
					Math.max((float)0x99/255.0f*(float)darkness/fadeDuration,(float)(darkness-fadeDuration)/fadeDuration),
					Math.max((float)0xCC/255.0f*(float)darkness/fadeDuration,(float)(darkness-fadeDuration)/fadeDuration), 1.0f);
			if(darkness < fadeDuration*2) {
				darkness++;
				((Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(40);
			} else if (darkness < fadeDuration*6) {
				((Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE)).cancel();
				darkness++;
				gl.glMatrixMode(GL10.GL_PROJECTION);
				gl.glLoadIdentity();
				GLU.gluOrtho2D(gl, bunny.getPositionX()-width/2+tileSizeX/2,
						bunny.getPositionX()+width/2+tileSizeX/2,
						bunny.getPositionY()+height/2.0f-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f+tileSizeY/2,
						bunny.getPositionY()-height/2.0f-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f+tileSizeY/2);
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				
				scorePre.setPosition(bunny.getPositionX()-width/2+tileSizeX/2, bunny.getPositionY()-height/2+tileSizeY/2-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f);
				score1.setPosition(bunny.getPositionX()-width/2+3*tileSizeX/2, bunny.getPositionY()-height/2+tileSizeY/2-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f);
				score2.setPosition(bunny.getPositionX()-width/2+2*tileSizeX, bunny.getPositionY()-height/2+tileSizeY/2-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f);
				score3.setPosition(bunny.getPositionX()-width/2+5*tileSizeX/2, bunny.getPositionY()-height/2+tileSizeY/2-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f);
				
				urLine.setPosition(bunny.getPositionX()+tileSizeX/2+width/6-tileSizeX/8, bunny.getPositionY()+tileSizeY/2-height/2-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f);
				ulLine.setPosition(bunny.getPositionX()+tileSizeX/2-width/6-tileSizeX/8, bunny.getPositionY()+tileSizeY/2-height/2-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f);
				
				ruLine.setPosition(bunny.getPositionX()+tileSizeX/2+width/4, bunny.getPositionY()+tileSizeY/2-height/6-tileSizeY/8-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f);
				rbLine.setPosition(bunny.getPositionX()+tileSizeX/2+width/4, bunny.getPositionY()+tileSizeY/2+height/6-tileSizeY/8-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f);
				
				brLine.setPosition(bunny.getPositionX()+tileSizeX/2+width/6-tileSizeX/8, bunny.getPositionY()+tileSizeY/2+height/4-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f);
				blLine.setPosition(bunny.getPositionX()+tileSizeX/2-width/6-tileSizeX/8, bunny.getPositionY()+tileSizeY/2+height/4-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f);

				luLine.setPosition(bunny.getPositionX()+tileSizeX/2-width/2, bunny.getPositionY()+tileSizeY/2-height/6-tileSizeY/8-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f);
				lbLine.setPosition(bunny.getPositionX()+tileSizeX/2-width/2, bunny.getPositionY()+tileSizeY/2+height/6-tileSizeY/8-height/2.0f*(float)(darkness-fadeDuration*2)/fadeDuration/1.5f);
			} else {
				((LevelActivity) context).clearPrefs();
				((LevelActivity) context).saveScore();
				bunny.setScore(-1000);
				resetGame();
				((LevelActivity) context).finish();
			}
		} else if (bunny.getLife()) {
			if (bunny.update(gl,fps)) {
				jumping = false;
			} else
				jumping = true;
			
			if (bunny.getClimbable())
				jumping = false;
			
			
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			GLU.gluOrtho2D(gl, bunny.getPositionX()-width/2+tileSizeX/2, bunny.getPositionX()+width/2+tileSizeX/2, bunny.getPositionY()+height/2+tileSizeY/2, bunny.getPositionY()-height/2+tileSizeY/2);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			
			scorePre.setPosition(bunny.getPositionX()-width/2+tileSizeX/2, bunny.getPositionY()-height/2+tileSizeY/2);
			score1.setPosition(bunny.getPositionX()-width/2+3*tileSizeX/2, bunny.getPositionY()-height/2+tileSizeY/2);
			score2.setPosition(bunny.getPositionX()-width/2+2*tileSizeX, bunny.getPositionY()-height/2+tileSizeY/2);
			score3.setPosition(bunny.getPositionX()-width/2+5*tileSizeX/2, bunny.getPositionY()-height/2+tileSizeY/2);
			
			urLine.setPosition(bunny.getPositionX()+tileSizeX/2+width/6-tileSizeX/8, bunny.getPositionY()+tileSizeY/2-height/2);
			ulLine.setPosition(bunny.getPositionX()+tileSizeX/2-width/6-tileSizeX/8, bunny.getPositionY()+tileSizeY/2-height/2);
			
			ruLine.setPosition(bunny.getPositionX()+tileSizeX/2+width/4, bunny.getPositionY()+tileSizeY/2-height/6-tileSizeY/8);
			rbLine.setPosition(bunny.getPositionX()+tileSizeX/2+width/4, bunny.getPositionY()+tileSizeY/2+height/6-tileSizeY/8);
			
			brLine.setPosition(bunny.getPositionX()+tileSizeX/2+width/6-tileSizeX/8, bunny.getPositionY()+tileSizeY/2+height/4);
			blLine.setPosition(bunny.getPositionX()+tileSizeX/2-width/6-tileSizeX/8, bunny.getPositionY()+tileSizeY/2+height/4);

			luLine.setPosition(bunny.getPositionX()+tileSizeX/2-width/2, bunny.getPositionY()+tileSizeY/2-height/6-tileSizeY/8);
			lbLine.setPosition(bunny.getPositionX()+tileSizeX/2-width/2, bunny.getPositionY()+tileSizeY/2+height/6-tileSizeY/8);
			
//			arrowRight.setPosition(bunny.getPositionX()+width/2-5*tileSizeX/2, bunny.getPositionY()+height/2-5*tileSizeY/2);
//			arrowLeft.setPosition(bunny.getPositionX()+width/2-9*tileSizeX/2, bunny.getPositionY()+height/2-5*tileSizeY/2);
//			arrowUp.setPosition(bunny.getPositionX()-width/2+tileSizeX/2, bunny.getPositionY()+height/2-5*tileSizeY/2);
		}  else {
			if (darkness == fadeDuration) {
				if (mp != null)
					mp.release();
				mp = MediaPlayer.create(context, R.raw.l50_wah);
				if (mp != null)
					mp.start();
			}
			if (bunny.update(gl,fps)) {
				jumping = false;
			} else
				jumping = true;
			
			
			gl.glClearColor(0.0f, (float)0x99/255*darkness/fadeDuration, (float)0xCC/255*darkness/fadeDuration, 1.0f);
			if(darkness != 0)
				darkness--;
			else {
				resetGame();
				((Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
				bunny.setScore(-1000);
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
		
		
		if (jumping){
			bunny.changeTexture(R.drawable.l50_rabbit_jump, null);
			movementCounter = 0;
		} else if (moving && bunny.getClimbable()) {
			if (movementCounter%12==0) {
				bunny.changeTexture(R.drawable.l50_rabbit_climb1, null);
			} else if (movementCounter%12==6) {
				bunny.changeTexture(R.drawable.l50_rabbit_climb2, null);
			}else if (movementCounter%6==3) {
				bunny.changeTexture(R.drawable.l50_rabbit_climb3, null);
			}
			movementCounter++;
		} else if (bunny.getClimbable()) {
			bunny.changeTexture(R.drawable.l50_rabbit_climb1, null);
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
		
		urLine.draw(gl);
		ulLine.draw(gl);
		ruLine.draw(gl);
		rbLine.draw(gl);
		brLine.draw(gl);
		blLine.draw(gl);
		luLine.draw(gl);
		lbLine.draw(gl);
		if(touching)
			touchPoint.draw(gl);
		
//		arrowLeft.draw(gl);
//		arrowRight.draw(gl);
//		arrowUp.draw(gl);
		
		frameCounter++;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.width = width;
		this.height = height;
		
		
		
		if (!scaled) {
			
			urLine.setHeight(height/4);
			ulLine.setHeight(height/4);
			ruLine.setWidth(width/4);
			rbLine.setWidth(width/4);
			brLine.setHeight(height/4);
			blLine.setHeight(height/4);
			luLine.setWidth(width/4);
			lbLine.setWidth(width/4);
			
			
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
			bunny.setPosition(positionX, positionY);	
			scorePre.scale(scaleX,scaleY);
			score1.scale(scaleX,scaleY);
			score2.scale(scaleX,scaleY);
			score3.scale(scaleX,scaleY);
//			urLine.scale(scaleX,scaleY);
//			ulLine.scale(scaleX,scaleY);
//			ruLine.scale(scaleX,scaleY);
//			rbLine.scale(scaleX,scaleY);
//			brLine.scale(scaleX,scaleY);
//			blLine.scale(scaleX,scaleY);
//			luLine.scale(scaleX,scaleY);
//			lbLine.scale(scaleX,scaleY);
	//		arrowLeft.scale(scaleX,scaleY);
	//		arrowRight.scale(scaleX,scaleY);
	//		arrowUp.scale(scaleX,scaleY);
			scaled = true;
		}
		
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
		
		tileSizeY = 30;
		tileSizeX = 30;
		scaled = false;
		oldFrameTime = 0;
		fps = 30.0f;
		
		LevelObject.clearTextures(gl);
		LevelObject.initMP(context, R.raw.l00_gold01);
		level = new LevelCollision(gl, context, R.drawable.l50_level01_coll);
		level.setCoinState(coinState);
		//load the l00_icon first to get it into memory
		bunny = new LevelObject(gl, context, level, positionX, positionY, tileSizeX, tileSizeY, R.drawable.l00_coin, null);
		bunny.setScore(score);
		bunny.changeTexture(R.drawable.l50_tiles, null);
		bunny.changeTexture(R.drawable.l50_rabbit_2, null);
		bunny.changeTexture(R.drawable.l50_rabbit_3, null);
		bunny.changeTexture(R.drawable.l50_rabbit_climb1, null);
		bunny.changeTexture(R.drawable.l50_rabbit_climb2, null);
		bunny.changeTexture(R.drawable.l50_rabbit_climb3, null);
		bunny.changeTexture(R.drawable.l50_strip_touched, null);
		bunny.changeTexture(R.drawable.l50_rabbit_1, null);
		bunny.enableGravity(true);
		bunny.enableCollision(true);
		
		scorePre = new LevelObject(gl, context, level, bunny.getPositionX()-width/2+tileSizeX/2, bunny.getPositionY()+height/2-tileSizeY/2, tileSizeX/2, tileSizeY/2, R.drawable.l00_coin, null);
		score1 = new LevelObject(gl, context, level, bunny.getPositionX()-width/2+3*tileSizeX/2, bunny.getPositionY()+height/2-tileSizeY/2, tileSizeX/2, tileSizeY/2, R.drawable.l50_numbers, null);
		score2 = new LevelObject(gl, context, level, bunny.getPositionX()-width/2+2*tileSizeX, bunny.getPositionY()+height/2-tileSizeY/2, tileSizeX/2, tileSizeY/2, R.drawable.l50_numbers, null);
		score3 = new LevelObject(gl, context, level, bunny.getPositionX()-width/2+5*tileSizeX/2, bunny.getPositionY()+height/2-tileSizeY/2, tileSizeX/2, tileSizeY/2, R.drawable.l50_numbers, null);
		
		float texcoord1[] = {
				0.0f,  0.0f,
				0.0f,  1.0f,
				1.0f,  1.0f,
				1.0f,  0.0f};
		urLine = new LevelObject(gl, context, level, 0, 0, tileSizeX/4, height/4, R.drawable.l50_strip, texcoord1);
		ulLine = new LevelObject(gl, context, level, 0, 0, tileSizeX/4, height/4, R.drawable.l50_strip, texcoord1);
		float texcoord2[] = {
				0.0f,  1.0f,
				1.0f,  1.0f,
				1.0f,  0.0f,
				0.0f,  0.0f};
		ruLine = new LevelObject(gl, context, level, 0, 0, width/4, tileSizeY/4, R.drawable.l50_strip, texcoord2);
		rbLine = new LevelObject(gl, context, level, 0, 0, width/4, tileSizeY/4, R.drawable.l50_strip, texcoord2);
		
		brLine = new LevelObject(gl, context, level, 0, 0, tileSizeX/4, height/4, R.drawable.l50_strip, null);
		blLine = new LevelObject(gl, context, level, 0, 0, tileSizeX/4, height/4, R.drawable.l50_strip, null);
		float texcoord3[] = {
				0.0f,  0.0f,
				1.0f,  0.0f,
				1.0f,  1.0f,
				0.0f,  1.0f};
		luLine = new LevelObject(gl, context, level, 0, 0, width/4, tileSizeY/4, R.drawable.l50_strip, texcoord3);
		lbLine = new LevelObject(gl, context, level, 0, 0, width/4, tileSizeY/4, R.drawable.l50_strip, texcoord3);
		
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
	
	public void touchScreen(int eventNo, float x, float y) {
//		Log.d("actionmove", "Action: "+eventNo+" x: "+x+" y: "+y+" oldX: "+oldX+" oldY: "+oldY);
		
//		if(!bunny.getLife()) {
//			resetGame();
//		}
		
		if (eventNo!=MotionEvent.ACTION_DOWN && eventNo!=MotionEvent.ACTION_MOVE) {
			bunny.move(2,0.0f);
			bunny.move(3,0.0f);
			if (bunny.getClimbable()) {
				bunny.move(0,0.0f);
				bunny.move(1,0.0f);
			}
			urLine.changeTexture(R.drawable.l50_strip, null);
			ulLine.changeTexture(R.drawable.l50_strip, null);
			ruLine.changeTexture(R.drawable.l50_strip, null);
			rbLine.changeTexture(R.drawable.l50_strip, null);
			brLine.changeTexture(R.drawable.l50_strip, null);
			blLine.changeTexture(R.drawable.l50_strip, null);
			luLine.changeTexture(R.drawable.l50_strip, null);
			lbLine.changeTexture(R.drawable.l50_strip, null);
			moving = false;
			touching = false;
			oldX = -1;
			oldY = -1;
			return;
		}
		
		int oldPos = -1;
		int newPos = -1;
		int killPos = -1;
		float amountX = 0.0f;
		float amountY = 0.0f;
		
		touching = true;
		touchPoint.setPosition(bunny.getPositionX()-width/2+x+tileSizeX/2, bunny.getPositionY()-height/2+y+tileSizeY/2);
		
		
			if (y <= height/3) {
				newPos = 0*3;
				amountY = Math.min(1.0f,(height/3-y)/height*4);
			} else if (height*2/3 <= y) {
				newPos = 2*3;
				amountY = Math.min(1.0f,(y-height*2/3)/height*4);
			} else {
				newPos = 1*3;
			}
			if (x <= width/3) {
				newPos += 0;
				amountX = Math.min(1.0f,(width/3-x)/width*4);
			} else if (width*2/3 <= x) {
				newPos += 2;
				amountX = Math.min(1.0f,(x-width*2/3)/width*4);
			} else {
				newPos += 1;
			}
		
		if (eventNo==MotionEvent.ACTION_MOVE) {
			if (oldY <= height/3) {
				oldPos = 0*3;
			} else if (height*2/3 <= oldY) {
				oldPos = 2*3;
			} else {
				oldPos = 1*3;
			}
			if (oldX <= width/3) {
				oldPos += 0;
			} else if (width*2/3 <= oldX) {
				oldPos += 2;
			} else {
				oldPos += 1;
			}
			if (newPos!=oldPos) {
//				if (oldPos>=3 && newPos<3)
//					((Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(40);
				killPos=oldPos;
			}
		}
		if (newPos!=oldPos) {
			killPos=oldPos;
			oldX = x;
			oldY = y;
		}
		
		switch (killPos) {
		case 0:
			if (bunny.getClimbable())
				bunny.move(0,0.0f);
			bunny.move(2,0.0f);
			moving = false;
			ulLine.changeTexture(R.drawable.l50_strip, null);
			luLine.changeTexture(R.drawable.l50_strip, null);
			break;
		case 1:
			if (bunny.getClimbable())
				bunny.move(0,0.0f);

			moving = false;
			ulLine.changeTexture(R.drawable.l50_strip, null);
			urLine.changeTexture(R.drawable.l50_strip, null);
			break;
		case 2:
			if (bunny.getClimbable())
				bunny.move(0,0.0f);
			bunny.move(3,0.0f);
			moving = false;
			urLine.changeTexture(R.drawable.l50_strip, null);
			ruLine.changeTexture(R.drawable.l50_strip, null);
			break;
		case 3:
			bunny.move(2,0.0f);
			moving = false;
			luLine.changeTexture(R.drawable.l50_strip, null);
			lbLine.changeTexture(R.drawable.l50_strip, null);
			break;
		case 5:
			bunny.move(3,0.0f);
			moving = false;
			ruLine.changeTexture(R.drawable.l50_strip, null);
			rbLine.changeTexture(R.drawable.l50_strip, null);
			break;
		case 6:
			if (bunny.getClimbable())
				bunny.move(1,0.0f);
			bunny.move(2,0.0f);
			moving = false;
			lbLine.changeTexture(R.drawable.l50_strip, null);
			blLine.changeTexture(R.drawable.l50_strip, null);
			break;
		case 7:
			if (bunny.getClimbable())
				bunny.move(1,0.0f);
			moving = false;
			blLine.changeTexture(R.drawable.l50_strip, null);
			brLine.changeTexture(R.drawable.l50_strip, null);
			break;
		case 8:
			if (bunny.getClimbable())
				bunny.move(1,0.0f);
			bunny.move(3,0.0f);
			moving = false;
			rbLine.changeTexture(R.drawable.l50_strip, null);
			brLine.changeTexture(R.drawable.l50_strip, null);
			break;
		}
		
		switch (newPos) {
		case 0:
			if (bunny.getClimbable()&&!bunny.getClimbableTop()) {
				bunny.move(0,5.0f*amountY);
			} else if ((eventNo==MotionEvent.ACTION_DOWN || newPos!=oldPos)&& !jumping)
				bunny.move(0,15.0f);
			
			bunny.move(2,5.0f*amountX);
			moving = true;
			ulLine.changeTexture(R.drawable.l50_strip_touched, null);
			luLine.changeTexture(R.drawable.l50_strip_touched, null);
			break;
		case 1:
			if (bunny.getClimbable()&&!bunny.getClimbableTop()) {
				bunny.move(0,5.0f*amountY);
				moving = true;
			} else if ((eventNo==MotionEvent.ACTION_DOWN || newPos!=oldPos)&& !jumping)
				bunny.move(0,15.0f);
			
			ulLine.changeTexture(R.drawable.l50_strip_touched, null);
			urLine.changeTexture(R.drawable.l50_strip_touched, null);
			break;
		case 2:
			if (bunny.getClimbable()&&!bunny.getClimbableTop()) {
				bunny.move(0,5.0f*amountY);
			} else if ((eventNo==MotionEvent.ACTION_DOWN || newPos!=oldPos)&& !jumping)
				bunny.move(0,15.0f);
			
			bunny.move(3,5.0f*amountX);
			moving = true;
			urLine.changeTexture(R.drawable.l50_strip_touched, null);
			ruLine.changeTexture(R.drawable.l50_strip_touched, null);
			break;
		case 3:
			bunny.move(2,5.0f*amountX);
			moving = true;
			luLine.changeTexture(R.drawable.l50_strip_touched, null);
			lbLine.changeTexture(R.drawable.l50_strip_touched, null);
			break;
		case 5:
			bunny.move(3,5.0f*amountX);
			moving = true;
			ruLine.changeTexture(R.drawable.l50_strip_touched, null);
			rbLine.changeTexture(R.drawable.l50_strip_touched, null);
			break;
		case 6:
			if (bunny.getClimbable())
				bunny.move(1,5.0f*amountY);
			
			bunny.move(2,5.0f*amountX);
			moving = true;
			lbLine.changeTexture(R.drawable.l50_strip_touched, null);
			blLine.changeTexture(R.drawable.l50_strip_touched, null);
			break;
		case 7:
			if (bunny.getClimbable()) {
				bunny.move(1,5.0f*amountY);
				moving = true;
			}
			blLine.changeTexture(R.drawable.l50_strip_touched, null);
			brLine.changeTexture(R.drawable.l50_strip_touched, null);
			break;
		case 8:
			if (bunny.getClimbable())
				bunny.move(1,5.0f*amountY);
			
			bunny.move(3,5.0f*amountX);
			moving = true;
			rbLine.changeTexture(R.drawable.l50_strip_touched, null);
			brLine.changeTexture(R.drawable.l50_strip_touched, null);
			break;
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

	public void clear() {
		if (mp!=null) {
			if (mp.isPlaying())
				mp.stop();
			mp.release();
		}
		LevelObject.clean();
		
	}
}

