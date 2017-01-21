package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.FloorListData;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class FloorListAdapter extends AbstractEntityAdapter<FloorListData> {


	/**
	 * @param context
	 */
	public FloorListAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		return inflater.inflate(R.layout.item_floorlist, null);
	}

	@Override
	protected Object makeHolder() {
		return new ViewHoder();
	}

	@Override
	protected void setHolderValue(Object holder, FloorListData data) {
		ViewHoder obj = (ViewHoder) holder;
		if(data.floorName.contains(".2")) {
			obj.floorNameIv.setImageResource(R.drawable.map_list_2);
		} else {
			obj.floorNameIv.setImageResource(R.drawable.map_list);
		}
		obj.floorNameTv.setText(data.floorName);
		obj.specialTv.setText(data.specialName);
		obj.remarkView.setText(data.floorDesc);
	}

	class ViewHoder {
		@ViewInject(R.id.iv_floorname)
		ImageView floorNameIv;
		
		@ViewInject(R.id.tv_floorname)
		TextView floorNameTv;
		
		@ViewInject(R.id.tv_special)
		TextView specialTv;
		
		@ViewInject(R.id.tv_remark)
		TextView remarkView;
	}
}
