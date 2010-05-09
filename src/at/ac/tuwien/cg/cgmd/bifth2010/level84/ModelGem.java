package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level84.SoundManager.SoundFX;

/**
 * Model representing a "Gem". Gets texture dependent on gem type.
 * @author Gerald, Georg
 */

public class ModelGem extends Model {

	private int gemType;
	
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
	
	/** soundmanager for executing soundfx **/
	private SoundManager soundman;
	
	/** drainMap used for collision detection **/
	private HashMap<Integer, ModelDrain> drainMap;
	
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
	public ModelGem(int gemType, int textureResource) {
		this();
		this.gemType = gemType;
		this.textureResource = textureResource;
		this.isFalling = false;
	}

	public void setSoundManager(SoundManager soundManager)
	{
		this.soundman = soundManager;
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
	public void startFall(HashMap<Integer, ModelDrain> drains)	{
		this.isFalling = true;
		drainMap = drains;
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
	public boolean checkFallCollision()	{
		if (gemPos < streetLevel) {
			return true;
		}	
		else return false;
	}

	public void checkCollisionType(float streetPos, float deviceRotation)
	{
		//check type of collision (hit/miss)
		Iterator<ModelDrain> i = drainMap.values().iterator();
		while(i.hasNext())
		{
			ModelDrain drain = i.next();
			// calculate get the position of the drain
			
			float tempPos = streetPos + drain.getPosition();
			
			if ((tempPos > -2) || (tempPos < 2))
			{
				Log.i("POSITION", "drainstlye: " + drain.getDrainStyle() + " / "+ tempPos);

				//check draintype and see if it fits
				int drainType = drain.getDrainStyle();
				if ( drainType == 0)
				{
					this.soundman.playSound(SoundFX.BREAK);
				}
					else //check if gem hits or misses the drain
					{

							if (this.gemType == drainType)
							{
								this.soundman.playSound(SoundFX.HIT);
								break;
							}
							else
							{
								this.soundman.playSound(SoundFX.MISS);
								break;
							}
//								if (Math.abs(drain.getOrientationAngle() - deviceRotation ) < 1.0f)
//								{
//									this.soundman.playSound(SoundFX.HIT);
//									break;
//								}
//								else
//								{
//									this.soundman.playSound(SoundFX.MISS);
//									break;
//								}
//							}
					}
			}
			else
			{
				break;
			}
		}
	}
	
	/**
	 *
	 * Update the model's transformations.
	 */
	public void update(GL10 gl, double deltaTime, float streetPos, float deviceRotation) {
		
		if (this.isFalling) {
			if (!checkFallCollision()) {
				fallSpeed += 5f * deltaTime;
				gemPos -= fallSpeed;
			}
			else
			{
				//checkCollisionType(streetPos, deviceRotation);
				this.soundman.playSound(SoundFX.MISS);
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

	

