package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

/**
 * A container for multiple child Sprites
 * 
 * This subclass of Sprite provides all functionality of a
 * normal sprite, but in addition to that also provides an
 * object-oriented way of adding child Sprites to this object
 * that will be "attached" to this sprite, and can be positioned
 * relative to their container.
 * 
 * Children can be placed behind the container (addChildBack)
 * and in front of the container (addChildFront).
 * 
 * Child sprites will automatically be scaled, rotated and moved
 * the same way as their container - they are "attached" to the
 * container.
 * 
 * @author Thomas Perl
 *
 */

public class SpriteContainer extends Sprite {
	/** all children that are drawn behind the actual sprite */
	protected Vector<Sprite> childrenBack;
	/** all children that are drawn in front of the actual sprite */
	protected Vector<Sprite> childrenFront;

	/**
	 * Creates a SpriteContainer from a TexturePart
	 * @param texturePart the texture part used for drawing
	 */
	public SpriteContainer(TexturePart texturePart) {
		this(texturePart, texturePart.getWidth(), texturePart.getHeight());
	}

	/**
	 * Creates a SpriteContainer from a TexturePart with a given width/height
	 * @param texturePart the texture part used for drawing
	 * @param width the width of the sprite
	 * @param height the height of the sprite
	 */
	public SpriteContainer(TexturePart texturePart, float width, float height) {
		this(texturePart, width, height, 0, 0);
	}

	/**
	 * Creates a SpriteContainer from a TexturePart with a given width/height and x-pos/y-pos
	 * @param texturePart the texture part used for drawing
	 * @param width the width of the sprite
	 * @param height the height of the sprite
	 * @param x the x-pos
	 * @param y the y-pos
	 */

	public SpriteContainer(TexturePart texturePart, float width, float height, float x, float y) {
		super(texturePart, width, height, x, y);
		childrenBack = new Vector<Sprite>();
		childrenFront = new Vector<Sprite>();
	}

	/**
	 * adds a child to the back-vector
	 * @param child the child that is added to the back
	 */
	public void addChildBack(Sprite child) {
		childrenBack.addElement(child);
	}

	/**
	 * adds a child to the front-vector
	 * @param child the child that is added to the front
	 */
	public void addChildFront(Sprite child) {
		childrenFront.addElement(child);
	}

	/**
	 * draws all children that are behind the actual sprite
	 * 
	 * @param gl The Open GL ES context
	 */
	@Override
	protected void onBeforeDraw(GL10 gl) {
		/* Draw all children */
		for (Sprite i : childrenBack) {
			i.draw(gl);
		}
	}

	/**
	 * draws all children that are in front of the actual sprite
	 * 
	 * @param gl The Open GL ES context
	 */
	@Override
	protected void onAfterDraw(GL10 gl) {
		for (Sprite i : childrenFront) {
			i.draw(gl);
		}
	}

}
