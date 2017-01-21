package com.gheng.exhibit.widget;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

import com.gheng.exhibit.view.adapter.AbstractEntityAdapter;

public class PageGallery extends Gallery
{
	/**
	 * 这里的数值，限制了每次滚动的最大长度，图片宽度为480PX。这里设置600效果好一些。 这个值越大，滚动的长度就越大。
	 * 也就是会出现一次滚动跨多个Image。这里限制长度后，每次滚动只能跨一个Image
	 */
	private static final int timerAnimation = 1;
	private static final int time = 5000;
	private static boolean isTouch = false;
	private final Handler mHandler = new Handler(){
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg){
			switch (msg.what){
			case timerAnimation:
				int position = getSelectedItemPosition();
				AbstractEntityAdapter adapter = (AbstractEntityAdapter) getAdapter();
				if(null == adapter)
					break;
				List list = adapter.getData();
				if(isTouch || list == null || list.size() == 0 || list.size() == 1){
					return;
				}
				if (position >= (getCount() - 1)){
					onScroll(null, null, -1, 0);
					onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
				} else{
					onScroll(null, null, 1, 0);
					onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
				}
//				PageGallery.this.setSelection(++position);
				break;

			default:
				break;
			}
		};
	};

	private Timer timer = new Timer();
	private TimerTask task = new TimerTask(){
		public void run(){
			mHandler.sendEmptyMessage(timerAnimation);
		}
	};

	public PageGallery(Context paramContext){
		super(paramContext);
		timer.schedule(task, time, time);
	}

	public PageGallery(Context paramContext, AttributeSet paramAttributeSet){
		super(paramContext, paramAttributeSet);
		timer.schedule(task, time, time);

	}

	public PageGallery(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt){
		super(paramContext, paramAttributeSet, paramInt);
		timer.schedule(task, time, time);
	}

	private boolean isScrollingLeft(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2){
		float f2 = paramMotionEvent2.getX();
		float f1 = paramMotionEvent1.getX();
		return f2 > f1;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isTouch = true;
			timer.cancel();
			break;

		case MotionEvent.ACTION_UP:
			isTouch = false;
			timer = new Timer();
			task = new TimerTask(){
				public void run(){
					mHandler.sendEmptyMessage(timerAnimation);
				}
			};
			timer.schedule(task, time, time);
			break;
		}
		return super.onTouchEvent(event);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onFling(MotionEvent e1,
			MotionEvent e2, float paramFloat1, float paramFloat2){
		AbstractEntityAdapter adapter = (AbstractEntityAdapter) getAdapter();
		if(adapter == null)
			return true;
		List list = adapter.getData();
		if(list == null || list.size() == 0 || list.size() == 1){
			return true;
		}
		int keyCode;
		if (isScrollingLeft(e1, e2)){
			keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
		} else{
			keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(keyCode, null);
		return true;
	}
	
	public void destroy(){
		timer.cancel();
	}
}

/*public class PageGallery extends Gallery {

	public PageGallery(Context context) {
		super(context);
	}

	public PageGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int kEvent;
		if (isScrollingLeft(e1, e2)) { // Check if scrolling left
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else { // Otherwise scrolling right
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(kEvent, null);
		return true;

	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

}*/
