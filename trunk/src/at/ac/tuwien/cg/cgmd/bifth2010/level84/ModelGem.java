package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import javax.microedition.khronos.opengles.GL10;

/**
 * Model representing a "Gem". Gets texture dependent on gem type.
 * @author Gerald, Georg
 */

public class ModelGem extends Model {

	/** width of the gem **/
	float width = 0.3f;
	/** starting position where gem begings to fall **/
	private final float gemStartpos = -2f; 
	private float gemPos = gemStartpos;
	/** fall speed of the gem **/
	private float fallSpeed = 0.0f;
	/** flag if gem is falling or not **/
	private boolean isFalling = false;
	
	/** position of the streetlevel **/
	private float streetLevel = -10f; 	//TODO: ev. Übergabe des wertes ??
	
	/**
	 * Creates a new gem model.
	 */
	public ModelGem() {

		//Adjust the width of Model's quad.
		vertices[0] = vertices[6] = -width/2.0f;
		vertices[3] = vertices[9] = width/2.0f;
		
		//Adjust the height of Model's quad.
		vertices[1] = vertices[4] = -width/2.0f;
		vertices[7] = vertices[10] = width/2.0f;
		
		fillBuffers();
	}
	
	/**
	 * Creates a new gem model with an initial texture resource.
	 * @param textureResource
	 */
	public ModelGem(int textureResource) {
		this();
		this.textureResource = textureResource;
		this.isFalling = false;
	}

	/**
	 * reset gem position
	 */
	public void resetPosition() {
		gemPos = gemStartpos;
		fallSpeed = 0;
	}
	
	/**
	 * start fall animation
	 */
	public void startFall()	{
		this.isFalling = true;
	}
	
	/**
	 * end fall animation
	 */
	public void endFall() {
		this.isFalling = false;
		resetPosition();
	}
	
	/**
	 * check collision of gem and drain
	 * @return collision: true/false
	 */
	public boolean checkCollision()	{
		if (gemPos <= streetLevel) {
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * Update the model's transformations.
	 */
	public void update(GL10 gl, double deltaTime) {
		
		if (this.isFalling) {
			if (!checkCollision()) {
				fallSpeed += 5f * deltaTime;
				gemPos -= fallSpeed;
			}
			else {
				//TODO: check ausrichtung, punkte, ...
				endFall();
			}
		}
	}
	
	/**
	 * Draw the gem if it was chosen and is falling
	 */
	public void draw(GL10 gl) {

		//mTrans = Matrix4x4.mult(Matrix4x4.RotateZ(gemRotation), mTrans);
		if (this.isFalling) {
			gl.glPushMatrix();
			gl.glTranslatef(0, 0, gemPos);
			gl.glMultMatrixf(mTrans.toFloatArray(), 0);
			
			super.draw(gl);
			
			gl.glPopMatrix();
		}
	}
}

	

