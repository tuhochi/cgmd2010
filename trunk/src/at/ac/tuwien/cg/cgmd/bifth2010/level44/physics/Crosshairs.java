package at.ac.tuwien.cg.cgmd.bifth2010.level44.physics;

import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.GameScene;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.sound.SoundPlayer;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.AimBar;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Sprite;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Texture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.TextureParts;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Util;

public class Crosshairs {
	/** the squared distance which the crosshairs mustn't exceed to stay green */
	private static final int MAX_DISTANCE_2 = 2500;
	/** the milliseconds it takes to load and shoot if the crosshairs are green */
	private static final int MILLISECONDS_TO_SHOOT = 3000;
	/** milliseconds between two shots */
	private static final int WAIT_TIME_BETWEEN_SHOTS = 3000;
	/** amount of pixels for the maximum recoil position change */
	private static final int RECOIL_STRENGTH = 50;
	
	/** the GameScene */
	private GameScene scene = null;
	/** the green crosshairs */
	private Sprite spriteGreen = null;
	/** the red crosshairs */
	private Sprite spriteRed = null;
	/** the width of the screen */
	private int screenWidth = 0;
	/** the height of the screen */
	private int screenHeight = 0;
	/** the rabbit */
	private PhysicalRabbit rabbit = null;
	/** the desired x-position */
	private float desiredX = 0.f;
	/** the desired y-position */
	private float desiredY = 0.f;
	/** is the weapon loaded? */
	private boolean isLoaded = false;
	/** timestamp, since the crosshairs turned green or -1 if they are red */
	private long timeStamp = -1L;
	/** timestamp of last shot */
	private long timeOfLastShot = -1L;
	
	private AimBar aimBar = null;

	public Crosshairs(GameScene scene, Texture texture, int width, int height) {
		this.scene = scene;
		this.screenWidth = width;
		this.screenHeight = height;

		spriteRed = new Sprite(TextureParts.makeCrosshairsRed(texture));
		spriteRed.setCenter(24, 24);

		spriteGreen = new Sprite(TextureParts.makeCrosshairsGreen(texture));
		spriteGreen.setCenter(24, 24);
		
		aimBar = new AimBar(texture);
		aimBar.setPosition(screenWidth/2, screenHeight/8+aimBar.getHeight()/2);
	}

	public void setRabbit(PhysicalObject rabbit) {
		this.rabbit = (PhysicalRabbit)rabbit;
	}

	public void draw(GL10 gl) {
		if (isNearRabbit() && allowedToShoot())
			spriteGreen.draw(gl);
		else
			spriteRed.draw(gl);
		
		if (this.isLoaded) {
			aimBar.setProgress(timeStamp > 0 ? Math.min(1.f, (float)(System.currentTimeMillis() - timeStamp) / (float)MILLISECONDS_TO_SHOOT) : 0f);
			aimBar.draw(gl);
		}
	}

	public void setX(float x) {
		spriteRed.setX(x);
		spriteGreen.setX(x);
	}

	public void setY(float y) {
		spriteRed.setY(y);
		spriteGreen.setY(y);
	}

	public float getX() {
		return spriteRed.getX();
	}

	public float getY() {
		return spriteGreen.getY();
	}

	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
	}

	public void move(float dx, float dy) {
		setX(spriteRed.getX() + dx);
		setY(spriteRed.getY() + dy);
	}

	public boolean isNearRabbit() {
		float dx = rabbit.getX() - getX();
		float dy = rabbit.getY() - getY();
		boolean near = dx*dx + dy*dy < MAX_DISTANCE_2; // if distance < 50 => is near

		return near;
	}

	private void setDesiredPosition(float x, float y) {
		desiredX = x;
		desiredY = y;
	}
	
	private boolean allowedToShoot() {
		return (timeOfLastShot == -1 || (System.currentTimeMillis() - timeOfLastShot) > WAIT_TIME_BETWEEN_SHOTS);
	}

	/**
	 * move the crosshairs if the rabbit has not landed
	 * 
	 * they follow the rabbit if they are near him, otherwise the move in the form of a lying 8
	 */
	public void ai() {
		// if rabbit has landed move out of the screen at the top
		if (rabbit.hasLanded()) {
			setDesiredPosition(screenWidth/2, -spriteRed.getHeight());
		} 
		// rabbit is in the air
		else {
			// follow the rabbit if crosshairs are near and the last shot wasn't within last 1.5 seconds
			if (isNearRabbit() && allowedToShoot()) {
				setDesiredPosition(rabbit.getX(), rabbit.getY());

				// 3 Seconds since crosshairs turned green? -> shoot
				if (timeStamp > 0 && System.currentTimeMillis() - timeStamp >= MILLISECONDS_TO_SHOOT) {
					shoot();
					// reset time and load-status
					timeOfLastShot = System.currentTimeMillis();
					timeStamp = -1L;
					isLoaded = false;
				}
			} 
			// crosshairs are not near the rabbit -> move in form of lying 8
			else {
				setDesiredPosition((float)(screenWidth/2 + screenWidth/2.5*Math.sin((double)(System.currentTimeMillis()/4000.))),
						(float)(screenHeight/2 + screenHeight/3*Math.sin((double)(System.currentTimeMillis()/2000.))));
			}
		}

		float dx = desiredX - getX();
		float dy = desiredY - getY();

		move(dx/220.f, dy/220.f);
	}

	/**
	 * load or unload the weapon (load if crosshairs are near rabbit)
	 * 
	 * @return true, if the loading sound shall be played
	 */
	public boolean changeLoadingState() {
		if (isNearRabbit() && allowedToShoot()) {
			// load and return true to play loading sound
			if (!isLoaded) {
				timeStamp = System.currentTimeMillis();
				isLoaded = true;

				return true;
			}

			// already loaded, play no sound
			return false;
		} else {
			// unload
			timeStamp = -1L;
			isLoaded = false;

			return false;
		}
	}

	/**
	 * shoot on the rabbit
	 */
	public void shoot() {
		// play shooting sound
		SoundPlayer.getInstance(scene.getContext()).play(SoundPlayer.SoundEffect.SHOT, 0.5f);
		
		// position change caused by recoil of the rifle
		setY(getY()-RECOIL_STRENGTH);
		
		// if the rabbit was hit, loose one coin
		if (this.hits(rabbit)) {
			// loose a coin
			rabbit.looseCoin();
			
			// vibrate and notify UI to update TextView after a short period of time (when shot-sound is finished)
			(new Thread() {
				public void run() {
					try {
						Thread.sleep(300L);
					} catch(Exception ex) {}
					finally {
						scene.getVibrator().vibrate(100L);
					}
				}
			}).start();
		}
	}
	
	public boolean hits(PhysicalObject o) {
		// TODO: hit test
		return true;
	}
}