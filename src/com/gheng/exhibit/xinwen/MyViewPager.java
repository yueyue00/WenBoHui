package com.gheng.exhibit.xinwen;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {
	private boolean flag = true;

	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		switch (arg0.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (flag) {
				return super.onTouchEvent(arg0);
			} else {
				return false;
			}
		default:
			break;
		}
		if (flag) {
			return super.onTouchEvent(arg0);
		} else {
			return false;
		}

		//
	}

	@Override
	public void scrollTo(int x, int y) {
		if (flag) {
			super.scrollTo(x, y);
		}
	}

	/**
	 * 设置是否可以左右滑动
	 * 
	 * @param flag
	 */
	public void setSlide(boolean flag) {
		this.flag = flag;
	}

}
