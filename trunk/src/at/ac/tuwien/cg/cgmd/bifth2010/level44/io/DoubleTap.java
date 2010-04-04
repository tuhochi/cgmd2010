package at.ac.tuwien.cg.cgmd.bifth2010.level44.io;

public class DoubleTap implements InputGesture {
	private float x;
	private float y;
	
	public DoubleTap(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public float getEndX() {
		return x;
	}

	@Override
	public float getEndY() {
		return y;
	}

	@Override
	public float getStartX() {
		return x;
	}

	@Override
	public float getStartY() {
		return y;
	}

	@Override
	public float getLength() {
		return Swipe.MAX_LENGTH;
	}

}
