package at.ac.tuwien.cg.cgmd.bifth2010.level83;

public class AnimationManager implements Animatable {
	
	private Animatable[] toAnimate;
	private int count;
	
	public AnimationManager(int size) {
		toAnimate = new Animatable[size];
		count = 0;
	}
	
	@Override
	public void animate(float deltaTime) {
		for (int i=0; i < count; i++) {
			toAnimate[i].animate(deltaTime);
		}
	}
	
	public int addAnimatable(Animatable ani) {
		if (count > toAnimate.length - 1)
			return -1;
		toAnimate[count] = ani;
		count++;
		return count-1;
	}

}
