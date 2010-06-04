package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import android.util.Log;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;

/**
 * The Class CustomGestureDetector.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class CustomGestureDetector
{
	/** The listener. */
	private final CustomOnGestureListener listener;
	
	/** The current start event. */
	private MotionEvent currentStartEvent;
	
	/** The last action. */
	private int lastAction = -1;
	
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
		Log.v(LevelActivity.TAG, "onTouchEvent(" + e + ", downTime=" + e.getDownTime() + ", eventTime=" + e.getEventTime() + ")");
		
		OUTER_SWITCH:
		switch(e.getAction())
		{
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			
			Log.d(LevelActivity.TAG, "ACTION_DOWN or ACTION_MOVE");
			
			if(currentStartEvent == null)
			{
				Log.d(LevelActivity.TAG, "(currentStartEvent == null), resetting currentStartEvent");
				currentStartEvent = e;
				firstX = e.getX();
				firstY = e.getY();
				isLongPress = true;
				listener.onDown(e);
				break;
			}
			
			switch(e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				
				Log.d(LevelActivity.TAG, "Subdivision: ACTION_DOWN");
				if(lastAction == MotionEvent.ACTION_DOWN)
				{
					Log.d(LevelActivity.TAG, "lastAction is ACTION_DOWN, breaking");
					break OUTER_SWITCH;
				}
				
				Log.d(LevelActivity.TAG, "calling onDown()");
				listener.onDown(e);
				break OUTER_SWITCH;
				
			case MotionEvent.ACTION_MOVE:
				
				Log.d(LevelActivity.TAG, "Subdivision: ACTION_MOVE");
				float distanceX = lastX-e.getX();
				float distanceY = lastY-e.getY();
				
				Log.d(LevelActivity.TAG, "checking if this is a longpress or a move ... ");
				if(!isLongPress || Math.abs(e.getX()-firstX) > Config.TOUCH_DEADZONE || Math.abs(e.getY()-firstY) > Config.TOUCH_DEADZONE)
				{
					isLongPress = false;
					Log.e(LevelActivity.TAG, " ... move, calling onScroll()");
					listener.onScroll(currentStartEvent, e, distanceX, distanceY);
				}
				else
					Log.d(LevelActivity.TAG, " ... longpress, doing nothing.");
				break OUTER_SWITCH;
			}
			
		case MotionEvent.ACTION_UP:
			
			Log.d(LevelActivity.TAG, "ACTION_UP");
			
			if(currentStartEvent == null)
			{
				Log.e(LevelActivity.TAG, "currentStartEvent is null, breaking");
				break;
			}

			if(lastAction == MotionEvent.ACTION_UP)
			{
				Log.d(LevelActivity.TAG, "lastAction is ACTION_UP, breaking");
				break;
			}
			
			Log.d(LevelActivity.TAG, "calling onUp, setting currentStartEvent to null");
			listener.onUp(e, isLongPress, e.getEventTime()-e.getDownTime());
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
		 * Notified on a down touch
		 * 
		 * @param e the event that startet the touch
		 * @return true, if the event is consumed, else false
		 */
		public boolean onDown(MotionEvent e);
		
		/**
		 * Notified on an up touch
		 * 
		 * @param e the event that startet the touch
		 * @param wasLongPress whether this touch was stationary or not
		 * @param duration how long this touch lasted
		 * @return true, if the event is consumed, else false
		 */
		public boolean onUp(MotionEvent e, boolean wasLongPress, long duration);
	}
}
