package at.ac.tuwien.cg.cgmd.bifth2010.level20;


/**
 * This Animator moves its RenderEntity along a straight line
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class LineAnimator extends Animator {


	protected float destX; 
	protected float destY;
	protected float speed;
	
	
	
	/** 
	 * @param re
	 * @param destX
	 * @param destY
	 * @param speed
	 */
	public LineAnimator(RenderEntity re, float destX, float destY, float speed) {
		
		super(re);
		
		this.destX = destX;
		this.destY = destY;
		this.speed = speed;
	}
	
	/**
	 * Updates the Animation object and the objects within. 
	 * 
	 * @param dt The delta time since the last frame.
	 */
	public void update(float dt) {
		
		if (re == null)
			return;
		
		float curSpeed = speed * (dt/1000.f);
		
		// Do a straight line		
		float dx = destX - re.x;
		float dy = destY - re.y;
		
		float dist = dx*dx + dy*dy;
		
		if (dist <= curSpeed * curSpeed) {
			re.setPos(destX, destY);			

			// Trigger an event or whatever
			EventManager.getInstance().dispatchEvent(EventManager.ANIMATION_COMPLETE, this);
			re = null;
			return;
		}
		
		// Fasten up calculation by pre multiplying speed :P
		dist = curSpeed / (float)Math.sqrt(dist);
		
		dx *= dist;
		dy *= dist;
		
		re.setPos(re.x + dx, re.y + dy);
		
	}
	
	/** 
	 * Creates a random destination for the animator object.
	 *  
	 * @param distFactor The max distance from the origin to the destination.
	 * */
	public void random(float distFactor) {
		
		// Ferdi: There's one easy way ;)
		float pi_2 = (float)Math.PI * 0.5f;  // = |_ 
		float a = (float) Math.random() * pi_2 + pi_2 * 0.5f; // = \/
//		Vector2 direction = new Vector2((float) Math.random(), (float) Math.random());
//		direction.normalize();	
		// Bring in -0.5/0.5 range.
//		direction.x -= 0.5;
//		direction.y -= 0.5;		
		distFactor *= LevelActivity.renderView.getHeight() / 480.f;
		destX = destX + (float)Math.cos(a) * distFactor;
		destY = destY + (float)Math.sin(a) * distFactor;
	}
	

}
