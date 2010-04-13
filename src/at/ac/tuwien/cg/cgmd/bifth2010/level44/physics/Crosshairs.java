package at.ac.tuwien.cg.cgmd.bifth2010.level44.physics;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Sprite;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Texture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.TextureParts;

public class Crosshairs {
	private Sprite spriteGreen = null;
	private Sprite spriteRed = null;
	private PhysicalObject rabbit = null;
	
	public Crosshairs(Texture texture) {
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
		 
		 return dx*dx + dy*dy < 900;
	}
}
