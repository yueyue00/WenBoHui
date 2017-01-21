package com.gheng.exhibit.widget;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;

import com.gheng.exhibit.utils.DipUtils;
import com.gheng.exhibit.view.adapter.AbstractEntityAdapter;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class ViewPagerPoint extends RelativeLayout {
	/**
	 * 轮换页面
	 */
	private PageGallery gallery;
	/**
	 * 线性画点布局
	 */
	private LinearLayout linearLayout;

	private OnItemChangeListener onItemChangeListener;

	/**
	 * @param context
	 */
	public ViewPagerPoint(Context context) {
		super(context);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ViewPagerPoint(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ViewPagerPoint(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		gallery = new PageGallery(context);
		gallery.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
		addView(gallery);
		linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rp.setMargins(0, 0, 0, DipUtils.dip2px(getContext(), 0));
		linearLayout.setLayoutParams(rp);
		addView(linearLayout);

		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				position %= ((AbstractEntityAdapter<?>) parent.getAdapter())
						.getData().size();
				active(position);
				if (onItemChangeListener != null) {
					onItemChangeListener.change(position);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	/**
	 * 设置Adapter
	 */
	public void setAdapter(SpinnerAdapter adapter) {
		gallery.setAdapter(adapter);
		initData(adapter);
	}

	private void initData(SpinnerAdapter adapter) {
		AbstractEntityAdapter<?> aAdapter = (AbstractEntityAdapter<?>) adapter;
		linearLayout.removeAllViews();
		int margin = DipUtils.dip2px(getContext(), 1.5f);
		if (adapter == null)
			return;
		int count = aAdapter.getData().size();
		for (int i = 0; i < count; i++) {
			ImageView iv = new ImageView(linearLayout.getContext());
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
			MarginLayoutParams mp = new MarginLayoutParams(param);
			mp.setMargins(margin, 0, margin, 0);
			iv.setLayoutParams(mp);
			iv.setPadding(margin, 0, margin, 0);
			iv.setImageResource(R.drawable.inactive_page_image);
			linearLayout.addView(iv);
		}
		// gallery.setSelection(0, false);
	}

	/**
	 * 激活第几个图片
	 */
	private void active(int index) {
		int count = linearLayout.getChildCount();
		if (count < 2) {
			linearLayout.setVisibility(View.GONE);
			return;
		}
		linearLayout.setVisibility(View.VISIBLE);
		for (int i = 0; i < count; i++) {
			ImageView iv = (ImageView) linearLayout.getChildAt(i);
			iv.setImageResource(R.drawable.inactive_page_image);
		}
		ImageView iv = (ImageView) linearLayout.getChildAt(index);
		iv.setImageResource(R.drawable.active_page_image);
	}

	/**
	 * @param onItemChangeListener
	 *            the onItemChangeListener to set
	 */
	public void setOnItemChangeListener(
			OnItemChangeListener onItemChangeListener) {
		this.onItemChangeListener = onItemChangeListener;
	}

	/**
	 * 点击事件
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		gallery.setOnItemClickListener(listener);
	}

	public interface OnItemChangeListener {
		public void change(int index);
	}

	private static boolean isTouch = false;

	class PageGallery extends Gallery {
		/**
		 * 这里的数值，限制了每次滚动的最大长度，图片宽度为480PX。这里设置600效果好一些。 这个值越大，滚动的长度就越大。
		 * 也就是会出现一次滚动跨多个Image。这里限制长度后，每次滚动只能跨一个Image
		 */
		private static final int timerAnimation = 1;
		private static final int time = 4000;
		private final Handler mHandler = new Handler() {
			@SuppressWarnings("deprecation")
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case timerAnimation:
					int position = getSelectedItemPosition();
					AbstractEntityAdapter adapter = (AbstractEntityAdapter) getAdapter();
					if (null == adapter)
						break;
					List<?> list = adapter.getData();
					if (isTouch || list == null || list.size() == 0
							|| list.size() == 1) {
						return;
					}
					if (position >= (getCount() - 1)) {
						onScroll(null, null, -1, 0);
						onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
					} else {
						onScroll(null, null, 1, 0);
						onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
					}
					// PageGallery.this.setSelection(++position);
					break;

				default:
					break;
				}
			};
		};

		private Timer timer = new Timer();
		private TimerTask task = new TimerTask() {
			public void run() {
				mHandler.sendEmptyMessage(timerAnimation);
			}
		};

		public PageGallery(Context paramContext) {
			super(paramContext);
			timer.schedule(task, time, time);
		}

		public PageGallery(Context paramContext, AttributeSet paramAttributeSet) {
			super(paramContext, paramAttributeSet);
			timer.schedule(task, time, time);

		}

		public PageGallery(Context paramContext,
				AttributeSet paramAttributeSet, int paramInt) {
			super(paramContext, paramAttributeSet, paramInt);
			timer.schedule(task, time, time);
		}

		private boolean isScrollingLeft(MotionEvent paramMotionEvent1,
				MotionEvent paramMotionEvent2) {
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
				task = new TimerTask() {
					public void run() {
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
		public boolean onFling(MotionEvent e1, MotionEvent e2,
				float paramFloat1, float paramFloat2) {
			AbstractEntityAdapter<?> adapter = (AbstractEntityAdapter<?>) getAdapter();
			if (adapter == null)
				return true;
			List<?> list = adapter.getData();
			if (list == null || list.size() == 0 || list.size() == 1) {
				return true;
			}
			int keyCode;
			if (isScrollingLeft(e1, e2)) {
				keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
			} else {
				keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
			}
			onKeyDown(keyCode, null);
			return true;
		}

		public void destroy() {
			timer.cancel();
		}
	}
}
