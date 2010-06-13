package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import javax.microedition.khronos.opengles.GL10;

/**
 * Model that represents a gem's shape. This is needed to display a transparent alternative of a 
 * selected gem in order to simplify its alignment.
 * @author Georg, Gerald
 */

public class ModelGemShape extends Model {

	/** Width of the gem shape */
	float width = 1.0f;
	/** Flag, if gem is visible or not */
	private boolean isVisible = false;
	/** Z-position */
	private final float posZ = -2f; 
	
	/**
	 * Creates a new gem shape model and adjusts the inherited quad's size.
	 */
	public ModelGemShape() {
		
		//Adjust the width of Model's quad.
		vertices[0] = vertices[6] = -width/2.0f;
		vertices[3] = vertices[9] = width/2.0f;
		
		//Adjust the height of Model's quad.
		vertices[1] = vertices[4] = -width/2.0f;
		vertices[7] = vertices[10] = width/2.0f;
		
		fillBuffers();
	}
	
	/**
	 * Creates a new gem shape model with an initial texture resource.
	 * @param textureResource texture resource
	 */
	public ModelGemShape(int textureResource) {
		this();
		this.textureResource = textureResource;
	}
	
	/**
	 * Sets the gem shape's visibility.
	 * @param v visibility: either true or false
	 */
	public void setVisible(boolean v) {
		this.isVisible = v;
	}
	
	/**
	 * Draws the gem if it is visible.
	 */
	public void draw(GL10 gl) {
		if (this.isVisible) {
			gl.glPushMatrix();
			gl.glTranslatef(0, 0, posZ);
			
			super.draw(gl);
			
			gl.glPopMatrix();
		}
	}
}
