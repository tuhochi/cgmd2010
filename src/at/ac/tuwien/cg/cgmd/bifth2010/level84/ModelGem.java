package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

import javax.microedition.khronos.opengles.GL10;

import android.os.Vibrator;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level84.SoundManager.SoundFX;

/**
 * Model representing a "Gem". Gets texture dependent on gem type.
 * @author Gerald, Georg
 */

public class ModelGem extends Model {

	private int gemType;
	
	/** width of the gem **/
	float width = 1.0f;
	/** starting position where gem begins to fall **/
	private final float startPosZ = -2f; 
	private float posZ = startPosZ;
	/** fall speed of the gem **/
	private float fallSpeed = 0.0f;
	
	/** flag, if gem is falling or not **/
	private boolean isFalling = false;
	
	/** z position of the street **/
	private float streetPosZ;
	
	/** soundmanager for executing soundfx **/
	private SoundManager soundman;
	
	/** vibrator for vibrations **/
	private Vibrator vibrator;
	
	private long[] vibratorPattern;
	
	/** drainMap used for collision detection **/
	private HashMap<Integer, ModelDrain> drains;
	
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
	public ModelGem(int gemType, int textureResource, float streetPosZ, HashMap<Integer, ModelDrain> drains) {
		this();
		
		this.gemType = gemType;
		this.textureResource = textureResource;
		this.streetPosZ = streetPosZ;
		this.drains = drains;
	}

	public void setSoundManager(SoundManager soundManager)
	{
		this.soundman = soundManager;
	}
	
	public void setVibrator(Vibrator vibrator)
	{
		this.vibrator = vibrator;
	}
	
	/**
	 * reset gem position
	 */
	public void resetPosition() {
		posZ = startPosZ;
		fallSpeed = 0;
	}
	
	public boolean isFalling() {
		return isFalling;
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
	public boolean checkFallCollision()	{
		return posZ < streetPosZ;
	}

	public void checkCollisionType(float streetPos, float deviceRotation, ProgressManager progman)
	{
		boolean drainhit = false;
		//check type of collision (hit/miss)
		Iterator<ModelDrain> i = drains.values().iterator();
		while(i.hasNext())
		{
			if (!drainhit)
			{
				ModelDrain drain = i.next();
				// calculate the position of the drain
				
				float tempPos = streetPos + drain.getPosition();
				if ((tempPos > 1) || (tempPos < -1)){ } //if this drain has no interesting position, skip to the next one
				else
				{
					
					int drainType = drain.getDrainStyle();
					if (drainType == 0)
					{
						this.soundman.playSound(SoundFX.BREAK);
						progman.loseMoneyByBreak(drainType);
					}
					else if (drainType == this.gemType)
					{
						//TODO: check orientations
						//if (Math.abs(drain.getOrientationAngle() - deviceRotation ) < 1.0f)
						long vDuration = 300;
						if (this.vibrator != null) { this.vibrator.vibrate(vDuration); }
						this.soundman.playSound(SoundFX.HIT);
						progman.loseMoneyByHit(drainType);
					}
					else { this.soundman.playSound(SoundFX.MISS); }
					drainhit = true;
					break;
				}
			}
			
		}
		if (!drainhit)
		{
			this.soundman.playSound(SoundFX.MISS);
		}
		
	}
	
	/**
	 *
	 * Update the model's transformations.
	 */
	public void update(double deltaTime, float streetPos, float deviceRotation, ProgressManager progman) {
		
		if (this.isFalling) {
			if (!checkFallCollision()) {
				fallSpeed += 5f * deltaTime;
				posZ -= fallSpeed;
			}
			else {
				checkCollisionType(streetPos, deviceRotation, progman);
				//this.soundman.playSound(SoundFX.MISS);
				endFall();
			}
		}
	}
	
	/**
	 * Draw the gem if it was chosen and is falling
	 */
	public void draw(GL10 gl) {
		if (this.isFalling) {
			gl.glPushMatrix();
			gl.glTranslatef(0, 0, posZ);
			gl.glMultMatrixf(mTrans.toFloatArray(), 0);
			
			super.draw(gl);
			
			gl.glPopMatrix();
		}
	}
}

	

