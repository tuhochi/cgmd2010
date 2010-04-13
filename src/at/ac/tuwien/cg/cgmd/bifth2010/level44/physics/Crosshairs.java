package at.ac.tuwien.cg.cgmd.bifth2010.level44.physics;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Sprite;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Texture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.TextureParts;

public class Crosshairs {
	private static final int MAX_DISTANCE_2 = 2500;
	/** the green crosshairs */
	private Sprite spriteGreen = null;
	/** the red crosshairs */
	private Sprite spriteRed = null;
	/** the width of the screen */
	private int screenWidth = 0;
	/** the height of the screen */
	private int screenHeight = 0;
	/** the rabbit */
	private PhysicalObject rabbit = null;
	/** the desired x-position */
	private float desiredX = 0.f;
	/** the desired y-position */
	private float desiredY = 0.f;
	
	public Crosshairs(Texture texture, int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
		
		spriteRed = new Sprite(TextureParts.makeCrosshairsRed(texture));
		spriteRed.setCenter(24, 24);
		
		spriteGreen = new Sprite(TextureParts.makeCrosshairsGreen(texture));
		spriteGreen.setCenter(24, 24);
	}
	
	public void setRabbit(PhysicalObject rabbit) {
		this.rabbit = rabbit;
	}
	
	public void draw(GL10 gl) {
		if (isNearRabbit())
			spriteGreen.draw(gl);
		else
			spriteRed.draw(gl);
	}
	
	public void setX(float x) {
		spriteRed.setX(x);
		spriteGreen.setX(x);
	}
	
	public void setY(float y) {
		spriteRed.setY(y);
		spriteGreen.setY(y);
	}
	
	public float getX() {
		return spriteRed.getX();
	}
	
	public float getY() {
		return spriteGreen.getY();
	}
	
	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
	}
	
	public void move(float dx, float dy) {
		setX(spriteRed.getX() + dx);
		setY(spriteRed.getY() + dy);
	}
	
	public boolean isNearRabbit() {
		 float dx = rabbit.getX() - getX();
		 float dy = rabbit.getY() - getY();
		 
		 // if distance < 40 => is near
		 return dx*dx + dy*dy < MAX_DISTANCE_2;
	}
	
	private void setDesiredPosition(float x, float y) {
		desiredX = x;
		desiredY = y;
	}
	
	public void ai() {
		// crosshairs are not near the rabbit -> move in 8
		if (!isNearRabbit()) {
			setDesiredPosition((float)(screenWidth/2 + screenWidth/2.5*Math.sin((double)(System.currentTimeMillis()/4000.))),
							   (float)(screenHeight/2 + screenHeight/3*Math.sin((double)(System.currentTimeMillis()/2000.))));
		} else {
			setDesiredPosition(rabbit.getX(), rabbit.getY());
		}
		
		float dx = desiredX - getX();
		float dy = desiredY - getY();
		
		move(dx/300.f, dy/300.f);
	}
}
