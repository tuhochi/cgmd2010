package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import java.util.ArrayList;

/**
 * This class is an implementation of the {@link Animatable} interface. When 
 * its {@link #animate(float)} method is called, it animates all 
 * <code>Animatable</code>s which have been added.
 * 
 * @author Manuel Keglevic, Thomas Schulz
 */
public class AnimationManager extends Animatable {
	
	private ArrayList<Animatable> toAnimate;
	
	/**
	 * Constructor which initializes the AnimationManager with 
	 * the given size (number of items).
	 * 
	 * @param size - initial size 
	 */
	public AnimationManager(int size) {
		super();
		toAnimate = new ArrayList<Animatable>(size);
	}
	
	@Override
	public void animate(float deltaTime) {
		for (int i=0; i < toAnimate.size(); i++) {
			toAnimate.get(i).animate(deltaTime);
		}
	}
	
	/**
	 * Adds an animatable item.
	 * 
	 * @param ani	An implementation of the {@link Animatable} interface.
	 * @return		The index of the added <code>Animatable</code>.
	 */
	public int addAnimatable(Animatable ani) {
		toAnimate.add(ani);
		return toAnimate.size()-1;
	}

	/**
	 * Removes the {@link Animatable} with the given index <code>index</code>.
	 * @param a - Animateabel object to remove
	 */
	public void removeAnimation(Animatable a) {
		toAnimate.remove(a);
	}
}
