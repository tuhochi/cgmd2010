package at.ac.tuwien.cg.cgmd.bifth2010.level83;

/**
 * An interface for objects which can be animated. Implementations have to 
 * provide an {@link #animate(float)} method that performs the animation of the
 * object.
 */
public abstract class Animatable implements Comparable<Animatable>{

	
	public int id;
	
	
	public Animatable() {
		id = IDGenerator.generateID();
	}
	
	/**
	 * This method is used to animate the <code>Animatable</code> depending on 
	 * the passed time <code>deltaTime</code>.
	 * 
	 * @param deltaTime	Time passed since last call.
	 */
	public abstract void animate(float deltaTime);
	
	@Override
	public int compareTo(Animatable another) {
		if(another.id == this.id)
			return 0;
		else 
			return -1;
	}
}
