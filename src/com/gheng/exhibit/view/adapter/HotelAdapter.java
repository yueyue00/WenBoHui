package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.HotelData;
import com.gheng.exhibit.utils.AppTools;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lenovo
 * 
 */
public class HotelAdapter extends AbstractEntityAdapter<HotelData> {

	int size = 0;

	/**
	 * @param context
	 */
	public HotelAdapter(Context context) {
		super(context);
		size = context.getResources().getDrawable(R.drawable.star).getIntrinsicHeight();
	}

	@Override
	protected View makeView(int position) {
		return inflater.inflate(R.layout.item_hotel, null);
	}

	@Override
	protected Object makeHolder() {
		return new ViewHoder();
	}

	@Override
	protected void setHolderValue(Object holder, HotelData data) {
		ViewHoder obj = (ViewHoder) holder;
		obj.tv_name.setText(data.name);
		bitmapUtils.display(obj.iv, AppTools.imageChange(data.logo));
		String s = "a";
		obj.service.setText(data.type.replace(",", "\n"));
//		obj.textView1.setText(data.type.replace(" ", "\n"));
		obj.room_ratingbar.setRating(data.score);

		LayoutParams lp = obj.room_ratingbar.getLayoutParams();
		lp.height = size;
	}

	class ViewHoder {
		@ViewInject(R.id.iv)
		ImageView iv;
		@ViewInject(R.id.tv_name)
		TextView tv_name;

		@ViewInject(R.id.room_ratingbar)
		RatingBar room_ratingbar;
		@ViewInject(R.id.service)
		TextView service;
		@ViewInject(R.id.textView1)
		TextView textView1;
	}
}
