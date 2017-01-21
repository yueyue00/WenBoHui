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
 * 会议适配器
 * @author lileixing
 */
public class ConferenceAdapter extends AbstractEntityAdapter<PchiData> {

	/**
	 * @param context
	 */
	public ConferenceAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		View v =  inflater.inflate(R.layout.item_conference, null);
		v.setLayoutParams(new ListView.LayoutParams(-1, context.getResources().getDimensionPixelSize(R.dimen.pchi_item_height)));
		if(position % 2 == 0)
			v.setBackgroundColor(Color.WHITE);
		return v;
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, PchiData data) {
		ViewHolder obj = (ViewHolder)holder;
		obj.tv.setText(data.name);
		obj.iv.setImageResource(data.icon);
	}
	
	class ViewHolder{
		@ViewInject(R.id.tv)
		TextView tv;
		
		@ViewInject(R.id.iv)
		ImageView iv;
		
	}

}
