/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

/**
 * This class manages the animation of a {@code ProductEntity} while being moved into a shopping cart 
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class Animator {
	
	// This counter increases by 1 everytime a new animator is created. 
	protected static int count = 0;

	protected int id;
	protected RenderEntity re;
	protected float destX; 
	protected float destY;
	protected float speed;
	
	
	
	/**
	 * @param re
	 * @param destX
	 * @param destY
	 */
	public Animator(RenderEntity re, float destX, float destY, float speed) {
		
		this.id = ++count;
		this.re = re;
		this.destX = destX;
		this.destY = destY;
		this.speed = speed;
		
		re.animated = true;
	}
	
	
	/**
	 * Updates the Animation object and the objects within. 
	 * 
	 * @param dt The delta time since the last frame.
	 */
	public void update(float dt) {
		
		if (re == null)
			return;
		
		// Do a straight line		
		float dx = destX - re.x;
		float dy = destY - re.y;
		
		float dist = dx*dx + dy*dy;
		
		if (dist <= speed * speed) {
			re.setPos(destX, destY);			

			// Trigger an event or whatever
			EventManager.getInstance().dispatchEvent(EventManager.ANIMATION_COMPLETE, this);
			re = null;
			return;
		}
		
		// Fasten up calculation by pre multiplying speed :P
		dist = speed / (float)Math.sqrt(dist);
		
		dx *= dist;
		dy *= dist;
		
		re.setPos(re.x + dx, re.y + dy);
		
	}
	
}
