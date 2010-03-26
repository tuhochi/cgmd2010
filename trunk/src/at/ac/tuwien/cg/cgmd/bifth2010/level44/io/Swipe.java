package at.ac.tuwien.cg.cgmd.bifth2010.level44.io;

public class Swipe implements InputGesture {
	private float startX = 0;
	private float startY = 0;
	private float endX = 0;
	private float endY = 0;
	
	private float length = 0;
	private InputGesture.DisplayHalf displayHalf;
	
	public Swipe(float x1, float y1, float x2, float y2, InputGesture.DisplayHalf displayHalf) {
		this.startX = x1;
		this.startY = y1;
		this.endX = x2;
		this.endY = y2;
		
		this.displayHalf = displayHalf;
		this.length = Math.round(Math.sqrt((endX - startX) + (endX - startX) + (endY - startY) * (endY - startY)));
	}
	
	public float getLength() {
		return length;
	}
	
	public InputGesture.DisplayHalf getDisplayHalf() {
		return displayHalf;
	}
	
	public boolean isLeftHalf() {
		return displayHalf.equals(InputGesture.DisplayHalf.LEFT);
	}
	
	public boolean isRightHalf() {
		return !isLeftHalf();
	}

	@Override
	public float getEndX() {
		return endX;
	}

	@Override
	public float getEndY() {
		return endY;
	}

	@Override
	public float getStartX() {
		return startX;
	}

	@Override
	public float getStartY() {
		return startY;
	}
}
