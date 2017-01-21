package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.ScheduleInfoSupportData;
import com.gheng.exhibit.utils.StringTools;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class ScheduleInfoAdapter extends
		AbstractEntityAdapter<ScheduleInfoSupportData> {

	private LayoutParams titleParam;
	
	private LayoutParams timeParam;
	
	/**
	 * @param context
	 */
	public ScheduleInfoAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		View v = inflater.inflate(R.layout.item_schedule_info, null);
		if(position % 2 == 1){
			v.setBackgroundColor(context.getResources().getColor(R.color.agray));
		}else{
			v.setBackgroundColor(Color.WHITE);
		}
		return v;
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, ScheduleInfoSupportData data) {
		ViewHolder obj = (ViewHolder) holder;
		if(titleParam == null)
			titleParam = (LayoutParams) obj.title.getLayoutParams();
		if(timeParam == null)
			timeParam = (LayoutParams) obj.time.getLayoutParams();
		
		obj.name.setVisibility(View.VISIBLE);
		obj.time.setVisibility(View.VISIBLE);
		obj.arrow.setVisibility(View.VISIBLE);
		
		obj.time.setLayoutParams(timeParam);
		obj.title.setLayoutParams(titleParam);
		
		obj.name.setVisibility(View.VISIBLE);
		if(StringTools.isBlank(data.speaker)){
			if(StringTools.isBlank(data.time)){
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-2, -2);
				lp.addRule(RelativeLayout.CENTER_IN_PARENT);
				obj.title.setLayoutParams(lp);
				obj.time.setLayoutParams(timeParam);
			}else{
				RelativeLayout.LayoutParams tp = new LayoutParams(timeParam);
				tp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				tp.addRule(RelativeLayout.BELOW, R.id.title);
				obj.time.setLayoutParams(tp);
				
				RelativeLayout.LayoutParams tp2 = new LayoutParams(titleParam);
				tp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
				obj.title.setLayoutParams(tp2);
				
				obj.name.setVisibility(View.GONE);
			}
			obj.arrow.setVisibility(View.GONE);
		}else{
			obj.name.setText(data.speaker);
			obj.arrow.setVisibility(View.VISIBLE);
		}
		obj.title.setText(data.name);
		obj.time.setText(data.time);
	}

	class ViewHolder {
		@ViewInject(R.id.title)
		TextView title;
		@ViewInject(R.id.name)
		TextView name;
		@ViewInject(R.id.time)
		TextView time;
		@ViewInject(R.id.arrow)
		ImageView arrow;
	}

}
