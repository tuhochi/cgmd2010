package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

/**
 * SpriteContainer that represents the CoinBucket of the Rabbit
 * 
 * @author Thomas Perl
 *
 */
public class CoinBucketSprite extends SpriteContainer {
	/** number of coins at the beginning */
	public static final int FULL_COIN_COUNT = 10;
	/** Sprite for the front of the bucket */
	private Sprite bucketFront = null;
	/** the texture used for creating */
	private Texture texture = null;
	/** all coins that are left in the bucket */
	private Vector<Sprite> coins = new Vector<Sprite>();

	/**
	 * Helper Method for Creating a new Coin
	 * @return a Coin with a random position in the bucket
	 */
	private Sprite makeCoin() {
		Random random = new Random();
		Sprite coin = new Sprite(TextureParts.makeCoin(texture));
		coin.setPosition(coin.getWidth() + random.nextInt((int) getWidth() - (int) coin.getWidth() * 2) - getWidth() / 2, 35 + random.nextInt(10));
		return coin;
	}

	/**
	 * Create a CoinBucket from the texture
	 * @param texture the texture to create the bucket
	 */
	public CoinBucketSprite(Texture texture) {
		super(TextureParts.makeBucketBack(texture));
		setCenter(30.5f, 0);

		this.texture = texture;
		setCoinCount(FULL_COIN_COUNT);

		bucketFront = new Sprite(TextureParts.makeBucket(texture));
		bucketFront.setCenter(30.5f, 0);
		bucketFront.setPosition(0, 35);
	}

	/**
	 * Rotate the bucket (due to gravity, maybe?)
	 * 
	 * @param angle The (absolute) angle of the rotation
	 */
	@Override
	public void setRotation(float angle) {
		super.setRotation(angle);

		// Rotate the bucket front a bit in addition to rotating the bucket as a whole
		bucketFront.setRotation(angle / 2);
	}

	/**
	 * Overridden method of Sprite to draw the bucket front and coins
	 * 
	 * @param gl The OpenGL ES context
	 */
	@Override
	protected void onAfterDraw(GL10 gl) {
		for (Sprite coin : coins) {
			coin.draw(gl);
		}

		if (bucketFront != null) {
			bucketFront.draw(gl);
		}
	}

	/**
	 * Remove one coin from the bucket
	 * 
	 * @return true, if there are no coins left, false otherwise
	 */
	public boolean looseCoin() {
		if (!coins.isEmpty()) {
			coins.remove(0);
		}

		return coins.isEmpty();
	}

	/**
	 * Count the coins in the bucket
	 * 
	 * @return the number of coins left
	 */
	public int getCoinCount() {
		return coins.size();
	}

	/**
	 * Add or remove coins to set the coin size
	 * 
	 * This will first remove as much coins as necessary
	 * and then add more coins so that the coin count at
	 * the end of the function will be equal to the "count"
	 * parameter.
	 * 
	 * @param count the number of new coins
	 */
	public void setCoinCount(int count) {
		while (coins.size() > count) {
			coins.remove(0);
		}
		while (coins.size() < count) {
			coins.add(makeCoin());
		}
	}
}
