package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.util.Random;


public class CoinBucketSprite extends SpriteContainer {
	/** number of coins at the beginning */
	private static final int FULL_COIN_COUNT = 10;
	
	public CoinBucketSprite(Texture texture) {
		super(TextureParts.getBucket(texture));
		setCenter(64, 0);
		
		Random random = new Random();
		
		for (int i=0; i<FULL_COIN_COUNT; i++) {
			Sprite coin = new Sprite(TextureParts.getCoin(texture));
			coin.setPosition(random.nextInt(60)-30, 10+random.nextInt(10));
			addChildFront(coin);
		}
	}
	
	public boolean looseCoin() {
		if (!this.childrenFront.isEmpty()) {
			this.childrenFront.remove(this.childrenFront.size()-1);
		}
		
		return this.childrenFront.isEmpty();
	}

	public int getCoinCount() {
		return childrenFront.size();
	}
}
