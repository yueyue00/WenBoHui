package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.support.AboutData;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class AboutAdapter extends AbstractEntityAdapter<AboutData> {

	/**
	 * @param context
	 */
	public AboutAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		View v = inflater.inflate(R.layout.item_about, null);
		v.setLayoutParams(new ListView.LayoutParams(-1, context.getResources()
				.getDimensionPixelSize(R.dimen.pchi_item_height)));
		v.setBackgroundColor(Color.WHITE);
		return v;
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, AboutData data) {
		ViewHolder obj = (ViewHolder) holder;
		obj.tv.setText(data.name);
		if(data.isNew){
			obj.tv_new.setVisibility(View.VISIBLE);
			obj.tv_new.setText(((BaseActivity)context).getLanguageString("有新版本"));
		}else{
			obj.tv_new.setVisibility(View.GONE);
		}
	}

	class ViewHolder {
		@ViewInject(R.id.tv)
		TextView tv;
		@ViewInject(R.id.tv_new)
		TextView tv_new;
	}

}
