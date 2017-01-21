package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.NationData;
import com.gheng.exhibit.utils.AppTools;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class NationAdapter extends AbstractEntityAdapter<NationData> {

	/**
	 * @param context
	 */
	public NationAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		View v = inflater.inflate(R.layout.item_nation, null);
		v.setLayoutParams(new ListView.LayoutParams(-1, context.getResources()
				.getDimensionPixelSize(R.dimen.nation_item_height)));
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
	protected void setHolderValue(Object holder, NationData data) {
		ViewHolder obj = (ViewHolder) holder;
		obj.tv.setText(data.name);
		if(-1 != data.id){
			bitmapUtils.display(obj.iv, AppTools.imageChange(data.logo));
		}
	}

	class ViewHolder {
		@ViewInject(R.id.tv)
		TextView tv;
		
		@ViewInject(R.id.iv)
		ImageView iv;
	}

}
