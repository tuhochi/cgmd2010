package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Container;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Item;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Mirror;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Texture;

public class Rabbit extends Container {
	private Item leftWing;
	private Item rightWing;
	private Item coinBucket;
	
	public Rabbit(Texture texture) {
		super(TextureParts.makeRabbitHead(texture));
		setCenter(84, 64);
		
		leftWing = new Item(TextureParts.makeWing(texture));
		leftWing.setCenter(128, 64);
		leftWing.setPosition(0, -10);
		
		rightWing = new Item(TextureParts.makeWing(texture).setMirror(Mirror.HORIZONTAL));
		rightWing.setCenter(0, 64);
		rightWing.setPosition(0, -10);
		
		coinBucket = new CoinBucket(texture);
		coinBucket.setPosition(0, 50);

		addChild(leftWing);
		addChild(rightWing);
		addChild(coinBucket);
	}
	
	public void setWingAngle(float angle) {
		leftWing.setRotation(-angle);
		rightWing.setRotation(angle);
	}
	
	public void setLeftWingAngle(float angle) {
		leftWing.setRotation(-angle);
	}
	
	public void setRightWingAngle(float angle) {
		rightWing.setRotation(angle);
	}
	
	public void flapLeftWing() {
		//TODO
	}
	
	public void flapRightWing() {
		//TODO
	}

	@Override
	public void setRotation(float angle) {
		super.setRotation(angle);

		/* Simulate gravity for the coin bucket */
		coinBucket.setRotation(-angle/2);
	}

}
