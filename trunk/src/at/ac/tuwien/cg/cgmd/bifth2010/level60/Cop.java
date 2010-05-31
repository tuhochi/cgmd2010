package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Cop extends Tablet {
	private boolean leftfoot = true;
	private boolean front = true;
	private textureManager texman;
	
	public Cop(int initPosX, int initPosY, Context context, GL10 gl, textureManager man) {
		super(context, 50, 50, initPosX, initPosY, man.getTexture("cop_front_l"), gl);
		texman = man;
	}
	
//	public boolean getLeftFoot() { return leftfoot; }
//	public boolean getFront() { return front; }
	
}
