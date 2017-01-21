package com.gheng.exhibit.widget;

import android.content.Context;
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
public class ViewPagerPoint2 extends RelativeLayout {
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
	public ViewPagerPoint2(Context context) {
		super(context);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ViewPagerPoint2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ViewPagerPoint2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		gallery = new PageGallery(context);
		gallery.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
		addView(gallery);
		linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rp.setMargins(0, 0, 0, DipUtils.dip2px(getContext(), 20));
		linearLayout.setLayoutParams(rp);
		addView(linearLayout);

		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				position %= ((AbstractEntityAdapter<?>) parent.getAdapter()).getData().size();
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
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
	public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener) {
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

	public class PageGallery extends Gallery {

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

	}

}
