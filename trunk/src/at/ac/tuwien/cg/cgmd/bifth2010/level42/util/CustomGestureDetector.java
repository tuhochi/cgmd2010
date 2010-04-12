package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import android.view.MotionEvent;

public class CustomGestureDetector
{
	private static final int MOVE_THRESHOLD = 30;
	private final CustomOnGestureListener listener;
	private MotionEvent currentStartEvent;
	private int lastAction;
	private float lastX,lastY;
	private float firstX,firstY;
	private boolean isLongPress;
	
	public CustomGestureDetector(CustomOnGestureListener listener)
	{
		this.listener = listener;
		isLongPress = true;
	}
	
	public void onTouchEvent(MotionEvent e)
	{
		if(currentStartEvent == null)
		{
			currentStartEvent = e;
			firstX = e.getX();
			firstY = e.getY();
			isLongPress = true;
		}
		
		switch(e.getAction())
		{
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			if(currentStartEvent == null)
				break;
			
			float distanceX = lastX-e.getX();
			float distanceY = lastY-e.getY();
			
			if(!isLongPress || Math.abs(e.getX()-firstX) > MOVE_THRESHOLD || Math.abs(e.getY()-firstY) > MOVE_THRESHOLD)
			{
				isLongPress = false;
				listener.onScroll(currentStartEvent, e, distanceX, distanceY);
			}
			break;
			
		case MotionEvent.ACTION_UP:
			if(currentStartEvent == null)
				break;
			
			if(lastAction != MotionEvent.ACTION_UP)
			{
				if(isLongPress)
					listener.onTouchUp(currentStartEvent, e.getEventTime()-e.getDownTime());
				else
					listener.onScroll(currentStartEvent, e, lastX-e.getX(), lastY-e.getY());
			}
			
			currentStartEvent = null;
			break;
		}

		lastX = e.getX();
		lastY = e.getY();
		lastAction = e.getAction();
	}
	
	public interface CustomOnGestureListener
	{
		/**
		 * Notified when a scroll occurs with the initial on down MotionEvent and the current move MotionEvent. The distance in x and y is also supplied for convenience.
		 * 
		 * Parameters
		 * 		e1  The first down motion event that started the scrolling.
		 * 		e2  The move motion event that triggered the current onScroll.
		 * 		distanceX  The distance along the X axis that has been scrolled since the last call to onScroll. This is NOT the distance between e1 and e2.
		 * 		distanceY  The distance along the Y axis that has been scrolled since the last call to onScroll. This is NOT the distance between e1 and e2.
		 * 
		 * Returns
		 * 		true if the event is consumed, else false
		 */
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
		
		/**
		 * Notified when a long press occurs with the initial on down MotionEvent that trigged it.
		 * 
		 * Parameters
		 * 		e  The initial on down motion event that started the longpress.
		 * 		duration  How long the press lasted
		 */
		public boolean onTouchUp(MotionEvent e, long duration);
	}
}
