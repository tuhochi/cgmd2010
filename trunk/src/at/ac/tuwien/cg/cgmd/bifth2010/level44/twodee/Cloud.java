package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.util.Random;

public class Cloud extends Sprite {
	private int id;
	private float screenWidth;
	private float screenHeight;

	public Cloud(Texture texture, int id, float screenWidth, float screenHeight) {
		super(TextureParts.makeCloud(texture, id));

		this.id = id;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		Random rand = new Random();
		setPosition(rand.nextInt((int) screenWidth), rand.nextInt((int) (screenHeight/2))-screenHeight/3);
	}
	
	public void step() {
		float x = getX()+(1+.5f*id)*.5f;

		if (x > screenWidth+getWidth()) {
			x = -getWidth();
		}
		
		setPosition(x, getY());
	}

}
