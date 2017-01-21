package com.gheng.exhibit.widget;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.gheng.exhibit.view.BaseActivity;
import com.smartdot.wenbo.huiyi.R;

/**
 * 展位view
 * 
 * @author lileixing
 */
public class EmptyView extends TextView {

	private String text;
	
	public EmptyView(Context context) {
		super(context);
		text = ((BaseActivity) context).getLanguageString("未找到相关信息");
		setLayoutParams(new LayoutParams(-1,-1));
		setGravity(Gravity.CENTER);
		float size = getResources().getDimensionPixelSize(R.dimen.content_font_size_big);
		setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
		show(false);
	}

	public void show(boolean show) {
		if (show) {
			setText(text);
		} else {
			setText("");
		}
	}
}
