/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * This TouchListener's onTouch method is invoked, before a touch event is dispatched to a View. 
 * We use this to sleep the Thread here, so that it doesn't fire an unnecessary amount of events.
 * 
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class TouchListener implements OnTouchListener {

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		
		// Dispatch the event to our RenderView immediately
		boolean b = view.onTouchEvent(event);
		
		// Sleep the thread, so that it doesn't fire too many events
//		try {
//			Thread.sleep(35);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		// Return the result from the views onTouchEvent method. (If it is false, the onTouchEvent method gets called a second time.)
		return b;
	}

}
