package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.PartnerListData;
import com.gheng.exhibit.utils.AppTools;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class PartnerAdapter extends AbstractEntityAdapter<PartnerListData> {

	/**
	 * @param context
	 */
	public PartnerAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View makeView(int position) {
		View v = inflater.inflate(R.layout.item_partener, null);
	/*	v.setLayoutParams(new ListView.LayoutParams(-1, context.getResources()
				.getDimensionPixelSize(R.dimen.pchi_item_height)));*/
		if (position % 2 == 0) {
			v.setBackgroundColor(Color.WHITE);
		} else {
			int color = context.getResources().getColor(R.color.btn_gray);
			v.setBackgroundColor(color);
		}
		return v;
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, PartnerListData data) {
		ViewHolder obj = (ViewHolder) holder;
		obj.tv.setText(data.name);
		bitmapUtils.display(obj.iv, AppTools.imageChange(data.imageurl));
	}

	class ViewHolder {
		@ViewInject(R.id.tv)
		TextView tv;
		
		@ViewInject(R.id.iv)
		ImageView iv;
	}

}
