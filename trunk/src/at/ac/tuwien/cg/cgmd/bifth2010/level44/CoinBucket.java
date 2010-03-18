package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import java.util.Random;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Container;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Item;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Texture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.TextureParts;

public class CoinBucket extends Container {
	public CoinBucket(Texture texture) {
		super(TextureParts.getBucket(texture));
		setCenter(64, 0);
		
		Random random = new Random();
		
		for (int i=0; i<10; i++) {
			Item coin = new Item(TextureParts.getCoin(texture));
			coin.setPosition(random.nextInt(60)-30, 10+random.nextInt(10));
			addChildFront(coin);
		}
	}
}
