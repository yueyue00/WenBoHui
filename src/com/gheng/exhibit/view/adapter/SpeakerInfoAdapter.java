package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.Schedule;
import com.gheng.exhibit.utils.I18NUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class SpeakerInfoAdapter extends AbstractEntityAdapter<Schedule> {

	/**
	 * @param context
	 */
	public SpeakerInfoAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		View v = inflater.inflate(R.layout.item_speaker_info, null);
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
	protected void setHolderValue(Object holder, Schedule data) {
		ViewHolder obj = (ViewHolder) holder;
		obj.tv.setText((String)I18NUtils.getValue(data, "name"));
	}

	class ViewHolder {
		@ViewInject(R.id.tv)
		TextView tv;
	}

}
