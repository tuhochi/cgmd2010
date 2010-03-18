package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Container;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Item;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Mirror;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Texture;

public class RabbitHead extends Container {
	private Item leftWing;
	private Item rightWing;
	
	public RabbitHead(Texture texture) {
		super(TextureParts.makeRabbitHead(texture));
		setCenter(84, 64);
		
		leftWing = new Item(TextureParts.makeWing(texture));
		leftWing.setCenter(128, 64);
		leftWing.setPosition(0, -10);
		
		rightWing = new Item(TextureParts.makeWing(texture).setMirror(Mirror.HORIZONTAL));
		rightWing.setCenter(0, 64);
		rightWing.setPosition(0, -10);
		
		addChild(leftWing);
		addChild(rightWing);
	}
	
	public void setWingAngle(float angle) {
		leftWing.setRotation(-angle);
		rightWing.setRotation(angle);
	}

}
