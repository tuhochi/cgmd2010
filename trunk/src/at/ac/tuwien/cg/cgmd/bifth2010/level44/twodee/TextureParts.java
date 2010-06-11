package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

/**
 * class that provides static methods for creating TextureParts of the one big Texture
 * 
 * @author thp
 *
 */
public class TextureParts {
	/**
	 * creates a TexturePart for the rabbit-head
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeRabbitHead(Texture texture) {
		return new TexturePart(texture, 70, 519, 70 + 100, 519 + 72);
	}

	/**
	 * Make a damaged version of the rabbit head
	 * 
	 * @param texture
	 *            The main texture for the game
	 * @param severity
	 *            Severity of damage from 0-3
	 * @return A TexturePart pointing to the damaged rabbit head
	 */
	public static TexturePart makeDamagedRabbitHead(Texture texture, int severity) {
		float x, y;

		x = severity * 110.f;
		y = 661.f;

		return new TexturePart(texture, x, y, x + 100.f, y + 72.f);
	}

	/**
	 * creates a TexturePart for the rabbit-wing
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeWing(Texture texture) {
		return new TexturePart(texture, 0, 519, 0 + 55, 519 + 60);
	}

	/**
	 * creates a TexturePart for the bucket-back
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeBucketBack(Texture texture) {
		return new TexturePart(texture, 180, 519, 180 + 61, 519 + 58);
	}

	/**
	 * creates a TexturePart for the bucket
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeBucket(Texture texture) {
		return new TexturePart(texture, 260, 519, 260 + 61, 519 + 54);
	}

	/**
	 * creates a TexturePart for the coin
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeCoin(Texture texture) {
		return new TexturePart(texture, 340, 519, 340 + 19, 519 + 19);
	}

	/**
	 * creates a TexturePart for the sky
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeSky(Texture texture) {
		return new TexturePart(texture, 0, 0, 0 + 10, 0 + 320);
	}

	/**
	 * creates a TexturePart for the cloud
	 * @param texture the one big texture
	 * @param id determines which cloud to create
	 * @return the created TexturePart
	 */
	public static TexturePart makeCloud(Texture texture, int id) {
		int pos = id % 3;

		switch (pos) {
		case 1:
			return new TexturePart(texture, 280, 0, 280 + 133, 0 + 188);
		case 2:
			return new TexturePart(texture, 20, 218, 20 + 200, 218 + 144);
		default:
			return new TexturePart(texture, 20, 0, 20 + 250, 0 + 195);
		}
	}

	/**
	 * creates a TexturePart for the meadow
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeMeadow(Texture texture) {
		return new TexturePart(texture, 0, 382, 0 + 930, 382 + 130);
	}

	/**
	 * creates a TexturePart for the hills
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeHills(Texture texture) {
		return new TexturePart(texture, 234, 242, 234 + 790, 242 + 120);
	}

	/**
	 * creates a TexturePart for the mountains
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeMountains(Texture texture) {
		return new TexturePart(texture, 420, 0, 420 + 600, 0 + 210);
	}

	/**
	 * creates a TexturePart for the red crosshairs
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeCrosshairsRed(Texture texture) {
		return new TexturePart(texture, 976, 426, 976 + 48, 426 + 48);
	}

	/**
	 * creates a TexturePart for the green crosshairs
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeCrosshairsGreen(Texture texture) {
		return new TexturePart(texture, 976, 376, 976 + 48, 376 + 48);
	}

	/**
	 * creates a TexturePart for the aim-bar background
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeAimBarBackground(Texture texture) {
		return new TexturePart(texture, 370, 519, 370 + 185, 519 + 24);
	}

	/**
	 * creates a TexturePart for the aim-bar foreground
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeAimBarForeground(Texture texture) {
		return new TexturePart(texture, 370, 549, 370 + 185, 549 + 24);
	}

	/**
	 * creates a TexturePart for the intro-background
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeIntroBackground(Texture texture) {
		return new TexturePart(texture, 486, 689, 486 + 480, 689 + 320);
	}

	/**
	 * creates a TexturePart for the virtual finger
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeVirtualFinger(Texture texture) {
		return new TexturePart(texture, 565, 521, 565 + 48, 521 + 48);
	}

	/**
	 * creates a TexturePart for the red star
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeRedStar(Texture texture) {
		return new TexturePart(texture, 630, 519, 630 + 30, 519 + 30);
	}

	/**
	 * creates a TexturePart for the yellow star
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeYellowStar(Texture texture) {
		return new TexturePart(texture, 630, 554, 630 + 30, 554 + 30);
	}

	/**
	 * creates a TexturePart for the touch arrows
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeIntroTouchArrows(Texture texture) {
		return new TexturePart(texture, 670, 519, 670 + 162, 519 + 75);
	}

	/**
	 * creates a TexturePart for the left arrow
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeIntroLeftArrow(Texture texture) {
		return new TexturePart(texture, 849, 519, 849 + 45, 519 + 43);
	}

	/**
	 * creates a TexturePart for the up-arrow
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeIntroUpArrow(Texture texture) {
		return new TexturePart(texture, 917, 519, 917 + 32, 519 + 42);
	}

	/**
	 * creates a TexturePart for the right-arrow
	 * @param texture the one big texture
	 * @return the created TexturePart
	 */
	public static TexturePart makeIntroRightArrow(Texture texture) {
		return new TexturePart(texture, 963, 519, 963 + 41, 519 + 45);
	}

}
