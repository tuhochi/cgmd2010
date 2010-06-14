package at.ac.tuwien.cg.cgmd.bifth2010.level20;

/**
 * This Animator moves its RenderEntity down, according to scrool speed
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class FallAnimator extends Animator {

	float acceleration;
	float velocity;
	
	/**
	 * @param re
	 * @param speed
	 */
	public FallAnimator(RenderEntity re, float acceleration) {
		super(re);
		
		this.acceleration = 150;
		this.velocity = 0;
	}

	/**
	 * Updates the Animation object and the objects within. 
	 * 
	 * @param dt The delta time since the last frame.
	 */
	@Override
	public void update(float dt) {
		
		if (re == null)
			return;
		
		// Increase fall velocity each frame		
		velocity += acceleration * dt * 0.001f;
		
		float dy = re.y - velocity;
		float dx = re.x - LevelActivity.gameManager.curScrollSpeed * dt;
		
		re.setPos(dx, dy);
		
		if (dy <= -re.height) {
			// Trigger an event or whatever
			EventManager.getInstance().dispatchEvent(EventManager.ANIMATION_COMPLETE, this);
			re = null;
			return;
		}		
	}
}
