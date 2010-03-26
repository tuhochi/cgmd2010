package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import javax.microedition.khronos.opengles.GL10;

import java.nio.FloatBuffer;

public class Item {
	private float x;
	private float y;
	private float width;
	private float height;
	private float angle;
	private float scale;

	private TexturePart texturePart;
	private FloatBuffer vtxCoords;
	
	public Item(TexturePart texturePart) {
		this(texturePart, texturePart.getWidth(), texturePart.getHeight(), 0, 0);
	}
	
	public Item(TexturePart texturePart, float width, float height) {
		this(texturePart, width, height, 0, 0);
	}
	
	public Item(TexturePart texturePart, float width, float height, float x, float y) {
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
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void setCenter(float cx, float cy) {
		recalculateVtxCoords(cx, cy);
	}
	
	public void setRotation(float angle) {
		this.angle = angle;
	}
	
	protected void onBeforeDraw(GL10 gl) {
		/* Override this in subclasses to draw children, etc... */
	}

	protected void onAfterDraw(GL10 gl) {
		/* Override this in subclasses to draw children, etc... */
	}
	
	public void draw(GL10 gl) {
		//System.err.println("drawing item: " + this.toString());
		/* Save the current Model-View matrix */
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		
		gl.glTranslatef(x, y, 0);
		gl.glRotatef(angle, 0, 0, 1); /* rotate clockwise on the z=0 2D plane */
		gl.glScalef(scale, scale, 1); /* scale everything with the given scale factor */
		
		/* Draw any attached objects here (i.e. children) */
		onBeforeDraw(gl);
		
		/* Enable vertex array and texture array for drawing */
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		/**
		 * Configure texture, color and vertices (for glDrawArrays)
		 * The color used is white to use the texture's color data.
		 **/
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturePart.getTextureName());
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texturePart.getTexCoords());
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vtxCoords);
		gl.glColor4f(1, 1, 1, 1);
		
		/* Draw two triangles with our 4 coordinates */
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		/* Draw more attached objects here */
		onAfterDraw(gl);

		/* Disable vertex array and texture array after drawing */
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		/* Restore the Model-View matrix */
		gl.glPopMatrix();
	}
	

	/**
	 * Reposition the vertex array, centering the item at (0, 0).
	 */
	private void recalculateVtxCoords() {
		float cx = width/2,
		      cy = height/2;
		recalculateVtxCoords(cx, cy);
	}
	
	/**
	 * Reposition the vertex array of this item so that the (item-relative)
	 * coordinates (cx, cy) are positioned exactly at (0, 0) before all
	 * transformations take place.
	 * 
	 * @param cx The x-position inside the item to be placed at x=0
	 * @param cy The y-position inside the item to be placed at y=0
	 */
	private void recalculateVtxCoords(float cx, float cy) {
		float [] coords = new float[] {
				-cx, -cy,
				-cx, height-cy,
				width-cx, -cy,
				width-cx, height-cy,
		};
		
		vtxCoords = Util.floatArrayToBuffer(coords);
	}
	
	public String toString() {
		return this.getClass().getName() + ", " + width + "x" + height + " at (" + x + ", " + y + ")";
	}
}
