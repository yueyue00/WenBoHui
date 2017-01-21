package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.view.support.PchiData;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class PchiAdapter extends AbstractEntityAdapter<PchiData> {

	/**
	 * @param context
	 */
	public PchiAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		View v = inflater.inflate(R.layout.item_pchi, null);
		v.setLayoutParams(new ListView.LayoutParams(-1, context.getResources().getDimensionPixelSize(R.dimen.pchi_item_height)));
//		if (position % 2 == 1) {
//			v.setBackgroundColor(Color.WHITE);
//		} else {
//			int color = context.getResources().getColor(R.color.btn_gray);
//			v.setBackgroundColor(color);
//		}
		v.setBackgroundColor(Color.BLACK);
		return v;
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, PchiData data) {
		ViewHolder obj = (ViewHolder) holder;
		obj.tv.setText(data.name);
		obj.iv.setImageResource(data.icon);
	}

	class ViewHolder {
		@ViewInject(R.id.tv)
		TextView tv;

		@ViewInject(R.id.iv)
		ImageView iv;
	}

}
