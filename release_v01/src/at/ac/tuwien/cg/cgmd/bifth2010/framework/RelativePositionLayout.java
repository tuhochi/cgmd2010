package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsoluteLayout;

/**
 * This layout arranges its children relatively to its absolute size. 
 * Each child is given a position in x=[0..1] and y=[0..1] indicating the relative position of it.
 * The specified position of the children is given relatively to their center.
 *    
 * @author Peter Rautek
 *
 */

@SuppressWarnings("deprecation") 
public class RelativePositionLayout extends AbsoluteLayout { 

	private static final String CLASS_TAG = RelativePositionLayout.class.getName();

	Map<View, PointF> mMapRelativeCoordinates = new HashMap<View, PointF>();

	public RelativePositionLayout(Context context) { 
		super(context); 
	} 

	public RelativePositionLayout(Context context, AttributeSet attrs) { 
		super(context, attrs); 
	} 

	public RelativePositionLayout(Context context, AttributeSet attrs, int defStyle) { 
		super(context, attrs, defStyle); 
	} 

	@Override 
	protected void onLayout(boolean changed, int l, int t, int r, int b) { 

		int count = getChildCount(); 

		for (int i = 0; i < count; i++) { 
			View child = getChildAt(i); 
			if (child.getVisibility() != GONE) { 

				AbsoluteLayout.LayoutParams lp = 
					(AbsoluteLayout.LayoutParams) child.getLayoutParams();

				int childLeft = 0;
				int childTop = 0;

				PointF p = mMapRelativeCoordinates.get(child);
				if(p!=null) {
					float fChildWidth = (float)child.getMeasuredWidth();
					float fChildHeight = (float)child.getMeasuredHeight();
					float fLeft = p.x * this.getMeasuredWidth() - (fChildWidth*0.5f);
					float fTop = p.y * this.getMeasuredHeight() - (fChildHeight*0.5f);
					childLeft = (int) fLeft;  
					childTop = (int) fTop;
				}

				child.layout(childLeft, childTop, 
						childLeft + child.getMeasuredWidth(), 
						childTop + child.getMeasuredHeight()); 

			} 
		} 
	}



	/**
	 * Add a child-view to this layout.
	 * 
	 * @param v The view to be added as a child.
	 * @param fx The horizontal position [0..1] of the child's center.
	 * @param fy The vertical position [0..1] of the child's center.
	 */
	public void addView(View v, float fx, float fy) {
		mMapRelativeCoordinates.put(v, new PointF(fx, fy));
		super.addView(v);
	}

	@Override
	public void removeView(View v) {
		mMapRelativeCoordinates.remove(v);
		super.removeView(v);
	}

	/**
	 * Set the position of an existing child-view.
	 * 
	 * @param v The view that is already a child of this layout.
	 * @param x The new horizontal position [0..1] of the child's center.
	 * @param y The new vertical position [0..1] of the child's center.
	 */
	public void setRelativePosition(View child, float x, float y) {
		PointF p = mMapRelativeCoordinates.get(child);
		if(p!=null) {
			p.x = x;
			p.y = y;
		} else {
			throw new InvalidParameterException("The view is not a child of this layout");
		}
	} 


}