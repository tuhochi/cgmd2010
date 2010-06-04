/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.game.Bunny;


/**
 * Uses an arbitrary number of textures to generate an animation sequence. 
 * 
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class SpriteAnimationEntity extends RenderEntity {

	/** Stores the IDs of the textures to use for the animation. */
	private int[] textureIds;	
	/** The index of the current texture. */
	private int textureIdx;
	/** How fast the animation progresses. */
	private int fps;
	/** The accumulated time of the animation used to progress the textures. */
	private float timePassed;
	/** The next time the texture has to progress. */
	private float updateTime;
	/** The duration the player is handicapped when crashing into an obstacle. */
	protected float crashDuration;	
	/** Flag whether the bunny crashed into an obstacle. */
	protected boolean crashed;
	/** Displays a cursing speech balloon for the bunny on crashes. */
	protected RenderEntity curseBubble;
	

	/**
	 * Constructor of the class <code>SpriteAnimationEntity</code>.
	 * @param x	X position of the entity.
	 * @param y	Y position of the entity.
	 * @param z	Z position of the entity.
	 * @param height Height of the entity.
	 * @param width	Width of the entity.
	 */
	public SpriteAnimationEntity(float x, float y, float z, float height, float width) {
		super(x, y, z, height, width);
		crashed = false;
		textureIdx = 0;
		fps = 10;
		timePassed = 0;
		updateTime = 1.f/fps;	
		crashDuration = LevelActivity.instance.getResources().getInteger(R.integer.l20_crash_duration) * 1000;
	}
	
	/**
	 * Sets the texture sequence for animation.
	 * @param textureIds	The IDs of the textures to use for the animation.
	 */
	public void setAnimationSequence(int[] textureIds) {
		if (null != textureIds && textureIds.length > 0) {				
			this.textureIds = textureIds;
			texture = textureIds[0];
			textureIdx = 0;
		}
	}
	
	/**
	 * Updates the entity.	
	 * @param dt	The passed time since the last update.
	 */
	public void update(float dt) {
		timePassed += dt;
		if(crashed)
		{
			if (timePassed >= crashDuration) {
				run();
			}
		}
		else {
			if (timePassed >= updateTime) {
				textureIdx++;
				textureIdx %= textureIds.length;
				texture = textureIds[textureIdx];
				timePassed = updateTime;
				updateTime += 1.f/fps;
			}
		}		
	}

	public int getFps() {
		return fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
	}
	
	/** Performs the a crash of the bunny with an obstacle. */	
	public void crash() {
		angle = 65;
		crashed = true;
		curseBubble.visible = true;
		timePassed = 0;
	}
	
	/** Sets the bunny in its running position. */	
	public void run() {
		angle = 0;
		crashed = false;
		curseBubble.visible = false;
		timePassed = 0;
		updateTime = 1.f/fps;
	}
	
	@Override
	public void render(GL10 gl) {
		super.render(gl);		
		curseBubble.render(gl);		
	}

}
