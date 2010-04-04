package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.util.Random;


public class CoinBucketSprite extends SpriteContainer {
	public CoinBucketSprite(Texture texture) {
		super(TextureParts.getBucket(texture));
		setCenter(64, 0);
		
		Random random = new Random();
		
		for (int i=0; i<10; i++) {
			Sprite coin = new Sprite(TextureParts.getCoin(texture));
			coin.setPosition(random.nextInt(60)-30, 10+random.nextInt(10));
			addChildFront(coin);
		}
	}
}
