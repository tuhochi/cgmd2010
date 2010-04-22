package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import android.view.MotionEvent;

/**
 * The Class CustomGestureDetector.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class CustomGestureDetector
{
	
	/** The Constant MOVE_THRESHOLD. */
	private static final int MOVE_THRESHOLD = 30;
	
	/** The listener. */
	private final CustomOnGestureListener listener;
	
	/** The current start event. */
	private MotionEvent currentStartEvent;
	
	/** The last action. */
	private int lastAction;
	
	/** The last x/y. */
	private float lastX,lastY;
	
	/** The first x/y. */
	private float firstX,firstY;
	
	/** Whether the current action is a long press. */
	private boolean isLongPress;
	
	/**
	 * Instantiates a new custom gesture detector.
	 *
	 * @param listener the listener
	 */
	public CustomGestureDetector(CustomOnGestureListener listener)
	{
		this.listener = listener;
		isLongPress = true;
	}
	
	/**
	 * On touch event.
	 *
	 * @param e the e
	 */
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
			else
			{
				listener.onLongTouch(currentStartEvent, e.getEventTime()-e.getDownTime());
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
	
	/**
	 * The listener interface for receiving gesture events.
	 * The class that is interested in processing a gesture
	 * event implements this interface, and the object created
	 * with that class is registered with a CustomGestureDetector using the
	 * CustomGestureDetector's constructor. When
	 * the gesture event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see CustomOnGestureEvent
	 */
	public interface CustomOnGestureListener
	{
		
		/**
		 * Notified when a scroll occurs with the initial on down MotionEvent and the current move MotionEvent. The distance in x and y is also supplied for convenience.
		 * 
		 * @param e1 The first down motion event that started the scrolling.
		 * @param e2 The move motion event that triggered the current onScroll.
		 * @param distanceX The distance along the X axis that has been scrolled since the last call to onScroll. This is NOT the distance between e1 and e2.
		 * @param distanceY The distance along the Y axis that has been scrolled since the last call to onScroll. This is NOT the distance between e1 and e2.
		 * @return true, if the event is consumed, else false
		 */
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
		
		/**
		 * Notified during a long press occurs
		 * 
		 * @param e The initial on down motion event that started the longpress.
		 * @param duration How long the press lasted so far
		 * @return true, if the event is consumed, else false
		 */
		public boolean onLongTouch(MotionEvent e, long duration);
		
		/**
		 * Notified when a long press ends
		 * 
		 * @param e The initial on down motion event that started the longpress.
		 * @param duration How long the press lasted
		 * @return true, if the event is consumed, else false
		 */
		public boolean onTouchUp(MotionEvent e, long duration);
	}
}
