package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class Player {
	
	Quad hase = new Quad();
	Level level;
	
	public boolean doJump = false;
	public boolean doMoveRight = false;
	public boolean doMoveLeft = false;
	
	public void init(Level _level) {
		hase.init(-0.5f, -0.5f, 1, 1);
		level=_level;
	}
	
	public void draw(GL10 gl) {
		gl.glLoadIdentity();
		hase.draw(gl);
	}
	
	public void update(float dT) {
		if (doMoveLeft) {
			Log.d("player", "left");
			level.setPosition(1.0f, 0.0f);
		}
		if (doMoveRight) {
			Log.d("player", "right");
			level.setPosition(0.0f, 0.0f);
		}
		if (doJump) {
			Log.d("player", "jump");
			doJump=false;
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
