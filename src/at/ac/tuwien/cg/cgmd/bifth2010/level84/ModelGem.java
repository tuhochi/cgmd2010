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

	/** Type of the gem */
	private int gemType;
	
	/** Width of the gem */
	float width = 1.0f;
	/** Starting position where gem begins to fall */
	private final float startPosZ = -2f;
	/** End position of gem - when falling into the drain */
	private final float endPosZ = -13f;
	
	/** Flag, if collision was already checked or not */
	private boolean isCollisionChecked = false;
	
	/** Current z position */
	private float posZ = startPosZ;
	/** Fall speed of the gem */
	private float fallSpeed = 0.0f;
	
	/** Precision param for collision handling: delta that defines if gem hits drain at all */
	private float maxDeltaDrainPos = 1.6f;
	/** Precision param for collision handling: delta that defines if gem hits the drain's hole */
	private float maxDeltaHolePos = .7f;
	/** Precision param for collision handling: delta that defines if gem hits the drain's hole in the right angle */
	private float maxDeltaAngle = 5f; 
	
	/** Flag, if gem is falling or not */
	private boolean isFalling = false;
	
	/** Z-position of the street */
	private float streetPosZ;
	
	/** Executes sound effects */
	private SoundManager soundman;
	
	/** Necessary for vibrations */
	private Vibrator vibrator;
	/** Vibration pattern for a gem hitting the road/missing a drain */
	private long[] vibrationPatternMiss = {0, 30, 30, 30};
	/** Vibration pattern for a gem breaking apart */
	private long[] vibrationPatternBreak = {0, 80, 30, 80};
	
	/** Contains all the level's drains and is used for collision detection */
	private HashMap<Integer, ModelDrain> drains;
	
	/** The @link LevelActivity */
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
	
	/** Handler for dust-cloud animation */
	private Handler showDustAni = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			lvl.showAni(LevelActivity.AnimationType.DUST);
		}
	};
	
	/**
	 * Creates a new gem model and adjusts the inherited quad size.
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
	 * Creates a new gem model.
	 * @param gemType gem's type
	 * @param textureResource gem's texture
	 * @param streetPosZ gem's z position
	 * @param drains the level's drains
	 * @param lvl our @LevelActivity
	 */
	public ModelGem(int gemType, int textureResource, float streetPosZ, HashMap<Integer, ModelDrain> drains, LevelActivity lvl) {
		this();
		
		this.gemType = gemType;
		this.textureResource = textureResource;
		this.streetPosZ = streetPosZ;
		this.drains = drains;
		this.lvl = lvl;
	}

	/**
	 * Sets the @link SoundManager.
	 * @param soundManager the sound manager
	 */
	public void setSoundManager(SoundManager soundManager)
	{
		this.soundman = soundManager;
	}
	
	/**
	 * Sets the @link Vibrator.
	 * @param vibrator the vibrator
	 */
	public void setVibrator(Vibrator vibrator)
	{
		this.vibrator = vibrator;
	}
	
	/**
	 * Resets the gem's position to its initial starting position.
	 */
	public void resetPosition() {
		posZ = startPosZ;
		fallSpeed = 0;
	}
	
	/**
	 * Flag, if the gem is falling or not.
	 * @return true, if the gem is falling. Otherwise: false.
	 */
	public boolean isFalling() {
		return isFalling;
	}
	
	/**
	 * Starts the falling animation.
	 */
	public void startFall()	{
		this.isFalling = true;
	}
	
	/**
	 * Ends the falling animation and resets position.
	 */
	public void endFall() {
		this.isFalling = false;
		this.isCollisionChecked = false;
		resetPosition();
	}

	/**
	 * Main collision handling method
	 * @param streetPos current position of the street
	 * @param deviceRotation current rotation of the device
	 * @param progman reference to the progress manager to update the score
	 */
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
					//show splash animation and update money-points
					progman.loseMoneyByHit(gemType);
					showSplashAni.sendEmptyMessage(0);	
					if (vibrator != null) vibrator.vibrate(30);
				}
				else // show break animation if it is not the right gem for the drain
					breakApart(progman);
			}
			else // show break animation
				breakApart(progman);
		}
		else {
			// show dust animation
			this.soundman.playSound(SoundFX.MISS, 1f, 1f, 0);
			showDustAni.sendEmptyMessage(0);
			if (vibrator != null) vibrator.vibrate(vibrationPatternMiss, -1);
			endFall();
		}
	}
	
	/**
	 * Handles everything that is relevant for a breaking gem: 
	 * The adequate breaking animation is initialized, 
	 * the score is updated, sound effects are played and the vibration is executed.
	 * @param progman
	 */
	private void breakApart(ProgressManager progman) {
		this.soundman.playSound(SoundFX.BREAK, 1f, 1f, 0);
		
		showBreakAni.sendEmptyMessage(0);
		
		progman.loseMoneyByBreak(gemType);
		if (vibrator != null) 
			vibrator.vibrate(vibrationPatternBreak, -1);
		endFall();
	}
	
	/**
	 * Update the gem's transformation - happens only if the gem is falling.
	 * @param deltaTime passed time
	 * @param streetPos position of the @link ModelStreet
	 * @param deviceRotation device's rotation in degrees
	 * @param progman progress manager
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
	 * Draws the gem if it is falling (i.e., visible).
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