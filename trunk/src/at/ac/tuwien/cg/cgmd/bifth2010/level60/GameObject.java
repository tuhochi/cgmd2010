package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class GameObject extends Tablet {
	public static final int OBJECTCLASS_CAR = 1; 
	private int objectClass;
	private int framenr;
	private boolean isBeingDestroyed;
	
	public GameObject(int objectClass, Context context, int width, int height, float x, float y, int texture, GL10 gl) {
		super(context, width, height, x, y, texture, gl);
		this.objectClass = objectClass;
		isBeingDestroyed = false;
		framenr = 0;
	}
	
	public void destroy() {
		isBeingDestroyed = true;
	}
	
	public void update() {
		if (isBeingDestroyed) framenr++;
	}
}
