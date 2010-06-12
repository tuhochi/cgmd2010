package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import javax.microedition.khronos.opengles.GL10;

/**
 * ModelGemShape representing the shape of the gem.
 * @author Georg
 */

public class ModelGemShape extends Model {

	/** width of the gem shape **/
	float width = 1.0f;
	/** flag, if gem is visible or not **/
	private boolean isVisible = false;
	/** z position **/
	private final float posZ = -2f; 
	
	/**
	 * Creates a new gem shape model.
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
	 * @param textureResource
	 */
	public ModelGemShape(int textureResource) {
		this();
		this.textureResource = textureResource;
	}
	
	/**set gemshape (in)visible
	 * @param v visible is true or false
	 */
	public void setVisible(boolean v) {
		this.isVisible = v;
	}
	
	/**
	 * Draw the gem if it was chosen and is falling
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
