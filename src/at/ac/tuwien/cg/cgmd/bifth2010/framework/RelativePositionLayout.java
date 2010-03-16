package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsoluteLayout;


@SuppressWarnings("deprecation") 
public class RelativePositionLayout extends AbsoluteLayout { 

     public RelativePositionLayout(Context context) { 
          super(context); 
          // TODO Auto-generated constructor stub 
     } 

     public RelativePositionLayout(Context context, AttributeSet attrs) { 
          super(context, attrs); 
          // TODO Auto-generated constructor stub 
     } 

     public RelativePositionLayout(Context context, AttributeSet attrs, int defStyle) { 
          super(context, attrs, defStyle); 
          // TODO Auto-generated constructor stub 
     } 
      
     @Override 
     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

          int layoutWidth; 
          int layoutHight; 
           
          // Find out how big everyone wants to be 
          measureChildren(widthMeasureSpec, heightMeasureSpec); 
           
          //Give Layout its maximum size keeping the backgrounds aspect ratio 
          if(MeasureSpec.getMode(widthMeasureSpec)== MeasureSpec.AT_MOST && MeasureSpec.getMode(heightMeasureSpec)== MeasureSpec.AT_MOST){ 
               float widthRate = (float)MeasureSpec.getSize(widthMeasureSpec) / (float)getBackground().getIntrinsicWidth(); 
               float heightRate = (float)MeasureSpec.getSize(heightMeasureSpec) / (float)getBackground().getIntrinsicHeight(); 
               float rate = Math.min(widthRate, heightRate); 
               layoutWidth = (int)(getBackground().getIntrinsicWidth()*rate); 
               layoutHight = (int)(getBackground().getIntrinsicHeight()*rate); 

          }else{ 
               layoutWidth = getBackground().getIntrinsicWidth(); 
               layoutHight = getBackground().getIntrinsicHeight(); 
          } 
           
          // Account for padding too 
          layoutWidth += getPaddingLeft() + getPaddingRight(); 
          layoutHight += getPaddingTop() + getPaddingBottom(); 
        
          // Write measurements 
          setMeasuredDimension(resolveSize(layoutWidth, widthMeasureSpec), 
                resolveSize(layoutHight, heightMeasureSpec)); 

           
     } 
      
      
     @Override 
     protected void onLayout(boolean changed, int l, int t, int r, int b) { 
           
          int count = getChildCount(); 
           
        for (int i = 0; i < count; i++) { 
            View child = getChildAt(i); 
            if (child.getVisibility() != GONE) { 

                AbsoluteLayout.LayoutParams lp = 
                        (AbsoluteLayout.LayoutParams) child.getLayoutParams(); 

                int childLeft = getPaddingLeft() + (lp.x*(r-l)/1000) - (int)((float)child.getMeasuredWidth()/2.f);  
                int childTop = getPaddingTop() + (lp.y*(b-t)/1000) - (int)((float)child.getMeasuredHeight()/2.f);
                 child.layout(childLeft, childTop, 
                        childLeft + child.getMeasuredWidth(), 
                        childTop + child.getMeasuredHeight()); 

            } 
        } 
     } 
      
}