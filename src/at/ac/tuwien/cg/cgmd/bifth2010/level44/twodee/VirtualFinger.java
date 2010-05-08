package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import javax.microedition.khronos.opengles.GL10;

public class VirtualFinger extends Sprite {
	public enum DemoGesture { NONE, SWIPE_LEFT, SWIPE_RIGHT, DOUBLE_TAP };
	
	private static final int PAUSE_TICKS = 150;
	private static final int SWIPE_GESTURE_TICKS = 200;
	private static final int TAP_GESTURE_TICKS = 7;
	private static final int TAP_PAUSE_TICKS = 15;
	
	private float screenWidth;
	private float screenHeight;
	private DemoGesture currentGesture;
	private boolean visible;
	private int ticks;
	
	public VirtualFinger(Texture texture, float screenWidth, float screenHeight) {
		super(TextureParts.makeVirtualFinger(texture));
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.currentGesture = DemoGesture.NONE;
		this.visible = false;
		this.ticks = 0;
	}
	
	@Override
	public void draw(GL10 gl) {
		if (visible) {
			super.draw(gl);
		}
	}

	public void setGesture(DemoGesture gesture) {
		if (currentGesture != gesture) {
			ticks = 0;
			switch (gesture) {
				case NONE:
					visible = false;
					break;
				case SWIPE_LEFT:
					setX(screenWidth*.1f);
					break;
				case SWIPE_RIGHT:
					setX(screenWidth*.9f);
					break;
				case DOUBLE_TAP:
					setPosition(screenWidth*.5f,
							    screenHeight*.5f);
					break;
					
			}
			currentGesture = gesture;
		}
	}
	
	public void step() {
		ticks++;
		switch (currentGesture) {
			case SWIPE_LEFT:
			case SWIPE_RIGHT:
				if (ticks < SWIPE_GESTURE_TICKS) {
					setY(screenHeight*(.2f+(((float)ticks/(float)SWIPE_GESTURE_TICKS)*.6f)));
					visible = true;
				} else if (ticks < SWIPE_GESTURE_TICKS + PAUSE_TICKS) {
					visible = false;
				} else {
					ticks = 0;
				}
				break;
			case DOUBLE_TAP:
				if (ticks < TAP_GESTURE_TICKS) {
					visible = true;
				} else if (ticks < TAP_GESTURE_TICKS + TAP_PAUSE_TICKS) {
					visible = false;
				} else if (ticks < TAP_GESTURE_TICKS*2 + TAP_PAUSE_TICKS) {
					visible = true;
				} else if (ticks < TAP_GESTURE_TICKS*2 + TAP_PAUSE_TICKS*2 + PAUSE_TICKS) {
					visible = false;
				} else {
					ticks = 0;
				}
				break;
			default:
				break;
		}	
	}
	
}
