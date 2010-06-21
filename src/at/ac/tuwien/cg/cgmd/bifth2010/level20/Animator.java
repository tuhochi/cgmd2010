/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import at.ac.tuwien.cg.cgmd.bifth2010.level11.Vector2;

/**
 * This class manages the animation of a {@code ProductEntity}
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public abstract class Animator {
	
	/** Counts the number of created Animators and is assigned as ID. */
	protected static int count = 0;
	/** The unique Id of the Animator. */
	protected int id;
	/** The RenderEntity the Animator controlls. */
	protected RenderEntity re;	
	
	/**
	 * @param re
	 * @param destX
	 * @param destY
	 */
	public Animator(RenderEntity re) {
		
		this.id = ++count;
		this.re = re;
		
		re.animated = true;
	}
	
	
	/**
	 * Updates the Animation object and the objects within. 
	 * 
	 * @param dt The delta time since the last frame.
	 */
	public abstract void update(float dt);
	
	
}
