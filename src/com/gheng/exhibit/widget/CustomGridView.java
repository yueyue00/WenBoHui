package com.gheng.exhibit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CustomGridView extends GridView {
	public CustomGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomGridView(Context context) {
		super(context);
	}

	public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		 int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
		 MeasureSpec.AT_MOST);
		 super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
