package com.gheng.exhibit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 *
 * @author lileixing
 */
public class WrapWidthListView extends ListView {  
	  
    public WrapWidthListView(Context context) {  
        super(context);  
    }  
  
    public WrapWidthListView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public WrapWidthListView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        int width = 0;  
        for (int i = 0; i < getChildCount(); i++) {  
            View child = getChildAt(i);  
            child.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), heightMeasureSpec);  
            int w = child.getMeasuredWidth();  
            if (w > width) width = w;  
        }  
  
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width + getPaddingLeft() + getPaddingRight(), MeasureSpec.EXACTLY);  
  
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
    }  
} 
