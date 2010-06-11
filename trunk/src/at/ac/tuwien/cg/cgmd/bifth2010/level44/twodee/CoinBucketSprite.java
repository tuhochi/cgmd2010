package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class CoinBucketSprite extends SpriteContainer {
	/** number of coins at the beginning */
	public static final int FULL_COIN_COUNT = 10;

	private Sprite bucketFront = null;
	private Texture texture = null;
	private Vector<Sprite> coins = new Vector<Sprite>();

	private Sprite makeCoin() {
		Random random = new Random();
		Sprite coin = new Sprite(TextureParts.makeCoin(texture));
		coin.setPosition(coin.getWidth() + random.nextInt((int) getWidth() - (int) coin.getWidth() * 2) - getWidth() / 2, 35 + random.nextInt(10));
		return coin;
	}

	public CoinBucketSprite(Texture texture) {
		super(TextureParts.makeBucketBack(texture));
		setCenter(30.5f, 0);

		this.texture = texture;
		setCoinCount(FULL_COIN_COUNT);

		bucketFront = new Sprite(TextureParts.makeBucket(texture));
		bucketFront.setCenter(30.5f, 0);
		bucketFront.setPosition(0, 35);
	}

	@Override
	public void setRotation(float angle) {
		super.setRotation(angle);

		/**
		 * Rotate the bucket front a bit in addition to rotating the bucket as a
		 * whole
		 **/
		bucketFront.setRotation(angle / 2);
	}

	@Override
	protected void onAfterDraw(GL10 gl) {
		for (Sprite coin : coins) {
			coin.draw(gl);
		}

		if (bucketFront != null) {
			bucketFront.draw(gl);
		}
	}

	public boolean looseCoin() {
		if (!coins.isEmpty()) {
			coins.remove(0);
		}

		return coins.isEmpty();
	}

	public int getCoinCount() {
		return coins.size();
	}

	public void setCoinCount(int count) {
		while (coins.size() > count) {
			coins.remove(0);
		}
		while (coins.size() < count) {
			coins.add(makeCoin());
		}
	}
}
