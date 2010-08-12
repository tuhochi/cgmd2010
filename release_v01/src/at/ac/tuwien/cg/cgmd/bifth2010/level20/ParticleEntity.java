/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;

/**
 * Particle that can be used with the class ParticleSystem.
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 *
 */
public class ParticleEntity extends RenderEntity {
	 
	/** The velocity of the particle. */
	protected Vector2f vel;
	/** The duration the particle is visible. */
	protected float lifetime;	

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 */
	public ParticleEntity(float x, float y, float z, float width, float height, float speed) {
		super(x, y, z, width, height);	    
	    vel = new Vector2f(((float)Math.random()-0.5f)*speed, ((float)Math.random()-0.5f)*speed);	    
	    lifetime = 1500;
	}
	
		
	 /**
	  * Updates the ParticleEntity. 
	  * @param dt the passed time since the last frame.
	  */
	void update(float dt) {		  	    
	    x += vel.x;
	    y += vel.y;
	    lifetime -= dt;
	  }
	  
	  /** Whether the lifetime of the particle is over. */
	  public boolean dead() {
	    if (lifetime <= 0.0) {
	      return true;
	    } else {
	      return false;
	    }
	  }




}
