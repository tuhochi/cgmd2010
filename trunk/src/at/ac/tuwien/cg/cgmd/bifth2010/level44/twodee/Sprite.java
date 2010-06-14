package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Class representing a Sprite used for displaying on the GameScene
 * 
 * @author thp
 *
 */

public class Sprite {
	/** the x-coordinate */
	private float x;
	/** the y-coordinate */
	private float y;
	/** the width of the sprite */
	private float width;
	/** the height of the sprite */
	private float height;
	/** the angle (sprites can be rotated) */
	private float angle;
	/** the scale (sprites can be scaled) */
	private float scale;
	/** part of the one big texture that is displayed */
	private TexturePart texturePart;
	/** buffer for the vertex-coordinates */
	private FloatBuffer vtxCoords;

	/**
	 * Creates a Sprite from a TexturePart
	 * @param texturePart the texture part used
	 */
	public Sprite(TexturePart texturePart) {
		this(texturePart, texturePart.getWidth(), texturePart.getHeight(), 0, 0);
	}

	/**
	 * creates a Sprite from a TexturePart with a given width/height
	 * @param texturePart the texture part used
	 * @param width the width
	 * @param height the height
	 */
	public Sprite(TexturePart texturePart, float width, float height) {
		this(texturePart, width, height, 0, 0);
	}

	/**
	 * creates a Sprite from a TexturePart with a give width, height, x-pos, y-pos
	 * @param texturePart the texture part used
	 * @param width the width
	 * @param height the height
	 * @param x the x-pos
	 * @param y the y-pos
	 */
	public Sprite(TexturePart texturePart, float width, float height, float x, float y) {
		this.texturePart = texturePart;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.angle = 0;
		this.scale = 1;

		/* Center the item at (0, 0) by default */
		recalculateVtxCoords();
	}

	
	/**
	 * Sets the texture part and recalculates the vertex coordinates
	 * @param texturePart the new texture part
	 */
	public void setTexturePart(TexturePart texturePart) {
		this.texturePart = texturePart;
		this.recalculateVtxCoords();
	}

	/**
	 * @return the texture used for displaying
	 */
	public Texture getTexture() {
		return texturePart.getTexture();
	}

	/**
	 * sets the texturePart without recalculating the vertex coordinates
	 * @param texturePart the new texture part
	 */
	protected void updateTexturePart(TexturePart texturePart) {
		this.texturePart = texturePart;
	}

	/**
	 * move the sprite to a given position
	 * @param x the new x-pos
	 * @param y the new y-pos
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * move the sprite to a given x-pos
	 * @param x the new x-pos
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * move the sprite to a given y-pos
	 * @param y the new y-pos
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * set the scale-factor of the sprite
	 * @param scale the new scale-factor
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * @return the scale-factor of the sprite
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * sets the new center of the sprite
	 * 
	 * @param cx new center-x
	 * @param cy new center-y
	 */
	public void setCenter(float cx, float cy) {
		recalculateVtxCoords(cx, cy);
	}

	/**
	 * sets the rotation of the sprite
	 * @param angle the new rotation
	 */
	public void setRotation(float angle) {
		this.angle = angle;
	}

	/**
	 * @return the rotation-value of the sprite
	 */
	public float getRotation() {
		return angle;
	}

	/**
	 * @return the x-coordinate of the sprite
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y-coordinate of the sprite
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return the width of the sprite
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @return the height of the sprite
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * This methods draws all children that are behind the current sprite
	 * @param gl OpenGL
	 */
	protected void onBeforeDraw(GL10 gl) {
		/* Override this in subclasses to draw children, etc... */
	}

	/**
	 * This methods draws all children that are in front of the current sprite
	 * @param gl OpenGL
	 */
	protected void onAfterDraw(GL10 gl) {
		/* Override this in subclasses to draw children, etc... */
	}

	/**
	 * This methods draws the sprite with the current rotation and scale-factor on its current position
	 * @param gl OpenGL
	 */
	public void draw(GL10 gl) {
		// System.err.println("drawing item: " + this.toString());
		/* Save the current Model-View matrix */
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();

		gl.glTranslatef(x, y, 0);
		gl.glRotatef(angle, 0, 0, 1); /* rotate clockwise on the z=0 2D plane */
		gl.glScalef(scale, scale, 1); /*
									 * scale everything with the given scale
									 * factor
									 */

		/* Draw any attached objects here (i.e. children) */
		onBeforeDraw(gl);

		/* Enable vertex array and texture array for drawing */
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		/**
		 * Configure texture, color and vertices (for glDrawArrays) The color
		 * used is white to use the texture's color data.
		 **/
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturePart.getTextureName());
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texturePart.getTexCoords());
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vtxCoords);
		gl.glColor4f(1, 1, 1, 1);

		/* Draw two triangles with our 4 coordinates */
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		/* Disable vertex array and texture array after drawing */
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		/* Draw more attached objects here */
		onAfterDraw(gl);		
		
		/* Restore the Model-View matrix */
		gl.glPopMatrix();
	}

	/**
	 * Reposition the vertex array, centering the item at (0, 0).
	 */
	private void recalculateVtxCoords() {
		float cx = width / 2, cy = height / 2;
		recalculateVtxCoords(cx, cy);
	}

	/**
	 * Reposition the vertex array of this item so that the (item-relative)
	 * coordinates (cx, cy) are positioned exactly at (0, 0) before all
	 * transformations take place.
	 * 
	 * @param cx
	 *            The x-position inside the item to be placed at x=0
	 * @param cy
	 *            The y-position inside the item to be placed at y=0
	 */
	private void recalculateVtxCoords(float cx, float cy) {
		float[] coords = new float[] { -cx, -cy, -cx, height - cy, width - cx, -cy, width - cx, height - cy, };

		vtxCoords = Util.floatArrayToBuffer(coords);
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ", " + width + "x" + height + " at (" + x + ", " + y + ")";
	}
}
