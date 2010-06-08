package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class GameObject extends Tablet {
	public static final int OBJECTCLASS_CAR = 1; 
	private int objectClass;
	private int framenr;
	private boolean isBeingDestroyed;
	private int maxFrame;
	private String textureBaseName;
	private textureManager texman;
	
	public GameObject(int objectClass, Context context, int width, int height, float x, float y, int texture, textureManager texman, GL10 gl) {
		super(context, width, height, x, y, texture, gl);
		this.objectClass = objectClass;
		this.texman = texman;
		isBeingDestroyed = false;
		framenr = 0;
		switch (objectClass) {
		case OBJECTCLASS_CAR:
			maxFrame = 6;
			textureBaseName = "car";
			break;
		default: maxFrame = 0;
		}
	}
	
	public int getObjectClass() { return objectClass; }
	
	public void destroy() {
		isBeingDestroyed = true;
	}
	
	public boolean update() {
		if (isBeingDestroyed) {
			framenr++;
			if (framenr <= maxFrame) this.changeTexture(texman.getTexture(textureBaseName + framenr));
			else {
				isBeingDestroyed = false;
				return false;
			}
		}
		return true;
	}
}
