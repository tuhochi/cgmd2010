package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;

import android.os.Handler;
import android.os.Message;
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
	/** end position of gem - when falling into the drain **/
	private final float endPosZ = -13f;
	
	/** flag, if collision was already checked or not **/
	private boolean isCollisionChecked = false;
	
	private float posZ = startPosZ;
	/** fall speed of the gem **/
	private float fallSpeed = 0.0f;
	
	private float maxDeltaDrainPos = 1.6f;
	private float maxDeltaHolePos = .7f;
	private float maxDeltaAngle = 5f; 
	
	/** flag, if gem is falling or not **/
	private boolean isFalling = false;
	
	/** z position of the street **/
	private float streetPosZ;
	
	/** soundmanager for executing soundfx **/
	private SoundManager soundman;
	
	/** vibrator for vibrations **/
	private Vibrator vibrator;
	
	private long[] vibrationPatternMiss = {0, 30, 30, 30};
	private long[] vibrationPatternBreak = {0, 80, 30, 80};
	
	/** drainMap used for collision detection **/
	private HashMap<Integer, ModelDrain> drains;
	
	private LevelActivity lvl;
	
	/** Handler for gem break animations */
	private Handler showBreakAni = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			lvl.showBreakAni(gemType);
		}
	};
	
	/** Handler for water splash animation */
	private Handler showSplashAni = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			lvl.showAni(LevelActivity.AnimationType.SPLASH);
		}
	};
	
	/** Handler for dust/miss animation */
	private Handler showDustAni = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			lvl.showAni(LevelActivity.AnimationType.DUST);
		}
	};
	
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
	public ModelGem(int gemType, int textureResource, float streetPosZ, HashMap<Integer, ModelDrain> drains, LevelActivity lvl) {
		this();
		
		this.gemType = gemType;
		this.textureResource = textureResource;
		this.streetPosZ = streetPosZ;
		this.drains = drains;
		this.lvl = lvl;
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
		this.isCollisionChecked = false;
		resetPosition();
	}

	public void checkCollisionType(float streetPos, float deviceRotation, ProgressManager progman)
	{
		ModelDrain drainToCheck = null;
		isCollisionChecked = true;
		float deltaPos = Float.MAX_VALUE;
		
		//Invert device rotation because we need the real angle 
		//since the content (street) is rotated into the very opposite direction.
		deviceRotation *= -1;
		
		for (Iterator<ModelDrain> i = drains.values().iterator(); drainToCheck == null && i.hasNext();) {
			ModelDrain d = i.next();
			
			//Calculate the position of the drain. 
			//Interesting drains should be in the range of e.g. -[drainWidth] to +[drainWidth], 
			//so right in the middle of the screen.
			//If current drain is within range (maxDeltaDrainPos), use it.
			deltaPos = Math.abs(streetPos + d.getPosition()); 
			if (deltaPos < maxDeltaDrainPos)
				drainToCheck = d;
		}
		
		if (drainToCheck != null) {
			boolean drainHit = false;
			
			//Log.i("checking", "deltaPos: " + deltaPos + " - maxDeltaHolePos: " + maxDeltaHolePos);
			if (deltaPos < maxDeltaHolePos) {
				//Log.i("checking", "deeper!");
				float deltaAngle = Math.abs(drainToCheck.getOrientationAngle() - deviceRotation);
						
				switch (drainToCheck.getStyle()) {
				case ModelDrain.CLOSED:	breakApart(progman); break;
				case ModelDrain.ROUND: drainHit = drainToCheck.getStyle() == this.gemType; break;
				case ModelDrain.OCT: drainHit = drainToCheck.getStyle() == this.gemType && (deltaAngle % 45f) < maxDeltaAngle; break;
				case ModelDrain.RECT: drainHit = drainToCheck.getStyle() == this.gemType && (deltaAngle % 180f) < maxDeltaAngle; break;
				case ModelDrain.DIAMOND: drainHit = drainToCheck.getStyle() == this.gemType && deltaAngle < maxDeltaAngle; break;
				}
				
				if (drainHit) {
					progman.loseMoneyByHit(gemType);
					showSplashAni.sendEmptyMessage(0);	
					if (vibrator != null) vibrator.vibrate(30);
				}
				else
					breakApart(progman);
			}
			else
				breakApart(progman);
		}
		else {
			this.soundman.playSound(SoundFX.MISS, 1f, 1f, 0);
			showDustAni.sendEmptyMessage(0);
			if (vibrator != null) vibrator.vibrate(vibrationPatternMiss, -1);
			endFall();
		}
	}
	
	private void breakApart(ProgressManager progman) {
		this.soundman.playSound(SoundFX.BREAK, 1f, 1f, 0);
		
		showBreakAni.sendEmptyMessage(0);
		
		progman.loseMoneyByBreak(gemType);
		if (vibrator != null) 
			vibrator.vibrate(vibrationPatternBreak, -1);
		endFall();
	}
	
	/**
	 *
	 * Update the model's transformations.
	 */
	public void update(double deltaTime, float streetPos, float deviceRotation, ProgressManager progman) {
		if (this.isFalling) {
			fallSpeed += 5f * deltaTime;
			posZ -= fallSpeed;
			if (!isCollisionChecked && posZ < streetPosZ + 3.5f) {
				checkCollisionType(streetPos, deviceRotation, progman);
			}
			else if (posZ < endPosZ) {
				this.soundman.playSound(SoundFX.HIT, 1f, 1f, 0);
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