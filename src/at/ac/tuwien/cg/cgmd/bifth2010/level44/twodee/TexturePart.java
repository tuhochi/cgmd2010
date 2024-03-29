package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.nio.FloatBuffer;

/**
 * A rectangular sub-part of a Texture
 * 
 * This class can be used to describe a rectangular
 * sub-part of a bigger Texture object.
 * 
 * See the TextureParts class for an example on how
 * to create a TexturePart object from a Texture.
 * 
 * @author Matthias Tretter
 * @author Thomas Perl
 */

public class TexturePart {
	/** the one big texture */
	private Texture texture;
	/** the x-coordinate of the top-left corner */
	private float x1;
	/** the y-coordinate of the top-left corner */
	private float y1;
	/** the x-coordinate of the bottom-right corner */
	private float x2;
	/** the y-coordinate of the bottom-right corner */
	private float y2;
	/** the width of the texture part */
	private float width = -1.f;
	/** the height of the texture part */
	private float height = -1.f;
	/** the mirror of the texture part */
	private Mirror mirror;
	/** buffer used for the texture coordinates */
	private FloatBuffer texCoords;

	/**
	 * Describes a subsection of the given texture with (x1, y1) as the top left
	 * corner and (x2, y2) as the lower right corner.
	 * 
	 * @param texture
	 *            A texture from where the image data is taken
	 * @param x1
	 *            The x-coordinate of the upper left corner
	 * @param y1
	 *            The y-coordinate of the upper left corner
	 * @param x2
	 *            The x-coordinate of the lower right corner
	 * @param y2
	 *            The y-coordinate of the lower right corner
	 */
	public TexturePart(Texture texture, float x1, float y1, float x2, float y2) {
		this(texture, Mirror.NONE, x1, y1, x2, y2);
	}

	/**
	 * Describes a subsection of the given texture with (x1, y1) as the top left
	 * corner and (x2, y2) as the lower right corner. The texture can be
	 * mirrored on both axes.
	 * 
	 * @param texture
	 *            A texture from where the image data is taken
	 * @param mirror
	 *            the mirror of the texture
	 * @param x1
	 *            The x-coordinate of the upper left corner
	 * @param y1
	 *            The y-coordinate of the upper left corner
	 * @param x2
	 *            The x-coordinate of the lower right corner
	 * @param y2
	 *            The y-coordinate of the lower right corner
	 */
	public TexturePart(Texture texture, Mirror mirror, float x1, float y1, float x2, float y2) {
		this.texture = texture;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.mirror = mirror;

		recalculateTexCoords();
	}

	/**
	 * Gets the width of this part as it appears in the texture.
	 * 
	 * @return The width of the unscaled texture part.
	 */
	public float getWidth() {
		// added incredible performance boost: lazy loading of width --myellow
		if (width < 0) {
			width = Math.abs(x2 - x1);
		}

		return width;
	}

	/**
	 * Gets the height of this part as it appears in the texture.
	 * 
	 * @return The height of the unscaled texture part.
	 */
	public float getHeight() {
		// added incredible performance boost: lazy loading of height --myellow
		if (height < 0) {
			height = Math.abs(y2 - y1);
		}

		return height;
	}

	/**
	 * Set a new mirror mode. This will recalculate the texture coordinate array
	 * so that the texture is drawn correctly.
	 * 
	 * @param mirror
	 *            The new mirror mode to set on this texture part
	 * @return For convenience, this method returns "this"
	 */
	public TexturePart setMirror(Mirror mirror) {
		this.mirror = mirror;
		recalculateTexCoords();
		return this;
	}

	/**
	 * Get the OpenGL ES texture ID for this part.
	 * 
	 * @return The texture ID (name) to be used with glBindTexture
	 */
	public int getTextureName() {
		return texture.getTextureName();
	}

	/**
	 * Get the texture coordinate array for this part.
	 * 
	 * @return The float array to be used with glTexCoordPointer
	 */
	public FloatBuffer getTexCoords() {
		texCoords.rewind(); /* FIXME: Necessary? --thp */
		return texCoords;
	}

	/**
	 * Recalculate the texture coordinates array. This will be called when this
	 * TexturePart gets created initially and when the mirror mode is changed
	 * (using setMirror).
	 */
	private void recalculateTexCoords() {
		float xa, xb, ya, yb;
		float w = texture.getWidth(), h = texture.getHeight();

		/* If we want horizontal mirroring, exchange the X-coordinates */
		if (mirror == Mirror.HORIZONTAL || mirror == Mirror.BOTH) {
			xa = x2;
			xb = x1;
		} else {
			xa = x1;
			xb = x2;
		}

		/* If we want vertical mirroring, exchange the Y-coordinates */
		if (mirror == Mirror.VERTICAL || mirror == Mirror.BOTH) {
			ya = y2;
			yb = y1;
		} else {
			ya = y1;
			yb = y2;
		}

		float[] coords = new float[] { xa / w, ya / h, /* upper left */
		xa / w, yb / h, /* lower left */
		xb / w, ya / h, /* upper right */
		xb / w, yb / h, /* lower right */
		};

		texCoords = Util.floatArrayToBuffer(coords);
	}

	/**
	 * @return the one big texture
	 */
	public Texture getTexture() {
		return texture;
	}
}
